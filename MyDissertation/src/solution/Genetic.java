package solution;

import problem.Node;
import problem.Result;
import problem.State;

import java.util.Arrays;
import java.util.Random;

public class Genetic implements Algorithm {
    Random random = new Random();
    int NUMS = 20;
    int LENGTH = 50;
    int GENERATION = 100;
    int K = 10;
    double MUTATION_PROBABILITY = 0.3;
    double CROSSOVER_PROBABILITY = 0.9;
    int[][] population;


    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

    public Genetic(int NUMS, int LENGTH, int GENERATION, int K, double CROSSOVER_PROBABILITY, double MUTATION_PROB) {
        this.NUMS = NUMS;
        this.LENGTH = LENGTH;
        this.GENERATION = GENERATION;
        this.K = K;
        this.CROSSOVER_PROBABILITY = CROSSOVER_PROBABILITY;
        this.MUTATION_PROBABILITY = MUTATION_PROB;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        this.K = k;
    }

    public double getCROSSOVER_PROBABILITY() {
        return CROSSOVER_PROBABILITY;
    }

    public void setCROSSOVER_PROBABILITY(double CROSSOVER_PROBABILITY) {
        this.CROSSOVER_PROBABILITY = CROSSOVER_PROBABILITY;
    }

    public void setNUMS(int NUMS) {
        this.NUMS = NUMS;
    }

    public void setLENGTH(int LENGTH) {
        this.LENGTH = LENGTH;
    }

    public void setGENERATION(int GENERATION) {
        this.GENERATION = GENERATION;
    }

    public void setMUTATION_PROBABILITY(double MUTATION_PROBABILITY) {
        this.MUTATION_PROBABILITY = MUTATION_PROBABILITY;
    }

    /**
     * Genetic Algorithm
     *
     * @param node
     * @param result
     */
    @Override
    public void search(Node node, Result result) {
        //init the population
        population = initPopulation(NUMS, LENGTH);
        //get the init state
        State state = node.getState();
        int countStep = 0;
        int geneCount = 0;
        int[] bestAns = new int[LENGTH];
        boolean skipCrossover = false;
        double startTime = System.currentTimeMillis();
        while (geneCount < GENERATION) {
            countStep++;

            population = calFitnessInChromosome(state, population);
            population = sort(population);

            //find the path
            if (population[0][population[0].length - 1] == 0) {
                System.out.println("Find the target path!");
                //record time consumption
                double endTime = System.currentTimeMillis();
                result.addTime(endTime-startTime);
                //record steps
                result.setCountSuccess(result.getCountSuccess() + 1);
                result.addStepInList(countStep);
                return;
            }
            int[] temp = Arrays.copyOf(population[0], population[0].length - 1);
            if (Arrays.equals(bestAns, temp)) {
                geneCount += 1;
            } else {
                geneCount = 0;
            }
            bestAns = Arrays.copyOf(population[0], LENGTH);

            int[][] childPopulation = new int[NUMS][LENGTH];
            if (geneCount < K) {
                childPopulation[0] = bestAns;
            } else {
                skipCrossover = true;
            }


            int start;
            if (skipCrossover) {
                start = 0;
            } else {
                start = 1;
            }

            for (int i = start; i < NUMS; i++) {
                //insert point：i，j
                int[] father;
                int[] mother;
                do {
                    father = tournamentSelect(population);
                    mother = tournamentSelect(population);

                } while (Arrays.equals(father, mother));

                //crossover
                int[] son;
                if (random.nextDouble() < CROSSOVER_PROBABILITY && !skipCrossover) {
                    son = crossover(father, mother);
                } else {
                    son = father;
                }

                //mutation
                //if (random.nextDouble() < MUTATION_PROBABILITY) {
                for (int k = 0; k < son.length; k++) {
                    if (random.nextDouble() < MUTATION_PROBABILITY) {
                        son[k] = random.nextInt(4);
                    }
                }
                //}
                childPopulation[i] = Arrays.copyOf(son, son.length - 1);
                skipCrossover = false;
                //i++;
            }
            population = childPopulation;
        }

        //record the fail result
        result.setCountFailed(result.getCountFailed() + 1);
        System.out.println("Cannot find the final state! The step = " + countStep);
    }

    /**
     * @param nums   the number of chromosome in each population.
     * @param length the length of each chromosome.
     * @return nums chromosome with length of length.
     */
    int[][] initPopulation(int nums, int length) {
        int[][] population = new int[nums][length];
        for (int i = 0; i < nums; i++) {
            for (int j = 0; j < length; j++) {
                int move = random.nextInt(4);
                population[i][j] = move;
            }
        }
        return population;
    }

    /**
     * calculate the fitness of Chromosome and store in it
     *
     * @param initState  initial state
     * @param population population
     * @return chromosome with the last two int as fitness.
     */
    int[][] calFitnessInChromosome(State initState, int[][] population) {
        int num = population.length;
        int length = population[0].length;

        int[][] childPopulation = new int[num][length + 1];
        for (int j = 0; j < num; j++) {
            int[] chromosome = population[j];

            int[] childChromosome = new int[length + 1];
            //Each chromosome stands for series of moves.
            State newState = new State(initState);

            //record the min HeuristicCost in each chromosome
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < chromosome.length; i++) {
                newState = moveBlank(chromosome[i], newState);
                childChromosome[i] = chromosome[i];

                if (newState.calHeuristicCost() < min) {
                    min = newState.calHeuristicCost();
                }
            }
            childChromosome[chromosome.length] = min;

            childPopulation[j] = childChromosome;
        }
        return childPopulation;

    }

    /**
     * Move the blank in the state, follow the chromosome
     *
     * @param direction directions in the chromosome.
     * @param initState the initial state.
     * @return Moved state
     */
    State moveBlank(int direction, State initState) {
        int zx = initState.getZeroX();
        int zy = initState.getZeroY();
        int size = initState.getSize();
        int zi = zx * size + zy;
        int[] tiles = initState.getTiles();
        //new x and new y
        int dx = zx + X_MOVE[direction];
        int dy = zy + Y_MOVE[direction];

        if (dx < 0 || dx >= size || dy < 0 || dy >= size) {
            return initState;
        }

        //make a new array as new state to swap
        int newI = dx * size + dy;

        //swap
        int temp = tiles[newI];
        tiles[newI] = tiles[zi];
        tiles[zi] = temp;

        //update the zeroX and zeroY
        initState.setZeroX(dx);
        initState.setZeroY(dy);

        //update the ivs
        initState.setIvs(initState.calIvs());

        return initState;
    }

    //Sorted by smallest to largest
    int[][] sort(int[][] population) {
        Arrays.sort(population, (o1, o2) -> o1[o1.length - 1] - o2[o1.length - 1]);
        return population;
    }

    private int[] tournamentSelect(int[][] population) {
        int num = population.length;
        int length = population[0].length;

        int indexI = random.nextInt(num);
        int indexJ = random.nextInt(num);
        for (int i = 0; i < 5; i++) {
            int indexK = random.nextInt(num);
            if (population[indexK][length - 1] < population[indexI][length - 1]) {
                indexI = indexK;
            }
        }
        return population[indexI][length - 1] < population[indexJ][length - 1] ? population[indexI] : population[indexJ];
    }

    int[] crossover(int[] father, int[] mother) {
        int[] son = new int[father.length];
        int splitPoint = random.nextInt(father.length);
        for (int i = 0; i < splitPoint; i++) {
            son[i] = mother[i];
        }
        for (int i = splitPoint; i < father.length; i++) {
            son[i] = father[i];
        }

        return son;
    }

}


