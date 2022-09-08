import utils.NodesFactory;
import problem.Node;
import problem.Result;
import problem.State;
import solution.*;

import java.util.Collections;
import java.util.List;

public class NPuzzleRunner {
    /**
     * Get the algorithm and run it
     */
    class Runner {
        Algorithm algorithm;
        List<Node> initNodes;

        public Runner(Algorithm algorithm, List<Node> initNodes) {
            this.algorithm = algorithm;
            this.initNodes = initNodes;
        }

        public void run() {

            Result result = new Result();
            long startTime = System.currentTimeMillis();

            for (int count = 0; count < initNodes.size(); count++) {
                Node nStart = initNodes.get(count);
                System.out.println();
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("The No." + count + " Nodes");
                if (!solvable(nStart.getState())) {
                    System.out.println("This node are not solvable!");

                    result.setCountFailed(result.getCountFailed() + 1);
                    continue;
                }
                algorithm.search(nStart, result);
            }

            long endTime = System.currentTimeMillis();
            result.setTimeCost(endTime - startTime);
            System.out.println(result.toString());
            System.out.println("Total time cost = " + (result.getTimeCost() / 1000.0));
        }

    }

    /**
     * Judge the state whether solvable or not
     *
     * @param state
     * @return
     */
    static boolean solvable(State state) {
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




    public static void main(String[] args) {

        //Create random inits to start
        //NodesFactory nf = new NodesFactory("create_nodes.txt");
        //nf.insertNode2File(50,50);


        List<Node> initNodes = NodesFactory.InputNodeFromFile("init_nodes.txt");


        HillClimbing hc = new HillClimbing();
        Algorithm ga = new Genetic(20, 50, 2000, 0.3, 0.3, 0.3);
        SimulatedAnnealing sa = new SimulatedAnnealing(5, 0.001, 0.8, 150);
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        IterativeDeepeningAStar ida = new IterativeDeepeningAStar();

        NPuzzleRunner nr = new NPuzzleRunner();
        NPuzzleRunner.Runner runner = nr.new Runner(hc, initNodes);
        runner.run();


        ////GA
        //Result resultOfGA = new Result();
        //long startOfGA = System.currentTimeMillis();
        //for (int count = 0; count < initNodes.size(); count++) {
        //    Node nStart = initNodes.get(count);
        //    System.out.println("The No." + count + " Nodes");
        //    if (!solvable(nStart.getState())) {
        //        System.out.println("The No." + count + " Nodes");
        //        countFail++;
        //        resultOfGA.setCountFailed(resultOfGA.getCountFailed());
        //        continue;
        //    }
        //    Algorithm ga = new Genetic(20, 50, 2000, 0.3, 0.3, 0.3);
        //    ga.search(nStart, resultOfGA);
        //}
        //long endOfGA = System.currentTimeMillis();
        //resultOfGA.setTimeCost(endOfGA - startOfGA);
        //System.out.println("Total time cost = " + (resultOfGA.getTimeCost() / 1000.0));
        //
        //
        ////SA
        //Result resultOfSA = new Result();
        //long startOfSA = System.currentTimeMillis();
        //for (int count = 0; count < initNodes.size(); count++) {
        //    Node nStart = initNodes.get(count);
        //    System.out.println("The No." + count + " Nodes");
        //    if (!solvable(nStart.getState())) {
        //        System.out.println("This node are not solvable!");
        //        countFail++;
        //
        //        continue;
        //    }
        //    SimulatedAnnealing sa = new SimulatedAnnealing(5, 0.001, 0.8, 150);
        //    sa.search(nStart, resultOfSA);
        //}
        //long endOfSA = System.currentTimeMillis();
        //resultOfSA.setTimeCost(endOfSA - startOfSA);
        //System.out.println("Total time cost = " + (resultOfSA.getTimeCost() / 1000.0));
        //
        //
        ////BFS
        //Result resultOfBFS = new Result();
        //long startOfBFS = System.currentTimeMillis();
        //for (int count = 0; count < initNodes.size(); count++) {
        //    Node nStart = initNodes.get(count);
        //    System.out.println("The No." + count + " Nodes");
        //    if (!solvable(nStart.getState())) {
        //        System.out.println("This node are not solvable!");
        //
        //        countFail++;
        //        continue;
        //    }
        //    BreadthFirstSearch bfs = new BreadthFirstSearch();
        //    bfs.search(nStart, resultOfBFS);
        //}
        //long endOfBFS = System.currentTimeMillis();
        //resultOfSA.setTimeCost(endOfBFS - startOfBFS);
        //System.out.println("Total time cost = " + (resultOfBFS.getTimeCost() / 1000.0));
        //
        ////IDA
        //Result resultOfIDA = new Result();
        //long startOfIDA = System.currentTimeMillis();
        //for (int count = 0; count < initNodes.size(); count++) {
        //    Node nStart = initNodes.get(count);
        //    System.out.println("The No." + count + " Nodes");
        //    if (!solvable(nStart.getState())) {
        //        System.out.println("This node are not solvable!");
        //
        //        countFail++;
        //        continue;
        //    }
        //    IterativeDeepeningAStar ida = new IterativeDeepeningAStar();
        //    ida.search(nStart, resultOfBFS);
        //}
        //long endOfIDA = System.currentTimeMillis();
        //resultOfIDA.setTimeCost(endOfIDA - startOfIDA);
        //System.out.println("Total time cost = " + (resultOfIDA.getTimeCost() / 1000.0));


        //Genetic g = new Genetic(20,50,2000,0.3,0.3,0.3);
        //g.search(nStart);

        //SimulatedAnnealing sa = new SimulatedAnnealing(5,0.001,0.8,150);
        //sa.search(nStart);

        //BreadthFirstSearch bfs = new BreadthFirstSearch();
        //bfs.search(nStart);

        //IterativeDeepeningAStar ida = new IterativeDeepeningAStar();
        //ida.search(nStart);
    }


}

