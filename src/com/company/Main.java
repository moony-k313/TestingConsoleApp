package com.company;
import java.util.*;
import java.io.*;


class BallPass {
    private static int[][] matrix;
    private static int[][] numberedMatrix;
    private static ArrayList<Integer> pathList = new ArrayList<>();
    private static int rows = 0;
    private static int columns = 0;
    private static int sRow;
    private static int sColumn;
    private static int fRow;
    private static int fColumn;
    private static int startPos;
    private static int finishPos;
    private static final TreeMap<Integer, List<Integer>> graph = new TreeMap<>();
    private static final TreeMap<Integer, Integer> path = new TreeMap<>();
    //private static final String fileFullPath = "./Resources/Example1.txt"; //out of solutions
    private static final String fileFullPath = "./Resources/Example2.txt";
    //private static final String fileFullPath = "./Resources/Example3.txt";


    public static void main(String[] args) {
        getDataFromFile(fileFullPath);
        formGraph(matrix, numberedMatrix, graph);
        pathList = searchAlgorithm(graph, path, startPos, finishPos);
        if (pathList!= null) {
            consoleResult(numberedMatrix, finishPos, pathList, path);
        }

    }
// this method is only for print end matrix
    private static void printMatrix(String[][] matrix) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();

        }
    }
//this method is only for unwrapping data from file , exacltly the number of rows and columns
// start and finish positions  and start Matrix
    private static void getDataFromFile(String filePath) {
        File file = new File(filePath);
        String[] start;
        String[] finish;
        try {
            Scanner fileScanner = new Scanner(file);
            rows = Integer.parseInt(fileScanner.nextLine());
            columns = Integer.parseInt(fileScanner.nextLine());
            start = fileScanner.nextLine().split("\\D");
            finish = fileScanner.nextLine().split("\\D");
            sRow = Integer.parseInt(start[1]);
            sColumn = Integer.parseInt(start[2]);
            fRow = Integer.parseInt(finish[1]);
            fColumn = Integer.parseInt(finish[2]);
            matrix = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                String[] numbersOfRow = fileScanner.nextLine().split("\t");
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = Integer.parseInt(numbersOfRow[j]);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException error) {
            error.printStackTrace();
        }
    }
//this method is for defining the elements that is 0, so it firstly defines such fields(by adding new array that is bigger than matrix from file)
// and for each field that is 0 on start (then they were have an own number), method defines near fields, that also has 0(then number)
// and form a graph for future detecting connections and do it in algorithm (method - searchAlgorithm)
    static void formGraph(int[][] ar, int[][] biggerArray, TreeMap<Integer, List<Integer>> graph) {
        biggerArray = new int[ar.length + 2][ar[0].length + 2];
        int counter = 2;
        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == 1) {
                    biggerArray[i + 1][j + 1] = -1;
                } else {
                    biggerArray[i + 1][j + 1] = counter;
                    counter++;
                }

            }
        }

        startPos = biggerArray[sRow][sColumn];
        finishPos = biggerArray[fRow][fColumn];
        numberedMatrix = biggerArray;

        for (int i = 0; i < biggerArray.length; i++) {
            for (int j = 0; j < biggerArray[i].length; j++) {
                if (biggerArray[i][j] > 1) {
                    int[] wayArray = new int[4];
                    int wayCounter = 0;

                    if (biggerArray[i - 1][j] > 0 && biggerArray[i - 1][j] != 1) {//up
                        wayArray[wayCounter] = biggerArray[i - 1][j];
                        wayCounter++;
                    }
                    if (biggerArray[i][j - 1] > 0 && biggerArray[i][j - 1] != 1) { //left
                        wayArray[wayCounter] = biggerArray[i][j - 1];
                        wayCounter++;
                    }
                    if (biggerArray[i + 1][j] > 0 && biggerArray[i + 1][j] != 1) { // down
                        wayArray[wayCounter] = biggerArray[i + 1][j];
                        wayCounter++;
                    }
                    if (biggerArray[i][j + 1] > 0 && biggerArray[i][j + 1] != 1) { //right
                        wayArray[wayCounter] = biggerArray[i][j + 1];
                        wayCounter++;
                    }
                    List<Integer> list = new ArrayList<Integer>(Arrays.asList(wayArray[0], wayArray[1], wayArray[2], wayArray[3]));
                    list.removeAll(Arrays.asList(Integer.valueOf(0)));
                    graph.put(biggerArray[i][j], new ArrayList(list));


                }

            }
        }

    }
// this method is define for search the way from startPos to finish by algorithm , that takes dictionary , where key is a unit, and his value is an array of Integers, that have a way to go
// also it has a queue , that get a values, that is needed to check
// in dict "path" we have a pathes , that algorithm did before reach  finish , or before the queue members has ended
    static ArrayList<Integer> searchAlgorithm(TreeMap<Integer, List<Integer>> graph, TreeMap<Integer, Integer> path, int startPosition, int finish) {
        Queue<Integer> q = new ArrayDeque<Integer>();
        q.add(startPosition);

        while (!q.isEmpty()) {
            int temp = q.remove();
            //arr
            ArrayList<Integer> ways = new ArrayList<>(graph.get(temp));
            int[] wayss = new int[ways.size()];
            for (int i = 0; i < ways.size(); i++) {
                wayss[i] = ways.get(i);
            }

            if (temp == finish) {
                break;
            }

            for (int i = 0; i < wayss.length; i++) {
                if (!path.containsKey(wayss[i])) {
                    q.add(wayss[i]);
                    path.put(wayss[i], temp);
                }

            }

        }
        if (!path.containsKey(finish)) {
            System.out.println("there is no path(");
            return null ;
        } else {
            int temp = finish;
            int counter = 0;
            int[] tempArray = new int[path.size()];
            tempArray[counter] = temp;
            counter++;
            while (temp != startPosition) {
                temp = path.get(temp).intValue();
                tempArray[counter] = temp;
                counter++;
            }
            ArrayList<Integer> list = new ArrayList<>();


            for (int i = 0; i < tempArray.length; i++) {
                list.add(tempArray[i]);
            }
            list.removeAll(Arrays.asList(Integer.valueOf(0)));
            pathList = list;
            return list;

        }
    }
// this method is defined for printing result in the end , before it ,
// it takes prepared array(that numerated the fields , that was 0 in the start of programm),
// finish, to find and display it in the end
// Arraylist of int that already has needed fields (exactly value of fields)
// and dictionary , that have key-value form of needed path , value of key is his own pair that way to start point from the finish

    private static void consoleResult(int[][] numeredArray, int finish, ArrayList<Integer> pathList , TreeMap<Integer, Integer> pathDict) {
        String[][] strArrayForResult = new String[rows][columns];  //
        for (int i = 0; i < numeredArray.length; i++) {
            for (int j = 0; j < numeredArray[i].length; j++) {
                if (numeredArray[i][j] > 1) {
                    if (pathList.contains(numeredArray[i][j])) {
                        // upper
                        if (numeredArray[i - 1][j] > 1 && pathList.contains(numeredArray[i - 1][j]) && pathDict.get(numeredArray[i-1][j]) == numeredArray[i][j]) {
                            strArrayForResult[i - 1][j - 1] = "U";
                        }
                        // more left
                        else if (numeredArray[i][j - 1] > 1 && pathList.contains(numeredArray[i][j - 1]) && pathDict.get(numeredArray[i][j-1]) == numeredArray[i][j] ) {
                            strArrayForResult[i - 1][j - 1] = "L";
                        }
                        // more down
                        else if (numeredArray[i + 1][j] > 1 && pathList.contains(numeredArray[i + 1][j]) && pathDict.get(numeredArray[i+1][j]) == numeredArray[i][j] ) {
                            strArrayForResult[i - 1][j - 1] = "D";
                        }
                        // more right
                        else if (numeredArray[i][j + 1] > 1 && pathList.contains(numeredArray[i][j + 1]) && pathDict.get(numeredArray[i][j+1]) == numeredArray[i][j] ) {
                            strArrayForResult[i - 1][j - 1] = "R";
                        }
                        // final point -> "F"
                        if (numeredArray[i][j] == finish) {
                            strArrayForResult[i - 1][j - 1] = "F";
                        }
                    }
                    else {
                        strArrayForResult[i - 1][j - 1] = ".";
                    }
                } else if (numeredArray[i][j] == -1) { // Balls
                    strArrayForResult[i - 1][j - 1] = "O";
                }

            }

        }

        printMatrix(strArrayForResult);
        System.out.print("Steps for path = " + pathList.size());
    }
}