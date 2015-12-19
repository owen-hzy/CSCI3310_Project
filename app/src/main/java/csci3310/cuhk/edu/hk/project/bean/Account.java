package csci3310.cuhk.edu.hk.project.bean;

import android.database.Cursor;

import csci3310.cuhk.edu.hk.project.db.AccountTable;

public class Account {

    public int id;
    public String name;
    public double value;

    public Account() {

    }

    public Account(String name, double initialValue) {
        this.name = name;
        this.value = initialValue;
    }

    public static Account fromCursor(Cursor cursor) {
        Account account = new Account();
        account.id = cursor.getInt(cursor.getColumnIndex(AccountTable._ID));
        account.name = cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME));
        account.value = cursor.getShort(cursor.getColumnIndex(AccountTable.COLUMN_VALUE));

        return account;
    }
}
