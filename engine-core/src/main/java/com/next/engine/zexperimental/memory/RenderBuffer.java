package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;

@Experimental
public class RenderBuffer {

    /**
     * As far as I am aware, using nio is the old way of doing off heap memory management. The way kids
     * do it nowadays is with {@link java.lang.foreign}. Check {@link ForeignArena} for an example.
     */
    public void alloc() {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(1024);
    }
}
