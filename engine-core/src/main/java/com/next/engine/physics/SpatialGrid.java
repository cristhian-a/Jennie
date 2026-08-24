package com.next.engine.physics;

import java.util.ArrayList;
import java.util.List;

public final class SpatialGrid {
    private final int cellSize;
    private final List<Body>[][] cells;
    private final int cols, rows;

    private int queryCounter = 0;

    @SuppressWarnings("unchecked")
    public SpatialGrid(int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.cols = (int) Math.ceil((double) width / cellSize);
        this.rows = (int) Math.ceil((double) height / cellSize);
        this.cells = new ArrayList[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[c][r] = new ArrayList<>(16);
            }
        }
    }

    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[c][r].clear();
            }
        }
    }

    public void insert(Body body) {
        AABB box = body.getCollisionBox().getBounds();
        int left    = Math.max(0, (int) (box.x / cellSize));
        int right   = Math.min(cols - 1, (int) ((box.x + box.width) / cellSize));
        int top     = Math.max(0, (int) (box.y / cellSize));
        int bottom  = Math.min(rows - 1, (int) ((box.y + box.height) / cellSize));

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                cells[col][row].add(body);
            }
        }
    }

    /**
     * Queries for the {@code Body} agent's nearby entities, then performs a visit to the physics' narrow phase solver
     * @param axis axis to be considered {@link Axis}
     * @param agent {@link Body} to be queried for nearby entities {@link Body#getCollisionBox()}
     * @param motionDelta movement delta (i.e., the intended amount of motion to be applied)
     * @param physics {@link Physics} instance to be visited.
     */
    void queryBroadPhase(Axis axis, Body agent, float motionDelta, Physics physics) {
        queryCounter++;

        AABB box    = agent.getCollisionBox().getBounds();
        int left    = Math.max(0, (int) (box.x / cellSize));
        int right   = Math.min(cols - 1, (int) ((box.x + box.width) / cellSize));
        int top     = Math.max(0, (int) (box.y / cellSize));
        int bottom  = Math.min(rows - 1, (int) ((box.y + box.height) / cellSize));

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                List<Body> cell = cells[col][row];
                for (int i = 0; i < cell.size(); i++) {
                    Body other = cell.get(i);
                    if (other != agent && other.getLastQueryId() != queryCounter) {
                        other.setLastQueryId(queryCounter);
                        physics.solveNarrowPhase(axis, agent, other, motionDelta);
                    }
                }
            }
        }
    }

    /**
     * Queries for the {@code Body} agent's nearby entities, then performs a visit to the handler
     * @param agent {@link Body} to be queried for nearby entities {@link Body#getCollisionBox()}
     * @param handler {@link SpatialGridHandler} to be visited.
     */
    public void queryNeighbors(Body agent, SpatialGridHandler handler) {
        queryCounter++;

        AABB box    = agent.getCollisionBox().getBounds();
        int left    = Math.max(0, (int) (box.x / cellSize));
        int right   = Math.min(cols - 1, (int) ((box.x + box.width) / cellSize));
        int top     = Math.max(0, (int) (box.y / cellSize));
        int bottom  = Math.min(rows - 1, (int) ((box.y + box.height) / cellSize));

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                List<Body> cell = cells[col][row];
                for (int i = 0; i < cell.size(); i++) {
                    Body other = cell.get(i);
                    if (other != agent && other.getLastQueryId() != queryCounter) {
                        other.setLastQueryId(queryCounter);
                        handler.handleNeighbor(agent, other);
                    }
                }
            }
        }
    }

}
