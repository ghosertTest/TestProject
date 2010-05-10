public class TestQuestion2 {

    /**
     * 一著名软件公司的java笔试算法题！算法程序题：该公司笔试题就1个，要求在10分钟内作完。
     * 题目如下：用1、2、2、3、4、5这六个数字，用java写一个main函数，打印出所有不同的排列，如：
     * 512234、412345等，要求："4"不能在第三位，"3"与"5"不能相连。
     */

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        TestQuestion2 tester = new TestQuestion2();
        tester.process();

        long costTime = System.currentTimeMillis() - startTime;
        System.out.println("It cost time: " + costTime);
    }

    private void process() {
        int count = 0;
        for (int i = 122345; i <= 543221; i++) {
            if (isValidStr(Integer.toString(i))) {
                System.out.println(i);
                count++;
            }
        }
    }

    private boolean isValidStr(String str) {
        if (str.indexOf('1') == -1 || str.indexOf('2') == -1
                || str.indexOf('3') == -1 || str.indexOf('4') == -1
                || str.indexOf('5') == -1) {
            return false;
        } else if (!str.matches(".*2.*2.*")) {
            return false;
        } else if (str.matches("\\d\\d4\\d\\d\\d")) {
            return false;
        } else if (str.matches(".*(35|53).*")) {
            return false;
        }
        return true;
    }
}