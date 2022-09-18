package solution;

import problem.Node;
import problem.Result;
import problem.State;
import utils.Verifier;

import java.util.*;

public class DepthFirstSearch implements Algorithm {
    // Whether findTarget or not
    boolean findTarget;
    //the depth of the dfs
    int depth = 0;
    int countStep = 0;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

    private Stack<Node> open;
    private List<Node> closed;


    /**
     * Depth-first-search(DFS) algorithm.
     *
     * @param node
     * @param result
     */
    @Override
    public void search(Node node, Result result) {
        open = new Stack<>();
        closed = new LinkedList<>();
        countStep = 0;
        open.push(node);
        System.out.println();
        node.getState().display();

        while (!open.isEmpty() && !findTarget) {
            Node parent = open.pop();


            //System.out.println("The no." + countStep + " step, the cost =" + parent.getPathCost());


            closed.add(parent);
            moveBlank(parent);
        }
        if (findTarget) {
            System.out.println("Find the target path!");
            System.out.println();
            //record the success result
            result.setCountSuccess(result.getCountSuccess() + 1);
            //record the steps cost
            System.out.println("The countStep = " + countStep );
            result.addStepInList(countStep);
        } else {
            System.out.println("Failed, cannot find the target path.");
            //record the failed result
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


        for (int i = 0; i < 4; i++) {
            dx = zx + X_MOVE[i];
            dy = zy + Y_MOVE[i];
            if (dx < 0 || dx >= size || dy < 0 || dy >= size|| !Verifier.isLegalMove(node.getPreMove(),i)) {
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
                System.out.println("Find the target path, depth = " + (node.getPathCost() + 1));
                return;
            }
            //make a new node
            Node childNode = new Node(childState, pathCost + 1, node,i);
            if (!open.contains(childNode) && !closed.contains(childNode)) {
                countStep++;
                System.out.println("Create the No."+ countStep+" Node!");
                open.push(childNode);
            }
        }
    }
}
