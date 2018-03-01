package com.github.tianma8023.ipclearn.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.tianma8023.ipclearn.R;
import com.github.tianma8023.ipclearn.aidl.Book;

public class BookProviderActivity extends AppCompatActivity {

    private static final String TAG = "BookProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_provider);
        initData();
    }

    private void initData() {
        Uri bookUri = BookProvider.BOOK_CONTENT_URI;
        Uri userUri = BookProvider.USER_CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put("_id", "10");
        values.put("name", "Hello Android");

        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while(bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(bookCursor.getColumnIndex("_id"));
            book.bookName = bookCursor.getString(bookCursor.getColumnIndex("name"));
            Log.d(TAG, "query book: " + book);
        }
    }

}
