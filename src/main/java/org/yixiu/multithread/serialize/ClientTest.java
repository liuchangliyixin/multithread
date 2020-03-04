package org.yixiu.multithread.serialize;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {

        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            socket = new Socket("127.0.0.1",8081);
            /*User user = new User();
            user.setName("yixiu");*/
            out = new ObjectOutputStream(socket.getOutputStream());
            //out.writeObject(user);
            out.writeInt(345);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
