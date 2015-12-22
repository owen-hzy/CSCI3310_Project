package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.database.SQLException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.bean.Account;
import csci3310.cuhk.edu.hk.project.db.AccountTable;
import csci3310.cuhk.edu.hk.project.db.AccountsDataHelper;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class AccountActivity extends AppCompatActivity {

    @Bind(R.id.new_account_value)
    EditText mNameView;

    private AccountsDataHelper mDataHelper;
    private boolean newAccountFlag = true;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataHelper = new AccountsDataHelper(this);

        setContentView(R.layout.activity_new_account);
        ButterKnife.bind(this, AccountActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() == null) {
            newAccountFlag = true;
        } else {
            newAccountFlag = false;
            accountId = getIntent().getExtras().getInt(AccountTable._ID);
            mNameView.setText(getIntent().getExtras().getString(AccountTable.COLUMN_NAME));
        }

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

                if (newAccountFlag) {
                    try {
                        mDataHelper.insert(account);
                    } catch (SQLException e) {
                        Snackbar.make(v, "Name already exists", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    account.id = accountId;
                    try {
                        mDataHelper.update(account);
                    } catch (SQLException e) {
                        Snackbar.make(v, "Name already exists", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

//                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
//                startActivity(intent);
                NavUtils.navigateUpFromSameTask(AccountActivity.this);
            }
        });
    }
}
