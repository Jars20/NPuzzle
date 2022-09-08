package utils;

import problem.Node;
import problem.State;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodesFactory {

    private String INIT_NODES_FILE;

    public String getINIT_NODES_FILE() {
        return INIT_NODES_FILE;
    }

    public void setINIT_NODES_FILE(String INIT_NODES_FILE) {
        this.INIT_NODES_FILE = INIT_NODES_FILE;
    }

    public NodesFactory(String INIT_NODES_FILE) {
        this.INIT_NODES_FILE = INIT_NODES_FILE;
    }

    public static List<Node> InputNodeFromFile(String fileName) {
        ArrayList<Node> initNodes = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            //input the nodes from file
            bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

            //input each line
            String readString = null;
            while ((readString = bufferedReader.readLine()) != null) {
                initNodes.add(parseNode(readString));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return initNodes;
    }

    private static Node parseNode(String grids) {
        String[] tiles = grids.split(",");
        int[] inits = new int[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            inits[i] = Integer.parseInt(tiles[i].trim());
        }
        State initState = new State(inits);
        Node initNode = new Node(initState, 0, null);
        return initNode;
    }

    private List<int[]> createTiles(int nums, int size) {
        int[] tiles = new int[size * size];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = i;
        }
        List<int[]> rlt = new ArrayList<>(nums);
        Shuffle shuffleUtil = new Shuffle();

        //add shuffled tiles into list
        for (int i = 0; i < nums; i++) {
            int[] shuffled = shuffleUtil.shuffle(tiles);
            rlt.add(shuffled);
        }

        return rlt;
    }

    public void insertNode2File(int numsOf3, int numsOf4) {
        List<int[]> Nodes = new ArrayList<>(numsOf3+numsOf4);
        List<int[]> tilesOfSize3, tilesOfSize4;
        tilesOfSize3 = createTiles(numsOf3,3);
        tilesOfSize4 = createTiles(numsOf4,4);

        tilesOfSize3.addAll(tilesOfSize4);

        BufferedWriter bufferedWriter = null;
        try {
            FileWriter fw = new FileWriter(INIT_NODES_FILE);
            bufferedWriter = new BufferedWriter(fw);

            for (int[] tiles : tilesOfSize3) {
                String nodeTemp = Arrays.toString(tiles).replace("[","").replace("]","");

                bufferedWriter.write(nodeTemp);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedWriter!=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
