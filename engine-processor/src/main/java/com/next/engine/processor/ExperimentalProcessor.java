package com.next.engine.processor;

import com.next.engine.annotations.internal.Experimental;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_25)
@SupportedAnnotationTypes("com.next.engine.annotations.internal.Experimental")
public class ExperimentalProcessor extends AbstractProcessor {

    private Trees trees;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element root : roundEnv.getRootElements()) {
            TreePath path = trees.getPath(root);

            if (root.getKind() == ElementKind.RECORD) {
                continue;
            }

            new TreePathScanner<Void, Void>() {
                @Override
                public Void visitIdentifier(IdentifierTree node, Void unused) {
                    Element e = trees.getElement(getCurrentPath());
                    if (e == null) return super.visitIdentifier(node, unused);

                    Experimental exp = e.getAnnotation(Experimental.class);
                    if (exp != null) {
                        var diagnostic = exp.level() == Experimental.Level.ERROR ?
                                Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;

                        trees.printMessage(
                                diagnostic,
                                "Usage of experimental API: " + e.getSimpleName(),
                                node,
                                trees.getPath(root).getCompilationUnit()
                        );
                    }
                    return super.visitIdentifier(node, unused);
                }
            }.scan(path, null);
        }

        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "ExperimentalProcessor loaded"
        );

        // 1. Try to unwrap (for IntelliJ/JPS support)
        ProcessingEnvironment actualEnv = jbUnwrap(ProcessingEnvironment.class, processingEnv);

        try {
            // 2. Try to get the Trees instance
            this.trees = Trees.instance(actualEnv);
        } catch (IllegalArgumentException e) {
            // 3. Fallback: If unwrapping caused an issue or didn't help, try the original
            this.trees = Trees.instance(processingEnv);
        }
    }

    /**
     * Attempts to unwrap the ProcessingEnvironment if running inside IntelliJ.
     * If not in IntelliJ, or if it fails, it returns the original wrapper safely.
     * This is a workaround suggested by IntelliJ itself, don't blame me.
     */
    private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
        try {
            // We use reflection so we don't need a hard dependency on JetBrains classes
            final Class<?> apiWrappers = wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            final Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            T unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
            return unwrapped != null ? unwrapped : wrapper;
        } catch (Throwable ignored) {
            // If the class isn't found (not in IntelliJ), just return the original
            return wrapper;
        }
    }
}
