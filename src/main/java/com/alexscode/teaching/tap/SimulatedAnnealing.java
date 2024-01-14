package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithme Recuit Simulé
 * Rapidement abandonné en raison de résultats peu concluants et d'une exécution trop lente
 * @author Maxime Emonnot
 */
public class SimulatedAnnealing implements TAPSolver
{
  @Override
  public List<Integer> solve(Instance ist) {
    Objectives obj = new Objectives(ist);
    List<Integer> output = new ArrayList<>();

    // Factor declarations
    final double tempMax = 500000.0;
    final double tempMin = 0.0001;
    final double alpha   = 0.85;

    double temperature = tempMax;

    // Generate Candidate Solution (Nearest Neighbor)
    // Indices des "noeuds" non-visités + sélection du premier "noeud"
    List<String> unvisitedIndices = new ArrayList<>();
    double minRatio = Double.MAX_VALUE;
    String node = "0";
    for(int i = 0; i < ist.size; i++)
    {
      String currNode = Integer.toString(i);
      unvisitedIndices.add(currNode);
      double ratio = ist.costs[i] / ist.interest[i];
      if(ratio < minRatio)
      {
        minRatio = ratio;
        node = currNode;
      }
    }
    output.add(Integer.parseInt(node));
    unvisitedIndices.remove(node);

    // Exécution de la boucle
    while(!unvisitedIndices.isEmpty() 
    &&   (obj.time(output) < ist.timeBudget && obj.distance(output) < ist.maxDistance))
    {
      String nearestNode = new String();
      double nearestRatio = Double.MAX_VALUE;
          
      for(String indexStr : unvisitedIndices)
      {
        int index = Integer.parseInt(indexStr);
        int currentIndex = Integer.parseInt(node);
        double ratio = ist.distances[currentIndex][index] / ist.interest[index];
        
        if(ratio < nearestRatio)
        {
          nearestRatio = ratio;
          nearestNode = indexStr;
        }
      }

      output.add(Integer.parseInt(nearestNode));
      unvisitedIndices.remove(nearestNode);
      node = nearestNode;
    }
    output = output.subList(0, output.size() - 1);

    // Compute the Energy of initial Solution
    double currentEnergy = obj.distance(output);

    while(temperature > tempMin)
    {
      // Generate a new list (we remove an item and replace it with another that is not inserted)
      int i = (int)(Math.random() * output.size());
      int j = (int)(Math.random() * unvisitedIndices.size());
      List<Integer> newOutput = new ArrayList<>();
      for(int val : output) 
      {
        if(val != i)
          newOutput.add(val);
        else
          newOutput.add(Integer.parseInt(unvisitedIndices.get(j)));
      }

      // Compute new Energy
      double newEnergy   = obj.distance(output);
      double deltaEnergy = newEnergy - currentEnergy;
      
      if(accept(temperature, deltaEnergy) && obj.time(newOutput) < ist.timeBudget && obj.distance(newOutput) < ist.maxDistance)
      {
        output        = newOutput;
        currentEnergy = newEnergy;

        unvisitedIndices.add(Integer.toString(i));
        unvisitedIndices.remove(Integer.toString(j));
      }
      temperature *= alpha;
    }


    return output;
  }

  private boolean accept(double temperature, double deltaEnergy)
  {
    return (deltaEnergy < 0) || (Math.random() < Math.exp(-deltaEnergy / temperature));
  }
}
