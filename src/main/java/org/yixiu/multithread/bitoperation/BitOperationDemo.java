package org.yixiu.multithread.bitoperation;

public class BitOperationDemo {
    public static void main(String[] args) {
        for(int i=1;i<10;i++){
            System.out.println(i + " ========> " + isPowerOfTwo(i));
        }
        System.out.println("==========================");
    }

    /**
     * 判断一个数是否是2的幂
     * @param val
     * @return
     */
    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }
}
