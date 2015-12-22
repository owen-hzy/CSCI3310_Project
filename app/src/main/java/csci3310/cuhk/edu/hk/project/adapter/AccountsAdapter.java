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
import butterknife.OnLongClick;
import csci3310.cuhk.edu.hk.project.AccountActivity;
import csci3310.cuhk.edu.hk.project.AccountDetailsActivity;
import csci3310.cuhk.edu.hk.project.MainActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.bean.Account;
import csci3310.cuhk.edu.hk.project.db.AccountTable;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class AccountsAdapter extends BaseAbstractRecycleCursorAdapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;

    public AccountsAdapter(Context context) {
        super(context, null);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        Account account = Account.fromCursor(cursor);
        ((AccountViewHolder) holder).mIdView.setText(account.id + "");
        ((AccountViewHolder) holder).mNameView.setText(account.name);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountViewHolder(mLayoutInflater.inflate(R.layout.fragment_account, parent, false), this);
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_id)
        TextView mIdView;

        @Bind(R.id.account_name)
        TextView mNameView;

        AccountsAdapter mAdapter;

        AccountViewHolder(View view, AccountsAdapter adapter) {
            super(view);
            mAdapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnLongClick(R.id.account_item)
        boolean onItemLongClick() {
            Account account = Account.fromCursor((Cursor) mAdapter.getItem(getAdapterPosition()));
            Intent intent = new Intent(mAdapter.mContext, AccountActivity.class);
            intent.putExtra(AccountTable._ID, account.id);
            intent.putExtra(AccountTable.COLUMN_NAME, account.name);
            mAdapter.mContext.startActivity(intent);
            return true;
        }

        @OnClick(R.id.account_item)
        void onItemClick() {
            Account account = Account.fromCursor((Cursor) mAdapter.getItem(getAdapterPosition()));
            Intent intent = new Intent(mAdapter.mContext, AccountDetailsActivity.class);
            intent.putExtra(AccountTable.COLUMN_NAME, account.name);
            intent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.AccountDetail.toString());
            mAdapter.mContext.startActivity(intent);
        }
    }
}
