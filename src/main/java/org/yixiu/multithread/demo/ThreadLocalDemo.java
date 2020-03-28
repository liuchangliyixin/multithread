package org.yixiu.multithread.demo;

public class ThreadLocalDemo {

    static ThreadLocal<String> localVar1 = new ThreadLocal<>();
    static ThreadLocal<Integer> localVar2 = new ThreadLocal<>();

    static void printThreadLocal(){
        System.out.println(Thread.currentThread().getName() + " : " + "String Type Var -->" + localVar1.get());
        System.out.println(Thread.currentThread().getName() + " : " + "Integer Type Var -->" + localVar2.get());

        localVar1.remove();
        localVar2.remove();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            localVar1.set("Thread One Var");
            localVar2.set(66);
            printThreadLocal();
            System.out.println("After Removed: ");
            printThreadLocal();
        },"threadOne").start();

        new Thread(() -> {
            localVar1.set("Thread Two Var");
            localVar2.set(88);
            printThreadLocal();
            System.out.println("After Removed: ");
            printThreadLocal();
        },"threadTwo").start();
    }
}
