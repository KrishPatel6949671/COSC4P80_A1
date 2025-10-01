import java.util.Arrays;

public class BAM {
    public BAM(){
        // left side vectors
        int[][] A = {
            {-1, 1, 1, 1, -1},
            {-1, -1, -1, -1, 1},
            {-1, -1, -1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1},
            {1, 1, -1, 1, -1}
        };

        //right side vectors
        int[][] B = {
            {1, 1, -1, 1},
            {1, -1, -1, -1},
            {-1, -1, 1, 1},
            {-1, 1, 1, -1},
            {-1, 1, -1, 1},
            {1, 1, 1, 1},
            {-1, -1, -1, 1}
        };

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
            System.out.println("Weight Matrix: \n" + Arrays.deepToString(W));
        }

        //recall A -> B
        System.out.println("\nRecall B from A:");
        for (int[] vectorA : A) {
            int[] result = new int[m];
            for (int j = 0; j < m; j++) {
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    sum += vectorA[i] * W[i][j];
                }
                result[j] = normalize(sum);
            }
            System.out.println(Arrays.toString(vectorA) + " --> " + Arrays.toString(result));
        }

        //recall B -> A
        System.out.println("\nRecall A from B:");
        for (int[] vectorB : B) {
            int[] result = new int[n];
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int i = 0; i < m; i++) {
                    sum += vectorB[i] * W[j][i];
                }
                result[j] = normalize(sum);
            }
            System.out.println(Arrays.toString(vectorB) + " --> " + Arrays.toString(result));
        }

        //calculate cross talk for A vectors
        System.out.println("\nCross Talk for A vectors:");
        int globalCrosstalk = 0;

        for (int q = 0; q < A.length; q++) {
            int[] crosstalk = new int[m]; // same length as B
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

    }


    private int normalize(int x) {return x >= 0 ? 1 : -1;}

    private int dotProduct(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += v1[i] * v2[i];
        }
        return sum;
    }

    public static void main(String[] args){BAM bam = new BAM();}
}
