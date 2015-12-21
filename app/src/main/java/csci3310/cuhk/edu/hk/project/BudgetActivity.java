package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import csci3310.cuhk.edu.hk.project.bean.Budget;
import csci3310.cuhk.edu.hk.project.db.BudgetTable;
import csci3310.cuhk.edu.hk.project.db.BudgetsDataHelper;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;

public class BudgetActivity extends AppCompatActivity {

    @Bind(R.id.budget_value)
    EditText mValueView;

    private BudgetsDataHelper mDataHelper;
    private int budgetId;
    private String budgetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataHelper = new BudgetsDataHelper(this);

        setContentView(R.layout.activity_budget);
        ButterKnife.bind(this, BudgetActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.budget_toolbar);
        setSupportActionBar(toolbar);

        budgetId = getIntent().getExtras().getInt(BudgetTable._ID);
        budgetName = getIntent().getExtras().getString(BudgetTable.COLUMN_NAME);
        mValueView.setText(getIntent().getExtras().getString(BudgetTable.COLUMN_VALUE));

        getSupportActionBar().setTitle(budgetName);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.budget_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Budget budget = new Budget();
                if (TextUtils.isEmpty(mValueView.getText())) {
                    budget.value = 0.0;
                } else {
                    budget.value = Double.valueOf(mValueView.getText().toString());
                }

                budget.id = budgetId;
                budget.name = budgetName;
                mDataHelper.update(budget);

                Intent intent = new Intent(BudgetActivity.this, MainActivity.class);
                intent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Budget.toString());
                startActivity(intent);
            }
        });
    }

}
