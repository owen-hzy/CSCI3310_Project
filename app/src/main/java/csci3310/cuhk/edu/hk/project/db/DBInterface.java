package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentValues;
import android.support.v4.content.CursorLoader;

import java.util.List;

public interface DBInterface<T> {

    public T query(String id);

    public int clearAll();

    public void bulkInsert(List<T> listData);

    public ContentValues getContentValues(T data);

    public CursorLoader getCursorLoader();

}
