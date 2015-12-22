package csci3310.cuhk.edu.hk.project.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.AccountActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.adapter.AccountsAdapter;
import csci3310.cuhk.edu.hk.project.adapter.BaseAbstractRecycleCursorAdapter;
import csci3310.cuhk.edu.hk.project.adapter.BudgetsAdapter;
import csci3310.cuhk.edu.hk.project.adapter.ItemsAdapter;
import csci3310.cuhk.edu.hk.project.bean.Budget;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.AccountTable;
import csci3310.cuhk.edu.hk.project.db.AccountsDataHelper;
import csci3310.cuhk.edu.hk.project.db.BudgetsDataHelper;
import csci3310.cuhk.edu.hk.project.db.DBInterface;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;

public class ItemsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static enum ListType {
        Today("Today"), Week("Week"), Month("Month"),Year("Year"), Account("Account"),
        AccountDetail("AccountDetail"), Budget("Budget"), Category("Category");

        private String value;
        private ListType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static final String ITEM_ID = "item_id";
    public static final String ITEM_POSITION = "item_position";
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

    @Bind(R.id.item_summary_container)
    ViewGroup mSummaryContainer;

    @Bind(R.id.item_income_expense)
    ViewGroup mIncomeExpenseContainer;

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
            String itemId = ((TextView) mRecyclerView.getChildAt(viewHolder.getAdapterPosition()).findViewById(R.id.item_id)).getText().toString();
            ConfirmFragment confirmFragment = new ConfirmFragment();
            Bundle args = new Bundle();
            args.putString(ITEM_ID, itemId);
            args.putInt(ITEM_POSITION, viewHolder.getAdapterPosition());
            confirmFragment.setArguments(args);
            confirmFragment.show(getFragmentManager(), "confirm");
        }
    });

    public void deleteItem(String itemId, int position) {
        mDataHelper.delete(itemId);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
    }

    public void cancel(int position) {
        mAdapter.notifyItemChanged(position);
    }


    public ItemsFragment() {
        // Required empty public constructor
    }

    public static ItemsFragment newInstance(ListType listType, String accountName, String category, String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(LIST_TYPE, listType.toString());
        args.putString(AccountTable.COLUMN_NAME, accountName);

        if (listType.equals(ListType.Category)) {
            args.putString(RecordTable.COLUMN_CATEGORY, category);
            args.putString(RecordTable.COLUMN_TYPE, type);
        }

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
        } else if (mListType.equals(ListType.Budget)) {
            mDataHelper = new BudgetsDataHelper(getActivity());
        } else {
            mDataHelper = new RecordsDataHelper(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, rootView);

        if (mListType.equals(ListType.Account) || mListType.equals(ListType.Budget)) {
            mSummaryContainer.setVisibility(View.GONE);
        } else if (mListType.equals(ListType.Category)) {
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
            case AccountDetail:
            case Category:
                mAdapter = new ItemsAdapter(getActivity());
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
                break;
            case Account:
                mAdapter = new AccountsAdapter(getActivity());
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
                break;
            case Budget:
                mAdapter = new BudgetsAdapter(getActivity());
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);
                break;
            default:
                throw new IllegalArgumentException("Unknown List Type: " + mListType.toString());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    private void updateSummary(Cursor cursor) {

        if (mListType.equals(ListType.Today) || mListType.equals(ListType.Week) ||
                mListType.equals(ListType.Month) || mListType.equals(ListType.Year) || mListType.equals(ListType.AccountDetail) ||
                mListType.equals(ListType.Category)) {

            Double summary_amount = 0.0;
            Double income_amount = 0.0;
            Double expense_amount = 0.0;

            List<Record> records = null;

            if (cursor.moveToFirst()) {
                Record record = Record.fromCursor(cursor);
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
                while (cursor.moveToNext()) {
                    record = Record.fromCursor(cursor);

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
            }

            mHeaderValue.setText(String.format("%.2f", summary_amount));
            mIncomeValue.setText(String.format("%.2f", income_amount));
            mExpenseValue.setText(String.format("%.2f", expense_amount));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int td = now.get(Calendar.DAY_OF_MONTH);
        if (mListType.equals(ListType.Today)) {
            String today = year + "-" + month + "-" + (now.get(Calendar.DAY_OF_MONTH));
            String tomorrow = year + "-" + month + "-" + (now.get(Calendar.DAY_OF_MONTH) + 1);
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_TIMESTAMP + " between ? and ?", new String[] {today, tomorrow});
        } else if (mListType.equals(ListType.Week)) {
            // 7 - Sunday, 1 - Monday, 2 - Tuesday, 3 - Wednesday, 4 - Thursday...
            int weekday = (now.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : (now.get(Calendar.DAY_OF_WEEK) - 1);
            // Sunday as the first day of a week
            String start = year + "-" + month + "-" + (now.get(Calendar.DAY_OF_MONTH) + 1 - weekday );
            String end = year + "-" + month + "-" + (now.get(Calendar.DAY_OF_MONTH) + 1 - weekday + 8);
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_TIMESTAMP + " between ? and ?", new String[] {start, end});
        } else if (mListType.equals(ListType.Month)) {
            String startOfMonth = year + "-" + month + "-" + "01";
            String endOfMonth = year + "-" + month + "-" + "32";
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_TIMESTAMP + " between ? and ?", new String[] {startOfMonth, endOfMonth});
        } else if (mListType.equals(ListType.Year)) {
            String startOfYear = year + "-" + (Calendar.JANUARY + 1) + "-" + "01";
            String endOfYear = year + "-" + (Calendar.DECEMBER + 1) + "-" + "32";
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_TIMESTAMP + " between ? and ?", new String[] {startOfYear, endOfYear});
        } else if (mListType.equals(ListType.Category)) {
            String category = getArguments().getString(RecordTable.COLUMN_CATEGORY);
            String type = getArguments().getString(RecordTable.COLUMN_TYPE);
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_CATEGORY + " = ? and " + RecordTable.COLUMN_TYPE + " = ?", new String[] {category, type});
        } else if (mListType.equals(ListType.AccountDetail)) {
            String accountName = getArguments().getString(AccountTable.COLUMN_NAME);
            return mDataHelper.getCursorLoader(RecordTable.COLUMN_ACCOUNT_NAME + " = ?", new String[] {accountName});
        } else if (mListType.equals(ListType.Account) || mListType.equals(ListType.Budget)) {
            return mDataHelper.getCursorLoader(null, null);
        } else {
            throw new IllegalArgumentException("Unknown list type: " + mListType);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0) {
            if (mListType.equals(ListType.AccountDetail)) {
                ((TextView) mEmptyContainer.findViewById(R.id.emptyFragment_text)).setText("No Record For " + getArguments().getString(AccountTable.COLUMN_NAME));
            } else if (mListType.equals(ListType.Budget)) {
                loadBudget();
                return;
            } else {
                ((TextView) mEmptyContainer.findViewById(R.id.emptyFragment_text)).setText("No Record For " + mListType);
            }
            mRecyclerView.setVisibility(View.GONE);
            mEmptyContainer.setVisibility(View.VISIBLE);
        }
        updateSummary(data);
        mAdapter.changeCursor(data);
    }

    private void loadBudget() {
        String[] categoryValues = getActivity().getResources().getStringArray(R.array.category);
        List<Budget> budgets = new ArrayList<>();
        for (int i = 0; i < categoryValues.length; i++) {
            budgets.add(new Budget(categoryValues[i], 0.0));
        }
        mDataHelper.bulkInsert(budgets);
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
            Intent intent = new Intent(getActivity(), AccountActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
