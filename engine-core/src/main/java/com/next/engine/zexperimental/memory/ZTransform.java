package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

@Experimental
final class ZTransform {

    static final MemoryLayout TRANSFORM_LAYOUT =
            MemoryLayout.structLayout(
                    ValueLayout.JAVA_FLOAT.withName("x"),
                    ValueLayout.JAVA_FLOAT.withName("y"),
                    ValueLayout.JAVA_FLOAT.withName("vx"),
                    ValueLayout.JAVA_FLOAT.withName("vy")
            ).withName("transform");

    static final class Storage {
        static final VarHandle X = TRANSFORM_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("x")).withInvokeExactBehavior();

        static final VarHandle Y = TRANSFORM_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("y")).withInvokeExactBehavior();

        static final VarHandle VX = TRANSFORM_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("vx")).withInvokeExactBehavior();

        static final VarHandle VY = TRANSFORM_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("vy")).withInvokeExactBehavior();

        final MemorySegment data;
        final int capacity;
        int size;

        Storage(Arena arena, int capacity) {
            this.capacity = capacity;
            this.data = arena.allocate(TRANSFORM_LAYOUT, capacity);
        }

        int create(float x, float y, float vx, float vy) {
            int id = size++;
            X.set(data, 0L, (long) id, x);
            Y.set(data, 0L, (long) id, y);
            VX.set(data, 0L, (long) id, vx);
            VY.set(data, 0L, (long) id, vy);
            return id;
        }
    }
}
