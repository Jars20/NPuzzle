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
        int geneCount = 0;
        int[] bestAns = new int[LENGTH];

        //while (geneCount < GENERATION) {
        //    countStep++;
        //
        //    population = calFitnessInChromosome(state, population);
        //    population = sort(population);
        //
        //    System.out.println();
        //    System.out.println("The geneCount is " + geneCount);
        //    //System.out.println("The best ans = " + Arrays.toString(bestAns));
        //
        //    System.out.println("The fitness is :");
        //    for (int[] chromosome : population) {
        //        System.out.print(chromosome[chromosome.length - 1] + " ");
        //        //System.out.println(Arrays.toString(chromosome));
        //    }
        //    System.out.println();
        //
        //    //find the path
        //    if (population[0][population[0].length - 1] == 0) {
        //        System.out.println("Find the target path!");
        //
        //        result.setCountSuccess(result.getCountSuccess() + 1);
        //        result.addStepInList(countStep);
        //        return;
        //    }
        //    int[] temp = Arrays.copyOf(population[0], population[0].length - 1);
        //    if (Arrays.equals(bestAns, temp)) {
        //        geneCount += 1;
        //    } else {
        //        geneCount = 0;
        //    }
        //    bestAns = Arrays.copyOf(population[0], LENGTH);
        //
        //    int[][] childPopulation = new int[NUMS][LENGTH];
        //    childPopulation[0] = bestAns;
        //
        //    //cal the select prob of P(t)
        //    //store the select probability
        //    double[] selectProb = new double[population.length];
        //    double proSum = 0.0;
        //    for (int j = 0; j < selectProb.length; j++) {
        //        int fitness = population[j][population[0].length - 1];
        //        int prob = 160 - fitness;
        //        proSum += prob;
        //    }
        //    for (int j = 0; j < selectProb.length; j++) {
        //        int fitness = population[j][population[0].length - 1];
        //        int prob = 160 - fitness;
        //        selectProb[j] = prob / proSum;
        //    }
        //    //System.out.println("The k generation = " + geneCount + " is " + Arrays.toString(selectProb));
        //
        //    for (int i = 1; i < NUMS; i++) {
        //        //插入位置：i，j
        //        int[] father;
        //        int[] mother;
        //        do {
        //            father = roulette(population, selectProb);
        //            mother = roulette(population, selectProb);
        //        } while (Arrays.equals(father, mother));
        //
        //        int[] son = new int[LENGTH + 1];
        //        if (random.nextDouble() < 0.5) {
        //            son = crossover(father, mother);
        //        } else {
        //            son = father;
        //        }
        //
        //        //if (random.nextDouble() < 0.1) {
        //        for (int k = 0; k < son.length; k++) {
        //            if (random.nextDouble() < MUTATION_PROBABILITY) {
        //                son[k] = random.nextInt(4);
        //            }
        //        }
        //        //}
        //        childPopulation[i] = Arrays.copyOf(son, son.length - 1);
        //    }
        //    population = childPopulation;
        //}

        for (int i = 0; i < GENERATION; i++) {
            System.out.println();
            System.out.println("The " + i + " generation:");
            //Move and calculate the fitness
            population = calFitnessInChromosome(state, population);
            countStep++;

            //get the target
            for (int[] chromosome : population) {
                if (chromosome[chromosome.length - 1] == 0) {
                    System.out.println("Find the final state! The step = " + countStep);
                    //record the success result
                    result.setCountSuccess(result.getCountSuccess() + 1);
                    //record the steps cost
                    result.addStepInList(countStep);
                    return;
                }
            }

            //sort the population by fitness
            population = sort(population);
            //TODO：del
            System.out.println("chromosome after sorted! ");
            for (int[] chromosome : population) {
                System.out.print(chromosome[chromosome.length - 1] + " ");
            }
            System.out.println();

            double[] originSelectProb = new double[population.length];
            getSelProb(originSelectProb);
            //select: retain and eliminate
            int[][] retainPopulation = retain(population, originSelectProb);
            population = eliminate(population);

            //store the select probability
            double[] selectProb = new double[population.length];
            getSelProb(selectProb);


            //TODO：del
            int[][] show = mergeArrays(retainPopulation, population);
            System.out.println("chromosome after selected");
            for (int[] chromosome : show) {
                System.out.print(chromosome[chromosome.length - 1] + " ");
            }
            System.out.println();
            //System.out.println("selected : " + Arrays.toString(selectProb));

            for (int j = 0; j < population.length; j++) {
                int[] childChromosome = Arrays.copyOf(population[j], population[j].length - 1);
                population[j] = childChromosome;
            }
            for (int j = 0; j < retainPopulation.length; j++) {
                int[] childChromosome = Arrays.copyOf(retainPopulation[j], retainPopulation[j].length - 1);
                retainPopulation[j] = childChromosome;
            }


            //crossover
            population = crossover(population, selectProb, NUMS - retainPopulation.length);

            //mutation
            population = mutation(mergeArrays(retainPopulation, population));

        }
        //record the fail result
        result.setCountFailed(result.getCountFailed() + 1);
        System.out.println("Cannot find the final state! The step = " + countStep);
    }

    public void getSelProb(double[] selectProb) {
        double proSum = 0.0;
        for (int j = 0; j < selectProb.length; j++) {
            int fitness = population[j][population[0].length - 1];
            int prob = 160 - fitness;
            proSum += prob;
        }
        for (int j = 0; j < selectProb.length; j++) {
            int fitness = population[j][population[0].length - 1];
            int prob = 160 - fitness;
            selectProb[j] = prob / proSum;
        }
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

    /**
     * @param population The population to retain.
     * @return
     */
    int[][] retain(int[][] population, double[] originSelectProb) {
        int num = population.length;
        int length = population[0].length;
        int retainLength = (int) (num * RETAIN_PROBABILITY);

        //keep the best ans
        int[][] retainList = new int[retainLength][length];
        retainList[0] = Arrays.copyOf(population[0], length);

        //roulette() get the chromosome
        for (int i = 1; i < retainLength; i++) {
            int[] chromosome = Arrays.copyOf(roulette(population, originSelectProb), length);
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
        //length-1, del the fitness stored in array
        int[][] afterEliPopu = new int[afterEliminate][length];
        for (int i = 0; i < afterEliminate; i++) {
            int[] chromosome = Arrays.copyOf(population[i], length);
            afterEliPopu[i] = chromosome;
        }
        return afterEliPopu;
    }

    /**
     * @param population the population to crossover
     * @param nums       the number of retain population
     * @return
     */
    private int[][] crossover(int[][] population, double[] selectProb, int nums) {
        int[][] newPopulation = new int[nums][population[0].length];

        //roulette_algorithm get father and mother
        int[] father;
        int[] mother;
        for (int j = 0; j < nums; j++) {
            do {
                father = roulette(population, selectProb);
                mother = roulette(population, selectProb);
            } while (Arrays.equals(father, mother));
            newPopulation[j] = crossover(father, mother);
        }
        return newPopulation;
    }

    /**
     * select chromosome from population by selectProb
     *
     * @param population
     * @param selectProb
     * @return
     */
    private int[] roulette(int[][] population, double[] selectProb) {
        int[] father = new int[population[0].length];
        double index = 0.0;
        double bound = random.nextDouble();
        for (int i = 0; i < selectProb.length; i++) {
            index += selectProb[i];
            if (index > bound) {
                //System.out.println("Find the target father!");
                father = Arrays.copyOf(population[i], population[0].length);
                //System.out.println(Arrays.toString(father));
                return father;
            }
        }
        return father;
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
     * merge the two array
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


