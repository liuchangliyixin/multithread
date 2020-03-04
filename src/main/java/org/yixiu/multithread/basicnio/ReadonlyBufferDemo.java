package org.yixiu.multithread.basicnio;

import java.nio.ByteBuffer;

public class ReadonlyBufferDemo {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for(int i=0;i<buffer.capacity();i++){
            buffer.put((byte) (i+1));
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        for(int i=0;i<buffer.capacity();i++){
            byte b = buffer.get(i);
            buffer.put(i,b*=10);
        }

        /*readOnlyBuffer.flip();
        while(readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }*/

        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(10);
        while(readOnlyBuffer.remaining()>0){
            System.out.println(readOnlyBuffer.get());
        }
    }
}
