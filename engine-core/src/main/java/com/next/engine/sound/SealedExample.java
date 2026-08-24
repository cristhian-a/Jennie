package com.next.engine.sound;

import com.next.engine.annotations.internal.Experimental;

@Experimental
public sealed interface SealedExample {
    record Response(int code, String json) implements SealedExample {}
    record NotFound(int cope) implements SealedExample {}
    record ServerError(int code, String message) implements SealedExample {}
}
