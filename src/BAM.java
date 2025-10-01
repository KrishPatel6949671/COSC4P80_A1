import java.util.Arrays;

public class BAM {

    public BAM(){
        // left side vectors
        int[][] A = {
            {-1, 1, 1, 1, -1},
            {-1, -1, -1, -1, 1},
            {-1, -1, -1, 1, 1}
        };

        //right side vectors
        int[][] B = {
            {1, 1, -1, 1},
            {1, -1, -1, -1},
            {-1, -1, 1, 1}
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
            //System.out.println("Weight Matrix: \n" + Arrays.deepToString(W));
        }

        //recall A -> B
        



    }

    public static void main(String[] args){BAM bam = new BAM();}
}
