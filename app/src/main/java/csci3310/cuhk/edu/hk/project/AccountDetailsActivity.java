package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import csci3310.cuhk.edu.hk.project.db.AccountTable;
import csci3310.cuhk.edu.hk.project.fragment.ConfirmFragment;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class AccountDetailsActivity extends AppCompatActivity implements ConfirmFragment.OnDialogButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ItemsFragment.ListType listType = ItemsFragment.ListType.valueOf(getIntent().getExtras().getString(ItemsFragment.LIST_TYPE));
        String account_name = getIntent().getExtras().getString(AccountTable.COLUMN_NAME);
        getSupportActionBar().setTitle(account_name);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ItemsFragment.newInstance(listType, account_name, null, null), "items").commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailsActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnDialogButtonClick(String itemId, int itemPosition, Boolean confirm) {
        ItemsFragment itemsFragment = (ItemsFragment) getSupportFragmentManager().findFragmentByTag("items");
        if (confirm) {
            itemsFragment.deleteItem(itemId, itemPosition);
        } else {
            itemsFragment.cancel(itemPosition);
        }
    }
}
