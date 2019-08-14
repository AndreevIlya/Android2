package ru.homecatering;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

public class ContentDBReader implements Closeable {
    private Cursor cursor;
    private SQLiteDatabase database;
    private String[] allColumns = {"id", "name"};

    public ContentDBReader(SQLiteDatabase database) {
        this.database = database;
    }

    public void open() {
        query();
        cursor.moveToFirst();
    }

    public void close() {
        cursor.close();
    }

    public void refresh() {
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    private void query() {
        cursor = database.query(DataBaseHelper.TABLE_CONTENT, allColumns, null, null, null, null, null);
    }

    public ContentItem getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToProduct();
    }

    private ContentItem cursorToProduct() {
        ContentItem item = new ContentItem();
        item.setId(cursor.getInt(0));
        item.setName(cursor.getString(1));
        return item;
    }

    public int getCount() {
        return cursor.getCount();
    }

}
