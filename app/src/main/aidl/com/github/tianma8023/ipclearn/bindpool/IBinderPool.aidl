// IBinderPool.aidl
package com.github.tianma8023.ipclearn.bindpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
