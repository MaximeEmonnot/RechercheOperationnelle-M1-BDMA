package com.alexscode.teaching;

import com.alexscode.teaching.tap.*;

import java.util.List;
import java.util.TreeSet;

public class Main {
    private static void runTests(TAPSolver solver, Instance inst)
    {
      runTests(solver, inst, 1);
    }
    private static void runTests(TAPSolver solver, Instance inst, int nTests)
    {
      Objectives obj = new Objectives(inst);
    
      double sumInterest = 0.0;
      double minInterest = Double.MAX_VALUE;
      double maxInterest = Double.MIN_VALUE;
      double sumTime     = 0.0;
      double minTime     = Double.MAX_VALUE;
      double maxTime     = Double.MIN_VALUE;
      double sumDistance = 0.0;
      double minDistance = Double.MAX_VALUE;
      double maxDistance = Double.MIN_VALUE;
      int nFeasable      = 0;

      for(int i = 0; i < nTests; i++)
      {
        System.out.println("### TEST N°" + i + " ###");    

        List<Integer> solution = solver.solve(inst);
        
        double  interest    = obj.interest(solution);
        double  time        = obj.time(solution);
        double  distance    = obj.distance(solution);
        boolean bIsFeasable = isSolutionFeasible(inst, solution); 

        System.out.println("Interet: "   + interest);
        System.out.println("Temps: "     + time);
        System.out.println("Distance: "  + distance);
        System.out.println("Faisable ? " + bIsFeasable);

        sumInterest += interest;
        if(interest < minInterest) minInterest = interest;
        if(interest > maxInterest) maxInterest = interest;
        sumTime     += time;
        if(time < minTime)         minTime     = time;
        if(time > maxTime)         maxTime     = time;
        sumDistance += distance;
        if(distance < minDistance) minDistance = distance;
        if(distance > maxDistance) maxDistance = distance;
        nFeasable   += bIsFeasable ? 1 : 0;
      }
      System.out.println("\n\n===========================================================================================");
      System.out.println(" ### RESUME DES TESTS ###");
      System.out.println(" Nombre de tests :   " + nTests);
      System.out.println(" Interet moyen :     " + sumInterest / nTests     + " (Min : " + minInterest + " - Max : " + maxInterest + ")");
      System.out.println(" Temps moyen :       " + sumTime / nTests         + " (Min : " + minTime     + " - Max : " + maxTime     + ")");
      System.out.println(" Distance moyenne :  " + sumDistance / nTests     + " (Min : " + minDistance + " - Max : " + maxDistance + ")");
      System.out.println(" Ratio faisabilité : " + nFeasable * 100 / nTests + "%");
      System.out.println("===========================================================================================");

    }

    public static void main(String[] args) {
        Instance f4_small = Instance.readFile("./instances/f4_tap_0_20.dat", 330, 27);

        Instance f4_1_big = Instance.readFile("./instances/f4_tap_1_400.dat", 6600, 540);
        Instance f4_4_big = Instance.readFile("./instances/f4_tap_4_400.dat", 6600, 540);
        Instance f1_3_big = Instance.readFile("./instances/f1_tap_3_400.dat", 6600, 540);
        Instance f1_9_big = Instance.readFile("./instances/f1_tap_9_400.dat", 6600, 540);

        TAPSolver solver = new NearestNeighbor();
        Instance  inst   = f4_small;
        int       nTests = 50;
       
        runTests(solver, inst, nTests);
    }

    public static boolean isSolutionFeasible(Instance ist, List<Integer> sol){
        Objectives obj = new Objectives(ist);
        return obj.time(sol) <= ist.getTimeBudget() && obj.distance(sol) <= ist.getMaxDistance() && sol.size() == (new TreeSet<>(sol)).size();
    }
}
