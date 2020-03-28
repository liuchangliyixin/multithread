package org.yixiu.multithread.demo;

/**
 * threadLocals 只属于当前线程本身，不会被继承
 * inheritableThreadLocals 在创建子线程时会从父线程复制一份给子线程，可以继承
 */
public class InheritableThreadLocalDemo {

    public static ThreadLocal<String> localVar = new ThreadLocal<>();
    public static ThreadLocal<String> localVarInheritable = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        localVar.set("Main Value");
        localVarInheritable.set("Main Value Inheritable");

        new Thread(() -> {
            System.out.println("Thread Children Get Main Var Value :" + localVar.get());
            System.out.println("Thread Children Get Inheritable Main Var Value :" + localVarInheritable.get());
        },"Thread Children").start();

        Thread.currentThread().sleep(2000);

        System.out.println("Main Local Var Value : " + localVar.get());
        System.out.println("Main Inheritable Local Var Value : " + localVarInheritable.get());
    }
}
