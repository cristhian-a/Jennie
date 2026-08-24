package com.next.engine.graphics;

/// Defines a common interface for _fonts_ used by the rendering pipeline and the
/// UI components of the engine.
public interface TextFont {
    float measureWidth(String text);
    float getLineHeight();
    float getAscent();
}
