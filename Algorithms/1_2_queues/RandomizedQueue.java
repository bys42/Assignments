/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/15
 *  Description: RandomizedQueue Assignment.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueStore; // queue store
    private int queueSize;     // queue size

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        queueStore = (Item[]) new Object[1];
        queueSize = 0;
        return;
    }

    public static void main(String[] args) {
        RandomizedQueue<String> argDeque = new RandomizedQueue<>();

        for (int i = 0; i < args.length; i++) {
            argDeque.enqueue(args[i]);
        }

        for (String data : argDeque) {
            System.out.print(data + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < args.length; i++) {
            System.out.print(argDeque.dequeue() + " ");
        }
        System.out.print("\n");
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new IllegalArgumentException("Argument is null");

        if (queueSize == queueStore.length) resize(2 * queueStore.length);

        queueStore[queueSize++] = item;

        return;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");

        int dequeueIndex = StdRandom.uniform(queueSize);

        Item item = queueStore[dequeueIndex];

        queueStore[dequeueIndex] = queueStore[--queueSize];
        queueStore[queueSize] = null;

        if (queueSize > 0 && queueSize == queueStore.length / 4) resize(queueStore.length / 2);

        return item;
    }

    private void resize(int capacity) // resize queue store
    {
        Item[] newStore = (Item[]) new Object[capacity];

        for (int i = 0; i < queueSize; i++) {
            newStore[i] = queueStore[i];
        }
        queueStore = newStore;
        return;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return queueSize;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        int sampleIndex = StdRandom.uniform(queueSize);
        return queueStore[sampleIndex];
    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return queueSize == 0;
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new Iterator<Item>() {
            int[] order = StdRandom.permutation(queueSize);
            int currentIndex = 0;

            public boolean hasNext() {
                return currentIndex < queueSize;
            }

            public Item next() {
                if (hasNext()) return queueStore[order[currentIndex++]];
                throw new NoSuchElementException("iterator end");
            }
        };
    }
}
