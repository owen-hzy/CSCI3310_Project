package csci3310.cuhk.edu.hk.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.RecordActivity;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.RecordTable;

public class ItemsAdapter extends BaseAbstractRecycleCursorAdapter<RecyclerView.ViewHolder> {


    private final LayoutInflater mLayoutInflater;

    public ItemsAdapter(Context context) {
        super(context, null);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        Record item = Record.fromCursor(cursor);
        ((ItemViewHolder) holder).mIdView.setText(item.id + "");
        ((ItemViewHolder) holder).mCategoryView.setText(item.category);
        ((ItemViewHolder) holder).mCommentView.setText(item.timestamp);
        TextView amountView = ((ItemViewHolder) holder).mAmountView;
        amountView.setText(String.format("%.2f", item.amount));

        if (item.type.equals(Record.RecordType.Expense)) {
            amountView.setTextColor(Color.GREEN);
        }
        else if (item.type.equals(Record.RecordType.Income)) {
            amountView.setTextColor(Color.RED);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.fragment_item, parent, false), this);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_id)
        TextView mIdView;

        @Bind(R.id.record_category)
        TextView mCategoryView;

        @Bind(R.id.record_comment)
        TextView mCommentView;

        @Bind(R.id.record_amount)
        TextView mAmountView;

        ItemsAdapter mAdapter;

        ItemViewHolder(View view, ItemsAdapter adapter) {
            super(view);
            mAdapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.record_item)
        void onItemClick() {
            Log.d("ItemViewHolder", "onClick--> position = " + getAdapterPosition());
            Record item = Record.fromCursor((Cursor) mAdapter.getItem(getAdapterPosition()));
            Intent intent = new Intent(mAdapter.mContext, RecordActivity.class);
            intent.putExtra(RecordTable._ID, item.id);
            intent.putExtra(RecordTable.COLUMN_AMOUNT, item.amount + "");
            intent.putExtra(RecordTable.COLUMN_CATEGORY, item.category);
            intent.putExtra(RecordTable.COLUMN_TYPE, item.type.toString());
            intent.putExtra(RecordTable.COLUMN_TIMESTAMP, item.timestamp);
            intent.putExtra(RecordTable.COLUMN_ACCOUNT_NAME, item.account_name);
            mAdapter.mContext.startActivity(intent);
        }
    }
}
