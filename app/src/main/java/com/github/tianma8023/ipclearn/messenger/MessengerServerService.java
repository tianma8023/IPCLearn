package com.github.tianma8023.ipclearn.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.github.tianma8023.ipclearn.utils.TConstants;

/**
 * 运行在 :remote 进程上的后台Server Service
 */
public class MessengerServerService extends Service {

    private static final String TAG = "MessengerServerService";

    public MessengerServerService() {
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TConstants.MSG_FROM_CLIENT:
                    Log.i(TAG, "msg from client : " + msg.getData().get(TConstants.KEY_MSG));
                    Messenger client = msg.replyTo;
                    Message replyMsg = Message.obtain(null, TConstants.MGS_FROM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString(TConstants.KEY_REPLY, "Okay, I'm server, your message is received");
                    replyMsg.setData(bundle);
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
