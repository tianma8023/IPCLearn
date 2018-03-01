// IOnNewBookArrivedListener.aidl
package com.github.tianma8023.ipclearn.aidl;

import com.github.tianma8023.ipclearn.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
