package com.next.engine.debug;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public final class MemoryTool implements DevTool {

    public static final String HEAP_USED = "memory.heap.used";
    public static final String HEAP_COMMITED = "memory.heap.commited";
    public static final String HEAP_MAX = "memory.heap.max";
    public static final String GC_COLLECTIONS = "memory.gc.collections";
    public static final String GC_WORK_TIME = "memory.gc.work_time";

    private static final String LABEL_HEAP_USED = "HEAP used: ";
    private static final String LABEL_HEAP_COMMITED = "HEAP comm: ";
    private static final String LABEL_HEAP_MAX = "HEAP max: ";
    private static final String LABEL_GC_COLLECTIONS = "GC collections: ";
    private static final String LABEL_GC_WORK_TIME = "GC work time: ";
    private static final String LABEL_CLASSES_LOADED = "CLASSES loaded: ";
    private static final String LABEL_CLASSES_TOTAL = "CLASSES total: ";
    private static final String LABEL_CLASSES_UNLOADED = "CLASSES unloaded: ";

    private static final long ONE_MB = 1_048_576;

    private long used, committed, max;
    private long gcCount, gcWorkTime;
    private int frameCounter = 0;

    @Override
    public void update() {
        if (frameCounter++ % 60 != 0) return;

        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();

        used = heap.getUsed() / ONE_MB;
        committed = heap.getCommitted() / ONE_MB;
        max = heap.getMax() / ONE_MB;

        gcCount = 0;
        gcWorkTime = 0;
        for (var gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCount += gc.getCollectionCount();
            gcWorkTime += gc.getCollectionTime();
        }
    }

    @Override
    public void emit(DebugSink sink) {
        sink.text(HEAP_USED, LABEL_HEAP_USED + used, 10, 200, channel());
        sink.text(HEAP_COMMITED, LABEL_HEAP_COMMITED + committed, 10, 230, channel());
        sink.text(HEAP_MAX,  LABEL_HEAP_MAX + max, 10, 260, channel());

        sink.text(GC_COLLECTIONS, LABEL_GC_COLLECTIONS + gcCount, 10, 300, channel());
        sink.text(GC_WORK_TIME,  LABEL_GC_WORK_TIME + gcWorkTime + " ms", 10, 330, channel());
    }

    @Override
    public DebugChannel channel() {
        return DebugChannel.MEMORY;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
