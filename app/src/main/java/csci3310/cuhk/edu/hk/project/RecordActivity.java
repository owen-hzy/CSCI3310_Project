package csci3310.cuhk.edu.hk.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import csci3310.cuhk.edu.hk.project.bean.Budget;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.db.BudgetsDataHelper;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;
import csci3310.cuhk.edu.hk.project.fragment.AttributeFragment;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;
import csci3310.cuhk.edu.hk.project.fragment.ListDialogFragment;
import csci3310.cuhk.edu.hk.project.utils.DateTimeUtils;

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

    private InputMethodManager imm;

    private RecordsDataHelper mDataHelper;
    private BudgetsDataHelper mBudgetDataHelper;
    private EditText amountView;
    private boolean newRecordFlag = true;
    private int recordId;

    private String accountName;
    private String type;
    private String category;
    private String date;
    private String time = "00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataHelper = new RecordsDataHelper(this);
        mBudgetDataHelper = new BudgetsDataHelper(this);
        setContentView(R.layout.activity_new_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.attribute_toolbar);
        amountView = (EditText) findViewById(R.id.new_record_amount);
        // TODO: May add some filter to limit decimal length
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_new_record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record record = null;
                if (newRecordFlag) {
                    record = createRecord();
                    if (record == null) {
                        return;
                    }
                    mDataHelper.insert(record);
                    if (record.type.equals(Record.RecordType.Expense)) {
                        checkBudget(record.category);
                    }
                } else {
                    record = createRecord(recordId);
                    if (record == null) {
                        return;
                    }
                    mDataHelper.update(record);
                    if (record.type.equals(Record.RecordType.Expense)) {
                        checkBudget(record.category);
                    }
                }
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                intent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Today.toString());
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() == null) {
            newRecordFlag = true;
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            valueArray[3] = date;
            valueArray[4] = time;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            newRecordFlag = false;
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Edit");
            amountView.setText(getIntent().getExtras().getString(RecordTable.COLUMN_AMOUNT));
            recordId = getIntent().getExtras().getInt(RecordTable._ID);
            valueArray[0] = getIntent().getExtras().getString(RecordTable.COLUMN_ACCOUNT_NAME);
            valueArray[1] = getIntent().getExtras().getString(RecordTable.COLUMN_TYPE);
            valueArray[2] = getIntent().getExtras().getString(RecordTable.COLUMN_CATEGORY);
            String dateTime = getIntent().getExtras().getString(RecordTable.COLUMN_TIMESTAMP);
            valueArray[3] = dateTime.split(" ")[0];
            valueArray[4] = dateTime.split(" ")[1];
            accountName = valueArray[0];
            type = valueArray[1];
            category = valueArray[2];
            date = valueArray[3];
            time = valueArray[4];
        }

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, AttributeFragment.newInstance(labelArray, valueArray, iconArray), "attributeList").commit();

    }

    public void showListDialog(int attribute_position) {
        ListDialogFragment dialog = new ListDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ListDialogFragment.POSITION, attribute_position);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "listDialog");
    }

    private void checkBudget(String category) {
        Cursor cursor = mDataHelper.queryRaw("select sum(amount) as amount from record where type = 'Expense' and category = '" + category + "' group by category");
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = cursor.getDouble(cursor.getColumnIndex("amount"));
        }

        Budget budget = mBudgetDataHelper.query(category);
        if (budget == null) {
            budget = new Budget(category, 0.0);
        }
        if (budget.value < amount) {
            notifyBudget(amount, budget.value, category);
        }
    }

    private Record createRecord() {
        Record record = new Record();
        if (getAttributeFragment().getAttributeValue(0).equalsIgnoreCase("No Account")) {
            Snackbar.make(findViewById(R.id.create_new_record_fab), "Please Select Account", Snackbar.LENGTH_LONG).show();
            return null;
        }
        record.account_name = this.accountName;
        if (getAttributeFragment().getAttributeValue(1).equalsIgnoreCase("No Type")) {
            Snackbar.make(findViewById(R.id.create_new_record_fab), "Please Select Type", Snackbar.LENGTH_LONG).show();
            return null;
        }
        record.type = Record.RecordType.valueOf(this.type);
        if (getAttributeFragment().getAttributeValue(2).equalsIgnoreCase("No Category")) {
            Snackbar.make(findViewById(R.id.create_new_record_fab), "Please Select Category", Snackbar.LENGTH_LONG).show();
            return null;
        }
        record.category = this.category;
        record.timestamp = date + " " + time;
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
        imm.hideSoftInputFromWindow(amountView.getWindowToken(), 0);

        if (position < 3) {
            showListDialog(position);
        } else if (position == 3) {
            Calendar now = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dpd.setMaxDate(now);
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

        if (caller_position == 0) {
            this.accountName = listItems[position];
        } else if (caller_position == 1) {
            this.type = listItems[position];
        } else if (caller_position == 2) {
            this.category = listItems[position];
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
        Log.w(this.getLocalClassName(), "You picked the following date: " + date);
        getAttributeFragment().updateAttributeValue(3, date);
        this.date = date;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        Log.w(this.getLocalClassName(), "You picked the following time: " + time);
        getAttributeFragment().updateAttributeValue(4, time);
        this.time = time;
    }

    private void notifyBudget(double amount, double budget, String category) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_payment)
                .setContentTitle("Alert for " + category)
                .setContentText("Expense " + amount + " exceeds the budget " + budget);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Budget.toString());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }
}
