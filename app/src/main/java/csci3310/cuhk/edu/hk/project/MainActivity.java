package csci3310.cuhk.edu.hk.project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import csci3310.cuhk.edu.hk.project.fragment.EmptyFragment;
import csci3310.cuhk.edu.hk.project.fragment.RecordFragment;
import csci3310.cuhk.edu.hk.project.fragment.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecordFragment.OnListFragmentInteractionListener {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Today");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, EmptyFragment.newInstance("No Record For Today")).commit();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            actionBar.setTitle(getString(R.string.today));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Today")).commit();
        } else if (id == R.id.nav_week) {
            actionBar.setTitle(getString(R.string.week));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RecordFragment.newInstance()).commit();
        } else if (id == R.id.nav_month) {
            actionBar.setTitle(getString(R.string.month));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Month")).commit();
        } else if (id == R.id.nav_year) {
            actionBar.setTitle(getString(R.string.year));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Year")).commit();
        } else if (id == R.id.nav_account) {
            actionBar.setTitle(getString(R.string.account));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Account")).commit();
        } else if (id == R.id.nav_summary) {
            actionBar.setTitle(getString(R.string.summary));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Summary")).commit();
        } else if (id == R.id.nav_budget) {
            actionBar.setTitle(getString(R.string.budget));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Budget")).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyRecord item) {
        Snackbar.make(findViewById(R.id.coordinator_layout_container), item.content, Snackbar.LENGTH_LONG).show();
    }
}
