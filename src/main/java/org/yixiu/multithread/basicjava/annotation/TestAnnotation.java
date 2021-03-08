package org.yixiu.multithread.basicjava.annotation;

import java.lang.reflect.Method;

/**
 * @author yixiu
 * @title: TestAnnotation
 * @projectName multithread
 * @description: TODO
 * @date 2021/2/2813:02
 */
public class TestAnnotation {
    public static void main(String[] args) throws Exception {
        Class clazz = HelloCat.class;
        InvokeAnno annotation = (InvokeAnno) clazz.getAnnotation(InvokeAnno.class);
        String className = annotation.className();
        System.out.println("class name : " + className);
        String methodName = annotation.methodName();
        System.out.println("method name : " + methodName);

        Class<?> aClass = Class.forName(className);
        Method method = aClass.getDeclaredMethod(methodName);
        method.invoke(aClass.newInstance());
    }
}
