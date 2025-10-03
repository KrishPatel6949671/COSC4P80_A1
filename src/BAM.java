/*
KRISH PATEL
Student#: 6949671
COSC 4P80 Assignment 1
uncomment code labelled part B to see crosstalk calculation
uncomment extra vectors to see crosstalk effects
git files are in here because I used git to work between my desktop and laptop
*/


import java.util.Arrays;
import java.util.Random;

public class BAM {
    private Random rand = new Random();

    public BAM(){
        // left side vectors
        int[][] A = {
            {-1, 1, 1, 1, -1},
            {-1, -1, -1, -1, 1},
            {-1, -1, -1, 1, 1}
                /*
            {1, 1, 1, 1, 1},
            {1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {1, 1, -1, 1, -1}
                 */
        };
                                        //commented out vectors from part C
        //right side vectors
        int[][] B = {
            {1, 1, -1, 1},
            {1, -1, -1, -1},
            {-1, -1, 1, 1}
                /*
            {-1, 1, 1, -1},
            {-1, 1, -1, 1},
            {1, 1, 1, 1},
            {-1, -1, -1, 1}
                 */
        };

        //initialize weight matrix
        int n = A[0].length; // 5
        int m = B[0].length; // 4
        int[][] W = new int[n][m];

        //compute weight matrix
        for(int k = 0; k < A.length; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < m; j++){
                    W[i][j] += A[k][i] * B[k][j];
                }
            }
            //System.out.println("Weight Matrix: \n" + Arrays.deepToString(W)); //uncomment to see weight matrix after each addition
        }

        //recall A -> B
        System.out.println("\nRecall B from A:");
        for (int[] vectorA : A) {
            recallB(vectorA, W, n, m);
        }

        //recall B -> A
        System.out.println("\nRecall A from B:");
        for (int[] vectorB : B) {
            recallA(vectorB, W, n, m);
        }

        //PART D
        for(int run=1; run<=20; run++){
            System.out.println("Run " + run + ":");
            //pick random A vector and mutate it
            int index = rand.nextInt(A.length);
            int[] originalA = A[index];
            int[] mutatedA = mutate(originalA, 0.2);

            //recall B from mutated A then recall corrected A from B (A(mutated) -> B -> A(corrected))
            int[] recalledB = recallB(mutatedA, W, n, m);
            int[] correctedA = recallA(recalledB, W, n, m);

            //calculate Hamming distances
            int distMutated = hammingDistance(originalA, mutatedA);
            int distCorrected = hammingDistance(originalA, correctedA);

            System.out.println("Original A:  " + Arrays.toString(originalA) + " | Mutated A:   " + Arrays.toString(mutatedA) + " | Hamming Distance: " + distMutated + " | Corrected A: " + Arrays.toString(correctedA) + " | Hamming Distance: " + distCorrected);
        }

        /* PART C
        //calculate cross talk for A vectors
        System.out.println("\nCross Talk for A vectors:");
        int globalCrosstalk = 0;

        for (int q = 0; q < A.length; q++) {
            int[] crosstalk = new int[m];
            for (int k = 0; k < A.length; k++) {
                if (k == q) continue; // skip self
                int cos = dotProduct(A[q], A[k]);
                for (int j = 0; j < m; j++) {
                    crosstalk[j] += cos * B[k][j];
                }
            }
            // global crosstalk magnitude
            for (int val : crosstalk) {
                globalCrosstalk += Math.abs(val);
            }
            System.out.println("A" + (q+1) + ": " + Arrays.toString(crosstalk));
        }

        System.out.println("\nGlobal Crosstalk = " + globalCrosstalk);
         */

    }

    /**
     * Recalls vector B from vector A using weight matrix W
     * @param inputA vector from A
     * @param W weight matrix
     * @param n number of rows in W
     * @param m number of columns in W
     * @return recalled vector B
     */
    private int[] recallB(int[] inputA, int[][] W, int n, int m) {
        int[] result = new int[m];
        for (int j = 0; j < m; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += inputA[i] * W[i][j];
            }
            result[j] = normalize(sum);
        }
        System.out.println(Arrays.toString(inputA) + " --> " + Arrays.toString(result));
        return result;
    }

    /**
     * Recalls vector A from vector B using weight matrix W
     * @param inputB vector from B
     * @param W weight matrix
     * @param n number of rows in W
     * @param m number of columns in W
     * @return recalled vector A
     */
    private int[] recallA(int[] inputB, int[][] W, int n, int m) {
        int[] result = new int[n];
        for (int j = 0; j < n; j++) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum += inputB[i] * W[j][i];
            }
            result[j] = normalize(sum);
        }
        System.out.println(Arrays.toString(inputB) + " --> " + Arrays.toString(result));
        return result;
    }

    /**
     * Mutates a vector by flipping each bit with a given mutation rate
     * @param vector the original vector
     * @param mutationRate the probability of each bit being flipped
     * @return the mutated vector
     */
    private int[] mutate(int[] vector, double mutationRate) {
        int[] mutated = Arrays.copyOf(vector, vector.length);
        for (int i = 0; i < vector.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                mutated[i] = -mutated[i];
            }
        }
        return mutated;
    }

    /**
     * Calculates the Hamming distance between two vectors
     * @param v1 first vector
     * @param v2 second vector
     * @return the Hamming distance
     */
    private int hammingDistance(int[] v1, int[] v2) {
        int distance = 0;
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] != v2[i]) distance++;
        }
        return distance;
    }

    /**
     * Normalizes a value to +1 or -1
     * @param x the value to normalize
     * @return +1 if x >= 0, -1 otherwise
     */
    private int normalize(int x) {return x >= 0 ? 1 : -1;}

    /**
     * Calculates the dot product of two vectors
     * @param v1 first vector
     * @param v2 second vector
     * @return the dot product
     */
    private int dotProduct(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += v1[i] * v2[i];
        }
        return sum;
    }

    public static void main(String[] args){BAM bam = new BAM();}
}
