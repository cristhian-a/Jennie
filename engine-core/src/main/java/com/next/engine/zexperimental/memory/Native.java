package com.next.engine.zexperimental.memory;

import com.next.engine.annotations.internal.Experimental;

import java.lang.foreign.Linker;

@Experimental
class Native {

    void link() {
        // This can apparently be used to call native functions (C programs and stuff).
        // I don't know how to do or their usage, so I'll keep this here for
        // later search.
        var linker = Linker.nativeLinker();
    }
}
