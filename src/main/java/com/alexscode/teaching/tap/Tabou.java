package com.alexscode.teaching.tap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alexscode.teaching.utilities.ReversiblePair;

public class Tabou implements TAPSolver{
  @Override
  public List<Integer> solve(Instance ist) {
    Objectives obj = new Objectives(ist);
    List<Integer> currentSolution = generateInitialSolution(ist);

    List<Integer> bestSolution = new ArrayList<>(currentSolution);
    double bestSolutionCost = obj.distance(bestSolution);

    int tabuListSize = ist.size;
    List<ReversiblePair<Integer, Integer>> tabuList = new ArrayList<>();

    int maxIteration = 1;

    for (int i=0 ; i < maxIteration; i++) {
      List<Integer> neighbor = findBestNeighbor(currentSolution, tabuList, obj, ist);

      double neighborCost = obj.distance(neighbor);

      if (neighborCost < bestSolutionCost) {
        bestSolution = new ArrayList<>(neighbor);
        bestSolutionCost = neighborCost;
      }

      currentSolution = neighbor;
    }

    return bestSolution;
  }

  private List<Integer> generateInitialSolution(Instance ist) {
    TAPSolver solver = new NearestNeighbor();
    return solver.solve(ist);
}

private List<Integer> findBestNeighbor(List<Integer> solution, List<ReversiblePair<Integer, Integer>> tabuList, Objectives obj, Instance ist) {
    TAPSolver solver = new NearestNeighbor();

    int oldindex = 0;
    int newindex = 0;

    double bestNeighborCost = Double.MAX_VALUE;
    List<Integer> bestNeighbor = new ArrayList<>();
    for (int i = 0; i < solution.size(); i++) {
        for (int j = 0; j < ist.size; j++) {
            if (solution.get(i) != j && !solution.contains(j))  {
                // Create a neighbor by swapping the positions of two nodes in the solution
                int currentIndex = solution.get(i);
                
                List<Integer> neighbor = new ArrayList<>(solution);
                neighbor.set(i,j);
                
                // Check if the neighbor is in the tabu list
              if(!tabuList.contains(new ReversiblePair<Integer, Integer>(currentIndex, j)))
              {
              double neighborCost = obj.distance(neighbor);

                // Update the best neighbor if the cost is lower
              if (neighborCost < bestNeighborCost) {
                bestNeighborCost = neighborCost;
                bestNeighbor = new ArrayList<>(neighbor);
                oldindex = currentIndex;
                newindex = j;
              }
              }
            
          }
    }

}
  tabuList.add(new ReversiblePair<Integer,Integer>(oldindex, newindex));
  return bestNeighbor;
}
}


