package org.ljc.april2012;

import java.util.*;

public class SkipList<T> {
    private final Comparator<T> comparator;
    private static final Object MINUS_INFINITY = new Object();
    private SkipListNode bottomList;
    private SkipListNode topList;
    private final Random random = new Random();

    public SkipList(final Comparator<T> comparator) {
        this.comparator = comparator;
        bottomList = topList = new SkipListNode((T) MINUS_INFINITY);
    }

    /**
     * Adds the element to the bottom list and repeatedly flips a coin to decide
     * whether to promote it a level up
     */
    public final boolean add(final T t) {
        SkipListNode newNode = new SkipListNode(t);
        LinkedList<SkipListNode> possiblePromotions = pathFromTopToImmediatelySmallerBottomNode(newNode);
        insertAndMaybePromote(newNode.entryValue, possiblePromotions, null);
        return true;
    }

    private void insertAndMaybePromote(T value, LinkedList<SkipListNode> possiblePromotions, SkipListNode lastNodeICreated) {
        SkipListNode newNode = new SkipListNode(value);
        if (possiblePromotions.isEmpty()) {
            SkipListNode newTop = createSingletonList(newNode, lastNodeICreated);
            promoteToToplist(newTop);
        } else {
            //horizontal insertion
            SkipListNode nodeToInsertAfter = possiblePromotions.pop();
            SkipListNode next = nodeToInsertAfter.next;
            nodeToInsertAfter.next = newNode;
            newNode.prev = nodeToInsertAfter;
            newNode.next = next;
            if (next != null) {
                next.prev = newNode;
            }
            //vertical insertion
            linkToNodeBelow(newNode, lastNodeICreated);
        }
        if (flipCoin()) {
            insertAndMaybePromote(value, possiblePromotions, newNode);
        }
    }

    private void linkToNodeBelow(SkipListNode newNode, SkipListNode below) {
        if (below != null) {
            below.up = newNode;
            newNode.down = below;
        }
    }

    private void promoteToToplist(SkipListNode newTop) {
        newTop.down = topList;
        topList.up = newTop;
        topList = newTop;
    }

    private SkipListNode createSingletonList(SkipListNode newNode, SkipListNode below) {
        SkipListNode newTop = new SkipListNode((T) MINUS_INFINITY);
        newTop.next = newNode;
        newNode.prev = newTop;

        linkToNodeBelow(newNode, below);

        return newTop;
    }

    private boolean flipCoin() {
        return random.nextBoolean();
    }

    private LinkedList pathFromTopToImmediatelySmallerBottomNode(SkipListNode node) {
        SkipListNode list = topList;
        LinkedList<SkipListNode> promotions = new LinkedList<SkipListNode>();
        while (list != null) {
            while (list.next != null && compare(list.next.entryValue, node.entryValue) <= 0) list = list.next;
            promotions.push(list);
            list = list.down;
        }
        return promotions;
    }

    private void setValue(SkipListNode topNode, T t) {
        updateAllTheWayDown(topNode, t);
    }

    private void updateAllTheWayDown(SkipListNode current, T value) {
        while (current != null) {
            current.entryValue = value;
            current = current.down;
        }
    }

    public final boolean remove(final T t) {

        SkipListNode foundElement = findHighestLevelNodeWithValue(t);
        removeAllTheWayDown(foundElement);
        return foundElement != null;
    }

    /**
     * Returns the highest-level node with the specified value if found, or
     * null.
     */
    private SkipListNode findHighestLevelNodeWithValue(final T t) {
        SkipListNode list = topList;
        while (list != null) {
            while (list.next != null && compare(list.next.entryValue, t) <= 0) list = list.next;
            if (compare(list.entryValue, t) == 0) return list;
            list = list.down;
        }
        return null;
    }

    public final T find(final T t) {
        SkipListNode foundNode = findHighestLevelNodeWithValue(t);
        return foundNode == null ? null : foundNode.entryValue;

    }

    private void removeAllTheWayDown(SkipListNode node) {
        if (node == null || node.prev == null) return;
        SkipListNode prev = node.prev;
        prev.next = node.next;
        node.next.prev = prev;
        removeAllTheWayDown(node.down);
    }

    public final boolean contains(final Object t) {
        SkipListNode foundElement = findHighestLevelNodeWithValue((T) t);
        return foundElement == null ? false : true;
    }

    /**
     * The size of the skiplist is just the size of the bottom list
     */
    final int size() {
        int size = 0;
        SkipListNode current = bottomList.next; // there is a dummy element
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }

    /**
     * All elements are part of the bottom list
     */
    final boolean isEmpty() {
        return bottomList.next == null;
    }

    public final Iterator<T> iterator() {
        return new Iterator<T>() {
            SkipListNode currentNode = bottomList;

            @Override
            public boolean hasNext() {
                return currentNode.next != null;
            }

            @Override
            public T next() {
                if (currentNode.next == null)
                    throw new NoSuchElementException();
                currentNode = currentNode.next;
                return currentNode.entryValue;
            }

            @Override
            public void remove() {
                SkipList.this.remove(currentNode.entryValue);
            }
        };
    }

    final int compare(final T o1, T o2) {
        if (o1 == MINUS_INFINITY) {
            return -1;
        } else if (o2 == MINUS_INFINITY) {
            return 1;
        } else {
            return comparator.compare((T) o1, (T) o2);
        }
    }

    public void clear() {
        topList = bottomList = new SkipListNode((T) MINUS_INFINITY);
    }

    class SkipListNode {
        T entryValue;
        SkipListNode up;
        SkipListNode down;
        SkipListNode next;
        SkipListNode prev;

        public SkipListNode(final T value) {
            this.entryValue = value;
        }

        public String toString() {
            return entryValue.toString();
        }
    }

    public void uglyPrint() {
        SkipListNode list = topList;
        while (list != null) {
            printList(list);
            list = list.down;
        }
        System.out.println();
    }

    private void printList(SkipListNode list) {
        SkipListNode current = list.next;
        while (current != null) {
            System.out.print(current.entryValue + "  ");
            current = current.next;
        }
        System.out.println();
    }

}