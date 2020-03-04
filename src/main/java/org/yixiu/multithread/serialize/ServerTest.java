package org.yixiu.multithread.serialize;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) {
        ServerSocket serverSocket =null;
        ObjectInputStream in =null;

        try {
            serverSocket = new ServerSocket(8081);
            Socket socket = serverSocket.accept();
            in = new ObjectInputStream(socket.getInputStream());
            /*User user = (User)in.readObject();
            System.out.println(user);*/
            System.out.println(in.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
