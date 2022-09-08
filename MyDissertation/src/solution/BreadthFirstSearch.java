package solution;

import problem.Node;
import problem.Result;
import problem.State;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch implements Algorithm {
    Queue<Node> open;
    Queue<Node> closed;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

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
        int stepCount = 0;
        open.add(node);
        node.getState().display();

        while (!open.isEmpty()) {
            System.out.println("--------------------------------------------");
            Node parent = open.poll();

            stepCount++;
            assert parent != null;
            System.out.println("The no." + stepCount + " step:");
            parent.getState().display();

            closed.add(parent);
            if (parent.getState().isGoal()) {
                System.out.println("Find the target path, stepCost = " + parent.getPathCost());
                //record the success result
                result.setCountSuccess(result.getCountSuccess() + 1);
                //record the steps cost
                result.addStepInList(stepCount);

                return;
            }
            moveBlank(parent);
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
