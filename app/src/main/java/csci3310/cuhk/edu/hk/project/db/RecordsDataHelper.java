package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

import csci3310.cuhk.edu.hk.project.bean.Record;

public class RecordsDataHelper extends BaseDataHelper implements DBInterface<Record> {

    public RecordsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.getContentUri(RecordTable.TABLE_NAME);
    }

    @Override
    protected String getTableName() {
        return RecordTable.TABLE_NAME;
    }

    @Override
    public List<Record> query() {
        List<Record> records = new ArrayList<>();
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            records.add(Record.fromCursor(cursor));
            while (cursor.moveToNext()) {
                records.add(Record.fromCursor(cursor));
            }
        }
        return records;
    }

    @Override
    public Record query(String id) {
        Record item = null;
        Cursor cursor;
        cursor = query(null, RecordTable._ID + "= ?",
                new String[]{
                        id
                }, null);
        if (cursor.moveToFirst()) {
            item = Record.fromCursor(cursor);
        }
        cursor.close();
        return item;
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
    public Uri insert(Record data) {
        ContentValues values = getContentValues(data);
        return insert(values);
    }



    @Override
    public void bulkInsert(List<Record> listData) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (Record record : listData) {
            ContentValues values = getContentValues(record);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    @Override
    public int update(Record data) {
        ContentValues values = getContentValues(data);
        return update(values, RecordTable._ID + " = ?", new String[]{data.id + ""});
    }

    @Override
    public int delete(String id) {
        return delete(RecordTable._ID + "= ?", new String[]{id});
    }

    @Override
    public ContentValues getContentValues(Record data) {
        ContentValues values = new ContentValues();
        values.put(RecordTable.COLUMN_AMOUNT, data.amount);
        values.put(RecordTable.COLUMN_CATEGORY, data.category);
        values.put(RecordTable.COLUMN_TYPE, data.type.toString());
        values.put(RecordTable.COLUMN_TIMESTAMP, data.timestamp);
        values.put(RecordTable.COLUMN_ACCOUNT_NAME, data.account_name);
        return values;
    }

    @Override
    public CursorLoader getCursorLoader(String selection, String[] selectionArgs) {
        return new CursorLoader(getContext(), getContentUri(), null, selection, selectionArgs, RecordTable.COLUMN_TIMESTAMP + " DESC");
    }


}
