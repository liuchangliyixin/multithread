package org.yixiu.multithread.basicjava.annotation;

import java.lang.annotation.*;

/**
 * @author yixiu
 * @title: InvokeAnno
 * @projectName multithread
 * @description: TODO
 * @date 2021/2/2812:39
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SuppressWarnings("all")
public @interface InvokeAnno {

    int age() default 3;

    String className();

    String methodName() default "hello";
}
