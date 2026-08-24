package com.next.engine.data;

import com.next.engine.annotations.internal.Experimental;
import lombok.Getter;

/**
 * {@link Experimental} This class should not be implemented without thought, as structure of arrays implementations
 * may differ a lot in their bases case by case. Extend it cautiously, and don't be afraid to ditch it if you get
 * yourself fighting this class's internals. If the reusability proves itself useful, it will pass from experimental
 * to production status.
 * <p>
 * Abstract base class for a buffered structure of arrays that provides dynamic resizing capabilities
 * and management of buffered data. This class includes functionality to monitor and regulate the
 * size of the data structure while ensuring performance and memory optimizations.
 * <p>
 * It defines generic operations such as clearing the table, resizing arrays, and managing the
 * capacity of the internal data structures. Subclasses are required to implement custom behavior
 * for handling the resizing of internal arrays.
 * <p>
 * Subclasses are expected to provide their own implementation of {@link #resizeArrays(int)}, while
 * other utility methods such as {@link #nextSlot()} and {@link #clear()} are provided as final
 * implementations.
 * <p>
 * Implements {@link Buffered} to allow compatibility with buffer management systems.
 * <p>
 * Thread safety: Implementing classes are responsible for their own synchronization if used
 * concurrently.
 */
@Experimental
public abstract class AbstractResizableBuffer implements Buffered {

    /**
     * The maximum capacity of the data structure. This indicates the number of elements
     * the data structure can hold before requiring resizing or reallocation of resources.
     * <p>
     * The {@code capacity} field is typically used in contexts where memory or performance
     * optimizations are necessary, especially in the case of resizable or dynamically allocated
     * data structures.
     * <p>
     * It is updated dynamically in scenarios where the size of the data structure exceeds its current
     * capacity, and resizing mechanisms, such as doubling the capacity, are employed.
     */
    @Getter
    private int capacity;
    /**
     * Tracks the current number of elements stored in the data structure.
     * This variable is incremented whenever an element is added and decremented when an element is removed or cleared.
     * It is used to monitor and regulate the size of the collection, ensuring that operations such as capacity management
     * and resizing are handled appropriately based on the current count of elements.
     * <p>
     * <b>Important</b>: it is recommended to not modify this property directly and only let the table's inner methods
     * {@link #nextSlot()} and {@link #clear()} handle it. This field has public access mainly for performance reasons
     * in hot paths to reduce the number of virtual calls a getter would produce.
     */
    public int count;

    /**
     * Constructs an instance of {@code AbstractResizableBuffer} with the specified initial capacity.
     *
     * @param capacity the initial capacity of the buffer
     */
    protected AbstractResizableBuffer(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Sets {@link #count} to zero and calls {@link #onClear()} to perform any custom cleanup logic. If any of the data
     * structures are arrays of objects, it is recommended to call {@link java.util.Arrays#fill(Object[], Object)} to
     * avoid memory leaks. If your data structure consists of primitive value arrays, this method alone does the job.
     * @see #onClear()
     */
    @Override
    public final void clear() {
        onClear();
        count = 0;
    }

    /**
     * By default, this method does nothing.
     * <p>
     * Override this method to perform any custom cleanup logic when the table is cleared.
     * <p>
     * The cleanup logic is usually recommended in cases when the table's structure contains arrays of objects. To avoid
     * memory leaks, it is recommended to call {@link java.util.Arrays#fill(Object[], Object)} on these arrays.
     * <p>
     * Example:
     * <pre>{@code
     *     private String[] messages;
     *
     *     // ... rest of the implementation
     *
     *     @Override
     *     public final void onClear() {
     *          Arrays.fill(messages, null);
     *     }
     * }</pre>
     *
     * <p>
     * If your data structure consists of primitive value arrays, this method overriding is not necessary for performing
     * cleanup operations, but you might still want to override it for other purposes.
     *
     * @see java.util.Arrays#fill(Object[], Object)
     * @see #clear()
     */
    public void onClear() {}

    /**
     * Implement this method to resize the internal arrays to the specified new capacity. This method is called by
     * {@link #nextSlot()} logic whenever {@link #count} exceeds {@link #capacity}. The param passed to this method
     * is equal to the old capacity times two, so by default, the arrays always double in size.
     * <p>
     * A quick and efficient way of resizing primitive arrays is using {@link java.util.Arrays#copyOf(Object[], int)},
     * passing the target array and the new capacity.
     * <p>
     * Example:
     * <pre>{@code
     *      private int[] values;
     *
     *      @Override
     *      protected final void resizeArrays(int newCapacity) {
     *          values = Arrays.copyOf(values, newCapacity);
     *      }
     * }</pre>
     * @param newCapacity the new capacity of the data structure.
     *
     * @see java.util.Arrays#copyOf(Object[], int)
     */
    protected abstract void resizeArrays(int newCapacity);

    private void ensureCapacity() {
        if (count >= capacity) {
            capacity *= 2;
            resizeArrays(capacity);
        }
    }

    /**
     * Advances {@link #count} to the next slot and return its index, meant to be used by add methods. During the advance,
     * applies logic to check if the table needs to resize itself. If resize is required, calls {@link #resizeArrays(int)}
     * passing the new capacity.
     * @return the next available slot index for adding.
     */
    protected final int nextSlot() {
        ensureCapacity();
        int index = count;
        count++;
        return index;
    }

}
