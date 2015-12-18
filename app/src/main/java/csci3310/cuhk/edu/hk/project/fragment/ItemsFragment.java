package csci3310.cuhk.edu.hk.project.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.adapter.ItemsAdapter;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;
import csci3310.cuhk.edu.hk.project.utils.DateTimeUtils;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.item_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.item_header_value)
    TextView mHeaderValue;

    @Bind(R.id.income_summary)
    TextView mIncomeValue;

    @Bind(R.id.expense_summary)
    TextView mExpenseValue;

    private RecordsDataHelper mDataHelper;
    private ItemsAdapter mAdapter;
    private View rootView;
    private ViewGroup container;

    private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // callback for drag-n-drop, false to skip this feature
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            String recordId = ((TextView) mRecyclerView.getChildAt(viewHolder.getAdapterPosition()).findViewById(R.id.record_id)).getText().toString();
            mDataHelper.delete(recordId);
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();

            updateSummary();
        }
    });


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
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        updateSummary();
    }

    private void updateSummary() {
        List<Record> records = mDataHelper.query();
        Double summary_amount = 0.0;
        Double income_amount = 0.0;
        Double expense_amount = 0.0;
        for (Record record : records) {
            if (record.type.equals(Record.RecordType.Expense)) {
                summary_amount -= record.amount;
                expense_amount += record.amount;
            }
            else if (record.type.equals(Record.RecordType.Income)) {
                summary_amount += record.amount;
                income_amount += record.amount;
            } else {
                throw new IllegalArgumentException("Unknown Record Type" + record.type.toString());
            }
        }
        mHeaderValue.setText(String.format("%.2f", summary_amount));
        mIncomeValue.setText(String.format("%.2f", income_amount));
        mExpenseValue.setText(String.format("%.2f", expense_amount));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
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

//    private void load() {
//        List<Record> records = new ArrayList<>();
//        records.add(new Record("Lunch", 0.0, Record.RecordType.Expense, DateTimeUtils.getString(null)));
//        mDataHelper.bulkInsert(records);
//    }


}
