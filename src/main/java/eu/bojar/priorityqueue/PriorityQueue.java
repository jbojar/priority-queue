package eu.bojar.priorityqueue;

import java.util.ArrayList;

public class PriorityQueue<T, P extends Comparable<P>> {

  public class Element {
    public Element(T value, P priority, int idx) {
      this.value = value;
      this.priority = priority;
      this.idx = idx;
    }

    T value;
    P priority;
    int idx;

    boolean removed = false;

    void markRemoved() {
      removed = true;
    }

    public T getValue() {
      return value;
    }

    public P getPriority() {
      return priority;
    }

    public void changePriority(P newPriority) {
      checkNodeValidity();
      if (newPriority.compareTo(priority) < 0) {
        priority = newPriority;
        pullUp(idx);
      } else if (newPriority.compareTo(priority) > 0) {
        priority = newPriority;
        pushDown(idx);
      }
    }

    private void checkNodeValidity() {
      if (removed) {
        throw new IllegalStateException("node is no longer part of heap");
      }
    }

    public boolean isValid() {
      return !removed;
    }

    public void remove() {
      checkNodeValidity();

      if (tree.size() == 1) {
        tree.remove(idx);
        markRemoved();
      } if (idx == tree.size() - 1) {
        markRemoved();
        tree.remove(idx);
      } else {
        markRemoved();
        final Element nodeToMoveToThisIdx = tree.remove(tree.size() - 1);
        nodeToMoveToThisIdx.idx = idx;
        tree.set(idx, nodeToMoveToThisIdx);
        if (tree.get(parent(idx)).priority.compareTo(nodeToMoveToThisIdx.priority) > 0) {
          pullUp(idx);
        } else {
          pushDown(idx);
        }
      }

    }
  }

  final ArrayList<Element> tree;

  public PriorityQueue(int capacity) {
    tree = new ArrayList<>(capacity);
  }

  public int size() {
    return tree.size();
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public Element peek() {
    if (size() == 0) {
      throw new IllegalStateException("Heap is empty");
    }
    return tree.get(0);
  }

  public Element add(T e, P priority) {
    final int idx = size();
    final Element node = new Element(e, priority, idx);
    tree.add(node);
    pullUp(idx);
    return node;
  }

  public Element remove() {
    if (size() == 0) {
      throw new IllegalStateException("Heap is empty");
    }

    if (tree.size() == 1) {
      final Element removedNode = tree.remove(0);
      removedNode.markRemoved();
      return removedNode;
    } else {
      Element head = tree.get(0);
      head.markRemoved();
      final Element nodeToMoveToHead = tree.remove(tree.size() - 1);
      nodeToMoveToHead.idx = 0;
      tree.set(0, nodeToMoveToHead);
      pushDown(0);
      return head;
    }

  }

  private void pushDown(int i) {
    while (leftChild(i) < size() && tree.get(leftChild(i)).priority.compareTo(tree.get(i).priority) < 0 ||
            rightChild(i) < size() && tree.get(rightChild(i)).priority.compareTo(tree.get(i).priority) < 0) {
      int leftChildIdx = leftChild(i);
      int rightChildIdx = rightChild(i);
      if (rightChildIdx >= size() || tree.get(leftChildIdx).priority.compareTo(tree.get(rightChildIdx).priority) < 0) {
        swap(i, leftChildIdx);
        i = leftChildIdx;
      } else {
        swap(i, rightChildIdx);
        i = rightChildIdx;
      }
    }
  }

  private void pullUp(int i) {
    while (i != 0 && tree.get(parent(i)).priority.compareTo(tree.get(i).priority) > 0) {
      swap(i, parent(i));
      i = parent(i);
    }
  }

  int leftChild(int i) {
    return 2 * i + 1;
  }

  int rightChild(int i) {
    return 2 * i + 2;
  }

  int parent(int i) {
    return (i - 1) / 2;
  }

  void swap(int idx1, int idx2) {
    Element node1 = tree.get(idx1);
    Element node2 = tree.get(idx2);

    node1.idx = idx2;
    node2.idx = idx1;

    tree.set(idx1, node2);
    tree.set(idx2, node1);
  }

  private int compare(P a, P b) {
    return a.compareTo(b);
  }

  @Override
  public String toString() {
    return "MinHeap{" +
            "size=" + tree.size() +
            ", tree=" + tree +
            '}';
  }
}
