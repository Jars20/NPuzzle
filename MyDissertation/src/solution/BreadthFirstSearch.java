package solution;

import problem.Node;
import problem.Result;
import problem.State;
import utils.Verifier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch implements Algorithm {
    private Queue<Node> open;
    private List<Node> closed;
    int stepCount;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

    boolean findTarget;

    /**
     * Breadth First Search
     *
     * @param node
     * @param result
     */
    @Override
    public void search(Node node, Result result) {
        open = new LinkedList<>();
        closed = new LinkedList<>();
        findTarget = false;
        stepCount = 0;

        open.add(node);
        System.out.println();
        node.getState().display();

        while (!open.isEmpty() && !findTarget) {
            Node parent = open.poll();
            assert parent != null;

            closed.add(parent);
            moveBlank(parent);
        }

        if (findTarget) {
            System.out.println("Find the target path!");

            System.out.println();
            //record the success result
            result.setCountSuccess(result.getCountSuccess() + 1);
            //record the steps cost
            result.addStepInList(stepCount);
            return;
        }
        System.out.println("Failed, did not find the target path.");
        //record the failed result
        result.setCountFailed(result.getCountFailed() + 1);
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
            if (dx < 0 || dx >= size || dy < 0 || dy >= size || !Verifier.isLegalMove(node.getPreMove(), i)) {
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

            if (childState.isGoal()) {
                findTarget = true;
                System.out.println("Find the target path, stepCost = " + (node.getPathCost() + 1));
                return;
            }
            //make a new node which shouldn't be in open or closed list, add it to open list
            Node childNode = new Node(childState, pathCost + 1, node, i);
            if (!open.contains(childNode) && !closed.contains(childNode)) {
                open.add(childNode);
                stepCount++;
            }
        }
    }
}
