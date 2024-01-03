package com.alexscode.teaching.utilities;

import java.util.Objects;

public class ReversiblePair<L, R> 
{
  public ReversiblePair(L _left, R _right)
  {
    left  = _left;
    right = _right;
  }

  @Override
  public boolean equals(Object o)
  {
    if(o == null || getClass() != o.getClass()) return false;
    ReversiblePair<?, ?> rPair = (ReversiblePair<?, ?>)o;
    return (Objects.equals(left, rPair.left)  && Objects.equals(right, rPair.right))
        || (Objects.equals(left, rPair.right) && Objects.equals(right, rPair.left));
  }

  public L left;
  public R right;
}