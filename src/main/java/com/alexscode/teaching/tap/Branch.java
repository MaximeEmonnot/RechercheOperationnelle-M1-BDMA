package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Branch implements TAPSolver
{
  @Override
  public List<Integer> solve(Instance ist) {
    Objectives obj = new Objectives(ist);
    List<Integer> output = new ArrayList<>();

    double[][] ratios = new double[ist.size][ist.size];
    for(int i = 0; i < ist.size; i++)
      for(int j = 0; j < ist.size; j++)
        ratios[i][j] = (ist.distances[i][j] * ist.costs[j]) / ist.interest[j]; 
    
    
    double lowerBound = calculateLowerBound(ist, obj, ratios, output);

    recursiveBranch(ist, obj, ratios, output, lowerBound);

    return output;
  }

  private void recursiveBranch(Instance ist, Objectives obj, double[][] ratios, List<Integer> sequence, double lowerBound)
  {
    for(int i = 0; i < ist.size; i++)
    {
      if(!sequence.contains(i))
      {
        List<Integer> newSequence = new ArrayList<>(sequence);
        newSequence.add(i);
        double newLowerBound = calculateLowerBound(ist, obj, ratios, newSequence);
        if(lowerBound < newLowerBound)
          recursiveBranch(ist, obj, ratios, newSequence, newLowerBound);
      }
    }
  }

  private double calculateLowerBound(Instance ist, Objectives obj,  double[][] ratios, List<Integer> startingSequence)
  {
    List<Integer> sequence = new ArrayList<>(startingSequence);

    while(obj.distance(sequence) < ist.maxDistance && obj.time(sequence) < ist.timeBudget)
    {
      double lowestRatio = Double.MAX_VALUE;
      int    bestIndex   = 0;

      if(sequence.isEmpty())
      {
        for(int i = 0; i < ist.size; i++)
        {
          for(int j = 0; j < ist.size; j++)
          {
            if(i != j)
            {
              double ratio = ratios[i][j];
              if(ratio < lowestRatio)
              {
                bestIndex   = i;
                lowestRatio = ratio;
              }
            }
          }
        }
      }
      else
      {
        for(int i = 0; i < ist.size; i++)
        {
          if(sequence.get(sequence.size() - 1) != i && !sequence.contains(i))
          {
            double ratio = ratios[sequence.get(sequence.size() - 1)][i];
            if(ratio < lowestRatio)
            {
              bestIndex   = i;
              lowestRatio = ratio;
            }
          }
        }
      }

      sequence.add(bestIndex);
    }

    return obj.interest(sequence.subList(0, sequence.size() - 1));
  }
}
