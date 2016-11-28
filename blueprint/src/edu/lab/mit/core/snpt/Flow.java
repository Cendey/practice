package edu.lab.mit.core.snpt;

/**
 * <p>Project: blueprint</p>
 * <p>Summary: Flow</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Institution: MIT LIB Co., Ltd.</p>
 *
 * @author chao.deng
 * @version 1.0
 * @since 4/15/2016
 */
public class Flow {

    private int i = 20;
    private static int j;

    private static Flow first;

    private Flow() {
        this(0);
        System.out.println("Now, j = " + j);
        System.out.println("Constructor invoked without param!");
        System.out.println("---------------------------------------------------------------");
    }

    private Flow(int i) {
        this.i = i;
        System.out.println("Now, j = " + j);
        System.out.println("Constructor invoked with param i = !" + i);
        System.out.println("---------------------------------------------------------------");
    }

    {
        System.out.println("Now, j = " + j);
        ++j;
        System.out.println("None Static Block Initialization!");
        System.out.println("---------------------------------------------------------------");
    }

    static {
        j = 10;
        first = new Flow();
        System.out.println("Static Block Initialization!");
        System.out.println("---------------------------------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("Now, first.j = " + first.j);
        System.out.println("Now, j = " + j);
        Flow last = new Flow();
        System.out.println("Now, last.j = " + last.j);
        System.out.println("Now, j = " + j);
    }
}
