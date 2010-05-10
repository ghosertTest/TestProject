
public class DepthSearchGraph {
    
    private int n;
    private int[][] mat;
    private int[] visited;
    private String result = "";
    
    public DepthSearchGraph(int m1[][]) {
        n = m1.length;
        mat = m1;
        visited = new int[n];
    }
    
    public static void main(String[] args) {
        int mat1[][] = {
                {0, 1, 0, 1},
                {1, 0, 1, 1},
                {0, 1, 0, 1},
                {1, 1, 1, 0}
        };
        new DepthSearchGraph(mat1).depthFirstSearch();
    }
    
    public void depthFirstSearch() {
        for (int startIndex = 0; startIndex < n; startIndex++) {
            depthfs(startIndex);
        }
    }
    
    public void depthfs(int startIndex) {
        result = result + startIndex;
        visited[startIndex] = 1;
        if (result.length() == n) {
            System.out.println(result);
        }
        for (int j = 0; j < n; j++) {
            if (mat[startIndex][j] == 1 && visited[j] == 0) {
                depthfs(j);
            } else {
                continue;
            }
        }
        visited[startIndex] = 0;
        result = result.substring(0, result.length() - 1);
    }
}
