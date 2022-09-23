package solution;

import org.w3c.dom.ls.LSOutput;
import problem.Node;
import problem.Result;
import problem.State;
import utils.Verifier;

import java.util.*;


public class IterativeDeepeningAStar implements Algorithm {
    private int bound;
    //to record the min f(n) in each loop
    //private int minTotalCostRec = Integer.MAX_VALUE;
    private int stepCount;

    boolean pathGot;
    //tiles in state to sort
    private int[] tiles2Sort;
    //the size of tiles
    private int size;
    //the index of zero
    private int posOfZero;

    private int maxDepth;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{0, 0, 1, -1};
    final static int[] Y_MOVE = new int[]{1, -1, 0, 0};

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }


    /**
     * IDA* search
     *
     * @param node
     * @param result
     */
    @Override
    public void search(Node node, Result result) {
        stepCount = 0;
        //get the tiles and run IDA* base on it
        tiles2Sort = node.getState().getTiles();
        maxDepth = Integer.MIN_VALUE;
        size = node.getState().getSize();
        pathGot = false;

        posOfZero = node.getState().getZeroX()*size+node.getState().getZeroY();


        System.out.println();
        node.getState().display();
        int heuristicCost = calHeuristicCost(tiles2Sort, size);

        //the initial state is goal state
        if (node.getState().isGoal()) {
            pathGot = true;
        }
        //set bound
        setBound(calHeuristicCost(tiles2Sort, size));


        for (bound = heuristicCost; bound <= 100; bound = dfs(0, heuristicCost, -1)) {

            if (pathGot) {
                //find the final target
                result.setCountSuccess(result.getCountSuccess() + 1);
                //record the steps cost
                result.addStepInList(stepCount);
                System.out.println("The depth of the tree = " + (maxDepth+1));
                return;
            }
        }

    }

    public int dfs(int step, int heuristicCost, int preMove) {

        int totalCost = step + heuristicCost;
        if (totalCost > bound) {
            return step + heuristicCost;
        }
        //find the target Node
        if (heuristicCost == 0) {
            pathGot = true;
            return step;
        }

        //record the deepest depth
        if (step>maxDepth){
            maxDepth = step;
        }
        //before start, count the step cost
        stepCount++;
        int indexOfZero = posOfZero;

        //System.out.println("start! heuristic = "+heuristicCost+" , step= " + step+ " stepCunt = " + stepCount + " bound= " + bound );

        //set the up bound for the next loop
        int nextBound = 127;
        //the init index of zero
        int zx = indexOfZero / size;
        int zy = indexOfZero % size;

        int dx, dy, dIndex;
        int newHeuristic;
        int minDepth;
        for (int i = 0; i < 4; i++) {
            dx = zx + X_MOVE[i];
            dy = zy + Y_MOVE[i];
            if (dx < 0 || dy < 0 || dx > size - 1 || dy > size - 1 || !Verifier.isLegalMove(i, preMove)) {
                continue;
            }
            //the tile' s index to move
            dIndex = (dx * size) + dy;


            //get the target index of the moved tile
            int num = tiles2Sort[dx * size + dy];
            int tarX = (num - 1) / size;
            int tarY = (num - 1) % size;


            //swap
            tiles2Sort[indexOfZero] = tiles2Sort[dIndex];
            tiles2Sort[dIndex] = 0;
            posOfZero = dIndex;

            newHeuristic = heuristicCost - (Math.abs(tarX - dx) + Math.abs(tarY - dy)) + (Math.abs(tarX - zx) + Math.abs(tarY - zy));


            //dfs, and record the min f(n) in each loop
            minDepth = dfs(step + 1, newHeuristic, i);
            //get the target path
            if (pathGot) {
                return minDepth;
            }
            if (nextBound > minDepth) {
                nextBound = minDepth;
            }
            //backTracking
            tiles2Sort[dIndex] = tiles2Sort[indexOfZero];
            tiles2Sort[indexOfZero] = 0;

            posOfZero = indexOfZero;
        }

        return nextBound;
    }


    //Calculate the Manhattan distance
    private static int calHeuristicCost(int[] tiles, int size) {
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

}
