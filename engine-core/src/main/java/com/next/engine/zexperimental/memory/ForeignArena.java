package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collectors;

@Experimental
class ForeignArena {

    /**
     * Describing the {@code Arena}s provided by the JDK.
     */
    void of() {
        Arena arena = Arena.global();   // Shared arena for the whole application.
                                        // the global arena is only closed when the application exits.
        arena.close();                  // throws an UnsupportedOperationException

        Arena auto = Arena.ofAuto();    // its mem segments will be deallocated by the gc when the arena ref
                                        // turns unreachable. Can't be closed otherwise
        auto.close();                   // throws an UnsupportedOperationException

        Arena shared = Arena.ofShared();    // This arena is meant to be shared between threads.
        shared.close();                     // Its close operation is quite expensive.

        Arena conf = Arena.ofConfined();    // This arena is meant to be used by a single thread.
        conf.close();                       // Prefer it if no multi-threading is involved.

        // Whenever an arena is closed, all its memory segments will be deallocated.
    }

    void alloc() {
        Arena arena = Arena.global();
        MemorySegment memorySegment = arena.allocate(ValueLayout.JAVA_INT, 10L);
        // the above line is basically a pointer, equivalent to:
        // int* ptr = malloc(sizeof(int) * 10);

        for (long offset = 0L; offset < 10L; offset++) {
            memorySegment.setAtIndex(
                    ValueLayout.JAVA_INT, offset, 100
            );
        }
        
        var i = new int[10];
        for (int offset = 0; offset < 10; offset++) {
            i[offset] = memorySegment.getAtIndex(ValueLayout.JAVA_INT, offset);
        }
        IO.println(Arrays.toString(i));
    }

    void allocFrom() {
        var arena = Arena.ofAuto();
        var memSegment = arena.allocateFrom(ValueLayout.JAVA_INT, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

        var i = new int[10];
        for (int offset = 0; offset < 10; offset++) {
            i[offset] = memSegment.getAtIndex(ValueLayout.JAVA_INT, offset);
        }
        IO.println(Arrays.toString(i));
    }

    void memLayout() {
        // VERY IMPORTANT!!
        // Put all of these layouts and var handle objects in static final fields so the
        // JVM can properly optimize them.

        /*
        struct Data {
            int id;
            float value;
        }
         */
        final MemoryLayout DATA_LAYOUT =
                MemoryLayout.structLayout(
                        ValueLayout.JAVA_INT.withName("id"),
                        ValueLayout.JAVA_FLOAT.withName("value")
                ).withName("data");

        final VarHandle DATA_ID_VH =
                DATA_LAYOUT.varHandle(
                        MemoryLayout.PathElement.groupElement("id")
                );
        final VarHandle DATA_VALUE_VH =
                DATA_LAYOUT.varHandle(
                        MemoryLayout.PathElement.groupElement("value")
                );

        try (var arena = Arena.ofConfined()) {
            var memSegment = arena.allocate(DATA_LAYOUT, 1L);
            DATA_ID_VH.set(memSegment, 0L, 1);
            DATA_VALUE_VH.set(memSegment, 0L, 100.0f);

            var id = DATA_ID_VH.get(memSegment, 0L);
            var value = DATA_VALUE_VH.get(memSegment, 0L);
            IO.println(id + ", " + value);
        }

        // Example with arrays
        VarHandle DATA_ID_ARRAY_VH =
                DATA_LAYOUT.arrayElementVarHandle(
                        MemoryLayout.PathElement.groupElement("id")
                ).withInvokeExactBehavior();
        VarHandle DATA_VALUE_ARRAY_VH =
                DATA_LAYOUT.arrayElementVarHandle(
                        MemoryLayout.PathElement.groupElement("value")
                ).withInvokeExactBehavior();
        // withInvokeExactBehavior is used here, so when we cast our primitive values, we don't have
        // the boxing/unboxing overhead.

        try (var arena = Arena.ofConfined()) {
            var memSegment = arena.allocate(DATA_LAYOUT, 4L);

            for (long i = 0L; i < 4L; i++) {
                DATA_ID_ARRAY_VH.set(memSegment, 0L, i, 10 + (int) i);
                DATA_VALUE_ARRAY_VH.set(memSegment, 0L, i, 100.0f * i);
            }

            for (long i = 0L; i < 4L; i++) {
                var x = (int) DATA_ID_ARRAY_VH.get(memSegment, 0L, i);      // no boxing in this get
                var y = (float) DATA_VALUE_ARRAY_VH.get(memSegment, 0L, i); // no boxing in this get
                IO.println(x + ", " + y);
            }
        }

        /*
        This next example is for dealing with data alignment in the memory. Consider the following structure:
        struct Element {
            char id;
            int data;
            byte extra;
        }

        Because of the way its data is organized, it will cause misalignment in the memory, leading to the CPU maybe
        taking more cycles to read the memory.
        For this reason, it is good to keep the data properly aligned, and for that we use MemoryLayout.paddingLayout.

        For a primitive to be aligned, it must be stored in an address that is divisible by the number of bytes for
        that primitive type.
        byte    1 byte
        short   2 bytes
        char    2 bytes
        int     4 bytes
        float   4 bytes
        long    8 bytes
        double  8 bytes

        Remember to also consider proper alignment in the case the elements are stored in an array, so it might be
        necessary to properly pad the end of the data structure as well, so the next element is aligned.

        Doing the padding leads to a less compact memory layout but is friendly towards the cpu and will lead to less
        cycles to retrieve data, which is good in our application.

        Example: */
        final MemoryLayout ELEMENT_LAYOUT =
                MemoryLayout.structLayout(
                        ValueLayout.JAVA_CHAR,
                        MemoryLayout.paddingLayout(2L),
                        ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_BYTE,
                        MemoryLayout.paddingLayout(3L)
                ).withName("element");
    }

    static final class MEM_EX {
        static final MemoryLayout DATA_LAYOUT =
                MemoryLayout.structLayout(
                        ValueLayout.JAVA_INT.withName("id"),
                        ValueLayout.JAVA_FLOAT.withName("value")
                ).withName("data");

        static final VarHandle DATA_ID_VH =
                DATA_LAYOUT.varHandle(
                        MemoryLayout.PathElement.groupElement("id")
                ).withInvokeExactBehavior();

        static final VarHandle DATA_VALUE_VH =
                DATA_LAYOUT.varHandle(
                        MemoryLayout.PathElement.groupElement("value")
                ).withInvokeExactBehavior();

        void mappingWithFile() {
            var path = Path.of("Users/User/Desktop/Arquivo/data.bin");
            try (var fileChannel = FileChannel.open(path,
                    StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
                 var arena = Arena.ofShared()
            ) {

                var offset = 0L;
                var size = 1_024 * DATA_LAYOUT.byteSize();

                var segment = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, size, arena);

                // examples of file reading
                var map = segment.elements(DATA_LAYOUT)
                        .parallel()    // parallel works only with shared arenas
                        .collect(Collectors.groupingBy(
                                sgmt -> (int) DATA_ID_VH.get(sgmt, 0L),
                                Collectors.counting()
                        ));

                var avgMap = segment.elements(DATA_LAYOUT)
                        .parallel()
                        .collect(Collectors.groupingBy(
                                sgmt -> (int) DATA_ID_VH.get(sgmt, 0L),
                                Collectors.averagingDouble(
                                        sgmt -> (float) DATA_VALUE_VH.get(sgmt, 0L)
                                )
                        ));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
