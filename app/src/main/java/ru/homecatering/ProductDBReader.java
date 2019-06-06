package ru.homecatering;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Closeable;

public class ProductDBReader implements Closeable {
    private Cursor cursor;
    private SQLiteDatabase database;
    private String[] allColumns = {"id", "name", "parent", "price", "image"};

    ProductDBReader(SQLiteDatabase database) {
        this.database = database;
    }

    void open(String where) {
        if (where.equals("all")) queryAll();
        else queryByParent(where);
        cursor.moveToFirst();
    }

    public void close() {
        cursor.close();
    }

    public void refresh(String where) {
        int position = cursor.getPosition();
        if (where.equals("all")) queryAll();
        else queryByParent(where);
        cursor.moveToPosition(position);
    }

    private void queryAll() {
        cursor = database.query("products", allColumns, null, null, null, null, null);
    }

    private void queryByParent(String where) {
        String query = "SELECT * FROM " + DataBaseHelper.TABLE_PRODUCTS +
                " INNER JOIN content ON  " + DataBaseHelper.TABLE_PRODUCTS + ".parent =  " + DataBaseHelper.TABLE_CONTENT +
                ".id WHERE  " + DataBaseHelper.TABLE_CONTENT + ".name = \'" + where + "\'";
        Log.i("INFO", query);
        cursor = database.rawQuery(query, null);
    }

    Product getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToProduct();
    }

    private Product cursorToProduct() {
        Product product = new Product();
        product.setId(cursor.getInt(0));
        product.setName(cursor.getString(1));
        product.setParent(cursor.getInt(2));
        product.setPrice(cursor.getInt(3));
        product.setImage(cursor.getString(4));
        return product;
    }

    public int getCount() {
        return cursor.getCount();
    }

}
