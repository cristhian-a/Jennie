package com.next.engine.ui;

import lombok.Getter;

import java.util.*;

public final class FocusManager {
    private final AbstractContainer root;
    private final List<Focusable> elements = new ArrayList<>();

    private final Deque<AbstractContainer> scopeStack = new ArrayDeque<>();

    public FocusManager(AbstractContainer root) {
        this.root = Objects.requireNonNull(root);
        this.scopeStack.push(root);
        rebuild();
    }

    @Getter private Focusable focused;
    private int focusedIndex = -1;

    private Focusable pendingFocus;

    public void requestFocus(Focusable focus) {
        pendingFocus = focus;
    }

    public void rebuild() {
        Focusable preserved = focused;
        elements.clear();
        collect(currentScope());

        if (elements.isEmpty()) {
            focused = null;
            focusedIndex = -1;
            return;
        }

        if (pendingFocus != null) {
            setFocus(pendingFocus);
            pendingFocus = null;
            return;
        }

        if (preserved != null) {
            int i = elements.indexOf(preserved);
            if (i >= 0) {
                setFocus(i);
                return;
            }
        }

        setFocus(0);
    }

    private void collect(AbstractNode node) {
        if (!node.visible) return;
        if (node instanceof Focusable f && f.isFocusable()) elements.add(f);
        if (node instanceof AbstractContainer container) {
            for (int i = 0; i < container.children.size(); i++) {
                // Although I am an enemy of recursivity, I'll let the explicit recursive
                // call here for better readability.
                // The other solution is implementing a collect method in AbstractNode
                // and letting it resolve for its children.
                collect(container.children.get(i));
            }
        }
    }

    /**
     * Returns the current effective scope.
     * @return scope in the top of the scope stack.
     */
    public AbstractContainer currentScope() {
        return scopeStack.peek();
    }

    /**
     * Set scope to {@code container}, replacing the stack's top. If null, sets root as the top.
     * @param container to be set as the current scope.
     */
    public void setScope(AbstractContainer container) {
        scopeStack.pop();
        scopeStack.push(container == null ? root : container);
        rebuild();
    }

    /**
     * Pushes a new scope. Focus will be limited to this container subtree.
     * @param container to be set as the current scope.
     */
    public void pushScope(AbstractContainer container) {
        if (container == null) return;
        scopeStack.push(container);
        rebuild();
    }

    /**
     * Pops the current scope and restore the previous. If the root is the current scope, it will not be popped.
     * @return true if any scope was popped.
     */
    public boolean popScope() {
        if (scopeStack.size() <= 1) return false;   // leave root in place
        scopeStack.pop();
        rebuild();
        return true;
    }

    /**
     * Clears stack and sets the root as the current scope.
     */
    public void clearScopes() {
        scopeStack.clear();
        scopeStack.push(root);
        rebuild();
    }

    public void focusNext() {
        if (elements.isEmpty()) return;
        setFocus((focusedIndex + 1) % elements.size());
    }

    public void focusPrevious() {
        if (elements.isEmpty()) return;
        int n = elements.size();
        setFocus((focusedIndex + n - 1) % n);
    }

    public void setFocus(int index) {
        if (index < 0 || index >= elements.size()) return;

        Focusable f = elements.get(index);
        if (focused == f) return;
        if (focused != null) focused.onBlur();
        focused = f;
        focusedIndex = index;
        focused.onFocus();
    }

    public void setFocus(Focusable focus) {
        if (focus == null) return;
        int index = elements.indexOf(focus);
        setFocus(index);
    }

    public void activateFocused(String action) {
        if (focused != null) focused.onActivate(action);
    }

    public void focusDirection(float dx, float dy) {
        if (elements.isEmpty()) return;
        if (focused == null) {
            setFocus(0);
            return;
        }

        AbstractNode currentNode = (AbstractNode) focused;
        float cx = currentNode.globalBounds.x + currentNode.globalBounds.width / 2f;
        float cy = currentNode.globalBounds.y + currentNode.globalBounds.height / 2f;

        float bestScore = Float.POSITIVE_INFINITY;
        int bestIndex = -1;

        // normalize direction
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;
        dx /= len;
        dy /= len;

        for (int i = 0; i < elements.size(); i++) {
            Focusable candidate = elements.get(i);
            if (candidate == focused) continue;
            AbstractNode cNode = (AbstractNode) candidate;
            float tcx = cNode.globalBounds.x + cNode.globalBounds.width / 2f;
            float tcy = cNode.globalBounds.y + cNode.globalBounds.height / 2f;
            float vx = tcx - cx;
            float vy = tcy - cy;
            float vlen =  (float) Math.sqrt(vx * vx + vy * vy);
            if (vlen == 0) continue;
            // dot product to check angle alignment
            float dot = (vx * dx + vy * dy) / vlen; // cosine of an angle
            if (dot <= 0.3f) continue;  // threshold: only candidates closer than 30 degrees are considered
            // score combines angle and distance (lower better)
            float score = (1f - dot) * 1000 + vlen;
            if (score < bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }

        if (bestIndex >= 0) setFocus(bestIndex);
    }
}
