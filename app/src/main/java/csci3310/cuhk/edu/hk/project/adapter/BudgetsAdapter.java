package csci3310.cuhk.edu.hk.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import csci3310.cuhk.edu.hk.project.BudgetActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.bean.Budget;
import csci3310.cuhk.edu.hk.project.db.BudgetTable;

public class BudgetsAdapter extends BaseAbstractRecycleCursorAdapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;

    public BudgetsAdapter(Context context) {
        super(context, null);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        Budget budget = Budget.fromCursor(cursor);
        ((BudgetViewHolder) holder).mIdView.setText(budget.id + "");
        ((BudgetViewHolder) holder).mNameView.setText(budget.name);
        ((BudgetViewHolder) holder).mValueView.setText(String.format("%.2f", budget.value));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BudgetViewHolder(mLayoutInflater.inflate(R.layout.fragment_budget, parent, false), this);
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_id)
        TextView mIdView;

        @Bind(R.id.item_name)
        TextView mNameView;

        @Bind(R.id.item_value)
        TextView mValueView;

        BudgetsAdapter mAdapter;

        BudgetViewHolder(View view, BudgetsAdapter adapter) {
            super(view);
            mAdapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.bugdet_item)
        void onItemClick() {
            Budget budget = Budget.fromCursor((Cursor) mAdapter.getItem(getAdapterPosition()));
            Intent intent = new Intent(mAdapter.mContext, BudgetActivity.class);
            intent.putExtra(BudgetTable._ID, budget.id);
            intent.putExtra(BudgetTable.COLUMN_NAME, budget.name);
            intent.putExtra(BudgetTable.COLUMN_VALUE, budget.value);
            mAdapter.mContext.startActivity(intent);
        }
    }
}
