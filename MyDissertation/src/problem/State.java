package problem;

import java.util.Arrays;

public class State {
    private int[] tiles;
    private int size;
    private int zeroX;
    private int zeroY;
    private int ivs;

    //input is an int[] array
    public State(int[] tiles) {
        this.tiles = tiles;
        this.size = (int) Math.sqrt(tiles.length);
        this.zeroX = calZeroX();
        this.zeroY = calZeroY();
        this.ivs = calIvs();
    }

    public State(int[] tiles, int size) {
        this.tiles = tiles;
        this.size = size;
        this.zeroX = calZeroX();
        this.zeroY = calZeroY();
        this.ivs = calIvs();
    }

    public State(State state) {
        this.tiles = Arrays.copyOf(state.getTiles(), state.getTiles().length);
        this.size = state.getSize();
        this.zeroX = state.getZeroX();
        this.zeroY = state.getZeroY();
        this.ivs = state.getIvs();
    }

    public int[] getTiles() {
        return tiles;
    }

    public void setTiles(int[] tiles) {
        this.tiles = tiles;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getZeroX() {
        return zeroX;
    }

    public int getZeroY() {
        return zeroY;
    }

    public int getIvs() {
        return ivs;
    }

    public void setZeroX(int zeroX) {
        this.zeroX = zeroX;
    }

    public void setZeroY(int zeroY) {
        this.zeroY = zeroY;
    }

    /**
     * Get the inverse order number, to discuss whether solvable
     *
     * @return the inverse order number of the state.
     */
    public int calIvs() {
        int cnt = 0;
        for (int i = 0; i < size * size; i++) {
            if (tiles[i] == 0) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i]) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    //cal the x of blank
    //求0的坐标,纵坐标X,表示行数
    public int calZeroX() {
        for (int i = 0; i < size * size; i++) {
            if (tiles[i] == 0) {
                return i / size;
            }
        }
        System.out.println("Error. cannot find 0");
        return 0;
    }

    //cal the y of blank
    //求0的坐标,纵坐标Y,表示列数
    public int calZeroY() {
        for (int i = 0; i < size * size; i++) {
            if (tiles[i] == 0) {
                return i % size;
            }
        }
        System.out.println("Error. cannot find 0");
        return 0;
    }

    //To calculate heuristicCost
    public int calHeuristicCost() {
        int heuristic = 0;
        for (int i = 0; i < size * size; i++) {
            if (tiles[i] == 0) {
                continue;
            }
            int curX = i / size;
            int curY = i % size;
            int tarX = (tiles[i] - 1) / size;
            int tarY = (tiles[i] - 1) % size;
            heuristic += Math.abs(tarX - curX) + Math.abs(tarY - curY);
        }
        return heuristic;
    }

    public boolean isGoal() {
        return calHeuristicCost() == 0;
    }

    public void setIvs(int ivs) {
        this.ivs = ivs;
    }

    /**
     * Draw the tiles
     */
    public void display() {
        if (size == 3) {
            for (int i = 0; i < tiles.length; i += size) {
                layout3();
                System.out.print("| ");
                for (int j = i; j < i + size; j++) {
                    System.out.print(tiles[j] + " | ");
                }
                System.out.print("\n");
            }
            layout3();
            System.out.println();
        } else if (size == 4) {
            for (int i = 0; i < tiles.length; i += size) {
                layout4();
                System.out.print("| ");
                for (int j = i; j < i + size; j++) {
                    if (tiles[j] >= 10) {
                        System.out.print(tiles[j] + " | ");
                    } else {
                        System.out.print(" " + tiles[j] + " | ");
                    }
                }
                System.out.print("\n");
            }
            layout4();
            System.out.println();
        }
    }

    public void layout3() {
        System.out.print("+");
        for (int j = 0; j < size; j++) {
            System.out.print("---+");
        }
        System.out.print("\n");
    }

    public void layout4() {
        System.out.print("+");
        for (int j = 0; j < size; j++) {
            System.out.print("----+");
        }
        System.out.print("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }
        State state2 = (State) obj;
        int[] tiles2 = state2.getTiles();
        int[] tiles1 = getTiles();

        if (tiles2.length != tiles1.length) {
            return false;
        }
        for (int i = 0; i < tiles1.length; i++) {
            if (tiles1[i] != tiles2[i]) {
                return false;
            }
        }
        return true;
    }
}
