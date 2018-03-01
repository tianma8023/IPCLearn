package com.github.tianma8023.ipclearn.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {

    private boolean mIsServiceDestroyed = false;

    private String[] mDefinedMessages = new String[]{
            "Hello World",
            "What's your name?",
            "Tell you a joke: your brain has two parts, the left has nothing right, the right has nothing left",
            "Everything will be okay in the end",
            "You are the apple of my eye",
            "You are the sunshine of my life",
            "You are the CSS to my HTML",
    };

    public TCPServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        new Thread(new TCPServer()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
    }

    private class TCPServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                // 监听本地 18225 端口
                serverSocket = new ServerSocket(18225);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestroyed) {
                try {
                    // 接收客户端请求
                    final Socket clientSocket = serverSocket.accept();
                    // 开启新线程处理客户端连接
                    new Thread(new ClientWorker(clientSocket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ClientWorker implements Runnable {

        private Socket mClientSocket;

        ClientWorker(Socket clientSocket) {
            mClientSocket = clientSocket;
        }

        @Override
        public void run() {
            responseClient();
        }

        private void responseClient() {
            if (mClientSocket == null)
                return;
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                br = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(mClientSocket.getOutputStream()));

                pw.println("Welcome to our chat room");
                pw.flush();

                while(!mIsServiceDestroyed) {
                    String line = br.readLine();
                    System.out.println("msg from client: " + line);
                    if (line == null) {
                        // 客户端断开连接
                        break;
                    }
                    int randomIndex = new Random().nextInt(mDefinedMessages.length);
                    String responseMsg = mDefinedMessages[randomIndex];
                    pw.println(responseMsg);
                    pw.flush();
                    System.out.println("send: " + responseMsg);
                }
                System.out.println("client disconnect");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pw != null) {
                    pw.close();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
