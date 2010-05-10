public class TestQuestion2 {

    /**
     * һ���������˾��java�����㷨�⣡�㷨�����⣺�ù�˾�������1����Ҫ����10���������ꡣ
     * ��Ŀ���£���1��2��2��3��4��5���������֣���javaдһ��main��������ӡ�����в�ͬ�����У��磺
     * 512234��412345�ȣ�Ҫ��"4"�����ڵ���λ��"3"��"5"����������
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