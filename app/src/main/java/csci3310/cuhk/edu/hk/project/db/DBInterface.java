package csci3310.cuhk.edu.hk.project.db;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.List;

public interface DBInterface<T> {

    public List<T> query();

    public T query(String id);

    public int clearAll();

    public Uri insert(T data);

    public void bulkInsert(List<T> listData);

    public int update(T data);

    public int delete(String id);

    public ContentValues getContentValues(T data);

    public CursorLoader getCursorLoader();

}
