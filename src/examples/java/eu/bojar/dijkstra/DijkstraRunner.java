package eu.bojar.dijkstra;

import java.util.Arrays;
import java.util.List;

public class DijkstraRunner {

  public static void main(String[] args) {
    Graph g = new Graph();

    g.addNode(0)
            .addEdge(1, 7)
            .addEdge(2, 9)
            .addEdge(5, 14);
    g.addNode(1)
            .addEdge(0, 7)
            .addEdge(2, 10)
            .addEdge(3, 15);
    g.addNode(2)
            .addEdge(0, 9)
            .addEdge(1, 10)
            .addEdge(3, 11)
            .addEdge(5, 2);
    g.addNode(3)
            .addEdge(1, 15)
            .addEdge(2, 11)
            .addEdge(4, 6);
    g.addNode(4)
            .addEdge(3, 6)
            .addEdge(5, 9);
    g.addNode(5)
            .addEdge(0, 14)
            .addEdge(2, 2)
            .addEdge(4, 9);

    DijkstraAlgorithm dj = new DijkstraAlgorithm();
    List<Integer> path = dj.findShortestPath(g, 0, 4);

    assert(path.equals(Arrays.asList(0, 2, 5, 4)));

    System.out.println("Shortest path from 0 to 4: " + path);


  }
}
