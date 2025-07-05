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
                COL_ORDER_STATUS + " TEXT, " +
                "shippingFee REAL DEFAULT 0, " +
                "discount REAL DEFAULT 0)";
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

    public long insertProduct(String productName, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_IMAGE, image != null ? image : new byte[0]);
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
        Cursor cursor = db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID, null);
        Log.d("OrderDatabase", "getAllOrdersWithDetails returned " + cursor.getCount() + " rows");
        if (cursor.getCount() == 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getOrdersByStatusWithDetails(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID;
        if (status != null) {
            query += " WHERE o." + COL_ORDER_STATUS + " = ?";
        }
        Cursor cursor = db.rawQuery(query, status != null ? new String[]{status} : null);
        Log.d("OrderDatabase", "getOrdersByStatusWithDetails for status=" + status + " returned " + cursor.getCount() + " rows");
        if (cursor.getCount() == 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Thêm phương thức mới để lấy order theo orderId
    public Cursor getOrderByIdWithDetails(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT o." + COL_ORDER_ID + ", o." + COL_ORDER_STATUS + ", o.shippingFee, o.discount, p." + COL_PRODUCT_NAME + ", p." + COL_PRODUCT_PRICE + ", oi." + COL_ORDERITEM_QUANTITY + ", p." + COL_PRODUCT_IMAGE + " FROM " + TABLE_ORDERS + " o " +
                "LEFT JOIN " + TABLE_ORDER_ITEMS + " oi ON o." + COL_ORDER_ID + " = oi." + COL_ORDERITEM_ORDER_ID +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_ORDERITEM_PRODUCT_ID + " = p." + COL_PRODUCT_ID +
                " WHERE o." + COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
        Log.d("OrderDatabase", "getOrderByIdWithDetails for orderId=" + orderId + " returned " + cursor.getCount() + " rows");
        if (cursor.getCount() == 0) {
            cursor.moveToFirst();
        }
        return cursor;
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
            try {
                byte[] productImage1 = Utils.convertDrawableToByteArray(context, R.drawable.vefluffy1);
                byte[] productImage2 = Utils.convertDrawableToByteArray(context, R.drawable.vefluffy3);
                byte[] productImage3 = Utils.convertDrawableToByteArray(context, R.drawable.vefluffy1); // Tái sử dụng ảnh

                // Thêm nhiều đơn hàng với các trạng thái khác nhau
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
                // Thêm nhiều sản phẩm
                long productId1 = insertProduct("Gấu bông Sanrio", 250000, productImage1);
                long productId2 = insertProduct("Cappy barra nước mũi", 300000, productImage2);
                long productId3 = insertProduct("Gấu bông heo con", 150000, productImage3);
                long productId4 = insertProduct("Cappy barra matcha", 400000, productImage1);
                // Gán sản phẩm cho các đơn hàng
                insertOrderItem((int) orderId1, (int) productId1, 1);  // Chờ xác nhận 1
                insertOrderItem((int) orderId1, (int) productId2, 2);
                insertOrderItem((int) orderId2, (int) productId3, 1);  // Chờ xác nhận 2
                insertOrderItem((int) orderId2, (int) productId4, 1);

                insertOrderItem((int) orderId3, (int) productId2, 2);  // Đang chuẩn bị hàng 1
                insertOrderItem((int) orderId3, (int) productId3, 1);
                insertOrderItem((int) orderId4, (int) productId1, 1);  // Đang chuẩn bị hàng 2
                insertOrderItem((int) orderId5, (int) productId4, 1);  // Đang vận chuyển 1
                insertOrderItem((int) orderId5, (int) productId2, 2);
                insertOrderItem((int) orderId6, (int) productId3, 2);  // Đang vận chuyển 2

                insertOrderItem((int) orderId7, (int) productId1, 2);  // Thành công 1
                insertOrderItem((int) orderId7, (int) productId4, 1);
                insertOrderItem((int) orderId8, (int) productId2, 1);  // Thành công 2

                insertOrderItem((int) orderId9, (int) productId3, 1);  // Đã trả

                insertOrderItem((int) orderId10, (int) productId1, 1); // Đã hủy 1
                insertOrderItem((int) orderId11, (int) productId4, 2); // Đã hủy 2
                Log.d("OrderDatabase", "Sample data created successfully. Orders: " + getNumOfOrders() + ", Products: " + getNumOfProducts());
            } catch (Exception e) {
                Log.e("OrderDatabase", "Error creating sample data: " + e.getMessage());
            }
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

    private int getNumOfProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}