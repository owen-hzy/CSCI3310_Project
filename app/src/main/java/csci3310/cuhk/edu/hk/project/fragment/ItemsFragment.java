package csci3310.cuhk.edu.hk.project.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.adapter.ItemsAdapter;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;
import csci3310.cuhk.edu.hk.project.utils.DateTimeUtils;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.item_list)
    RecyclerView mRecyclerView;
    private RecordsDataHelper mDataHelper;
    private ItemsAdapter mAdapter;
    private View rootView;
    private ViewGroup container;

    public ItemsFragment() {
        // Required empty public constructor
    }

    public static ItemsFragment newInstance() {
        ItemsFragment fragment = new ItemsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataHelper = new RecordsDataHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        this.rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, this.rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ItemsAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0) {
            load();
        } else {
            mAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void load() {
        List<Record> records = new ArrayList<>();
        records.add(new Record(0, "Lunch", 10.0, Record.RecordType.EXPENSE, DateTimeUtils.getString(null)));
        records.add(new Record(1, "Lunch", 20.0, Record.RecordType.INCOME, DateTimeUtils.getString(null)));
        mDataHelper.bulkInsert(records);
    }
}
