package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor implements TAPSolver {

    @Override
    public List<Integer> solve(Instance ist) {
        Objectives obj = new Objectives(ist);
        List<Integer> output = new ArrayList<>();

        // Indices des "noeuds" non-visités
        List<String> unvisitedIndices = new ArrayList<>();
        for(int i = 0; i < ist.size; i++)
            unvisitedIndices.add(Integer.toString(i));

        // Sélection du premier "noeud"
        String node = unvisitedIndices.get((int)(Math.random() * ist.size));
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

                double ratio = ist.distances[currentIndex][index] * ist.costs[index] / ist.interest[index];
        
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

        return output.subList(0, output.size() - 1);
    }
    
}
