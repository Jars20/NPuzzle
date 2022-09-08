package solution;

import problem.Node;
import problem.Result;
import problem.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HillClimbing implements Algorithm {
    //flag = 0: searching;   1: found the target;    2: meet unexpected problem;
    int findFlag = 0;
    List<Node> open;
    List<Node> closed;


    /**
     * Hill climbing algorithm.
     *
     * @param node
     * @param result
     */
    @Override
    public void search(Node node, Result result) {
        open = new ArrayList<>();
        closed = new ArrayList<>();
        int countStep = 0;
        findFlag = 0;


        closed.add(node);
        node.getState().display();
        while (findFlag == 0) {
            System.out.println("--------------------------------------------");

            Node parent = closed.get(countStep);
            moveBlank(parent);

            //in each turn select the node with min f(x) cost
            int minFx = Integer.MAX_VALUE;
            int minFIndex = 0;
            for (int i = 0; i < open.size(); i++) {
                if (open.get(i).getHeuristicCost() < minFx) {
                    minFx = open.get(i).getHeuristicCost();
                    minFIndex = i;
                }
            }
            Node minOfTurn = open.get(minFIndex);
            minOfTurn.getState().display();


            //void local maximum problems
            System.out.println("Minimum heuristic distance is " + minFx);
            if (minOfTurn.getHeuristicCost() > parent.getHeuristicCost()) {
                System.out.println("Trapped in local maximum problems! stop the search");
                findFlag = 2;
                continue;
            }

            closed.add(minOfTurn);
            countStep++;
            open.clear();
        }
        if (findFlag == 1) {
            System.out.println("Find the path, total steps = " + countStep);

            //record the steps cost
            result.addStepInList(countStep);
            result.setCountSuccess(result.getCountSuccess() + 1);
        }
        if (findFlag == 2) {
            System.out.println("Ended in local maximum problems");
            result.setCountFailed(result.getCountFailed() + 1);
        }
    }

    /**
     * Move the blank and add the moved fragment to the list open.
     *
     * @param node
     */
    private void moveBlank(Node node) {
        State state = node.getState();
        int zx = state.getZeroX();
        int zy = state.getZeroY();
        int size = state.getSize();
        int zi = zx * size + zy;
        int[] tiles = state.getTiles();
        int pathCost = node.getPathCost();

        int dx, dy;

        //directions:down,up,left,right
        int[] xMove = new int[]{1, -1, 0, 0};
        int[] yMove = new int[]{0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            dx = zx + xMove[i];
            dy = zy + yMove[i];
            if (dx < 0 || dx >= size || dy < 0 || dy >= size) {
                continue;
            }

            //make a new array as new state to swap
            int newI = dx * size + dy;
            int[] newTile = Arrays.copyOf(tiles, tiles.length);

            //swap
            int temp = newTile[newI];
            newTile[newI] = newTile[zi];
            newTile[zi] = temp;
            State childState = new State(newTile);

            //make a new node
            Node childNode = new Node(childState, pathCost + 1, node);
            if (childState.isGoal()) {
                findFlag = 1;
            }
            open.add(childNode);
        }
    }
}
