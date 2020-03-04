package org.yixiu.multithread.basicnio;

import java.nio.IntBuffer;

public class IntBufferDemo {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for(int i=0;i<buffer.capacity();i++){
            buffer.put(i+1);
        }
        buffer.flip();
        while(buffer.hasRemaining()){
            System.out.println(buffer.get()+" ");
        }
    }
}
