package eu.bojar.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  final Map<Integer, Node> nodes = new HashMap<>();

  public Graph() {
  }

  public Node addNode(int label) {
    Node node = new Node(label);
    nodes.put(label, node);
    return node;
  }

  public Node node(int i) {
    return nodes.get(i);
  }

  public int nodesCount() {
    return nodes.size();
  }

  public class Node {
    final int label;
    final List<Edge> edges = new ArrayList<>();

    public Node(int label) {
      this.label = label;
    }

    public Node addEdge(int target, int weight) {
      Edge edge = new Edge(target, weight);
      edges.add(edge);
      return Node.this;
    }

    List<Edge> neighbors() {
      return edges;
    }
  }

  public class Edge {
    final int target;
    final int weight;

    public Edge(int target, int weight) {
      this.target = target;
      this.weight = weight;
    }

    Node targetNode() {
      return nodes.get(target);
    }
  }
}
