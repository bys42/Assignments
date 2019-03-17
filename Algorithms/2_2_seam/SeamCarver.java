/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/03/14
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {
    private static final double BORDER_ENERGY = 1000;
    private static final int COLOR_MASK = 0xFF;
    private static final int OFFSET_R = 16;
    private static final int OFFSET_G = 8;
    private static final int OFFSET_B = 0;
    private Picture picture;
    private boolean isPicTranspose;
    private double[][] picEnergy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        picEnergy = new double[height()][width()];
        for (int h = 0; h < height(); h++) {
            for (int w = 0; w < width(); w++) {
                picEnergy[h][w] = energyHelper(w, h);
            }
        }
        isPicTranspose = false;
    }

    // height of current picture
    public int height() {
        if (isPicTranspose) return picture.width();
        return picture.height();
    }

    // width of current picture
    public int width() {
        if (isPicTranspose) return picture.height();
        return picture.width();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int w;
        int h;

        if (isPicTranspose) {
            w = y;
            h = x;
        }
        else {
            w = x;
            h = y;
        }

        if (w < 0 || w >= picture.width()) throw new IllegalArgumentException();
        if (h < 0 || h >= picture.height()) throw new IllegalArgumentException();

        return picEnergy[h][w];
    }

    private double energyHelper(int x, int y) {
        if (x == 0 || x == picture.width() - 1) return BORDER_ENERGY;
        if (y == 0 || y == picture.height() - 1) return BORDER_ENERGY;

        int colorUp = picture.getRGB(x, y - 1);
        int colorDown = picture.getRGB(x, y + 1);
        int colorLeft = picture.getRGB(x - 1, y);
        int colorRight = picture.getRGB(x + 1, y);

        return Math.sqrt(gradient(colorUp, colorDown) + gradient(colorLeft, colorRight));
    }

    private int gradient(int color1, int color2) {
        int diffR = colorDiff(color1, color2, OFFSET_R);
        int diffG = colorDiff(color1, color2, OFFSET_G);
        int diffB = colorDiff(color1, color2, OFFSET_B);

        return diffR * diffR + diffG * diffG + diffB * diffB;
    }

    private int colorDiff(int color1, int color2, int offset) {
        return (color1 >> offset & COLOR_MASK) - (color2 >> offset & COLOR_MASK);
    }

    public static void main(String[] args) {

    }

    // current picture
    public Picture picture() {
        if (isPicTranspose) transposePic();
        return new Picture(picture);
    }

    private void transposePic() {
        int newWidth = picture.height();
        int newHeight = picture.width();

        Picture newPic = new Picture(newWidth, newHeight);
        double[][] newPicEnergy = new double[newHeight][newWidth];

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                newPic.setRGB(w, h, picture.getRGB(h, w));
                newPicEnergy[h][w] = picEnergy[w][h];
            }
        }
        picture = newPic;
        picEnergy = newPicEnergy;
        isPicTranspose = !isPicTranspose;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!isPicTranspose) transposePic();
        return findVerticalSeamHelper();
    }

    private int[] findVerticalSeamHelper() {
        int width = picture.width();
        int height = picture.height();
        int[] seam = new int[height];

        if (width <= 2 || height <= 2) {
            Arrays.fill(seam, 0);
            return seam;
        }

        int[][] predecessor = new int[height][width];
        double[][] energyTo = new double[height][width];
        int maxCol = width - 1;
        int maxRow = height - 1;

        Arrays.fill(energyTo[0], BORDER_ENERGY);
        for (int h = 1; h < height; h++) {
            double[] preEnRow = energyTo[h - 1];

            for (int w = 1; w < maxCol; w++) {
                int preCol = findMinPredecessor(preEnRow, w);

                predecessor[h][w] = preCol;
                energyTo[h][w] = preEnRow[preCol] + picEnergy[h][w];
            }

            predecessor[h][0] = 1;
            energyTo[h][0] = preEnRow[1] + BORDER_ENERGY;

            predecessor[h][maxCol] = maxCol - 1;
            energyTo[h][maxCol] = preEnRow[maxCol - 1] + BORDER_ENERGY;
        }

        int minId = 1;
        double minEnergy = energyTo[maxRow][1];

        for (int w = 1; w < maxCol; w++) {
            if (energyTo[maxRow][w] < minEnergy) {
                minId = w;
                minEnergy = energyTo[maxRow][w];
            }
        }

        seam[maxRow] = minId;
        for (int h = maxRow; h > 0; h--) {
            seam[h - 1] = predecessor[h][minId];
            minId = seam[h - 1];
        }
        return seam;
    }

    private int findMinPredecessor(double[] array, int index) {
        double dataL = array[index - 1];
        double dataM = array[index];
        double dataR = array[index + 1];

        if (dataM <= dataL) {
            if (dataM <= dataR) return index;
            return index + 1;
        }

        if (dataL <= dataR) return index - 1;
        return index + 1;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (isPicTranspose) transposePic();
        return findVerticalSeamHelper();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!isPicTranspose) transposePic();
        removeVerticalSeamHelper(seam);
    }

    private void removeVerticalSeamHelper(int[] seam) {
        int width = picture.width();
        int height = picture.height();

        if (width <= 1 || !isValidSeam(seam)) throw new IllegalArgumentException();

        int newWidth = width - 1;
        Picture newPic = new Picture(newWidth, height);
        double[][] newPicEnergy = new double[height][newWidth];

        for (int h = 0; h < height; h++) {
            int w = 0;
            for (; w < seam[h]; w++) newPic.setRGB(w, h, picture.getRGB(w, h));
            while (w < newWidth) newPic.setRGB(w, h, picture.getRGB(++w, h));
        }
        picture = newPic;

        for (int h = 0; h < height; h++) {
            int skipId = seam[h];

            System.arraycopy(picEnergy[h], 0, newPicEnergy[h], 0, skipId);

            if (skipId > 0) newPicEnergy[h][skipId - 1] = energyHelper(skipId - 1, h);

            if (skipId == newWidth) continue;

            System.arraycopy(picEnergy[h], skipId + 1, newPicEnergy[h], skipId, newWidth - skipId);
            newPicEnergy[h][skipId] = energyHelper(skipId, h);
        }
        picEnergy = newPicEnergy;
    }

    private boolean isValidSeam(int[] seam) {
        int width = picture.width();
        int height = picture.height();

        if (seam == null) return false;

        if (seam.length != height) return false;

        if (seam[0] < 0 || seam[0] >= width) return false;
        for (int h = 1; h < height; h++) {
            if (seam[h] < 0 || seam[h] >= width) return false;

            int diff = seam[h] - seam[h - 1];
            if (diff > 1 || diff < -1) return false;
        }

        return true;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (isPicTranspose) transposePic();
        removeVerticalSeamHelper(seam);
    }
}
