package com.github.tianma8023.ipclearn.bindpool;

import android.os.RemoteException;

/**
 * Created by Tianma on 2018/3/1.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
