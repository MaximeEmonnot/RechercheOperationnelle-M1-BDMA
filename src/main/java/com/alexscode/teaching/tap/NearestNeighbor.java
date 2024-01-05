package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor implements TAPSolver {

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
        
        double highestInterest = Double.MIN_VALUE;
        for(Operator o : operators)
        {
          // Calcul des ratios (On souhaite le ratio le plus petit)
          double[][] ratios = new double[ist.size][ist.size];
          for(int i = 0; i < ist.size; i++)
            for(int j = 0; j < ist.size; j++)
              ratios[i][j] = o.run(ist.distances[i][j], ist.costs[j], ist.interest[j]); 
    
          for(int n = 0; n < ist.size; n++)
          {
            List<Integer> sequence = new ArrayList<>();

            sequence.add(n);
            // Boucle d'exécution
            while(obj.distance(sequence) < ist.maxDistance && obj.time(sequence) < ist.timeBudget)
            {
              double lowestRatio = Double.MAX_VALUE;
              int    bestIndex   = 0;
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
              sequence.add(bestIndex);
            }
            sequence = sequence.subList(0, sequence.size() - 1);
            if(obj.interest(sequence) >= highestInterest)
            {
              highestInterest = obj.interest(sequence);
              output = new ArrayList<>(sequence);
            }
          }
        }
        return output;
    }
    
}
