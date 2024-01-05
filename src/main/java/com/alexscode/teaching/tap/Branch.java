package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Branch implements TAPSolver
{
  private static interface Operator
  {
    public double run(double distance, double cost, double interest);
  }

  @Override
  public List<Integer> solve(Instance ist) {
    Objectives obj = new Objectives(ist);
    List<Integer> output = new ArrayList<>();

    List<Operator> operators = new ArrayList<>();
    operators.add((distance, cost, interest) -> { return      distance     /        interest;});
    operators.add((distance, cost, interest) -> { return      cost         /        interest;});
    operators.add((distance, cost, interest) -> { return  distance * cost  /        interest;});
    operators.add((distance, cost, interest) -> { return      distance     / (interest * interest);});
    operators.add((distance, cost, interest) -> { return      cost         / (interest * interest);});
    operators.add((distance, cost, interest) -> { return (distance * cost) / (interest * interest);});
    
    double highestInterest = Double.MIN_NORMAL;

    for(Operator o : operators)
    {
      double[][] ratios = new double[ist.size][ist.size];
      for(int i = 0; i < ist.size; i++)
        for(int j = 0; j < ist.size; j++)
          ratios[i][j] = o.run(ist.distances[i][j], ist.costs[j], ist.interest[j]);
      
      List<Integer> sequence = new ArrayList<>();

      while(obj.time(sequence) < ist.timeBudget && obj.distance(sequence) < ist.maxDistance)
      {
        double lowerBound = calculateLowerBound(ist, obj, ratios, sequence);
        int    index      = -1;
        for(int i = 0; i < ist.size; i++)
        {
          if(!sequence.contains(i))
          {
            List<Integer> seqTest = new ArrayList<Integer>(sequence);
            seqTest.add(i);
            double nextLowerBound = calculateLowerBound(ist, obj, ratios, seqTest);
            if(nextLowerBound >= lowerBound)
            {
              lowerBound = nextLowerBound;
              index = i;
            }
          }
        }
        if(index >= 0)
          sequence.add(index);
        else break;
      }
      sequence = sequence.subList(0, sequence.size() - 1);
      if(obj.interest(sequence) > highestInterest)
      {
        highestInterest = obj.interest(sequence);
        output          = new ArrayList<>(sequence);
      }
    }
    return output;
  }

  private List<Integer> bestRatioSequence(Instance ist, Objectives obj, double[][] ratios, List<Integer> startingSequence)
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
    return sequence.subList(0, sequence.size() - 1);
  }

  private double calculateLowerBound(Instance ist, Objectives obj,  double[][] ratios, List<Integer> startingSequence)
  {
    return obj.interest(bestRatioSequence(ist, obj, ratios, startingSequence));
  }
}
