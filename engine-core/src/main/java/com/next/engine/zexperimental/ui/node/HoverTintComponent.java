package com.next.engine.zexperimental.ui.node;

public final class HoverTintComponent implements UIComponent {
    private UINode owner;

    @Override
    public void onAttach(UINode node) {
        this.owner = node;
    }

    @Override
    public void onPointerEnter() {
        if (owner instanceof LabelLeaf l) {
            l.setColor(0xFFFF0000); // Turn red on hover
            l.markAsDirty();
        }
    }
}
