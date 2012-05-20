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
     * whether to promote it a level up.
     */
    public final boolean add(final T t) {
        SkipListNode newNode = new SkipListNode(t);
        LinkedList<SkipListNode> possiblePromotions = pathFromTopToImmediatelySmallerBottomNode(newNode);
        insertAndMaybePromote(newNode.entryValue, possiblePromotions, null);
        return true;
    }

    private void insertAndMaybePromote(T value, final LinkedList<SkipListNode> possiblePromotions, final SkipListNode lastCreatedNode) {
        SkipListNode newNode = new SkipListNode(value);
        if (possiblePromotions.isEmpty()) {
            SkipListNode newTop = createSingletonList(newNode, lastCreatedNode);
            promoteToToplist(newTop);
        } else {
            //horizontal insertion
            SkipListNode nodeToInsertAfter = possiblePromotions.pop();
            linkToPreviousNode(newNode, nodeToInsertAfter);
            //vertical insertion
            linkToNodeBelow(newNode, lastCreatedNode);
        }
        //The node we just created will be the node below for the next promotion
        if (flipCoin()) {
            insertAndMaybePromote(value, possiblePromotions, newNode);
        }
    }

    private void linkToPreviousNode(final SkipListNode newNode, final SkipListNode nodeToInsertAfter) {
        SkipListNode next = nodeToInsertAfter.next;
        nodeToInsertAfter.next = newNode;
        newNode.prev = nodeToInsertAfter;
        newNode.next = next;
        if (next != null) next.prev = newNode;
    }

    private void linkToNodeBelow(final SkipListNode newNode, final SkipListNode below) {
        if (below != null) {
            below.up = newNode;
            newNode.down = below;
        }
    }

    private void promoteToToplist(final SkipListNode newTop) {
        newTop.down = topList;
        topList.up = newTop;
        topList = newTop;
    }

    private SkipListNode createSingletonList(final SkipListNode newNode, final SkipListNode below) {
        SkipListNode newTop = new SkipListNode((T) MINUS_INFINITY);
        newTop.next = newNode;
        newNode.prev = newTop;

        linkToNodeBelow(newNode, below);

        return newTop;
    }

    private boolean flipCoin() {
        return random.nextBoolean() && random.nextBoolean();
    }

    private LinkedList<SkipListNode> pathFromTopToImmediatelySmallerBottomNode(SkipListNode node) {
        SkipListNode list = topList;
        LinkedList<SkipListNode> promotions = new LinkedList<SkipListNode>();
        while (list != null) {
            while (list.next != null && compare(list.next.entryValue, node.entryValue) <= 0) list = list.next;
            promotions.push(list);
            list = list.down;
        }
        return promotions;
    }

    private void updateAllTheWayDown(SkipListNode current, final T value) {
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

    final public void updateValue(T oldValue, T newValue) {
        SkipListNode highestLevelNodeWithValue = findHighestLevelNodeWithValue(oldValue);
        updateAllTheWayDown(highestLevelNodeWithValue, newValue);

    }

    private void removeAllTheWayDown(SkipListNode node) {
        if (node == null || node.prev == null) return;
        SkipListNode prev = node.prev;
        prev.next = node.next;
        if (node.next != null) node.next.prev = prev;
        removeAllTheWayDown(node.down);
    }

    public final boolean contains(final Object t) {
        SkipListNode foundElement = findHighestLevelNodeWithValue((T) t);
        return foundElement != null;
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

    final int compare(final T o1, final T o2) {
        if (o1 == MINUS_INFINITY) {
            return -1;
        } else if (o2 == MINUS_INFINITY) {
            return 1;
        } else {
            return comparator.compare((T) o1, (T) o2);
        }
    }

    public final void clear() {
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

    public final void uglyPrint() {
        SkipListNode list = topList;
        while (list != null) {
            printList(list);
            list = list.down;
        }
        System.out.println();
    }

    private final void printList(SkipListNode list) {
        SkipListNode current = list.next;
        while (current != null) {
            System.out.print(current.entryValue + "  ");
            current = current.next;
        }
        System.out.println();
    }

}
