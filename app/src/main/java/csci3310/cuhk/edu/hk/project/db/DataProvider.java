package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import csci3310.cuhk.edu.hk.project.app.AppApplication;

public class DataProvider extends ContentProvider {

    public static final Object obj = new Object();
    private static final String AUTHORITY = "csci3310.cuhk.edu.hk.project.db";
    public static final String SCHEME = "content://";

    private static final int RECORD = 1;
    private static final int RECORD_ID = 2;
    private static final int ACCOUNT = 3;
    private static final int ACCOUNT_ID = 4;
    private static final int BUDGET = 5;
    private static final int BUDGET_ID = 6;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static DBHelper mDBHelper;
    public static DBHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(AppApplication.getContext());
        }
        return mDBHelper;
    }

    static {
        uriMatcher.addURI(AUTHORITY, "record", RECORD);
        uriMatcher.addURI(AUTHORITY, "record/#", RECORD_ID);
        uriMatcher.addURI(AUTHORITY, "account", ACCOUNT);
        uriMatcher.addURI(AUTHORITY, "account/#", ACCOUNT_ID);
        uriMatcher.addURI(AUTHORITY, "budget", BUDGET);
        uriMatcher.addURI(AUTHORITY, "budget/#", BUDGET_ID);
    }

    public static Uri getContentUri(String table) {
        return Uri.parse(SCHEME + AUTHORITY + "/" + table);
    }

    public DataProvider() {
    }

    private String matchTable(Uri uri) {
        String table;
        switch (uriMatcher.match(uri)) {
            case RECORD://Demo列表
                table = RecordTable.TABLE_NAME;
                break;
            case ACCOUNT:
                table = AccountTable.TABLE_NAME;
                break;
            case BUDGET:
                table = BudgetTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        return table;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (obj) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count = 0;
            db.beginTransaction();
            try {
                count = db.delete(matchTable(uri), selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public String getType(Uri uri) {
        // Private content provider, no need to implement getType
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (obj) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insert(matchTable(uri), null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        synchronized (obj) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            db.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    db.insertWithOnConflict(matchTable(uri), BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                return values.length;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        synchronized (obj) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(matchTable(uri));

            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        synchronized (obj) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count;
            db.beginTransaction();
            try {
                count = db.update(matchTable(uri), values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    public static class DBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "budgetPlanner.db";

        private static final int DB_VERSION = 16;

        private DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            AccountTable.TABLE.create(db);
            RecordTable.createTable(db);
            BudgetTable.TABLE.create(db);

            // Enable SQLite Foreign Keys Support
            db.execSQL("PRAGMA foreign_keys = ON;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RecordTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BudgetTable.TABLE_NAME);

            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            if (! db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

}
