package com.next.engine.data;

import com.next.engine.annotations.internal.Experimental;

import java.util.Arrays;

/**
 * {@link Experimental} <br/>
 * Primitive int to an object map that uses open addressing and is better suited for lookups than {@link SmallIntObjectMap},
 * although it performs worse when dealing with a tiny number of entries (less than 8~16).
 */
@Experimental
public final class IntObjectMap {

    private static final int EMPTY = 0x80000000;

    private int[] keys;
    private Object[] values;
    private int mask;
    private int size;

    public IntObjectMap(int initialCapacity) {
        int cap = 1;
        while (cap < initialCapacity) {
            cap = cap << 1;
        }

        keys = new int[cap];
        values = new Object[cap];
        Arrays.fill(keys, EMPTY);

        mask = cap - 1;
    }

    public IntObjectMap() {
        this(16);
    }

    public int size() {
        return size;
    }

    public Object get(int key) {
        int idx = findSlot(key);
        if (keys[idx] == key) {
            return values[idx];
        }
        return null;
    }

    public void put(int key, Object value) {
        if (size * 10 >= keys.length * 6) {
            resize();
        }

        int idx = findSlot(key);

        if (keys[idx] != key) {
            keys[idx] = key;
            size++;
        }

        values[idx] = value;
    }

    private int findSlot(int key) {
        int idx = mix(key) & mask;

        while (true) {
            int k = keys[idx];
            if (k == EMPTY || k == key) {
                return idx;
            }
            idx = (idx + 1) & mask;
        }
    }

    private void resize() {
        int[] oldKeys = keys;
        Object[] oldValues = values;

        int newCap = oldKeys.length << 1;

        keys = new int[newCap];
        values = new Object[newCap];
        Arrays.fill(keys, EMPTY);
        mask = newCap - 1;
        size = 0;

        for (int i = 0; i < oldKeys.length; i++) {
            int k = oldKeys[i];
            if (k != EMPTY) {
                put(k, oldValues[i]);
            }
        }
    }

    private static int mix(int x) {
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x;
    }
}
