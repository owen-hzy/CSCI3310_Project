package csci3310.cuhk.edu.hk.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;
import csci3310.cuhk.edu.hk.project.fragment.AttributeFragment;
import csci3310.cuhk.edu.hk.project.fragment.ListDialogFragment;

public class RecordActivity extends AppCompatActivity implements AttributeFragment.OnListFragmentInteractionListener, ListDialogFragment.OnDialogListItemSelectListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String[] labelArray = new String[] {
            "Account",
            "Record Type",
            "Category",
            "Date",
            "Time"
    };

    private String[] valueArray = new String[] {
            "No Account",
            "No Type",
            "No Category",
            "No Date",
            "00:00"
    };

    private int[] iconArray = new int[] {
            R.drawable.ic_payment,
            R.drawable.ic_type_24dp,
            R.drawable.ic_category,
            R.drawable.ic_menu_period,
            R.drawable.ic_time
    };

    private RecordsDataHelper mDataHelper;
    private EditText amountView;
    private boolean newRecordFlag = true;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataHelper = new RecordsDataHelper(this);
        setContentView(R.layout.activity_new_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.attribute_toolbar);
        amountView = (EditText) findViewById(R.id.new_record_amount);
        // TODO: May add some filter to limit decimal length

        if (getIntent().getExtras() == null) {
            newRecordFlag = true;
            Calendar calendar = Calendar.getInstance();
            valueArray[3] = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) + 1);
        } else {
            newRecordFlag = false;
            amountView.setText(getIntent().getExtras().getString(RecordTable.COLUMN_AMOUNT));
            recordId = getIntent().getExtras().getInt(RecordTable._ID);
            valueArray[1] = getIntent().getExtras().getString(RecordTable.COLUMN_TYPE);
            valueArray[2] = getIntent().getExtras().getString(RecordTable.COLUMN_CATEGORY);
            String dateTime = getIntent().getExtras().getString(RecordTable.COLUMN_TIMESTAMP);
            valueArray[3] = dateTime.split(" ")[0];
            valueArray[4] = dateTime.split(" ")[1];
        }

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_new_record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record record = null;
                try {
                    if (newRecordFlag) {
                        record = createRecord();
                        mDataHelper.insert(record);
                    } else {
                        record = createRecord(recordId);
                        mDataHelper.update(record);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Snackbar.make(v, "Please Select Record Type", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, AttributeFragment.newInstance(labelArray, valueArray, iconArray), "attributeList").commit();
    }

    public void showListDialog(int attribute_position) {
        ListDialogFragment dialog = new ListDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ListDialogFragment.POSITION, attribute_position);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "listDialog");
    }

    private Record createRecord() {
        Record record = new Record();
        record.type = Record.RecordType.valueOf(getAttributeFragment().getAttributeValue(1));
        record.category = getAttributeFragment().getAttributeValue(2);
        record.timestamp = getAttributeFragment().getAttributeValue(3) + " " + getAttributeFragment().getAttributeValue(4);
        if (TextUtils.isEmpty(amountView.getText())) {
            record.amount = Double.valueOf(0.0);
        } else {
            record.amount = Double.valueOf(amountView.getText().toString());
        }

        return record;
    }

    private Record createRecord(int id) {
        Record record = createRecord();
        record.id = id;
        return record;
    }

    @Override
    public void onListFragmentInteraction(int position) {
        if (position > 0 && position < 3) {
            showListDialog(position);
        } else if (position == 3) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.show(getFragmentManager(), "datePickerDialog");
        } else if (position == 4) {
            TimePickerDialog tpd = TimePickerDialog.newInstance(this, 0, 0, 0, true);
            tpd.show(getFragmentManager(), "timePickerDialog");
        }
    }

    private AttributeFragment getAttributeFragment() {
        AttributeFragment attributeFragment = (AttributeFragment) getSupportFragmentManager().findFragmentByTag("attributeList");
        return attributeFragment;
    }

    @Override
    public void onDialogListItemSelect(int caller_position, int position) {
        ListDialogFragment dialogFragment = (ListDialogFragment) getSupportFragmentManager().findFragmentByTag("listDialog");
        String[] listItems = dialogFragment.getListItems(caller_position);
        getAttributeFragment().updateAttributeValue(caller_position, listItems[position]);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
        Log.w(this.getLocalClassName(), "You picked the following date: " + date);
        getAttributeFragment().updateAttributeValue(3, date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        Log.w(this.getLocalClassName(), "You picked the following time: " + time);
        getAttributeFragment().updateAttributeValue(4, time);
    }
}
