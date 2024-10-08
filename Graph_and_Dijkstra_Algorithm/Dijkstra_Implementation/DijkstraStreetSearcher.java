
package hw8.spp;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {

  /**
   * Creates a StreetSearcher object.
   *
   * @param graph an implementation of Graph ADT.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
  }

  private boolean endpointsValid(String startName, String endName) {
    try { // THIS WORKS. Catch and then print message. Then must also return
      checkValidEndpoint(startName);
      checkValidEndpoint(endName);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }

  private void loadDistances(Vertex<String> startVtx, HashMap<Vertex<String>, Double> distancesSet) {
    for (Map.Entry<String, Vertex<String>> entry : vertices.entrySet()) {
      Vertex<String> vtx = entry.getValue();
      if (!vtx.equals(startVtx)) {
        distancesSet.put(vtx, MAX_DISTANCE);
      }
    }
    distancesSet.put(startVtx, 0.0);
  }


  /**
   * Find the shortest path.
   *
   * @param startName starting vertex name
   * @param endName   ending vertex name
   */
  @Override
  public void findShortestPath(String startName, String endName) {
    if (!endpointsValid(startName, endName)) {
      return;
    }

    Vertex<String> start = vertices.get(startName);
    Vertex<String> end = vertices.get(endName);


    double totalDist = -1;  // totalDist must be update below

    // TODO - Implement Dijkstra Algorithm!
    HashMap<Vertex<String>, Double> distances = new HashMap<>();

    HashSet<Vertex<String>> explored = new HashSet<>();

    loadDistances(start, distances);

    SmallerDistanceHigherPriority cmp = new SmallerDistanceHigherPriority(distances);

    PriorityQueue<Vertex<String>> vqueue = new PriorityQueue<>(cmp);

    vqueue.add(start);

    totalDist = executeSearch(totalDist, explored, distances, end, vqueue);
    // END my TODO
    // These method calls will create and print the path for you
    List<Edge<String>> path = getPath(end, start);
    if (VERBOSE) {
      printPath(path, totalDist);
    }
  }

  private Double executeSearch(Double totDist, HashSet<Vertex<String>> exploredSet,
                            HashMap<Vertex<String>, Double> distancesSet, Vertex<String> endpt,
                            PriorityQueue<Vertex<String>> vtxQueue) {
    while (!exploredSet.contains(endpt) && !vtxQueue.isEmpty()) {
      Vertex<String> cur = vtxQueue.poll(); // top element. Returns null if queue is empty.
      // but should have already checked for that above.
      exploredSet.add(cur); // already checked if queue is empty so this should not add null.
      totDist = distancesSet.get(cur); // if there was anything to add to the queue, it was added
      // in the previous loop iteration (or above when adding the source) to both queue AND "distances"
      // suppose we add our last element. Then we updated "distances" and queue on the prev iteration
      // already, when we added that last element. Now we enter this new iteration and pop it, and
      // update totalDist with it. Then we go through the loop body and have nothing to add.
      // Since we've added nothing, we fail to enter the new loop body.
      // Queue is empty and totalDist is updated appropriately.

      checkNeighbors(totDist, cur, exploredSet, distancesSet, vtxQueue);
    }
    return totDist;
  }

  private void checkNeighbors(Double totDist,
                                Vertex<String> cur, HashSet<Vertex<String>> exploredSet,
                                HashMap<Vertex<String>, Double> distancesSet,
                                PriorityQueue<Vertex<String>> vtxQueue) {
    Iterable<Edge<String>> outgoingEdges = graph.outgoing(cur); // using the iterable.
    Double currentNeighborsDist;
    for (Edge<String> edge : outgoingEdges) {
      Vertex<String> neighbor = graph.to(edge);
      if (!exploredSet.contains(neighbor)) { // not finalized yet.
        currentNeighborsDist = distancesSet.get(cur) + (Double) graph.label(edge);
        if (!vtxQueue.contains(neighbor)) { // haven't visited it at all yet.
          distancesSet.put(neighbor, currentNeighborsDist);
          graph.label(neighbor, edge); // neighbor is edge's "to", edge is its incoming edge
          // this amounts to setting the "previous"
          vtxQueue.add(neighbor);
        } else { // have visited it before.
          Double neighborsOriginalDist = distancesSet.get(neighbor);
          if (currentNeighborsDist < neighborsOriginalDist) { // found a new shorter path
            updateShorterPath(currentNeighborsDist, neighbor, edge, distancesSet, vtxQueue);
          } // else, already visited it, but the new path is not a shorter one, so leave it as is.
        }
      }
    } // finished loop, found nearest neighbor if any neighbors were left to explore. If queue
    // is now empty, won't enter another iteration of the loop. Otherwise, we'll get the next element
    // to explore.
    // we also add the next best element to "explored" if it exists at the top of the loop.
  }

  private void updateShorterPath(Double currentNeighborsDist,
                                 Vertex<String> neighbor, Edge<String> edge,
                                 HashMap<Vertex<String>, Double> distancesSet,
                                 PriorityQueue<Vertex<String>> vtxQueue) {
    vtxQueue.remove(neighbor); // remove to re-add it so it's def in the right place.
    graph.label(neighbor, edge); // rewire the "previous" cuz found a new shortest path.
    distancesSet.put(neighbor, currentNeighborsDist);
    vtxQueue.add(neighbor);
  }

  private static class SmallerDistanceHigherPriority implements Comparator<Vertex<String>> {

    HashMap<Vertex<String>, Double> distancesSet;

    private SmallerDistanceHigherPriority(HashMap<Vertex<String>, Double> d) {
      distancesSet = d;
    }

    @Override
    public int compare(Vertex<String> v1, Vertex<String> v2) {
      return Double.compare(distancesSet.get(v1), distancesSet.get(v2));
    }


  }

}
