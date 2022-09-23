import utils.NodesFactory;
import problem.Node;
import problem.Result;
import solution.*;
import utils.Verifier;

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

            List<Integer> stepsList = result.getStepsList();
            int i = 0;
            for (Integer integer : stepsList) {
                System.out.println("The step cost of No. " + (++i) + " is :" + integer);
            }
            System.out.println();
            System.out.println("The average step is: "+ result.getAvgStep());
        }

    }

    public static void main(String[] args) {

        //Create random inits to start
        NodesFactory nf = new NodesFactory("init_nodes.txt");
        //nf.insertNode2File(0,100);
        List<Node> initNodes = NodesFactory.InputNodeFromFile("init_nodes.txt");


        Algorithm dfs = new DepthFirstSearch();
        //ok
        //30 200 100 0.3 0.3 0.03 03
        Algorithm ga = new Genetic(150, 300, 2000, 0.02, 0.3, 0.01);
        //成功率2/100
        Algorithm sa = new SimulatedAnnealing(5, 0.01, 0.999, 150);
        //死循环(次数太多。一次8puzzle 64272步，时间410秒)
        Algorithm bfs = new BreadthFirstSearch();
        //成功率100，但是只能解决8puzzle问题。当处理15puzzle问题时候效率很低
        Algorithm ida = new IterativeDeepeningAStar();

        NPuzzleRunner nr = new NPuzzleRunner();
        NPuzzleRunner.Runner runner = nr.new Runner(ga, initNodes);
        runner.run();
    }

///**
    // * Judge the state whether solvable or not
    // *
    // * @param state
    // * @return
    // */
    //static boolean solvable(State state) {
    //    int size = state.getSize();
    //    if (size % 2 == 1) {
    //        return state.getIvs() % 2 == 0;
    //    } else {
    //        int[] tiles = state.getTiles();
    //        int height = 0;
    //        for (int i = 0; i < tiles.length; i++) {
    //            if (tiles[i] == 0) {
    //                height = i / size;
    //            }
    //        }
    //        return (state.getIvs() + Math.abs(height - size + 1)) % 2 == 0;
    //    }
    //}
}

