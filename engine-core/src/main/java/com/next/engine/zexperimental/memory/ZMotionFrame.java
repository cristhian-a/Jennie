package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

@Experimental
final class ZMotionFrame {

    static final MemoryLayout MOTION_LAYOUT =
            MemoryLayout.structLayout(
                    ValueLayout.JAVA_FLOAT.withName("entityId"),
                    ValueLayout.JAVA_FLOAT.withName("dx"),
                    ValueLayout.JAVA_FLOAT.withName("dy"),
                    ValueLayout.JAVA_FLOAT.withName("dz")
            ).withName("motionFrame");

    static final class Storage {
        static final VarHandle ENTITY = MOTION_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("entityId")).withInvokeExactBehavior();

        static final VarHandle DX = MOTION_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("dx")).withInvokeExactBehavior();

        static final VarHandle DY = MOTION_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("dy")).withInvokeExactBehavior();

        static final VarHandle DZ = MOTION_LAYOUT.varHandle(
                MemoryLayout.PathElement.groupElement("dz")).withInvokeExactBehavior();

        final MemorySegment data;
        final int capacity;
        int size;

        Storage(Arena arena, int capacity) {
            this.capacity = capacity;
            this.data = arena.allocate(MOTION_LAYOUT, capacity);
        }

        void submit(int entityId, float dx, float dy, float dz) {
            int i = size++;
            ENTITY.set(data, 0L, (long) i, entityId);
            DX.set(data, 0L, (long) i, dx);
            DY.set(data, 0L, (long) i, dy);
            DZ.set(data, 0L, (long) i, dz);
        }
    }
}
