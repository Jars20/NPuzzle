package solution;

import problem.Node;
import problem.Result;
import problem.State;

import java.util.*;


public class IterativeDeepeningAStar implements Algorithm {
    private int bound;
    //private Set<Integer> totalCosts;
    private int minTotalCostRec = Integer.MAX_VALUE;
    private int stepCount;
    private boolean pathGot;

    private Stack<Node> open;
    private Queue<Node> closed;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

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
        open = new Stack<>();
        closed = new LinkedList<>();

        stepCount = 0;
        node.getState().display();
        open.add(node);

        //set bound
        setBound(node.getHeuristicCost());

        pathGot = false;

        while (bound < node.getState().calHeuristicCost() * 2) {
            dfs();
            if (pathGot) {
                result.setCountSuccess(result.getCountSuccess() + 1);
                //record the steps cost
                result.addStepInList(stepCount);
                return;
            } else {
                bound = minTotalCostRec;
            }
            //totalCosts.clear();
            minTotalCostRec = Integer.MAX_VALUE;
        }
        System.out.println("Cannot find the target path!");
        result.setCountFailed(result.getCountFailed() + 1);
    }

    private void dfs() {
        while (!open.isEmpty()) {
            System.out.println("--------------------------------------------");
            Node parent = open.pop();

            stepCount++;
            assert parent != null;
            System.out.println("The no." + stepCount + " step:");
            System.out.println("The path cost = " + parent.getPathCost());
            parent.getState().display();

            closed.add(parent);
            if (parent.getState().isGoal()) {
                System.out.println("Find the target path, stepCost = " + parent.getPathCost());
                pathGot = true;
                return;
            }

            if (parent.getTotalCost() > bound) {
                //totalCosts.add(parent.getTotalCost());
                if (minTotalCostRec > parent.getTotalCost()) {
                    minTotalCostRec = parent.getTotalCost();
                }
            } else {
                moveBlank(parent);
            }
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

        for (int i = 0; i < 4; i++) {
            dx = zx + X_MOVE[i];
            dy = zy + Y_MOVE[i];
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

            //make a new node, add it to open list
            Node childNode = new Node(childState, pathCost + 1, node);
            if (!open.contains(childNode) && !closed.contains(childNode)) {
                open.add(childNode);
            }
        }
    }
}
