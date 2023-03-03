
package cn.com.infosec.data.structure.asn.support;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Keeps count of instances of the same element.
 *
 * @param <T> element type
 */
public class Counter<T> {
  private final Map<T, AtomicInteger> internalCounterMap;

  public Counter() {
    this(new HashMap<>());
  }

  public Counter(Map<T, AtomicInteger> internalCounterMap) {
    this.internalCounterMap = internalCounterMap;
  }

  /**
   * Returns the count of elements that were added for the given key.
   *
   * @param key key
   * @return count
   */
  public int count(final T key) {
    return internalCounterMap.computeIfAbsent(key, k -> new AtomicInteger())
        .getAndIncrement();
  }
}
