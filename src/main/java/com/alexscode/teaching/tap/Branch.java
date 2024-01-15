package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithme Branch&Bound
 * Utilise l'algorithme Best Ratio First pour le calcul des bornes inférieures
 * Parcours du meilleur noeud, les noeuds ayant une borne inférieure plus faible que la précédente sont ignorés...
 * Utilisation de plusieurs opérateurs qui peuvent avoir des résultats meilleurs dans certaines situations.
 * @author Maxime Emonnot
 */
public class Branch implements TAPSolver
{  
  /**
  * Interface Operateur
  * Equivalent à un simple std::function<void(double, double, double)> en C++
  * @author Maxime Emonnot
  */
  private static interface Operator
  {
    public double calculate(double distance, double cost, double interest);
  }

  @Override
  public List<Integer> solve(Instance ist) 
  {
    Objectives obj = new Objectives(ist);
    List<Integer> output = new ArrayList<>();
    // Déclaration des opérateurs (Utilisés lors du calcul de la borne inférieure)
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
      // Calcul des ratios (On souhaite le ratio le plus petit)
      double[][] ratios = new double[ist.size][ist.size];
      for(int i = 0; i < ist.size; i++)
        for(int j = 0; j < ist.size; j++)
          ratios[i][j] = o.calculate(ist.distances[i][j], ist.costs[j], ist.interest[j]);
      
      List<Integer> sequence = new ArrayList<>();

      /*
       * Boucle d'exécution : On parcourt un ensemble de situations où l'on ajoute une certaine requête à une liste de requêtes.
       * Pour chaque étape, on calcule la borne inférieure des étapes filles pour n'en retenir que la meilleure.
       */
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
        if(index >= 0) sequence.add(index);
      }
      sequence = sequence.subList(0, sequence.size() - 1);
      // Si la séquence est meilleure que la précédente, alors elle est enregistrée et l'ancienne (si elle existe) est écrasée 
      if(obj.interest(sequence) > highestInterest)
      {
        highestInterest = obj.interest(sequence);
        output          = new ArrayList<>(sequence);
      }
    }
    return output;
  }

  /**
   * Reprise de l'algorithme Best Ratio First pour calculer la borne inférieure
   * @param ist              -> Instance de requêtes dont on cherche la meilleure séquence
   * @param obj              -> Objectifs coût et distance
   * @param ratios           -> Ratios préalablement calculés pour le BRF
   * @param startingSequence -> Séquence de démarrage (peut être vide)
   * @see                       BestRatioFirst
   * @return                    Séquence générée
   */
  private List<Integer> bestRatioFirstSequence(Instance ist, Objectives obj, double[][] ratios, List<Integer> startingSequence)
  {
    List<Integer> sequence = new ArrayList<>(startingSequence);

    while(obj.distance(sequence) < ist.maxDistance && obj.time(sequence) < ist.timeBudget)
    {
      double lowestRatio = Double.MAX_VALUE;
      int    bestIndex   = 0;

      if(sequence.isEmpty())
      {
        for(int i = 0; i < ist.size; i++)
          for(int j = 0; j < ist.size; j++)
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
      else
        for(int i = 0; i < ist.size; i++)
          if(sequence.get(sequence.size() - 1) != i && !sequence.contains(i))
          {
            double ratio = ratios[sequence.get(sequence.size() - 1)][i];
            if(ratio < lowestRatio)
            {
              bestIndex   = i;
              lowestRatio = ratio;
            }
          }

      sequence.add(bestIndex);
    }
    return sequence.subList(0, sequence.size() - 1);
  }

  private double calculateLowerBound(Instance ist, Objectives obj, double[][] ratios, List<Integer> startingSequence)
  {
    return obj.interest(bestRatioFirstSequence(ist, obj, ratios, startingSequence));
  }
}
