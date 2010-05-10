import java.util.LinkedList;

public class BreadthSearchGraph {
    
    private int n;
    private int[][] mat;
    private int[] visited;
    private String result = "";
    
    public BreadthSearchGraph(int m1[][]) {
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
        new BreadthSearchGraph(mat1).breadthFirstSearch();
    }
    
    public void breadthFirstSearch() {
        for (int startIndex = 0; startIndex < n; startIndex++) {
            breadthfs(startIndex);
            result = "";
            visited = new int[n];
        }
    }
    
    public void breadthfs(int startIndex) {
        LinkedList list = new LinkedList();
        result = result + startIndex;
        visited[startIndex] = 1;
        list.add(new Integer(startIndex));
        
        while (!list.isEmpty()) {
            int i = ((Integer) list.removeFirst()).intValue();
	        for (int j = 0; j < n; j++) {
	            if (mat[i][j] == 1 && visited[j] == 0) {
                    result = result + j;
			        if (result.length() == n) {
			            System.out.println(result);
			        }
                    visited[j] = 1;
                    list.add(new Integer(j));
	            } else {
	                continue;
	            }
	        }
        }
    }
}
