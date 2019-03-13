/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/03/14
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final int BORDER_ENERGY = 1000;
    private final Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = picture;
    }

    public static void main(String[] args) {

    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private int sumSquare(int a, int b, int c) {
        return a * a + b * b + c * c;
    }

    private int gradient(Color color1, Color color2) {
        int diffR = color1.getRed() - color2.getRed();
        int diffG = color1.getGreen() - color2.getGreen();
        int diffB = color1.getBlue() - color2.getBlue();

        return sumSquare(diffR, diffG, diffB);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width()) throw new IllegalArgumentException();
        if (y < 0 || y >= height()) throw new IllegalArgumentException();

        if (x == 0 || x == width() - 1) return BORDER_ENERGY;
        if (y == 0 || y == height() - 1) return BORDER_ENERGY;

        Color colorUp = picture.get(x, y - 1);
        Color colorDown = picture.get(x, y + 1);
        Color colorLeft = picture.get(x - 1, y);
        Color colorRight = picture.get(x + 1, y);

        return Math.sqrt(gradient(colorUp, colorDown) + gradient(colorLeft, colorRight));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1) throw new IllegalArgumentException();
        // valid seam
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1) throw new IllegalArgumentException();
        // valid seam
    }
}
