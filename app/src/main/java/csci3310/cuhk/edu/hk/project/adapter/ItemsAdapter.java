package csci3310.cuhk.edu.hk.project.adapter;

import android.content.Context;
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
import csci3310.cuhk.edu.hk.project.bean.Record;

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
        @Bind(R.id.record_id)
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
//            if (getPosition() < 11) {
//                Intent intent = new Intent(mAdapter.mContext, DetailActivity.class);
//                intent.putExtra("position", getPosition());
//                intent.putExtra("title", item.title);
//                mAdapter.mContext.startActivity(intent);
//            } else {
//                Intent intent = new Intent(mAdapter.mContext, SelectActivity.class);
//                intent.putExtra("position", getPosition());
//                intent.putExtra("title", item.title);
//                mAdapter.mContext.startActivity(intent);
//            }
        }
    }
}
