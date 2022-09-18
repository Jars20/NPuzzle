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

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{0, 0, 1, -1};
    final static int[] Y_MOVE = new int[]{1, -1, 0, 0};

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }

    //TODO
    public static int[] xx;
    public static int[] yy;

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
        size = node.getState().getSize();
        pathGot = false;

        posOfZero = node.getState().getZeroX()*size+node.getState().getZeroY();


        System.out.println("++++++++++++++++++++++++++++++");
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
            System.out.println("bound expanded! bound = "+ bound);

            if (pathGot) {
                //find the final target
                result.setCountSuccess(result.getCountSuccess() + 1);
                //record the steps cost
                result.addStepInList(stepCount);
                System.out.println("Has cost steps = " + stepCount);
                return;
            }
        }

        //while (!pathGot) {
        //    //dfs with created stack
        //    //dfs(node);
        //    dfs(zeroX,zeroY, heuristicCost, -1);
        //
        //    //get target successful
        //    if (pathGot) {
        //        result.setCountSuccess(result.getCountSuccess() + 1);
        //        //record the steps cost
        //        result.addStepInList(stepCount);
        //
        //        return;
        //    } else {
        //        bound = minTotalCostRec;
        //    }
        //    //totalCosts.clear();
        //    minTotalCostRec = Integer.MAX_VALUE;
        //}
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

    //private void dfs(int zx, int zy, int len, int preDis) {
    //    if (pathGot) {
    //        return;
    //    }
    //    //int heuristicTemp;
    //    int heuristicCost = calHeuristicCost(tiles2Sort, size);
    //    //f(n) = g(n) +h(n)
    //    int totalCost = heuristicCost + len;
    //    //System.out.println("heuristicCost"+ heuristicCost+", totalcost = "+ totalCost);
    //
    //    stepCount++;
    //    //System.out.println("+++++++++++++++++++++++++++++++++");
    //    //System.out.println("The step count = " + stepCount);
    //    //System.out.println("The move count = " + len);
    //    //System.out.println(Arrays.toString(tiles2Sort));
    //    //System.out.println();
    //
    //    //update record minTotalCost
    //    if (minTotalCostRec > totalCost && totalCost > bound) {
    //        minTotalCostRec = totalCost;
    //    }
    //    if (len <= bound) {
    //        //meet the target
    //        if (heuristicCost == 0) {
    //            System.out.println("Find the target path, stepCost = " + len + 1);
    //            pathGot = true;
    //            return;
    //        }
    //        //abandon this path
    //        if (len == bound) {
    //            //System.out.println("len == bound!, backtracking!");
    //            return;
    //        }
    //    }
    //
    //
    //    for (int i = 0; i < 4; i++) {
    //        int dx = zx + X_MOVE[i];
    //        int dy = zy + Y_MOVE[i];
    //        if (dx < 0 || dy < 0 || dy >= size || dx >= size || !Verifier.isLegalMove(preDis, i)) {
    //            continue;
    //        }
    //        int zeroI = zx * size + zy;
    //        int newI = dx * size + dy;
    //
    //        //swap the tiles
    //        swap(tiles2Sort, zeroI, newI);
    //
    //        //get the moved tile's target
    //        //int num = tiles2Sort[dx * size + dy];
    //        //int tarX = (num - 1) / size;
    //        //int tarY = (num - 1) % size;
    //        //heuristicTemp = heuristicCost -(Math.abs(tarX-dx)+Math.abs(tarY-dy))+(Math.abs(tarX-zx)+Math.abs(tarY-zy));
    //
    //        //recursion
    //        if (totalCost <= bound) {
    //            System.out.println("totalCost <= bound :" + bound);
    //            dfs(dx, dy, len + 1, i);
    //            if (pathGot) {
    //                return;
    //            }
    //        }
    //        //backtracking
    //        swap(tiles2Sort, zeroI, newI);
    //
    //    }
    //
    //}
    //
    //void swap(int[] tiles, int zeroI, int newI) {
    //
    //    int temp = tiles[zeroI];
    //    tiles[zeroI] = tiles[newI];
    //    tiles[newI] = temp;
    //}

    ///**
    // * Move the blank and add the moved fragment to the list open.
    // *
    // * @param node
    // */
    //private void moveBlank(Node node) {
    //    State state = node.getState();
    //    int zx = state.getZeroX();
    //    int zy = state.getZeroY();
    //    int size = state.getSize();
    //    int zi = zx * size + zy;
    //    int[] tiles = state.getTiles();
    //    int pathCost = node.getPathCost();
    //
    //    int dx, dy;
    //
    //    for (int i = 0; i < 4; i++) {
    //        dx = zx + X_MOVE[i];
    //        dy = zy + Y_MOVE[i];
    //        if (dx < 0 || dx >= size || dy < 0 || dy >= size || !Verifier.isLegalMove(node.getPreMove(), i)) {
    //            continue;
    //        }
    //
    //        //make a new array as new state to swap
    //        int newI = dx * size + dy;
    //        int[] newTile = Arrays.copyOf(tiles, tiles.length);
    //
    //        //swap
    //        int temp = newTile[newI];
    //        newTile[newI] = newTile[zi];
    //        newTile[zi] = temp;
    //        State childState = new State(newTile);
    //
    //        if (childState.isGoal()) {
    //            System.out.println("Find the target path, stepCost = " + node.getPathCost() + 1);
    //            pathGot = true;
    //            return;
    //        }
    //
    //        //make a new node, add it to open list
    //        Node childNode = new Node(childState, pathCost + 1, node, i);
    //        if (!open.contains(childNode) && !closed.contains(childNode)) {
    //            open.add(childNode);
    //        }
    //    }
    //}

    //dfs use the created stack
    //private void dfs(Node node) {
    //    open = new Stack<>();
    //    closed = new LinkedList<>();
    //    open.add(node);
    //
    //    while (!open.isEmpty()) {
    //        System.out.println("--------------------------------------------");
    //        Node parent = open.pop();
    //
    //        stepCount++;
    //        assert parent != null;
    //        System.out.println("The no." + stepCount + " step:");
    //        System.out.println("The path cost = " + parent.getPathCost());
    //        parent.getState().display();
    //
    //        closed.add(parent);
    //
    //        if (parent.getTotalCost() > bound) {
    //            if (minTotalCostRec > parent.getTotalCost()) {
    //                minTotalCostRec = parent.getTotalCost();
    //            }
    //        } else {
    //            moveBlank(parent);
    //        }
    //    }
    //}
}
