package com.github.tianma8023.ipclearn.bindpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BindPoolService extends Service {

    private static final String TAG = "BindPoolService";

    private Binder mBinderPool = new BindPool.BindPoolImpl();

    public BindPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
