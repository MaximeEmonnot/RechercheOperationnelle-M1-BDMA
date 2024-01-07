package com.alexscode.teaching;

import com.alexscode.teaching.tap.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Main {
    private static void runTest(TAPSolver solver, Instance inst)
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
        if(interest < minInterest && bIsFeasable) minInterest = interest;
        if(interest > maxInterest && bIsFeasable) maxInterest = interest;
        sumTime     += time;
        if(time < minTime && bIsFeasable)         minTime     = time;
        if(time > maxTime && bIsFeasable)         maxTime     = time;
        sumDistance += distance;
        if(distance < minDistance && bIsFeasable) minDistance = distance;
        if(distance > maxDistance && bIsFeasable) maxDistance = distance;
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

    private static void runTimedTest(TAPSolver smallMediumSolver, TAPSolver bigSolver, Instance inst)
    {
      if(inst.getSize() > 250)
        runTimedTest(bigSolver, inst);
      else
        runTimedTest(smallMediumSolver, inst);
    }
    private static void runTimedTest(TAPSolver solver, Instance inst)
    {     
      Objectives obj         = new Objectives(inst);

      long before            = System.currentTimeMillis();
      List<Integer> solution = solver.solve(inst);
      long after             = System.currentTimeMillis();

      double  interest       = obj.interest(solution);
      double  time           = obj.time(solution);
      double  distance       = obj.distance(solution);
      boolean bIsFeasable    = isSolutionFeasible(inst, solution); 
      System.out.println("\n\n===========================================================================================");
      System.out.println("Interet:            " + interest);
      System.out.println("Temps:              " + time);
      System.out.println("Distance:           " + distance);
      System.out.println("Faisable ?          " + bIsFeasable);
      System.out.println("Durée d'exécution : " + (after - before) + "ms");
      System.out.println("===========================================================================================");
    }

    private static void runTimedTests(TAPSolver smallMediumSolver, TAPSolver bigSolver, Instance inst, int nTests)
    {
      if(inst.getSize() > 250) 
        runTimedTests(bigSolver, inst, nTests);
      else
        runTimedTests(smallMediumSolver, inst, nTests);
    }
    private static void runTimedTests(TAPSolver solver, Instance inst, int nTests)
    {      
      Objectives obj  = new Objectives(inst);

      double sumInterest = 0.0;
      double sumTime     = 0.0;
      double sumDistance = 0.0;
      long   nFeasable   = 0;
      long   sumExecTime = 0;

      for(int i = 0; i < nTests; i++)
      {
        long before            = System.currentTimeMillis();
        List<Integer> solution = solver.solve(inst);
        long after             = System.currentTimeMillis();

        double  interest       = obj.interest(solution);
        double  time           = obj.time(solution);
        double  distance       = obj.distance(solution);
        boolean bIsFeasable    = isSolutionFeasible(inst, solution); 

        sumInterest           += interest;
        sumTime               += time;
        sumDistance           += distance;
        nFeasable             += (bIsFeasable ? 1 : 0);
        sumExecTime           += (after - before);
      }

      System.out.println("\n\n==================================== Résumé ===============================================");
      System.out.println("Interet moyen :     " +  sumInterest / nTests);
      System.out.println("Temps moyen :       " +  sumTime     / nTests);
      System.out.println("Distance moyenne :  " +  sumDistance / nTests);
      System.out.println("Ratio faisabilité : " + (nFeasable   / nTests) * 100 + "%");
      System.out.println("Durée d'exécution : " +  sumExecTime / nTests  + "ms");
      System.out.println("===========================================================================================");
    }

    public static void main(String[] args) {
        // Instances de petite taille
        Instance f4_small      = Instance.readFile("./instances/f4_tap_0_20.dat",   330,  27);
        Instance tap_15_small  = Instance.readFile("./instances/tap_15_60.dat",     330,  27);
        // Instances de taille moyenne
        Instance tap_10_medium = Instance.readFile("./instances/tap_10_100.dat",   1200, 150);
        Instance tap_11_medium = Instance.readFile("./instances/tap_11_250.dat",   1200, 250);
        Instance tap_13_medium = Instance.readFile("./instances/tap_13_150.dat",   1200, 150);
        // Instances de grande taille
        Instance f4_1_big      = Instance.readFile("./instances/f4_tap_1_400.dat", 6600, 540);
        Instance f4_4_big      = Instance.readFile("./instances/f4_tap_4_400.dat", 6600, 540);
        Instance f1_3_big      = Instance.readFile("./instances/f1_tap_3_400.dat", 6600, 540);
        Instance f1_9_big      = Instance.readFile("./instances/f1_tap_9_400.dat", 6600, 540);
        Instance tap_14_big    = Instance.readFile("./instances/tap_14_400.dat",   6600, 540);

        // Définition des paramètres de test
        TAPSolver      smallMediumSolver = new Branch();
        TAPSolver      bigSolver         = new BestRatioFirst();
        int            nTests            = 10;
        List<Instance> instances         = new ArrayList<>();
        instances.add(f4_small);        
        instances.add(tap_15_small);
        instances.add(tap_10_medium);
        instances.add(tap_11_medium);
        instances.add(tap_13_medium);
        instances.add(f4_1_big);
        instances.add(f4_4_big);
        instances.add(f1_3_big);
        instances.add(f1_9_big);
        instances.add(tap_14_big);

        // Exécution : Pour chaque instance, on affiche le résultat moyen ainsi que la durée d'exécution moyenne
        for(Instance inst : instances)
          runTimedTests(smallMediumSolver, bigSolver, inst, nTests);
    }

    public static boolean isSolutionFeasible(Instance ist, List<Integer> sol){
        Objectives obj = new Objectives(ist);
        return obj.time(sol) <= ist.getTimeBudget() && obj.distance(sol) <= ist.getMaxDistance() && sol.size() == (new TreeSet<>(sol)).size();
    }
}
