package csci3310.cuhk.edu.hk.project.dataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import csci3310.cuhk.edu.hk.project.dataModel.Budget;
import csci3310.cuhk.edu.hk.project.dataModel.Category;
import csci3310.cuhk.edu.hk.project.dataModel.Currency;
import csci3310.cuhk.edu.hk.project.dataModel.Payment;
import csci3310.cuhk.edu.hk.project.dataModel.Record;
import csci3310.cuhk.edu.hk.project.dataModel.Subcategory;
import csci3310.cuhk.edu.hk.project.dataModel.Vendor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by csamyphew on 16/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private  static final String DATABASE_NAME = "budgetPlanner";

    //Table Names
    private static final String TABLE_CATEGORY = "Category";
    private static final String TABLE_SUBCAT = "SubCategory";

    private static final String TABLE_BUDGET = "Budget";

    private static final String TABLE_CURRENCY = "Currency";
    private static final String TABLE_PAYMENT = "Payment";
    private static final String TABLE_VENDOR = "Vendor";
    private static final String TABLE_RECORD = "Record";

    //Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SUBCAT_ID = "subcat_id";

    //Subcategory Table - foreign key column names
    private static final String KEY_CAT_ID = "cat_id";
    private static final String KEY_ID_2 = "subCat_id";
    private static final String KEY_NAME_2 = "subCat_name";

    //Currency Table - column name
    private static final String KEY_RATE = "rate";

    //Budget Table - column names
    private static final String KEY_AMOUNT = "amount";

    //Record Table - column names
    private static final String KEY_PRICE = "price";
    private static final String KEY_ID_3 = "record_id";
    //Record Table - foreign key column names
    private static final String KEY_RECORD_ID = "record_id";
    private static final String KEY_VENDOR_ID = "vendor_id";
    private static final String KEY_CURR_ID= "curr_id";
    private static final String KEY_PAYMENT_ID = "payment_id";


    //Table Create Statements
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE (" + KEY_NAME + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_SUBCATEGORY = "CREATE TABLE " + TABLE_SUBCAT + "("
            + KEY_ID_2 + " INTEGER PRIMARY KEY,"
            + KEY_NAME_2 + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_CAT_ID + " INTERGER NOT NULL,"
            + "FOREIGN KEY(" + KEY_CAT_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ")" + " ON DELETE SET NULL,"
            + "UNIQUE (" + KEY_NAME_2 + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_BUDGET = "CREATE TABLE " + TABLE_BUDGET + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AMOUNT + " REAL NOT NULL,"
            + KEY_TYPE + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE (" + KEY_TYPE + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_CURRENCY = "CREATE TABLE " + TABLE_CURRENCY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_RATE + " REAL NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE (" + KEY_NAME + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_PAYMENT = "CREATE TABLE " + TABLE_PAYMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE (" + KEY_NAME + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_VENDOR = "CREATE TABLE " + TABLE_VENDOR + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE (" + KEY_NAME + ") ON CONFLICT REPLACE"
            + ")";
    private static final String CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "("
            + KEY_ID_3 + " INTEGER PRIMARY KEY,"
            + KEY_PRICE + " REAL NOT NULL,"
            + KEY_TYPE + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_VENDOR_ID + " INTEGER,"
            + KEY_SUBCAT_ID + " INTEGER,"
            + KEY_PAYMENT_ID + " INTEGER,"
            + KEY_CURR_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_VENDOR_ID + ") REFERENCES " + TABLE_VENDOR + "(" + KEY_ID  + ")" + " ON DELETE SET NULL,"
            + "FOREIGN KEY(" + KEY_SUBCAT_ID + ") REFERENCES " + TABLE_SUBCAT + "(" + KEY_ID_2 + ")" + " ON DELETE SET NULL,"
            + "FOREIGN KEY(" + KEY_PAYMENT_ID + ") REFERENCES " + TABLE_PAYMENT + "(" + KEY_ID + ")" + " ON DELETE SET NULL,"
            + "FOREIGN KEY(" + KEY_CURR_ID + ") REFERENCES " + TABLE_CURRENCY + "(" + KEY_ID + ")" + " ON DELETE SET NULL" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override public void onCreate (SQLiteDatabase db) {
        //create tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SUBCATEGORY);
        db.execSQL(CREATE_TABLE_BUDGET);
        db.execSQL(CREATE_TABLE_CURRENCY);
        db.execSQL(CREATE_TABLE_PAYMENT);
        db.execSQL(CREATE_TABLE_VENDOR);
        db.execSQL(CREATE_TABLE_RECORD);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table when upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);

        //create new tables
        onCreate(db);
    }

    /* * get datetime * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    //--------------Create methods---------------//

    public long createCategory(Category cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare info from object
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, cat.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        //insert row
        long cat_id = db.insert(TABLE_CATEGORY, null, values);
        return cat_id;
    }
    //set the cat_id to null if no input, otherwise will crash as its foreign key
    public long createSubCategory(Subcategory subCat, long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_2, subCat.getName());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_CAT_ID, cat_id);

        long subCat_id = db.insert(TABLE_SUBCAT, null, values);
        return subCat_id;
    }
    public long createBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare info from object
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, budget.getAmount());
        values.put(KEY_TYPE, budget.getType());
        values.put(KEY_CREATED_AT, getDateTime());

        //insert row
        long budget_id = db.insert(TABLE_BUDGET, null, values);
        return budget_id;
    }
    public long createCurrency(Currency curr) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare info from object
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, curr.getName());
        values.put(KEY_RATE, curr.getRate());
        values.put(KEY_CREATED_AT, getDateTime());

        //insert row
        long curr_id = db.insert(TABLE_CURRENCY, null, values);
        return curr_id;
    }
    public long createPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare info from object
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, payment.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        //insert row
        long payment_id = db.insert(TABLE_PAYMENT, null, values);
        return payment_id;
    }
    public long createVendor(Vendor vendor) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare info from object
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, vendor.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        //insert row
        long vendor_id = db.insert(TABLE_VENDOR, null, values);
        return vendor_id;
    }
    //set the XXX_id to null if no input, otherwise will crash as its foreign key
    public long createRecord(Record record, long vendor_id, long subcat_id, long payment_id, long curr_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRICE, record.getPrice());
        values.put(KEY_TYPE, record.getType());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_VENDOR_ID, vendor_id);
        values.put(KEY_SUBCAT_ID, subcat_id);
        values.put(KEY_PAYMENT_ID, payment_id);
        values.put(KEY_CURR_ID, curr_id);

        long record_id = db.insert(TABLE_RECORD, null, values);
        return record_id;
    }




    //--------------Retrieve methods---------------//

    //Category
    public int getCategoryCount() {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Category getCategory(long cat_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_ID + " = " + cat_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);
        if(c != null) {
            c. moveToFirst();
        }

        Category cat = new Category();
        cat.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        cat.setName(c.getString(c.getColumnIndex(KEY_NAME)));

        return cat;
    }
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        String selectQuery = " SELECT * FROM " + TABLE_CATEGORY;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Category cat = new Category();
                cat.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                cat.setName(c.getString(c.getColumnIndex(KEY_NAME)));

                categories.add(cat);
            }
            while (c.moveToNext());
        }
        return categories;
    }

    //SubCategory
    public int getSubcategoryCount() {
        String countQuery = "SELECT * FROM " + TABLE_SUBCAT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Subcategory getSubCategory(long subCat_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SUBCAT + " WHERE " + KEY_ID_2 + " = " + subCat_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Subcategory subCat = new Subcategory();
        subCat.setId(c.getInt(c.getColumnIndex(KEY_ID_2)));
        subCat.setName(c.getString(c.getColumnIndex(KEY_NAME_2)));

        return subCat;
    }
    public List<Subcategory> getAllSubcategories() {
        List<Subcategory> subcategories = new ArrayList<Subcategory>();
        String selectQuery = " SELECT * FROM " + TABLE_SUBCAT;
        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Subcategory subCat = new Subcategory();
                subCat.setId(c.getInt(c.getColumnIndex(KEY_ID_2)));
                subCat.setName(c.getString(c.getColumnIndex(KEY_NAME_2)));

                subcategories.add(subCat);
            }
            while (c.moveToNext());
        }
        return subcategories;
    }
    public List<Subcategory> getAllSubcategoriesByCat (String cat_name) {
        List<Subcategory> subcategories = new ArrayList<Subcategory>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBCAT + " subcat, " + TABLE_CATEGORY + " cat"
                + " WHERE cat." + KEY_NAME + " = '" + cat_name + "'"
                + " AND subcat." + KEY_CAT_ID + " = " + "cat." + KEY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Subcategory subCat = new Subcategory();
                subCat.setId(c.getInt(c.getColumnIndex(KEY_ID_2)));
                subCat.setName(c.getString(c.getColumnIndex(KEY_NAME_2)));

                subcategories.add(subCat);
            }
            while (c.moveToNext());
        }
        return subcategories;
    }

    //Budget
    public int getBudgetCount() {
        String countQuery = "SELECT * FROM " + TABLE_BUDGET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Budget getBudget(long budget_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BUDGET + " WHERE " + KEY_ID + " = " + budget_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Budget budget = new Budget();
        budget.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        budget.setAmount(c.getDouble(c.getColumnIndex(KEY_AMOUNT)));
        budget.setType(c.getString(c.getColumnIndex(KEY_TYPE)));

        return budget;
    }
    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<Budget>();
        String selectQuery = " SELECT * FROM " + TABLE_BUDGET;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Budget budget = new Budget();
                budget.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                budget.setAmount(c.getDouble(c.getColumnIndex(KEY_AMOUNT)));
                budget.setType(c.getString(c.getColumnIndex(KEY_TYPE)));

                budgets.add(budget);
            }
            while (c.moveToNext());
        }
        return budgets;
    }

    //Currency
    public int getCurrencyCount() {
        String countQuery = "SELECT * FROM " + TABLE_CURRENCY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Currency getCurrency(long curr_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CURRENCY + " WHERE " + KEY_ID + " = " + curr_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Currency curr = new Currency();
        curr.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        curr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        curr.setRate(c.getDouble(c.getColumnIndex(KEY_RATE)));

        return curr;
    }
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<Currency>();
        String selectQuery = " SELECT * FROM " + TABLE_CURRENCY;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Currency curr = new Currency();
                curr.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                curr.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                curr.setRate(c.getDouble(c.getColumnIndex(KEY_RATE)));

                currencies.add(curr);
            }
            while (c.moveToNext());
        }
        return currencies;
    }

    //Payment
    public int getPaymentCount() {
        String countQuery = "SELECT * FROM " + TABLE_PAYMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Payment getPayment(long payment_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PAYMENT + " WHERE " + KEY_ID + " = " + payment_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Payment payment = new Payment();
        payment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        payment.setName(c.getString(c.getColumnIndex(KEY_NAME)));

        return payment;
    }
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<Payment>();
        String selectQuery = " SELECT * FROM " + TABLE_PAYMENT;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Payment payment = new Payment();
                payment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                payment.setName(c.getString(c.getColumnIndex(KEY_NAME)));

                payments.add(payment);
            }
            while (c.moveToNext());
        }
        return payments;
    }

    //Vendor
    public int getVendorCount() {
        String countQuery = "SELECT * FROM " + TABLE_VENDOR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Vendor getVendor(long vendor_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_VENDOR + " WHERE " + KEY_ID + " = " + vendor_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Vendor vendor = new Vendor();
        vendor.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        vendor.setName(c.getString(c.getColumnIndex(KEY_NAME)));

        return vendor;
    }
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<Vendor>();
        String selectQuery = " SELECT * FROM " + TABLE_VENDOR;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Vendor vendor = new Vendor();
                vendor.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                vendor.setName(c.getString(c.getColumnIndex(KEY_NAME)));

                vendors.add(vendor);
            }
            while (c.moveToNext());
        }
        return vendors;
    }

    //Record
    public int getRecordCount() {
        String countQuery = "SELECT * FROM " + TABLE_RECORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery,null);

        int count = c.getCount();
        c.close();

        return count;
    }
    public Record getRecord(long record_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_RECORD + " WHERE " + KEY_ID_3 + " = " + record_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c. moveToFirst();
        }

        Record record = new Record();
        record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
        record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
        record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
        record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return record;
    }
    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<Record>();
        String selectQuery = " SELECT * FROM " + TABLE_RECORD;
        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do{
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
                record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                records.add(record);
            }
            while (c.moveToNext());
        }
        return records;
    }
    public List<Record> getAllRecordsByVendor (String vendor_name) {
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORD + " record, " + TABLE_VENDOR + " vendor "
                + " WHERE vendor." + KEY_NAME + " = '" + vendor_name + "'"
                + " AND record." + KEY_VENDOR_ID + " = " + "vendor." + KEY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do{
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
                record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                records.add(record);
            }
            while (c.moveToNext());
        }
        return records;
    }
    public List<Record> getAllRecordsBySubcat (String subCat_name) {
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORD + " record, " + TABLE_SUBCAT + " subcat "
                + " WHERE subcat." + KEY_NAME_2 + " = '" + subCat_name + "'"
                + " AND record." + KEY_SUBCAT_ID + " = " + "subcat." + KEY_ID_2;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do{
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
                record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                records.add(record);
            }
            while (c.moveToNext());
        }
        return records;
    }
    public List<Record> getAllRecordsByCurrency (String curr_name) {
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORD + " record, " + TABLE_CURRENCY + " curr "
                + " WHERE curr." + KEY_NAME + " = '" + curr_name + "'"
                + " AND record." + KEY_CURR_ID + " = " + "curr." + KEY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do{
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
                record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                records.add(record);
            }
            while (c.moveToNext());
        }
        return records;
    }
    public List<Record> getAllRecordsByPayment (String payment_name) {
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_RECORD + " record, " + TABLE_PAYMENT + " payment "
                + " WHERE payment." + KEY_NAME + " = '" + payment_name + "'"
                + " AND record." + KEY_PAYMENT_ID + " = " + "payment." + KEY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do{
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(KEY_ID_3)));
                record.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                record.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                record.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                records.add(record);
            }
            while (c.moveToNext());
        }
        return records;
    }





    //--------------Update methods---------------//
    public int updateCategory(Category cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, cat.getName());

        //update row
        return db.update(TABLE_CATEGORY, values, KEY_ID + " = ?", new String[] { String.valueOf(cat.getId())});
    }
    public int updateSubCategory(Subcategory subCat, long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, subCat.getName());
        values.put(KEY_CAT_ID, cat_id);

        //update row
        return db.update(TABLE_SUBCAT, values, KEY_ID + " = ?", new String[] { String.valueOf(subCat.getId())});
    }
    public int updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, budget.getAmount());
        values.put(KEY_TYPE, budget.getType());

        //update row
        return db.update(TABLE_BUDGET, values, KEY_ID + " = ?", new String[] { String.valueOf(budget.getId())});
    }
    public int updateCurrency(Currency curr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, curr.getName());
        values.put(KEY_RATE, curr.getRate());

        //update row
        return db.update(TABLE_CURRENCY, values, KEY_ID + " = ?", new String[] { String.valueOf(curr.getId())});
    }
    public int updatePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, payment.getName());

        //update row
        return db.update(TABLE_PAYMENT, values, KEY_ID + " = ?", new String[] { String.valueOf(payment.getId())});
    }
    public int updateVendor(Vendor vendor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, vendor.getName());

        //update row
        return db.update(TABLE_VENDOR, values, KEY_ID + " = ?", new String[] { String.valueOf(vendor.getId())});
    }
    public int updateRecord(Record record, long vendor_id, long subcat_id, long payment_id, long curr_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRICE, record.getPrice());
        values.put(KEY_TYPE, record.getType());
        values.put(KEY_VENDOR_ID, vendor_id);
        values.put(KEY_SUBCAT_ID, subcat_id);
        values.put(KEY_PAYMENT_ID, payment_id);
        values.put(KEY_CURR_ID, curr_id);

        //update row
        return db.update(TABLE_RECORD, values, KEY_ID + " = ?", new String[] { String.valueOf(record.getId())});
    }




    //--------------Delete methods---------------//
    public void deleteCategory(long cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_ID + " = ?", new String[] { String.valueOf(cat_id) });
    }
    public void deleteAllCaterories(List<Category> categories){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Category cat : categories) {
            deleteCategory(cat.getId());
        }
    }

    public void deleteSubCategory(long subCat_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBCAT, KEY_ID + " = ?", new String[] { String.valueOf(subCat_id) });
    }
    public void deleteAllSubCaterories(List<Subcategory> subcategories){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Subcategory subcat : subcategories) {
            deleteSubCategory(subcat.getId());
        }
    }

    public void deleteBudget(long budget_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGET, KEY_ID + " = ?", new String[] { String.valueOf(budget_id) });
    }
    public void deleteAllBudgets(List<Budget> budgets){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Budget budget : budgets) {
            deleteBudget(budget.getId());
        }
    }

    public void deleteCurrency(long curr_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CURRENCY, KEY_ID + " = ?", new String[] { String.valueOf(curr_id) });
    }
    public void deleteAllCurrencies(List<Currency> currencies){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Currency currency : currencies) {
            deleteCurrency(currency.getId());
        }
    }

    public void deletePayment(long payment_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAYMENT, KEY_ID + " = ?", new String[] { String.valueOf(payment_id) });
    }
    public void deleteAllPayments(List<Payment> payments){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Payment payment : payments) {
            deletePayment(payment.getId());
        }
    }

    public void deleteVendor(long vendor_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VENDOR, KEY_ID + " = ?", new String[] { String.valueOf(vendor_id) });
    }
    public void deleteAllVendors(List<Vendor> vendors){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Vendor vendor : vendors) {
            deleteVendor(vendor.getId());
        }
    }

    public void deleteRecord(long record_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD, KEY_ID + " = ?", new String[] { String.valueOf(record_id) });
    }
    public void deleteAllRecords(List<Record> records){
        SQLiteDatabase db = this.getWritableDatabase();
        for(Record record : records) {
            deleteRecord(record.getId());
        }
    }



    //close the database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
