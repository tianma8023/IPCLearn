package com.github.tianma8023.ipclearn.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.github.tianma8023.ipclearn.db.DBOpenHelper;

/**
 * Remote content provider, 远程（可以看作成其他应用程序）的ContentProvider
 */
public class BookProvider extends ContentProvider {

    public static final String TAG = "BookProvider";

    public static final String AUTHORITY = "com.github.tianma8023.ipclearn.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    public BookProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate, current thread: " + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DBOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from " + DBOpenHelper.TABLE_NAME_BOOK);
        mDb.execSQL("delete from " + DBOpenHelper.TABLE_NAME_USER);

        mDb.execSQL("insert into " + DBOpenHelper.TABLE_NAME_BOOK + " values(3, 'android');");
        mDb.execSQL("insert into " + DBOpenHelper.TABLE_NAME_BOOK + " values(4, 'first code');");
        mDb.execSQL("insert into " + DBOpenHelper.TABLE_NAME_BOOK + " values(5, 'think in php');");

        mDb.execSQL("insert into " + DBOpenHelper.TABLE_NAME_USER + " values(1, 'Jasmin');");
        mDb.execSQL("insert into " + DBOpenHelper.TABLE_NAME_USER + " values(2, 'Tianma');");
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.TABLE_NAME_BOOK;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.TABLE_NAME_USER;
                break;
        }
        return tableName;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete, current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        int count = mDb.delete(table, selection, selectionArgs);
        if (count > 0) {
            ContentResolver contentResolver = mContext.getContentResolver();
            if (contentResolver != null)
                contentResolver.notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert, current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        mDb.insert(table, null, values);
        ContentResolver contentResolver = mContext.getContentResolver();
        if (contentResolver != null)
            contentResolver.notifyChange(uri, null);
        return uri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update, current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);

        if(table == null)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        int row = mDb.update(table, values, selection, selectionArgs);
        if (row > 0) {
            ContentResolver contentResolver = mContext.getContentResolver();
            if (contentResolver != null)
                contentResolver.notifyChange(uri, null);
        }
        return row;
    }
}
