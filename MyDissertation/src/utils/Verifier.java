package utils;

import problem.State;

public class Verifier {
    /**
     * Judge the state whether solvable or not
     *
     * @param state
     * @return
     */
    public static boolean solvable(State state) {
        int size = state.getSize();
        if (size % 2 == 1) {
            return state.getIvs() % 2 == 0;
        } else {
            int[] tiles = state.getTiles();
            int height = 0;
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i] == 0) {
                    height = i / size;
                }
            }
            return (state.getIvs() + Math.abs(height - size + 1)) % 2 == 0;
        }
    }

    public static boolean isLegalMove(int pre, int cur) {
        if ((pre == 0 && cur == 1) || (pre == 1 && cur == 0)) {
            return false;
        } else if ((pre == 2 && cur == 3) || (pre == 3 && cur == 2)) {
            return false;
        } else {
            return true;
        }

    }
}
