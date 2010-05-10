import java.util.Iterator;
import java.util.TreeSet;

public class TestQuestion {

    /**
     * 一著名软件公司的java笔试算法题！算法程序题：该公司笔试题就1个，要求在10分钟内作完。
     * 题目如下：用1、2、2、3、4、5这六个数字，用java写一个main函数，打印出所有不同的排列，如：
     * 512234、412345等，要求："4"不能在第三位，"3"与"5"不能相连。
     */

    private String[] b = new String[] { "1", "2", "2", "3", "4", "5" };

    private int n = b.length;

    private boolean[] visited = new boolean[n];

    private int[][] a = new int[n][n];

    private String result = "";

    private TreeSet set = new TreeSet();

    public static void main(String[] args) {
        new TestQuestion().start();
    }

    public void start() {

        // Initial the map a[][]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    a[i][j] = 0;
                } else {
                    a[i][j] = 1;
                }
            }
        }

        // 3 and 5 can not be the neighbor.
        a[3][5] = 0;
        a[5][3] = 0;

        // Begin to depth search.
        for (int i = 0; i < n; i++) {
            this.depthFirstSearch(i);
        }

        // Print result treeset.
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String string = (String) it.next();
            // "4" can not be the third position.
            if (string.indexOf("4") != 2) {
                System.out.println(string);
            }
        }
    }

    private void depthFirstSearch(int startIndex) {
        visited[startIndex] = true;
        result = result + b[startIndex];
        if (result.length() == n) {
            // Filt the duplicate value.
            set.add(result);
        }
        for (int j = 0; j < n; j++) {
            if (a[startIndex][j] == 1 && visited[j] == false) {
                depthFirstSearch(j);
            } else {
                continue;
            }
        }

        // restore the result value and visited value after listing a node.
        result = result.substring(0, result.length() - 1);
        visited[startIndex] = false;
    }
}
