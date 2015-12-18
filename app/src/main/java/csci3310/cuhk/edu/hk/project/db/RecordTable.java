package csci3310.cuhk.edu.hk.project.db;

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

    public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
            .addColumn(COLUMN_TYPE, Column.Constraint.NOT_NULL, Column.DataType.TEXT)
            .addColumn(COLUMN_AMOUNT, Column.DataType.REAL)
            .addColumn(COLUMN_CATEGORY, Column.DataType.TEXT)
            .addColumn(COLUMN_TIMESTAMP, Column.Constraint.NOT_NULL, Column.DataType.TEXT);
}
