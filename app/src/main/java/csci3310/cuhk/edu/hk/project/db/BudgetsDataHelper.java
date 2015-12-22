package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

import csci3310.cuhk.edu.hk.project.bean.Budget;

public class BudgetsDataHelper extends BaseDataHelper implements DBInterface<Budget> {

    public BudgetsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.getContentUri(BudgetTable.TABLE_NAME);
    }

    @Override
    protected String getTableName() {
        return BudgetTable.TABLE_NAME;
    }

    @Override
    public List<Budget> query() {
        List<Budget> budgets = new ArrayList<>();
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            budgets.add(Budget.fromCursor(cursor));
            while (cursor.moveToNext()) {
                budgets.add(Budget.fromCursor(cursor));
            }
        }
        return budgets;
    }

    @Override
    public Budget query(String category) {
        Cursor cursor = query(null, "name = ?", new String[] {category}, null);
        Budget budget = null;
        if (cursor.moveToFirst()) {
            budget = Budget.fromCursor(cursor);
        }
        return budget;
    }

    @Override
    public int clearAll() {
        synchronized (DataProvider.obj) {
            DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            return db.delete(BudgetTable.TABLE_NAME, null, null);
        }
    }

    @Override
    public Uri insert(Budget data) {
        ContentValues values = getContentValues(data);
        return insert(values);
    }

    @Override
    public void bulkInsert(List<Budget> listData) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (Budget budget : listData) {
            ContentValues values = getContentValues(budget);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    @Override
    public int update(Budget data) {
        ContentValues values = getContentValues(data);
        return update(values, BudgetTable._ID + " = ?", new String[]{data.id + ""});
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public ContentValues getContentValues(Budget data) {
        ContentValues values = new ContentValues();
        values.put(BudgetTable.COLUMN_NAME, data.name);
        values.put(BudgetTable.COLUMN_VALUE, data.value);

        return values;
    }

    @Override
    public CursorLoader getCursorLoader(String selection, String[] selectionArgs) {
        return new CursorLoader(getContext(), getContentUri(), null, selection, selectionArgs, BudgetTable._ID + " ASC");
    }
}
