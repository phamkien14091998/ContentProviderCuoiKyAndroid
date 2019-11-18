package com.example.provider_lan2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    static final String AUTHORITY= "com.example.provider_lan2";
    static final String CONTENT_PATH="sachdata";
    static final String URL="content://"+AUTHORITY+"/"+CONTENT_PATH;
    static final Uri CONTENT_URI= Uri.parse(URL);
    static final String TABLE_NAME="Sachs";
    private SQLiteDatabase db;

    private static HashMap<String,String> BOOKS_PROJECTION_MAP;
    static final int ALLITEMS=1;
    static final int ONEITEM=2;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,CONTENT_PATH,ALLITEMS);
        uriMatcher.addURI(AUTHORITY,CONTENT_PATH+"/#",ONEITEM);
    }

    public MyContentProvider(){

    }

    @Override
    public boolean onCreate() {
        Context context= getContext();
        DBHelper dbHelper= new DBHelper(context);
        db= dbHelper.getWritableDatabase();
        if(db==null)
            return false;
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] project, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder= new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                sqLiteQueryBuilder.setProjectionMap(BOOKS_PROJECTION_MAP);
                break;
            case ONEITEM:
                sqLiteQueryBuilder.appendWhere("msSach" + "="+uri.getPathSegments().get(1));
                break;
        }
        if (sortOrder == null || sortOrder=="")
            sortOrder="tenSach";
        Cursor cursor= sqLiteQueryBuilder.query(db,project,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
       long number_row= db.insert(TABLE_NAME,"",contentValues);
        if(number_row >0){
            Uri uri1= ContentUris.withAppendedId(CONTENT_URI,number_row);
            getContext().getContentResolver().notifyChange(uri1,null);
            return uri1;
        }
        throw new SQLException("Fail to add record intoo "+uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count= 0;
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                count= db.delete(TABLE_NAME,selection,selectionArgs);
                break;
            case ONEITEM:
                String id= uri.getPathSegments().get(1);
                count= db.delete(TABLE_NAME,"msSach"+"="+id+(!TextUtils.isEmpty(selection) ? "AND(" +selection +')': ""),selectionArgs);
                break;

                default: throw new IllegalArgumentException("Unkow URI"+uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return  count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                count= db.update(TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case ONEITEM:
                String id= uri.getPathSegments().get(1);
                count= db.update(TABLE_NAME,contentValues,"msSach"+"="+id+(!TextUtils.isEmpty(selection) ? "AND(" +selection +')': ""),selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unkow URI"+uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return  count;


    }
}
