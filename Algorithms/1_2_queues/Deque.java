/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/01/15
 *  Description: Deque Assignment.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private final Node endPoint; // Start & End Node of queue.
    private int dequeSize; // queue size.

    public Deque()                           // construct an empty deque
    {
        endPoint = new Node();
        endPoint.previous = endPoint;
        endPoint.next = endPoint;
        dequeSize = 0;
    }

    public static void main(String[] args) {
        Deque<String> argDeque = new Deque<>();

        for (int i = 0; i < args.length; i++) {
            argDeque.addFirst(args[i]);
            argDeque.addLast(args[i]);
        }

        for (String data : argDeque) {
            System.out.print(data + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < args.length; i++) {
            System.out.print(argDeque.removeFirst() + argDeque.removeLast() + " ");
        }
        return;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new IllegalArgumentException("Argument is null");

        Node node = new Node();

        node.data = item;
        node.previous = endPoint;
        node.next = endPoint.next;

        endPoint.next = node;
        node.next.previous = node;

        dequeSize++;
        return;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new IllegalArgumentException("Argument is null");

        Node node = new Node();

        node.data = item;
        node.previous = endPoint.previous;
        node.next = endPoint;

        endPoint.previous = node;
        node.previous.next = node;

        dequeSize++;
        return;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = endPoint.next.data;

        endPoint.next = endPoint.next.next;
        endPoint.next.previous = endPoint;

        dequeSize--;

        return item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item item = endPoint.previous.data;

        endPoint.previous = endPoint.previous.previous;
        endPoint.previous.next = endPoint;

        dequeSize--;

        return item;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return dequeSize == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return dequeSize;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new Iterator<Item>() {
            private Node currentNode = endPoint;

            public boolean hasNext() {
                return currentNode.next != endPoint;
            }

            public Item next() {
                if (hasNext()) {
                    currentNode = currentNode.next;
                    return currentNode.data;
                }
                throw new NoSuchElementException("iterator end");
            }
        };
    }

    private class Node {
        Item data;
        Node previous;
        Node next;
    }
}
