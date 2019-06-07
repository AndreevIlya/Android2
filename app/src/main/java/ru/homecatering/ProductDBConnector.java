package ru.homecatering;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Closeable;

public class ProductDBConnector implements Closeable {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    private ProductDBReader productDBReader;
    private ContentDBReader contentDBReader;

    ProductDBConnector(Context context) {
        this.dbHelper = new DataBaseHelper(context);
    }

    void open(String where) {
        database = dbHelper.getWritableDatabase();
        productDBReader = new ProductDBReader(database);
        contentDBReader = new ContentDBReader(database);
        productDBReader.open(where);
        contentDBReader.open();
    }

    public void close() {
        productDBReader.close();
        contentDBReader.close();
        database.close();
    }

    private void addToContent(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        database.insert(DataBaseHelper.TABLE_CONTENT, null, values);
    }

    public Product addProduct(String name, int parent, int price, String image) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("parent", parent);
        values.put("price", price);
        values.put("image", image);
        long id = database.insert(DataBaseHelper.TABLE_PRODUCTS, null, values);
        addToContent(name);
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setParent(parent);
        product.setPrice(price);
        product.setImage(image);
        return product;
    }

    ProductDBReader getProductDBReader() {
        Log.i("INFO", "Got product reader");
        return productDBReader;
    }

    ContentDBReader getContentDBReader() {
        return contentDBReader;
    }
}
