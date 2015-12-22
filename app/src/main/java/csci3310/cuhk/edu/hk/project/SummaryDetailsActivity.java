package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.fragment.ConfirmFragment;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class SummaryDetailsActivity extends AppCompatActivity implements ConfirmFragment.OnDialogButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ItemsFragment.ListType listType = ItemsFragment.ListType.valueOf(getIntent().getExtras().getString(ItemsFragment.LIST_TYPE));
        String category = getIntent().getExtras().getString(RecordTable.COLUMN_CATEGORY);
        String type = getIntent().getExtras().getString(RecordTable.COLUMN_TYPE);
        getSupportActionBar().setTitle(category + " " + type);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                ItemsFragment.newInstance(listType, null, category, type), "items").commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryDetailsActivity.this, RecordActivity.class);
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
