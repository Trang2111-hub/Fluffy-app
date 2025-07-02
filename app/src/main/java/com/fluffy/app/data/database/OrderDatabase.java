package com.fluffy.app.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluffy.app.R;
import com.fluffy.app.util.Utils;

public class OrderDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "orders_database.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_ORDERS = "Orders";
    private static final String TABLE_PRODUCTS = "Products";
    private static final String TABLE_ORDER_ITEMS = "OrderItems";

    private static final String COL_ORDER_ID = "id";
    private static final String COL_ORDER_STATUS = "status";
    private static final String COL_PRODUCT_ID = "id";
    private static final String COL_PRODUCT_NAME = "productName";
    private static final String COL_PRODUCT_PRICE = "price";
    private static final String COL_PRODUCT_IMAGE = "image";
    private static final String COL_ORDERITEM_ORDER_ID = "orderId";
    private static final String COL_ORDERITEM_PRODUCT_ID = "productId";
    private static final String COL_ORDERITEM_QUANTITY = "quantity";

    public OrderDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_STATUS + " TEXT)";
        db.execSQL(createOrdersTable);

        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_PRICE + " REAL, " +
                COL_PRODUCT_IMAGE + " BLOB)";
        db.execSQL(createProductsTable);

        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ORDERITEM_ORDER_ID + " INTEGER, " +
                COL_ORDERITEM_PRODUCT_ID + " INTEGER, " +
                COL_ORDERITEM_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COL_ORDERITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COL_ORDER_ID + "), " +
                "FOREIGN KEY(" + COL_ORDERITEM_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "))";
        db.execSQL(createOrderItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long insertOrder(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_STATUS, status);
        return db.insert(TABLE_ORDERS, null, values);
    }

    public long insertProduct(String productName, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_IMAGE, image);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public void insertOrderItem(int orderId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDERITEM_ORDER_ID, orderId);
        values.put(COL_ORDERITEM_PRODUCT_ID, productId);
        values.put(COL_ORDERITEM_QUANTITY, quantity);
        db.insert(TABLE_ORDER_ITEMS, null, values);
    }

    public Cursor getAllOrdersWithDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " FROM " + TABLE_ORDERS + " o " +
                "JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID, null);
    }

    public Cursor getOrdersByStatusWithDetails(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " FROM " + TABLE_ORDERS + " o " +
                "JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID +
                " WHERE o." + COL_ORDER_STATUS + " = ?", new String[]{status});
    }

    public int updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_STATUS, newStatus);
        return db.update(TABLE_ORDERS, values, COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
    }

    public int deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ORDERS, COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
    }

    public void createSampleData(Context context) {
        if (getNumOfOrders() == 0) {
            byte[] productImage1 = Utils.convertDrawableToByteArray(context, R.drawable.vefluffy1);
            byte[] productImage2 = Utils.convertDrawableToByteArray(context, R.drawable.vefluffy3);

            long orderId1 = insertOrder("Chờ xác nhận");
            long orderId2 = insertOrder("Đang chuẩn bị");
            long orderId3 = insertOrder("Đang vận chuyển");
            long orderId4 = insertOrder("Thành công");
            long orderId5 = insertOrder("Đã trả");
            long orderId6 = insertOrder("Đã hủy");

            long productId1 = insertProduct("Gấu bông Sanrio", 250000, productImage1);
            long productId2 = insertProduct("Búp bê Barbie", 300000, productImage2);

            insertOrderItem((int) orderId1, (int) productId1, 1);
            insertOrderItem((int) orderId2, (int) productId2, 2);
            insertOrderItem((int) orderId3, (int) productId1, 1);
            insertOrderItem((int) orderId4, (int) productId2, 3);
            insertOrderItem((int) orderId5, (int) productId1, 2);
            insertOrderItem((int) orderId6, (int) productId2, 1);
        }
    }

    private int getNumOfOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}