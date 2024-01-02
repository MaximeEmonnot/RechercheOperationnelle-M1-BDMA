package com.alexscode.teaching.tap;

import java.util.ArrayList;
import java.util.List;

public class Branch implements TAPSolver
{
  @Override
  public List<Integer> solve(Instance ist) {
    Objectives obj = new Objectives(ist);
    List<Integer> output = new ArrayList<>();

    return output;
  }
}
