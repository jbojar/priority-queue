package eu.bojar.examples.dijkstra;

import eu.bojar.priorityqueue.PriorityQueue;

import java.util.*;

public class DijkstraAlgorithm {

  public List<Integer> findShortestPath(Graph g, int start, int target) {

    PriorityQueue<Graph.Node, Integer> openQueue = new PriorityQueue<>(g.nodesCount());
    Map<Integer, PriorityQueue<Graph.Node, Integer>.Element> open = new HashMap<>();
    Set<Integer> closed = new HashSet<>();

    int[] parent = new int[g.nodesCount()];

    for (int i = 0; i < g.nodesCount(); i++) {
      parent[i] = -1;
    }
    final PriorityQueue<Graph.Node, Integer>.Element startHandle = openQueue.add(g.node(start), 0);
    open.put(start, startHandle);

    while (openQueue.size() > 0) {
      PriorityQueue<Graph.Node, Integer>.Element nodeHandle = openQueue.remove();
      open.remove(nodeHandle.value().label);

      closed.add(nodeHandle.value().label);

      for (Graph.Edge edge : nodeHandle.value().neighbors()) {
        if (!closed.contains(edge.target)) {
          PriorityQueue<Graph.Node, Integer>.Element neighborHandle = open.get(edge.targetNode().label);

          if (neighborHandle == null) {
            int newDist = nodeHandle.priority() + edge.weight;
            parent[edge.target] = nodeHandle.value().label;
            PriorityQueue<Graph.Node, Integer>.Element newNeighborHandle = openQueue.add(edge.targetNode(), newDist);
            open.put(edge.target, newNeighborHandle);
          } else {
            int newDist = nodeHandle.priority() + edge.weight;
            if (newDist < neighborHandle.priority()) {
              neighborHandle.changePriority(newDist);
              parent[edge.target] = nodeHandle.value().label;
            }
          }
        }
      }

    }

    int current = target;
    List<Integer> path = new LinkedList<>();
    path.add(current);
    while (parent[current] != -1) {
      current = parent[current];
      path.add(0, current);
    }
    return path;
  }

}

