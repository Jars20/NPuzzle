package solution;

import problem.Node;
import problem.Result;
import problem.State;
import utils.Verifier;

import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing implements Algorithm {
    private Random random = new Random();
    private double TEMPERATURE;
    private double TEMPERATURE_MIN;
    private double ATTENUATION;
    private int LOOP_COUNT;

    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

    public SimulatedAnnealing(double TEMPERATURE, double TEMPERATURE_MIN, double ATTENUATION, int LOOP_COUNT) {
        this.TEMPERATURE = TEMPERATURE;
        this.TEMPERATURE_MIN = TEMPERATURE_MIN;
        this.ATTENUATION = ATTENUATION;
        this.LOOP_COUNT = LOOP_COUNT;
    }

    public double getTEMPERATURE() {
        return TEMPERATURE;
    }

    public void setTEMPERATURE(double TEMPERATURE) {
        this.TEMPERATURE = TEMPERATURE;
    }

    public double getTEMPERATURE_MIN() {
        return TEMPERATURE_MIN;
    }

    public void setTEMPERATURE_MIN(double TEMPERATURE_MIN) {
        this.TEMPERATURE_MIN = TEMPERATURE_MIN;
    }

    public double getATTENUATION() {
        return ATTENUATION;
    }

    public void setATTENUATION(double ATTENUATION) {
        this.ATTENUATION = ATTENUATION;
    }

    public int getLOOP_COUNT() {
        return LOOP_COUNT;
    }

    public void setLOOP_COUNT(int LOOP_COUNT) {
        this.LOOP_COUNT = LOOP_COUNT;
    }

    /**
     * Simulated Annealing Algorithm
     *
     * @param node node
     */
    @Override
    public void search(Node node, Result result) {
        int countStep = 0;
        Node cur = node;
        cur.getState().display();
        double temperature = TEMPERATURE;
        double temperature_min = TEMPERATURE_MIN;

        while (temperature > temperature_min) {
            //System.out.println("The current temperature is " + temperature);
            for (int i = 0; i < LOOP_COUNT; i++) {
                //System.out.println("------------------------------");
                int heuristicCost = cur.getHeuristicCost();

                if (cur.getState().isGoal()) {
                    System.out.println("Find the path, total steps = " + countStep);

                    result.setCountSuccess(result.getCountSuccess() + 1);
                    //record the steps cost
                    result.addStepInList(countStep);
                    return;
                }
                //move, get random neighbour
                Node childNode = randomMove(cur);
                countStep++;
                double dTotalCost = childNode.getHeuristicCost() - heuristicCost;
                double r = random.nextDouble();
                if (dTotalCost < 0) {
                    //System.out.println("Accept the better one");
                    //cur.getState().display();

                    cur = childNode;

                } else if (Math.exp(-dTotalCost / temperature) > r) {
                    //System.out.println("Accepted, although the node isn't better.");
                    //cur.getState().display();

                    cur = childNode;
                } else {
                    //System.out.println("Did not accept!");
                    continue;
                }
            }
            temperature *= ATTENUATION;
        }
        //search failed
        System.out.println();
        System.out.println("Did not find the path!");
        result.setCountFailed(result.getCountFailed() + 1);
        return;
    }

    /**
     * Move the blank one step in random.
     *
     * @param node the input node
     * @return Moved blank
     */
    private Node randomMove(Node node) {
        State state = node.getState();
        int zx = state.getZeroX();
        int zy = state.getZeroY();
        int size = state.getSize();
        int zi = zx * size + zy;
        int[] tiles = state.getTiles();
        int pathCost = node.getPathCost();

        int dx, dy;

        int randomI;
        do {
            //random a direction
            randomI = random.nextInt(4);

            dx = zx + X_MOVE[randomI];
            dy = zy + Y_MOVE[randomI];
        } while (dx < 0 || dx >= size || dy < 0 || dy >= size|| !Verifier.isLegalMove(randomI,node.getPreMove()));

        int newI = dx * size + dy;
        //make a new array as new state to swap
        int[] newTile = Arrays.copyOf(tiles, tiles.length);
        //swap
        int temp = newTile[newI];
        newTile[newI] = newTile[zi];
        newTile[zi] = temp;

        State childState = new State(newTile);
        //make a new node
        return new Node(childState, pathCost + 1, node, randomI);
    }
}
