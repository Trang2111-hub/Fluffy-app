package com.fluffy.app.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String COL_PRODUCT_IMAGE = "image"; // TEXT cho imageUrl
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
                COL_ORDER_STATUS + " TEXT, " +
                "shippingFee REAL DEFAULT 0, " +
                "discount REAL DEFAULT 0)";
        db.execSQL(createOrdersTable);

        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_PRICE + " REAL, " +
                COL_PRODUCT_IMAGE + " TEXT)"; // Sử dụng TEXT cho imageUrl
        db.execSQL(createProductsTable);

        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ORDERITEM_ORDER_ID + " INTEGER, " +
                COL_ORDERITEM_PRODUCT_ID + " INTEGER, " +
                COL_ORDERITEM_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COL_ORDERITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COL_ORDER_ID + "), " +
                "FOREIGN KEY(" + COL_ORDERITEM_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "), " +
                "PRIMARY KEY (" + COL_ORDERITEM_ORDER_ID + ", " + COL_ORDERITEM_PRODUCT_ID + "))"; // Thêm PRIMARY KEY để tránh trùng lặp
        db.execSQL(createOrderItemsTable);
        Log.d("OrderDatabase", "Tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long insertOrder(String status, double shippingFee, double discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_STATUS, status);
        values.put("shippingFee", shippingFee);
        values.put("discount", discount);
        long id = db.insert(TABLE_ORDERS, null, values);
        Log.d("OrderDatabase", "Inserted order with ID: " + id + ", Status: " + status);
        return id;
    }

    public long insertProduct(String productName, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_IMAGE, imageUrl != null ? imageUrl : "");
        long id = db.insert(TABLE_PRODUCTS, null, values);
        Log.d("OrderDatabase", "Inserted product with ID: " + id + ", Name: " + productName);
        return id;
    }

    public void insertOrderItem(int orderId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDERITEM_ORDER_ID, orderId);
        values.put(COL_ORDERITEM_PRODUCT_ID, productId);
        values.put(COL_ORDERITEM_QUANTITY, quantity);
        long result = db.insert(TABLE_ORDER_ITEMS, null, values);
        Log.d("OrderDatabase", "Inserted order item: orderId=" + orderId + ", productId=" + productId + ", Result: " + result);
    }

    public Cursor getAllOrdersWithDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID, null);
        Log.d("OrderDatabase", "getAllOrdersWithDetails returned " + (cursor != null ? cursor.getCount() : 0) + " rows");
        return cursor != null ? cursor : null; // Trả về null nếu cursor null
    }

    public Cursor getOrdersByStatusWithDetails(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID;
        if (status != null) {
            query += " WHERE o." + COL_ORDER_STATUS + " = ?";
        }
        Cursor cursor = db.rawQuery(query, status != null ? new String[]{status} : null);
        Log.d("OrderDatabase", "getOrdersByStatusWithDetails for status=" + status + " returned " + (cursor != null ? cursor.getCount() : 0) + " rows");
        return cursor != null ? cursor : null;
    }

    public Cursor getOrderByIdWithDetails(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", o.shippingFee, o.discount, p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID +
                " WHERE o." + COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
        Log.d("OrderDatabase", "getOrderByIdWithDetails for orderId=" + orderId + " returned " + (cursor != null ? cursor.getCount() : 0) + " rows");
        return cursor != null ? cursor : null;
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
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                String imageUrl1 = "https://product.hstatic.net/200000856317/product/1_a41846ccd71048e9821786ecd014c5c5_5345003f832342a79dda25df1bebff56_large.png";
                String imageUrl2 = "https://product.hstatic.net/200000856317/product/55_2cc6557970f746f290cdbfa4e81b7b97_09bb8b7ee14b47219356b1cc331e4e6a_large.png";
                String imageUrl3 = "https://product.hstatic.net/200000856317/product/9_14649aab2988403dab767c787548201a_f5257f011a22440a877bf3f9eb4dd591_large.png";
                String imageUrl4 = "https://product.hstatic.net/200000856317/product/9_14649aab2988403dab767c787548201a_f5257f011a22440a877bf3f9eb4dd591_large.png";

                long orderId1 = insertOrder("Chờ xác nhận", 0.0, 0.0);
                long orderId2 = insertOrder("Chờ xác nhận", 0.0, 0.0);
                long orderId3 = insertOrder("Đang chuẩn bị hàng", 0.0, 0.0);
                long orderId4 = insertOrder("Đang chuẩn bị hàng", 0.0, 0.0);
                long orderId5 = insertOrder("Đang vận chuyển", 0.0, 0.0);
                long orderId6 = insertOrder("Đang vận chuyển", 0.0, 0.0);
                long orderId7 = insertOrder("Thành công", 0.0, 0.0);
                long orderId8 = insertOrder("Thành công", 0.0, 0.0);
                long orderId9 = insertOrder("Đã trả", 0.0, 0.0);
                long orderId10 = insertOrder("Đã hủy", 0.0, 0.0);
                long orderId11 = insertOrder("Đã hủy", 0.0, 0.0);

                long productId1 = insertProduct("Gấu bông Sanrio", 250000, imageUrl1);
                long productId2 = insertProduct("Cappy barra nước mũi", 300000, imageUrl2);
                long productId3 = insertProduct("Gấu bông heo con", 150000, imageUrl3);
                long productId4 = insertProduct("Cappy barra matcha", 400000, imageUrl4);

                insertOrderItem((int) orderId1, (int) productId1, 1);
                insertOrderItem((int) orderId1, (int) productId2, 2);
                insertOrderItem((int) orderId2, (int) productId3, 1);
                insertOrderItem((int) orderId2, (int) productId4, 1);

                insertOrderItem((int) orderId3, (int) productId2, 2);
                insertOrderItem((int) orderId3, (int) productId3, 1);
                insertOrderItem((int) orderId4, (int) productId1, 1);
                insertOrderItem((int) orderId5, (int) productId4, 1);
                insertOrderItem((int) orderId5, (int) productId2, 2);
                insertOrderItem((int) orderId6, (int) productId3, 2);

                insertOrderItem((int) orderId7, (int) productId1, 2);
                insertOrderItem((int) orderId7, (int) productId4, 1);
                insertOrderItem((int) orderId8, (int) productId2, 1);

                insertOrderItem((int) orderId9, (int) productId3, 1);

                insertOrderItem((int) orderId10, (int) productId1, 1);
                insertOrderItem((int) orderId11, (int) productId4, 2);

                db.setTransactionSuccessful();
                Log.d("OrderDatabase", "Sample data created successfully. Orders: " + getNumOfOrders() + ", Products: " + getNumOfProducts());
            } catch (Exception e) {
                Log.e("OrderDatabase", "Error creating sample data: " + e.getMessage(), e);
            } finally {
                db.endTransaction();
            }
        }
    }

    private int getNumOfOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private int getNumOfProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}