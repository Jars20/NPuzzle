package problem;

/**
 * record the node, include 4 fragment: state, parent, pathCost, heuristicCost
 */
public class Node {
    private State state;
    private int heuristicCost;
    private int pathCost;
    private Node parent;
    //preMove: 0:DOWN  1:UP  2:LEFT  3:RIGHT
    private int preMove;


    public Node(State state, int pathCost, Node parent,int direction) {
        this.state = state;
        this.heuristicCost = state.calHeuristicCost();
        this.pathCost = pathCost;
        this.parent = parent;
        this.preMove = direction;
    }

    //constructor
    public Node(State state, int heuristicCost, int pathCost, Node parent) {
        this.state = state;
        this.heuristicCost = heuristicCost;
        this.pathCost = pathCost;
        this.parent = parent;
    }

    //totalCost f(n)=g(n)+h(n)
    public int getTotalCost() {
        return heuristicCost + pathCost;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node2compare = (Node) obj;
        State state2 = node2compare.getState();
        return state.equals(state2);
    }

    //getter and setter
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int getPreMove() {
        return preMove;
    }

    public void setPreMove(int preMove) {
        this.preMove = preMove;
    }


}
