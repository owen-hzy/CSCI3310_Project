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
    public Record query(String id) {
        Record item = null;
        Cursor cursor;
        cursor = query(null, RecordTable.COLUMN_ID + "= ?",
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
    public ContentValues getContentValues(Record data) {
        ContentValues values = new ContentValues();
        values.put(RecordTable.COLUMN_ID, data.id);
        values.put(RecordTable.COLUMN_AMOUNT, data.amount);
        values.put(RecordTable.COLUMN_CATEGORY, data.category);
        values.put(RecordTable.COLUMN_TYPE, data.type.toString());
        values.put(RecordTable.COLUMN_TIMESTAMP, data.timestamp);
        return values;
    }

    @Override
    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, RecordTable.COLUMN_ID + " DESC");
    }
}
