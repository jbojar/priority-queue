package eu.bojar.examples.simple;

import eu.bojar.priorityqueue.PriorityQueue;

public class PriorityQueueSimpleExample {

  public static void main(String[] args) {
    PriorityQueue<String, Integer> pq = new PriorityQueue<>();

    PriorityQueue<String, Integer>.Element element2 = pq.add("element2", 20);
    PriorityQueue<String, Integer>.Element element1 = pq.add("element1", 10);
    PriorityQueue<String, Integer>.Element element3 = pq.add("element3", 30);

    System.out.println("Initially head element is:");
    System.out.println(String.format("value: %s priority: %d", pq.peek().value(), pq.peek().priority()));
    System.out.println();

    element2.changePriority(5);

    System.out.println("After changing element2 priority to 5 head element is:");
    System.out.println(String.format("value: %s priority: %d", pq.peek().value(), pq.peek().priority()));
    System.out.println();

    element1.changePriority(35);
    System.out.println("After changing element1 priority to 35 dequeueed elements in priority order are:");
    while(!pq.isEmpty()) {
      PriorityQueue<String, Integer>.Element element = pq.remove();
      System.out.println(String.format("value: %s priority: %d", element.value(), element.priority()));
    }

  }
}
