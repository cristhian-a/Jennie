package com.next.engine.ui;

import java.util.List;

public final class GridLayout implements Layout {

    private final int columns;
    private final float hgap;
    private final float vgap;

    private float[] colWidths = new float[0];
    private float[] rowHeights = new float[0];

    public GridLayout(int columns, float hgap, float vgap) {
        if (columns < 1) throw new IllegalArgumentException("columns must be > 0");
        this.columns = columns;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    @Override
    public void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {
        final int elements = children.size();
        final int rows = (elements + columns - 1) / columns;

        if (colWidths.length != columns) colWidths = new float[columns];
        if (rowHeights.length != rows) rowHeights = new float[rows];

        for (int i = 0; i < columns; i++) colWidths[i] = 0f;
        for (int i = 0; i < rows; i++) rowHeights[i] = 0f;

        // calculating max preferred size per column and row
        for (int i = 0; i < elements; i++) {
            final AbstractNode child = children.get(i);
            int c = i % columns;
            int r = i / columns;
            float pw = child.preferredSize.width;
            float ph = child.preferredSize.height;
            colWidths[c] = Math.max(colWidths[c], pw);
            rowHeights[r] = Math.max(rowHeights[r], ph);
        }

        // computing totals with gaps
        float totalWidth = 0f;
        for (int i = 0; i < columns; i++) totalWidth += colWidths[i];
        if (columns > 1) totalWidth += hgap * (columns - 1);

        float totalHeight = 0f;
        for (int i = 0; i < rows; i++) totalHeight += rowHeights[i];
        if (rows > 1) totalHeight += vgap * (rows - 1);

        container.preferredSize.set(totalWidth, totalHeight);
    }

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        final Rect content = container.contentBounds();
        final int elements = children.size();
        if (elements == 0) return;
        final int rows = (elements + columns - 1) / columns;

        // distributing extra space evenly among columns/rows if content is larger
        float extraW = content.width - container.preferredSize.width;
        if (extraW > 0) {
            float addPerCol = extraW / columns;
            for (int i = 0; i < columns; i++) colWidths[i] += addPerCol;
        }

        float extraH = content.height - container.preferredSize.height;
        if (extraH > 0 && rows > 0) {
            float addPerRow = extraH / rows;
            for (int i = 0; i < rows; i++) rowHeights[i] += addPerRow;
        }

        // Assigning child local bounds in local coordinates
        float yCursor = content.y;
        for (int r = 0; r < rows; r++) {
            float xCursor = content.x;
            for (int c = 0; c < columns; c++) {
                int index = r * columns + c;
                if (index >= elements) break;
                AbstractNode child = children.get(index);
                float cw = colWidths[c];
                float rh = rowHeights[r];
                child.localBounds.set(xCursor, yCursor, cw, rh);
                xCursor += cw + hgap;
            }
            yCursor += rowHeights[r] + vgap;
        }
    }
}
