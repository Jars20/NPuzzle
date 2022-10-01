import utils.NodesFactory;
import problem.Node;
import problem.Result;
import solution.*;
import utils.Verifier;

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

                if (!Verifier.solvable(nStart.getState())) {
                    System.out.println("This node are not solvable!");

                    result.setCountUnsolvable(result.getCountUnsolvable() + 1);
                    continue;
                }
                algorithm.search(nStart, result);
            }

            long endTime = System.currentTimeMillis();
            result.setTimeCost(endTime - startTime);
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println(result.toString());
            System.out.println("Total time cost = " + (result.getTimeCost() / 1000.0));

            //for genetic to show the time consumption
            if (!result.getTimeList().isEmpty()) {
                System.out.println();
                System.out.println("+++++++++++++time consumption++++++++++++");
                List<Double> times = result.getTimeList();
                for (int i = 0; i < times.size(); i++) {
                    System.out.println("The No. " + i + " Node Time cost = " + (times.get(i)/1000)+" s.");
                }
                System.out.println();
                System.out.println("The average time consumption = "+ (result.getAvgTime()/1000) +" s.");
            }

            System.out.println("+++++++++++++step consumption++++++++++++");
            List<Integer> stepsList = result.getStepsList();
            int i = 0;
            for (Integer integer : stepsList) {
                System.out.println("The step cost of No. " + (++i) + " is :" + integer);
            }
            System.out.println();
            System.out.println("The average step is: " + result.getAvgStep());
        }

    }

    public static void main(String[] args) {

        //Create random inits to start
        NodesFactory nf = new NodesFactory("24_init_nodes.txt");
        //TODO: modify to adapt to 24-puzzle
        //nf.insertNode2File(0,0,0);
        List<Node> initNodes = NodesFactory.InputNodeFromFile("24_init_nodes.txt");


        Algorithm dfs = new DepthFirstSearch();
        //30 200 100 0.03 03
        Algorithm ga = new Genetic(100, 500, 30,10,0.9, 0.01);
        Algorithm sa = new SimulatedAnnealing(5, 0.01, 0.9999, 150);
        Algorithm bfs = new BreadthFirstSearch();
        Algorithm ida = new IterativeDeepeningAStar();

        NPuzzleRunner nr = new NPuzzleRunner();
        NPuzzleRunner.Runner runner = nr.new Runner(ga, initNodes);
        runner.run();
    }

}

