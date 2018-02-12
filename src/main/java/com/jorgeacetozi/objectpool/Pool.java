package com.jorgeacetozi.objectpool;

import com.jorgeacetozi.objectpool.BlockingPool.BlockingPoolBuilder;

public interface Pool<T> {
  
  public abstract T borrowFromPool();

  public abstract void returnToPool(T obj);

  public abstract int getMinSize();

  public abstract int getMaxSize();

  public abstract long getExpiration();

  public abstract int availableObjects();

  public static <T> BlockingPoolBuilder<T> blocking() {
    return new BlockingPoolBuilder<>();
  }
  
}
