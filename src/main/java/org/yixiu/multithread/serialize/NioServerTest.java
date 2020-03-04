package org.yixiu.multithread.serialize;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServerTest {

    private int port;

    private Selector selector;

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public NioServerTest(int port){
        this.port = port;

        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);

            selector = Selector.open();

            server.register(selector,SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NioServerTest(8081).listen();
    }

    private void listen() {
        System.out.println("Listen on " + port);
        try {
            while (true){
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    process(key);
                }

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void process(SelectionKey key) throws IOException {
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            key = channel.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()){
            SocketChannel channel = (SocketChannel)key.channel();
            int len = channel.read(buffer);
            if(len > 0){
                buffer.flip();
                String content = new String(buffer.array(),0,len);
                key = channel.register(selector,SelectionKey.OP_WRITE);
                key.attach(content);
                System.out.println("output " + content);
            }
        }else if(key.isWritable()){
            SocketChannel channel = (SocketChannel)key.channel();
            String content = (String)key.attachment();
            channel.write(ByteBuffer.wrap(("output " + content).getBytes()));
            channel.close();
        }
    }
}
