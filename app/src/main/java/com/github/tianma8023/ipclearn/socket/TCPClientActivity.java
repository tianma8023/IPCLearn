package com.github.tianma8023.ipclearn.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tianma8023.ipclearn.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TCPClientActivity extends AppCompatActivity {

    public static final int MSG_SERVER_CONNECTED = 1;
    public static final int MSG_RECEIVE_NEW_MSG = 2;

    private static final String TAG = "TCPClientActivity";

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    @BindView(R.id.msg_board) TextView msgBoard;
    @BindView(R.id.send_random_msg_btn) TextView sendMsgButton;

    private static String[] mDefinedMsgs = new String[]{
            "This is client, Hello~",
            "How can I change the world?",
            "Nothing's gonna change my love for you",
            "You are my today and all of my tomorrows",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        ButterKnife.bind(this);
        startService(new Intent(this, TCPServerService.class));
        new Thread(new TCPServerMonitor()).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClientSocket != null && !mClientSocket.isClosed()) {
            try {
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECEIVE_NEW_MSG:
                    msgBoard.setText(msgBoard.getText() + (String)msg.obj);
                    return true;
                case MSG_SERVER_CONNECTED:
                    Toast.makeText(TCPClientActivity.this, "TCP Server Connected", Toast.LENGTH_SHORT).show();
                    sendMsgButton.setEnabled(true);
                    return true;
            }
            return false;
        }
    });

    @OnClick(R.id.send_random_msg_btn)
    void sendRandomMsg() {
        final String msg = mDefinedMsgs[new Random().nextInt(mDefinedMsgs.length)];
        if (mPrintWriter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println(msg);
                }
            }).start();
            String showMsg = "client " + getCurrentTimeStamp() + ": " + msg + "\n";
            msgBoard.setText(msgBoard.getText() + showMsg);
        }
    }

    private class TCPServerMonitor implements Runnable {

        @Override
        public void run() {
            connectTCPServer();
        }

        private void connectTCPServer() {
            // 建立客户端与服务端连接
            Socket clientSocket = null;
            while (clientSocket == null) {
                try {
                    clientSocket = new Socket("localhost", 18225);
                    mClientSocket = clientSocket;
                    mPrintWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                    mHandler.sendEmptyMessage(MSG_SERVER_CONNECTED);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "connect tcp server failed, retry...");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            // 接收服务端消息
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (!TCPClientActivity.this.isFinishing()) {
                    String msg = br.readLine();
                    if (msg != null) {
                        String timeStamp = getCurrentTimeStamp();
                        final String receivedMsg = "server " + timeStamp + ": " + msg + "\n";
                        System.out.println(receivedMsg);
                        mHandler.obtainMessage(MSG_RECEIVE_NEW_MSG, receivedMsg).sendToTarget();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mPrintWriter != null) {
                    mPrintWriter.close();
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("TCPClientActivity disconnect");
            }

        }
    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("(HH:mm:ss)", Locale.getDefault()).format(new Date());
    }
}
