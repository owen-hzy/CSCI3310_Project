package csci3310.cuhk.edu.hk.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;


import csci3310.cuhk.edu.hk.project.Reminder.NotifyService;
import csci3310.cuhk.edu.hk.project.db.AccountTable;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.fragment.ConfirmFragment;
import csci3310.cuhk.edu.hk.project.fragment.ItemsFragment;
import csci3310.cuhk.edu.hk.project.fragment.SummaryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConfirmFragment.OnDialogButtonClickListener {

    private ActionBar actionBar;
    private ItemsFragment.ListType mListType;
//    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Today");

        if (getIntent().getExtras() == null) {
            mListType = ItemsFragment.ListType.Today;
        } else {
            mListType = ItemsFragment.ListType.valueOf(getIntent().getExtras().getString(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Today.toString()));
        }

        //Notification


        Intent myIntent = new Intent(this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(2015,11,22,21,13,0);
//        calendar.set(Calendar.HOUR_OF_DAY, 20);
//        calendar.set(Calendar.MINUTE, 00);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 120000, pendingIntent);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            if (mListType.equals(ItemsFragment.ListType.AccountDetail)) {
                String account_name = getIntent().getExtras().getString(AccountTable.COLUMN_NAME);
                actionBar.setTitle(account_name);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ItemsFragment.newInstance(mListType, account_name, null, null), "items").commit();
            } else if (mListType.equals(ItemsFragment.ListType.Category)) {
                String category = getIntent().getExtras().getString(RecordTable.COLUMN_CATEGORY);
                String type = getIntent().getExtras().getString(RecordTable.COLUMN_TYPE);
                actionBar.setTitle(category + " " + type);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ItemsFragment.newInstance(mListType, null, category, type)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ItemsFragment.newInstance(mListType, null, null, null), "items").commit();
                actionBar.setTitle(mListType.toString());
            }
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            actionBar.setTitle(getString(R.string.today));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Today, null, null, null), "items").commit();
        } else if (id == R.id.nav_week) {
            actionBar.setTitle(getString(R.string.week));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Week, null, null, null), "items").commit();
        } else if (id == R.id.nav_month) {
            actionBar.setTitle(getString(R.string.month));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Month, null, null, null), "items").commit();
        } else if (id == R.id.nav_year) {
            actionBar.setTitle(getString(R.string.year));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Year, null, null, null), "items").commit();
        } else if (id == R.id.nav_account) {
            actionBar.setTitle(getString(R.string.account));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Account, null, null, null), "items").commit();
        } else if (id == R.id.nav_summary) {
            actionBar.setTitle(getString(R.string.summary));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SummaryFragment.newInstance(), "summary").commit();
        } else if (id == R.id.nav_budget) {
            actionBar.setTitle(getString(R.string.budget));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ItemsFragment.newInstance(ItemsFragment.ListType.Budget, null, null, null), "items").commit();
            // TODO: Retrieve database records to determine which fragment to show
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
