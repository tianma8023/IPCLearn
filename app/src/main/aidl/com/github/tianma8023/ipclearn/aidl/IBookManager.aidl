// IBookManager.aidl
package com.github.tianma8023.ipclearn.aidl;

// Declare any non-default types here with import statements
import com.github.tianma8023.ipclearn.aidl.Book;
import com.github.tianma8023.ipclearn.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
