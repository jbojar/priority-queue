# priority-queue
Heap based Java PriorityQueue with changePriority operation.

## Example usage

```java
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
```

This should print:
```
Initially head element is:
value: element1 priority: 10

After changing element2 priority to 5 head element is:
value: element2 priority: 5

After changing element1 priority to 35 dequeueed elements in priority order are:
value: element2 priority: 5
value: element3 priority: 30
value: element1 priority: 35
```

For other example usage see `eu.bojar.examples.dijkstra.DijkstraAlgorithm` in `src/examples/java` directory.
