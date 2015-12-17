package csci3310.cuhk.edu.hk.project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import csci3310.cuhk.edu.hk.project.fragment.AttributeFragment;
import csci3310.cuhk.edu.hk.project.fragment.ListDialogFragment;

public class NewRecordActivity extends AppCompatActivity implements AttributeFragment.OnListFragmentInteractionListener, ListDialogFragment.OnDialogListItemSelectListener {

    private String[] labelArray = new String[] {
            "Account",
            "Category",
            "SubCategory"
    };

    private String[] defaultValueArray = new String[] {
            "No Account",
            "No Category",
            "No SubCategory"
    };

    private int[] iconArray = new int[] {
            R.drawable.ic_payment,
            R.drawable.ic_category,
            R.drawable.ic_sub_category
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.attribute_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_new_record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("UNDO", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, AttributeFragment.newInstance(labelArray, defaultValueArray, iconArray), "attributeList").commit();
    }

    public void showListDialog(int attribute_position) {
        ListDialogFragment dialog = new ListDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ListDialogFragment.POSITION, attribute_position);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), labelArray[attribute_position]);
    }



    @Override
    public void onListFragmentInteraction(int position) {
        showListDialog(position);
    }

    @Override
    public void onDialogListItemSelect(int caller_position, int position) {
        AttributeFragment attributeFragment = (AttributeFragment) getSupportFragmentManager().findFragmentByTag("attributeList");
        attributeFragment.updateAttributeValue(caller_position, "Option " + position);
        Snackbar.make(findViewById(R.id.attribute_coordinator_layout_container), "Option: " + position, Snackbar.LENGTH_LONG).show();
    }
}
