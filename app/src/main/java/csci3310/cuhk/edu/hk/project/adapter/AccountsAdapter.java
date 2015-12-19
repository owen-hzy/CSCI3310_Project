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
import csci3310.cuhk.edu.hk.project.bean.Account;

public class AccountsAdapter extends BaseAbstractRecycleCursorAdapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;

    public AccountsAdapter(Context context) {
        super(context, null);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        Account account = Account.fromCursor(cursor);
        ((AccountViewHolder) holder).mNameView.setText(account.name);
        TextView valueView = ((AccountViewHolder) holder).mValueView;
        valueView.setText(String.format("%.2f", account.value));

        if (account.value > 0) {
            valueView.setTextColor(Color.GREEN);
        } else if (account.value < 0) {
            valueView.setTextColor(Color.RED);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountViewHolder(mLayoutInflater.inflate(R.layout.fragment_account, parent, false), this);
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.account_name)
        TextView mNameView;

        @Bind(R.id.account_value)
        TextView mValueView;

        AccountsAdapter mAdapter;

        AccountViewHolder(View view, AccountsAdapter adapter) {
            super(view);
            mAdapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.account_item)
        void onItemClick() {
            Log.d("AccountViewHolder", "onClick--> position = " + getAdapterPosition());
            Account account = Account.fromCursor((Cursor) mAdapter.getItem(getAdapterPosition()));
        }
    }
}
