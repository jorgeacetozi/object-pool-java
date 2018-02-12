package com.jorgeacetozi.objectpool;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class PoolTest {

  @Test
  public void shouldCreateBlockingPool() {
    Pool<Object> pool =
        Pool.blocking().minSize(5).maxSize(10).expiration(10000l).create(Object::new).build();

    assertThat(pool.getMinSize(), is(5));
    assertThat(pool.getMaxSize(), is(10));
    assertThat(pool.getExpiration(), is(10000l));
    assertThat(pool.availableObjects(), is(5));
  }

}
