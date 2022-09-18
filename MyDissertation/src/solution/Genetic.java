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
    int GENERATION = 200;
    double RETAIN_PROBABILITY = 0.3;
    double ELIMINATE_PROBABILITY = 0.3;
    double MUTATION_PROBABILITY = 0.3;
    int[][] population;


    //directions:down,up,left,right
    final static int[] X_MOVE = new int[]{1, -1, 0, 0};
    final static int[] Y_MOVE = new int[]{0, 0, -1, 1};

    public Genetic() {
    }

    public Genetic(int NUMS, int LENGTH, int GENERATION, double RETAIN_PROBABILITY, double ELIMINATE_PROBABILITY, double MUTATION_PROB) {
        this.NUMS = NUMS;
        this.LENGTH = LENGTH;
        this.GENERATION = GENERATION;
        this.RETAIN_PROBABILITY = RETAIN_PROBABILITY;
        this.ELIMINATE_PROBABILITY = ELIMINATE_PROBABILITY;
        this.MUTATION_PROBABILITY = MUTATION_PROB;
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

    public void setRETAIN_PROBABILITY(double RETAIN_PROBABILITY) {
        this.RETAIN_PROBABILITY = RETAIN_PROBABILITY;
    }

    public void setELIMINATE_PROBABILITY(double ELIMINATE_PROBABILITY) {
        this.ELIMINATE_PROBABILITY = ELIMINATE_PROBABILITY;
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

        for (int i = 0; i < GENERATION; i++) {
            System.out.println();
            System.out.println("The " + i + " generation:");
            //Move and calculate the fitness
            population = calFitnessInChromosome(state, population);
            countStep++;
            for (int[] chromosome : population) {
                if (chromosome[chromosome.length - 1] == 0) {
                    System.out.println("Find the final state! The step = " + countStep);
                    //record the success result
                    result.setCountSuccess(result.getCountSuccess() + 1);
                    //record the steps cost
                    result.addStepInList(countStep);

                    System.out.println("The chromosome is" + Arrays.toString(chromosome));
                    return;
                }
            }
            //sort the population by fitness
            population = select(population);
            for (int[] chromosome : population) {
                System.out.println("chromosome after sort");
                System.out.println(Arrays.toString(chromosome));
            }

            //select
            int[][] retainPopulation = retain(population);
            population = eliminate(population);

            //crossover
            population = crossover(population, NUMS - retainPopulation.length);

            //mutation
            population = mutation(mergeArrays(retainPopulation, population));

        }
        //record the fail result
        result.setCountFailed(result.getCountFailed()+1);
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
            //chromosome the last two index store min HeuristicCost
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
    int[][] select(int[][] population) {
        Arrays.sort(population, (o1, o2) -> o1[o1.length - 1] - o2[o1.length - 1]);
        return population;
    }

    /**
     * @param population The population to retain.
     * @return
     */
    int[][] retain(int[][] population) {
        int num = population.length;
        int length = population[0].length;
        int retainLength = (int) (num * RETAIN_PROBABILITY);
        int[][] retainList = new int[retainLength][length - 1];
        for (int i = 0; i < retainLength; i++) {
            int[] chromosome = Arrays.copyOf(population[i], length - 1);
            retainList[i] = chromosome;
        }
        return retainList;
    }

    /**
     * @param population The population to eliminate.
     * @return The population after eliminating.
     */
    int[][] eliminate(int[][] population) {
        int num = population.length;
        int length = population[0].length;
        int eliminateLength = (int) (num * ELIMINATE_PROBABILITY);
        int afterEliminate = num - eliminateLength;
        int[][] afterEliPopu = new int[afterEliminate][length - 1];
        for (int i = 0; i < afterEliminate; i++) {
            int[] chromosome = Arrays.copyOf(population[i], length - 1);
            afterEliPopu[i] = chromosome;
        }
        return afterEliPopu;
    }

    /**
     * @param population the population to crossover
     * @param nums       the number of retain population
     * @return
     */
    int[][] crossover(int[][] population, int nums) {
        int num = population.length;
        int length = population[0].length;
        int[][] newPopulation = new int[nums][population[0].length];
        for (int j = 0; j < nums; j++) {
            newPopulation[j] = crossover(population[j], population[random.nextInt(num)]);
        }
        return newPopulation;
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

    /**
     * Mutate the population
     *
     * @param population
     * @return
     */
    int[][] mutation(int[][] population) {
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[0].length; j++) {
                if (random.nextDouble() < MUTATION_PROBABILITY) {
                    population[i][j] = random.nextInt(4);
                }
            }
        }
        return population;
    }

    /**
     * merge the retain population and eliminated population
     *
     * @param first
     * @param second
     * @return
     */
    int[][] mergeArrays(int[][] first, int[][] second) {
        int num1 = first.length;
        int num2 = second.length;
        int length = first[0].length;
        int[][] newArray = new int[num1 + num2][length];

        for (int i = 0; i < num1; i++) {
            newArray[i] = Arrays.copyOf(first[i], length);
        }
        for (int i = 0; i < num2; i++) {
            newArray[num1 + i] = Arrays.copyOf(second[i], length);
        }
        return newArray;
    }

}


