package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;
import com.next.engine.zexperimental.memory.ZTransform.Storage;

@Experimental
final class ZMovementSystem {

    void update(Storage t, float dt) {
        for (int i = 0; i < t.size; i++) {
            float x = (float) Storage.X.get(t.data, 0L, (long) i);
            float y = (float) Storage.Y.get(t.data, 0L, (long) i);
            float vx = (float) Storage.VX.get(t.data, 0L, (long) i);
            float vy = (float) Storage.VY.get(t.data, 0L, (long) i);

            Storage.X.set(t.data, 0L, (long) i, x + vx * dt);
            Storage.Y.set(t.data, 0L, (long) i, y + vy * dt);
        }
    }
}
