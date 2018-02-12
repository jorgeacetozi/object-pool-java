package com.jorgeacetozi.objectpool;

import java.util.Hashtable;
import java.util.function.Supplier;
import java.util.stream.IntStream;

final class BlockingPool<T> implements Pool<T> {
  private final int minSize;
  private final int maxSize;
  private final long expiration;
  private final Hashtable<T, Long> available;
  private final Hashtable<T, Long> using;

  private BlockingPool(final BlockingPoolBuilder<T> builder) {
    this.minSize = builder.minSize;
    this.maxSize = builder.maxSize;
    this.expiration = builder.expiration;
    this.available = new Hashtable<>(this.maxSize);
    this.using = new Hashtable<>(this.maxSize);

    IntStream.rangeClosed(1, 5).forEach((i) -> {
      this.available.put(builder.supplier.get(), this.expiration);
    });
  }

  @Override
  public synchronized T borrowFromPool() {
    return null;
  }

  @Override
  public void returnToPool(T obj) {}

  @Override
  public int getMinSize() {
    return this.minSize;
  }

  @Override
  public int getMaxSize() {
    return this.maxSize;
  }

  @Override
  public long getExpiration() {
    return this.expiration;
  }

  @Override
  public int availableObjects() {
    return this.available.size();
  }

  public static class BlockingPoolBuilder<T> {
    private Supplier<T> supplier;
    private int minSize;
    private int maxSize;
    private long expiration;

    public PoolMax minSize(int size) {
      this.minSize = size;
      return new PoolMax(this);
    }

    public class PoolMax {
      private BlockingPoolBuilder<T> builder;

      public PoolMax(BlockingPoolBuilder<T> builder) {
        this.builder = builder;
      }

      public PoolExpiration maxSize(int size) {
        builder.maxSize = size;
        return new PoolExpiration(builder);
      }
    }

    public class PoolExpiration {
      private BlockingPoolBuilder<T> builder;

      public PoolExpiration(BlockingPoolBuilder<T> builder) {
        this.builder = builder;
      }

      public PoolCreator expiration(long expiration) {
        this.builder.expiration = expiration;
        return new PoolCreator(this.builder);
      }
    }

    public class PoolCreator {
      private BlockingPoolBuilder<T> builder;

      public PoolCreator(BlockingPoolBuilder<T> builder) {
        this.builder = builder;
      }

      public PoolBuilder create(Supplier<T> supplier) {
        this.builder.supplier = supplier;
        return new PoolBuilder(this.builder);
      }
    }

    public class PoolBuilder {
      private BlockingPoolBuilder<T> builder;

      public PoolBuilder(BlockingPoolBuilder<T> builder) {
        this.builder = builder;
      }

      public BlockingPool<T> build() {
        return new BlockingPool<T>(this.builder);
      }
    }
  }
}
