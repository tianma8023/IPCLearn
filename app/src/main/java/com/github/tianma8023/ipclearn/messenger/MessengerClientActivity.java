package com.github.tianma8023.ipclearn.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.tianma8023.ipclearn.R;
import com.github.tianma8023.ipclearn.utils.TConstants;

public class MessengerClientActivity extends AppCompatActivity {

    private static final String TAG = "MessengerClientActivity";

    private Messenger mMessenger;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, TConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString(TConstants.KEY_MSG, "Hello, this is client.");
            msg.setData(data);
            // 指定message的replyTo为Messenger对象
            msg.replyTo = mGetReplyMessenger;
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static class MessengerHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TConstants.MGS_FROM_SERVER:
                    Log.i(TAG, "msg from server: " + msg.getData().getString(TConstants.KEY_REPLY));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_client);

        Intent intent = new Intent(this, MessengerServerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
