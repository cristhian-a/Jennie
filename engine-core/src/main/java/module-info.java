/**
 * The module declaration for the `engine.core` module.
 * <p>
 * This module serves as the core library for the engine framework, managing
 * functionalities such as data handling, events, graphics rendering, I/O,
 * sound processing, physics, animations, debugging, user interfaces,
 * and more. It provides the essential parts and services required to
 * operate the engine's systems effectively.
 * <p>
 * Requirements:
 * - The module has dependencies on several external libraries and modules,
 *   including Jackson for JSON processing, JSpecify for type annotations,
 *   and others such as Java Management and Java Desktop.
 * <p>
 * Features:
 * - Exports a wide range of packages for engine functionalities, allowing
 *   them to be used by external modules and applications.
 * - Opens specific packages to the `com.fasterxml.jackson.databind` module
 *   to support serialization and deserialization with Jackson.
 */
module engine.core {
    requires static engine.annotations;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.datatransfer;
    requires java.desktop;
    requires java.management;
    requires static lombok;
    requires org.jspecify;

    opens com.next.engine.scene to com.fasterxml.jackson.databind;
    opens com.next.engine.system to com.fasterxml.jackson.databind;
    opens com.next.engine.dto to com.fasterxml.jackson.databind;

    exports com.next.engine;
    exports com.next.engine.data;
    exports com.next.engine.event;
    exports com.next.engine.graphics;
    exports com.next.engine.graphics.awt;
    exports com.next.engine.io;
    exports com.next.engine.sound;
    exports com.next.engine.sound.jxsound;
    exports com.next.engine.system;
    exports com.next.engine.model;
    exports com.next.engine.scene;
    exports com.next.engine.physics;
    exports com.next.engine.dto;
    exports com.next.engine.animation;
    exports com.next.engine.util;
    exports com.next.engine.debug;
    exports com.next.engine.ui;
    exports com.next.engine.ui.component;
    exports com.next.engine.ui.widget;
    exports com.next.engine.ui.style;
}