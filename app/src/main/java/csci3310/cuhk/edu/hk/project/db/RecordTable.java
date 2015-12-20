package csci3310.cuhk.edu.hk.project.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import csci3310.cuhk.edu.hk.project.db.database.Column;
import csci3310.cuhk.edu.hk.project.db.database.SQLiteTable;

public class RecordTable implements BaseColumns {

    // Database table
    public static final String TABLE_NAME = "record";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ACCOUNT_NAME = "account_name";

    public static void createTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(TABLE_NAME);
        sb.append("(");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY,");
        sb.append(COLUMN_TYPE + " TEXT NOT NULL,");
        sb.append(COLUMN_CATEGORY + " TEXT,");
        sb.append(COLUMN_AMOUNT + " REAL,");
        sb.append(COLUMN_TIMESTAMP + " TEXT NOT NULL,");
        sb.append(COLUMN_ACCOUNT_NAME + " TEXT REFERENCES " + AccountTable.TABLE_NAME + "(" + AccountTable.COLUMN_NAME + ") ON UPDATE CASCADE ON DELETE CASCADE);");

        db.execSQL(sb.toString());
    }
}
