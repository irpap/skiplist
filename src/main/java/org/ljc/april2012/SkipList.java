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
    final boolean add(final T t) {
        SkipListNode newNode = new SkipListNode(t);
        SkipListNode topNodeWithValue = findTopNodeWithValue(topList, t);
        //caller should do this instead TODO
        if (topNodeWithValue != null) {
            setValue(topNodeWithValue, t);
            return true;
        }

        LinkedList<SkipListNode> possiblePromotions = findPromotionPathToImmediatelySmallerNode(topList, newNode, new LinkedList<SkipListNode>());

        insertAndMaybePromote(newNode.entryValue, possiblePromotions, null);
        return true;
    }

    private void insertAndMaybePromote(T value, LinkedList<SkipListNode> possiblePromotions, SkipListNode lastNodeICreated) {
        SkipListNode newNode = new SkipListNode(value);
        if (possiblePromotions.isEmpty()) {
            SkipListNode newTop = createSingletonList(newNode);
            promoteToToplist(newTop);
        } else {
            SkipListNode nodeToInsertAfter = possiblePromotions.pop();
            System.out.println("inserting/promoting new node " + value + "after " + nodeToInsertAfter);
            //horizontal
            SkipListNode next = nodeToInsertAfter.next;

            nodeToInsertAfter.next = newNode;
            newNode.prev = nodeToInsertAfter;
            newNode.next = next;
            if (next != null) {
                next.prev = newNode;
            }
            //vertical
            SkipListNode below = lastNodeICreated;
            if (below != null) {
                below.up = newNode;
                newNode.down = below;
                System.out.println("inserting " + newNode + " on top of " + below);
            } else System.out.println("below was null");

        }
        if (flipCoin()) {
            insertAndMaybePromote(value, possiblePromotions, newNode);
        }
    }

    private void promoteToToplist(SkipListNode newTop) {
        newTop.down = topList;
        topList.up = newTop;
        topList = newTop;
    }

    private SkipListNode createSingletonList(SkipListNode newNode) {
        SkipListNode newTop = new SkipListNode((T) MINUS_INFINITY);
        newTop.next = newNode;
        newNode.prev = newTop;
        return newTop;
    }

    private boolean flipCoin() {
        return random.nextBoolean();
    }

    private LinkedList findPromotionPathToImmediatelySmallerNode(SkipListNode list, SkipListNode node, LinkedList<SkipListNode> promotions) {
        while (list != null) {
            if (list.next == null || compare(list.next.entryValue, node.entryValue) > 0) {
                promotions.push(list);
                System.out.println("pushing " + list);
                findPromotionPathToImmediatelySmallerNode(list.down, node, promotions);
            }
            list = list.next;
        }
        return promotions;
    }

    private void setValue(SkipListNode topNode, T t) {
        updateAllTheWayDown(topNode, t);
    }

    private void updateAllTheWayUp(SkipListNode current, T value) {
        while (current != null) {
            current.entryValue = value;
            current = current.up;
        }
    }

    private void updateAllTheWayDown(SkipListNode current, T value) {
        while (current != null) {
            current.entryValue = value;
            current = current.down;
        }
    }

//    private SkipListNode promoteMaybe(SkipListNode nodeBelow,
//                                      SkipListNode mostLeftNode) {
//
//        while (true) {
//            if (random.nextBoolean())
//                return mostLeftNode;
//            SkipListNode newNode = new SkipListNode(nodeBelow.entryValue);
//            newNode.down = nodeBelow;
//            nodeBelow.up = newNode;
//
//            SkipListNode onTopOfMostLeft = mostLeftNode.up;
//            if (onTopOfMostLeft == null) {
//                onTopOfMostLeft = new SkipListNode((T) MINUS_INFINITY);
//                mostLeftNode.up = onTopOfMostLeft;
//                onTopOfMostLeft.down = mostLeftNode;
//            }
//            addToSingleList(onTopOfMostLeft, newNode);
//
//            nodeBelow = newNode;
//            mostLeftNode = onTopOfMostLeft;
//
//        }
//    }

    final boolean remove(final T t) {

        SkipListNode foundElement = findTopNodeWithValue(topList, t);
        removeAllTheWayDown(foundElement);
        return foundElement != null;
    }

    /**
     * Returns the highest-level node with the specified value if found, or
     * null.
     */
    private SkipListNode findTopNodeWithValue(final SkipListNode list, final T t) {
        SkipListNode current = list;

        // Keep going while the elements are smaller than the value we
        // search for
        while ((current.next != null) && (compare(t, current.entryValue) > 0)) {
            current = current.next;
        }
        // Here current is either equal or greater than the value we search for
        if (compare(t, current.entryValue) == 0) {
            return current;
        }
        // Go to the list below via the previous node
        if (current.prev == null || current.prev.down == null)
            return null;

        return findTopNodeWithValue(current.prev.down, t);
    }

    public final T find(final T t) {
        SkipListNode foundNode = findTopNodeWithValue(topList, t);
        return foundNode == null ? null : foundNode.entryValue;

    }

    private void removeAllTheWayDown(SkipListNode node) {
        if (node == null || node.prev == null) return;
        SkipListNode prev = node.prev;
        prev.next = node.next;
        node.next.prev = prev;
        removeAllTheWayDown(node.down);
    }

    final boolean contains(final Object t) {
        SkipListNode foundElement = findTopNodeWithValue(topList, (T) t);
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

    final Iterator<T> iterator() {
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

    final int compare(final Object o1, Object o2) {
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

    private class SkipListNode {
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

    void uglyPrint() {
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
            System.out.print(((SkipListMapEntry) current.entryValue).getKey() + "  ");
            current = current.next;
        }
        System.out.println();
    }

}