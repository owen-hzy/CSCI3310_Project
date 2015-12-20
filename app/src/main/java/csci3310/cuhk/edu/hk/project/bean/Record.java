package csci3310.cuhk.edu.hk.project.bean;

import android.database.Cursor;

import csci3310.cuhk.edu.hk.project.db.RecordTable;

public class Record {

    public enum RecordType {
        Income, Expense
    }

    public int id;
    public String category;
    public double amount;
    public RecordType type;
    public String timestamp;
    public String account_name;

    public Record() {
    }

    public Record(String category, double amount, RecordType type, String timestamp, String account_name) {
        this.category = category;
        this.type = type;
        this.timestamp = timestamp;
        this.amount = amount;
        this.account_name = account_name;
    }

    public static Record fromCursor(Cursor cursor) {
        Record record = new Record();
        record.id = cursor.getInt(cursor.getColumnIndex(RecordTable._ID));
        record.amount = cursor.getDouble(cursor.getColumnIndex(RecordTable.COLUMN_AMOUNT));
        record.type = RecordType.valueOf(cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_TYPE)));
        record.category = cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_CATEGORY));
        record.timestamp = cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_TIMESTAMP));
        record.account_name = cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_ACCOUNT_NAME));

        return record;
    }
}
