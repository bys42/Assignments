/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/15
 *  Description: Permutation assignment.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        if (args.length == 0) throw new NoSuchElementException("Argument is empty");

        int collectCount = Integer.parseInt(args[0]);

        if (collectCount < 0)
            throw new IllegalArgumentException("Argument should be positive interger");

        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        for (int inputCount = 1; !StdIn.isEmpty(); inputCount++) {
            String inputString = StdIn.readString();
            int event = StdRandom.uniform(inputCount);

            if (event >= collectCount) {
                continue;
            }

            if (queue.size() == collectCount) {
                queue.dequeue();
            }

            queue.enqueue(inputString);
        }

        for (String collectString : queue) {
            System.out.println(collectString);
        }

        return;
    }
}
