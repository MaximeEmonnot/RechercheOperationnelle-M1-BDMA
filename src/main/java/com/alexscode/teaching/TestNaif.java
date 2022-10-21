package com.alexscode.teaching;

import com.alexscode.teaching.tap.Instance;
import com.alexscode.teaching.tap.TAPSolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestNaif implements TAPSolver {
    @Override
    public List<Integer> solve(Instance ist) {

        List<Integer> test = new ArrayList<>();
        test.add(0);
        test.add(3);

        return test;
    }
}
