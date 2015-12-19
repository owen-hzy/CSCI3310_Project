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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.adapter.AccountsAdapter;
import csci3310.cuhk.edu.hk.project.adapter.BaseAbstractRecycleCursorAdapter;
import csci3310.cuhk.edu.hk.project.adapter.ItemsAdapter;
import csci3310.cuhk.edu.hk.project.bean.Account;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.AccountsDataHelper;
import csci3310.cuhk.edu.hk.project.db.DBInterface;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static enum ListType {
        Today("Today"), Week("Week"), Month("Month"),Year("Year"), Account("Account");

        private String value;
        private ListType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static final String RECORD_ID = "record_id";
    public static final String RECORD_POSITION = "record_position";
    public static final String LIST_TYPE = "list_type";

    @Bind(R.id.item_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.item_header_value)
    TextView mHeaderValue;

    @Bind(R.id.income_summary)
    TextView mIncomeValue;

    @Bind(R.id.expense_summary)
    TextView mExpenseValue;

    @Bind(R.id.empty_container)
    LinearLayout mEmptyContainer;

    @Bind(R.id.item_income_expense_list)
    LinearLayout mIncomeExpenseContainer;

    private ListType mListType;

    private DBInterface mDataHelper;
    private BaseAbstractRecycleCursorAdapter<RecyclerView.ViewHolder> mAdapter;

    private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // callback for drag-n-drop, false to skip this feature
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            String recordId = ((TextView) mRecyclerView.getChildAt(viewHolder.getAdapterPosition()).findViewById(R.id.record_id)).getText().toString();
            ConfirmFragment confirmFragment = new ConfirmFragment();
            Bundle args = new Bundle();
            args.putString(RECORD_ID, recordId);
            args.putInt(RECORD_POSITION, viewHolder.getAdapterPosition());
            confirmFragment.setArguments(args);
            confirmFragment.show(getFragmentManager(), "confirm");
        }
    });

    public void deleteItem(String recordId, int position) {
        mDataHelper.delete(recordId);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();

        updateSummary();
    }

    public void cancel(int position) {
        mAdapter.notifyItemChanged(position);
    }


    public ItemsFragment() {
        // Required empty public constructor
    }

    public static ItemsFragment newInstance(ListType listType) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(LIST_TYPE, listType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListType = ListType.valueOf(getArguments().getString(LIST_TYPE));
        if (mListType.equals(ListType.Account)) {
            mDataHelper = new AccountsDataHelper(getActivity());
            setHasOptionsMenu(true);
        } else {
            mDataHelper = new RecordsDataHelper(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, rootView);

        if (mListType.equals(ListType.Account)) {
            mIncomeExpenseContainer.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        switch (mListType) {
            case Today:
            case Week:
            case Month:
            case Year:
                mAdapter = new ItemsAdapter(getActivity());
                mRecyclerView.setAdapter(mAdapter);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
                break;
            case Account:
                mAdapter = new AccountsAdapter(getActivity());
                mRecyclerView.setAdapter(mAdapter);
                break;
            default:
                throw new IllegalArgumentException("Unknown List Type: " + mListType.toString());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        updateSummary();
    }

    private void updateSummary() {

        Double summary_amount = 0.0;

        if (mListType.equals(ListType.Today) || mListType.equals(ListType.Week) ||
                mListType.equals(ListType.Month) || mListType.equals(ListType.Year)) {

            Double income_amount = 0.0;
            Double expense_amount = 0.0;

            List<Record> records = null;
            switch (mListType) {
                case Today:
                case Week:
                case Month:
                case Year:
                    records = mDataHelper.query();
                    break;
                default:
                    break;
            }

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
        } else if (mListType.equals(ListType.Account)) {
            List<Account> accounts = mDataHelper.query();

            for (Account account : accounts) {
                summary_amount += account.value;
            }

            mHeaderValue.setText(String.format("%.2f", summary_amount));
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyContainer.setVisibility(View.VISIBLE);
            ((TextView) mEmptyContainer.findViewById(R.id.emptyFragment_text)).setText("No Record For " + mListType);
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mListType.equals(ListType.Account)) {
            inflater.inflate(R.menu.account_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_new_account) {
            Log.d(this.getClass().getName(), "New Account Clicked");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
