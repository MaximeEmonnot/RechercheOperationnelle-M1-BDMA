package com.alexscode.teaching;

import com.alexscode.teaching.tap.Instance;
import com.alexscode.teaching.tap.Objectives;
import com.alexscode.teaching.tap.TAPSolver;

import java.util.List;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Instance f4_small = Instance.readFile("./instances/f4_tap_0_20.dat", 330, 27);

        Objectives obj = new Objectives(f4_small);

        TAPSolver solver = new TestNaif();
        List<Integer> solution = solver.solve(f4_small);

        System.out.println("Interet: " + obj.interest(solution));
        System.out.println("Temps: " + obj.time(solution));
        System.out.println("Distance: " + obj.distance(solution));

        System.out.println("Feasible ? " + isSolutionFeasible(f4_small, solution));
    }

    public static boolean isSolutionFeasible(Instance ist, List<Integer> sol){
        Objectives obj = new Objectives(ist);
        return obj.time(sol) <= ist.getTimeBudget() && obj.distance(sol) <= ist.getMaxDistance() && sol.size() == (new TreeSet<>(sol)).size();
    }
}