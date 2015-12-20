package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

import csci3310.cuhk.edu.hk.project.bean.Account;

public class AccountsDataHelper extends BaseDataHelper implements DBInterface<Account> {

    public AccountsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.getContentUri(AccountTable.TABLE_NAME);
    }

    @Override
    protected String getTableName() {
        return AccountTable.TABLE_NAME;
    }

    @Override
    public List<Account> query() {
        List<Account> accounts = new ArrayList<>();
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            accounts.add(Account.fromCursor(cursor));
            while (cursor.moveToNext()) {
                accounts.add(Account.fromCursor(cursor));
            }
        }
        return accounts;
    }

    @Override
    public Account query(String id) {
        return null;
    }

    @Override
    public int clearAll() {
        synchronized (DataProvider.obj) {
            DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            return db.delete(RecordTable.TABLE_NAME, null, null);
        }
    }

    @Override
    public Uri insert(Account data) {
        ContentValues values = getContentValues(data);
        return insert(values);
    }

    @Override
    public void bulkInsert(List<Account> listData) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (Account account : listData) {
            ContentValues values = getContentValues(account);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    @Override
    public int update(Account data) {
        ContentValues values = getContentValues(data);
        return update(values, AccountTable._ID + " = ?", new String[]{data.id + ""});
    }

    @Override
    public int delete(String id) {
        return delete(AccountTable._ID + "= ?", new String[] { id });
    }

    @Override
    public ContentValues getContentValues(Account data) {
        ContentValues values = new ContentValues();
        values.put(AccountTable.COLUMN_NAME, data.name);

        return values;
    }

    @Override
    public CursorLoader getCursorLoader(String selection, String[] selectionArgs) {
        return new CursorLoader(getContext(), getContentUri(), null, selection, selectionArgs, AccountTable._ID + " DESC");
    }


}
