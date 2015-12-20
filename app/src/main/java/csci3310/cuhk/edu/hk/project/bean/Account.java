package csci3310.cuhk.edu.hk.project.bean;

import android.database.Cursor;

import csci3310.cuhk.edu.hk.project.db.AccountTable;

public class Account {

    public int id;
    public String name;

    public Account() {

    }

    public Account(String name) {
        this.name = name;
    }

    public static Account fromCursor(Cursor cursor) {
        Account account = new Account();
        account.id = cursor.getInt(cursor.getColumnIndex(AccountTable._ID));
        account.name = cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME));

        return account;
    }
}
