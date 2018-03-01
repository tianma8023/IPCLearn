package com.github.tianma8023.ipclearn.bindpool;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.tianma8023.ipclearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindPoolClientActivity extends AppCompatActivity {

    private static final String TAG = "BindPoolClientActivity";

    public static final int SHOW_MSG = 0;

    @BindView(R.id.bindPoolShowMsg) TextView bindPoolShowMsg;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_MSG:
                    bindPoolShowMsg.setText((String)msg.obj);
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_pool_client);
        ButterKnife.bind(this);
        new Thread(new Worker()).start();
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            doWork();
        }

        private void doWork() {
            BindPool bindPool = BindPool.getInstance(BindPoolClientActivity.this);
            IBinder securityCenterBinder = bindPool.queryBinder(BindPool.BINDER_SECURITY_CENTER);
            ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(securityCenterBinder);

            Log.d(TAG, "visit ISecurityCenter");
            String content = "Hello World,你好世界";
            StringBuilder sb = new StringBuilder();
            sb.append("Content: " + content + "\n");
            try {
                String password = securityCenter.encrypt(content);
                sb.append("encrypt: " + password + "\n");
                String decrypt = securityCenter.decrypt(password);
                sb.append("decrypt: " + decrypt + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            IBinder computeBinder = bindPool.queryBinder(BindPool.BINDER_COMPUTE);
            ICompute compute = ComputeImpl.asInterface(computeBinder);
            Log.d(TAG, "visit ICompute");
            try {
                sb.append("2 + 3 = " + compute.add(2, 3));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mHandler.obtainMessage(SHOW_MSG, sb.toString()).sendToTarget();
        }
    }

}
