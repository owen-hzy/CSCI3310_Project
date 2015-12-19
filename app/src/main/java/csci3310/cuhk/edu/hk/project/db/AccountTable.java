package csci3310.cuhk.edu.hk.project.db;

import android.provider.BaseColumns;

import csci3310.cuhk.edu.hk.project.db.database.Column;
import csci3310.cuhk.edu.hk.project.db.database.SQLiteTable;

public class AccountTable implements BaseColumns {

    // Database table name
    public static final String TABLE_NAME = "account";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VALUE = "value";

    public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
            .addColumn(COLUMN_NAME, Column.Constraint.UNIQUE, Column.DataType.TEXT)
            .addColumn(COLUMN_VALUE, Column.DataType.REAL);
 }
