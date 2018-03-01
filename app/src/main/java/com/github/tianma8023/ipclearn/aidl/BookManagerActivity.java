package com.github.tianma8023.ipclearn.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.tianma8023.ipclearn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";
    public static final int MSG_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    @BindView(R.id.book_msg_text_view) TextView mTextView;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            // binder线程（子线程）
            Log.d(TAG, "binder died. Thread name: " + Thread.currentThread().getName());
            if (mRemoteBookManager == null)
                return;
            mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mRemoteBookManager = null;
            // TODO: 重新绑定Remote Service
            // 跟 onServiceDisconnected 最大的区别在于，binderDied在子线程，onServiceDisconnected在主线程（UI线程）
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                mRemoteBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                List<Book> bookList = bookManager.getBookList();
                Log.i(TAG, "query book list, list type: " + bookList.getClass().getCanonicalName());
                Log.i(TAG, "query book list: " + bookList);

                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            // 主线程
            Log.i(TAG, "onServiceDisconnected. Thread name: " + Thread.currentThread().getName());
            // TODO: 重新绑定Remote Service

        }
    };

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEW_BOOK_ARRIVED:
                    String message = "received new book: " + msg.obj;
                    Log.d(TAG, message);
                    mTextView.setText(mTextView.getText().toString() + message + "\n");
                    break;
            }
            return true;
        }
    });

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MSG_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);

        ButterKnife.bind(this);

        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        preDestroy();
        super.onDestroy();
    }

    private void preDestroy() {
        // unregister listener
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "unregister listener: " + mOnNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
    }
}
