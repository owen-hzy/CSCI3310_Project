package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.bean.Account;
import csci3310.cuhk.edu.hk.project.db.AccountsDataHelper;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class NewAccountActivity extends AppCompatActivity {

    @Bind(R.id.new_account_amount)
    EditText mValueView;

    @Bind(R.id.new_account_value)
    EditText mNameView;

    private AccountsDataHelper mDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataHelper = new AccountsDataHelper(this);

        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this, NewAccountActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_new_account_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNameView.getText())) {
                    Snackbar.make(v, "Please Input Account Name", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Account account = new Account();
                account.name = mNameView.getText().toString();
                if (TextUtils.isEmpty(mValueView.getText())) {
                    account.value = 0.0;
                } else {
                    account.value = Double.valueOf(mValueView.getText().toString());
                }

                mDataHelper.insert(account);

                Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                intent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Account.toString());
                startActivity(intent);
            }
        });
    }
}
