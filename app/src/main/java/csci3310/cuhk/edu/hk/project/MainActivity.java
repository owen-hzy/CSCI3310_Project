package csci3310.cuhk.edu.hk.project;

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

import csci3310.cuhk.edu.hk.project.dataHelper.DatabaseHelper;
import csci3310.cuhk.edu.hk.project.dataModel.Budget;
import csci3310.cuhk.edu.hk.project.dataModel.Category;
import csci3310.cuhk.edu.hk.project.dataModel.Currency;
import csci3310.cuhk.edu.hk.project.dataModel.Payment;
import csci3310.cuhk.edu.hk.project.dataModel.Record;
import csci3310.cuhk.edu.hk.project.dataModel.Subcategory;
import csci3310.cuhk.edu.hk.project.dataModel.Vendor;
import csci3310.cuhk.edu.hk.project.fragment.EmptyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Database Helper
    private DatabaseHelper db;
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
                Intent intent = new Intent(MainActivity.this, NewRecordActivity.class);
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

            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, EmptyFragment.newInstance("No Record For Today")).commit();
        }


        //-----------------------database testing-----------------------
        db = new DatabaseHelper((getApplicationContext()));

        //clear all databases
        /*
        db.deleteAllCaterories(db.getAllCategories());
        db.deleteAllSubCaterories(db.getAllSubcategories());
        db.deleteAllBudgets(db.getAllBudgets());
        db.deleteAllCurrencies(db.getAllCurrencies());
        db.deleteAllPayments(db.getAllPayments());
        db.deleteAllVendors(db.getAllVendors());
        db.deleteAllRecords(db.getAllRecords());
        */

        //-------------- Categories -------------
        Category cat1 = new Category("Food");
        Category cat2 = new Category("Transportation");
        Category cat3 = new Category("Entertainment");
        Category cat4 = new Category("Necessities");
        Category cat5 = new Category("Income");


        long cat1_id = db.createCategory(cat1);
        long cat2_id = db.createCategory(cat2);
        long cat3_id = db.createCategory(cat3);
        long cat4_id = db.createCategory(cat4);
        long cat5_id = db.createCategory(cat5);


//        Log.d("Category Count", "Cat Count: " + db.getCategoryCount());
//        Log.d("Category", "Cat 1: " + db.getCategory(cat1_id).getName());


        //-------------- SubCategories -------------
        Subcategory subCat1 = new Subcategory("Breakfast");
        Subcategory subCat2 = new Subcategory("Lunch");
        Subcategory subCat3 = new Subcategory("Dinner");
        Subcategory subCat4 = new Subcategory("Railway");
        Subcategory subCat5 = new Subcategory("Bus");
        Subcategory subCat6 = new Subcategory("Taxi");
        Subcategory subCat7 = new Subcategory("Grocery");
        Subcategory subCat8 = new Subcategory("Salary");
        Subcategory subCat9 = new Subcategory("Movie");



        long subCat1_id = db.createSubCategory(subCat1,cat1_id);
        long subCat2_id = db.createSubCategory(subCat2,cat1_id);
        long subCat3_id = db.createSubCategory(subCat3,cat1_id);
        long subCat4_id = db.createSubCategory(subCat4,cat2_id);
        long subCat5_id = db.createSubCategory(subCat5,cat2_id);
        long subCat6_id = db.createSubCategory(subCat6,cat2_id);
        long subCat7_id = db.createSubCategory(subCat7,cat4_id);
        long subCat8_id = db.createSubCategory(subCat8,cat5_id);
        long subCat9_id = db.createSubCategory(subCat9,cat3_id);



//        Log.d("Subcategories Count", "SubCat Count: " +db.getSubcategoryCount());
//        Log.d("SubCategory", "subCat 2: " + db.getSubCategory(subCat2_id).getName());
//        List<Subcategory> results = db.getAllSubcategoriesByCat("Food");
//        for (Subcategory result : results) {
//            Log.d("SubCat", "Food subcat: " + result.getName());
//        }


        //-------------- Budget -------------
        Budget budget1 = new Budget(6000,"All");
        Budget budget2 = new Budget(3500,"Food");
        Budget budget3 = new Budget(1500,"Transpotation");

        long budget1_id = db.createBudget(budget1);
        long budget2_id = db.createBudget(budget2);
        long budget3_id = db.createBudget(budget3);

        Log.d("Budget Count", "Budget Count: " +db.getBudgetCount());
        Log.d("Budget Count", "Budget All Count: " +db.getAllBudgets().size());
        Log.d("Budget", "Budget 1:" + db.getBudget(budget1_id).getType());


        //-------------- Currency -------------
        Currency currency1 = new Currency("USD",7.7504);
        Currency currency2 = new Currency("TWD",0.2345);
        Currency currency3 = new Currency("HKD",1);

        long currency1_id =db.createCurrency(currency1);
        long currency2_id =db.createCurrency(currency2);
        long currency3_id =db.createCurrency(currency3);

//        Log.d("Currency Count", "Currency Count: " +db.getCurrencyCount());
//        Log.d("Currency Count", "Currency All Count: " + db.getAllCurrencies().size());
//        Log.d("Currency", "Currency 2:" + db.getCurrency(currency2_id).getName());


        //-------------- Payment -------------
        Payment payment1 = new Payment("Cash");
        Payment payment2 = new Payment("Octopus");
        Payment payment3 = new Payment("Debit Card");
        Payment payment4 = new Payment("Credit Card");

        long payment1_id = db.createPayment(payment1);
        long payment2_id = db.createPayment(payment2);
        long payment3_id = db.createPayment(payment3);
        long payment4_id = db.createPayment(payment4);

//        Log.d("Payment", "Payment 4:" + db.getPayment(payment4_id).getName());

        payment4.setName("Visa Card");
        db.updatePayment(payment4);
        payment4_id = db.createPayment(payment4);


//        Log.d("Payment Count", "Payment Count: " +db.getPaymentCount());
//        Log.d("Payment Count", "Payment All Count: " + db.getAllPayments().size());
//        Log.d("Payment", "Payment 4:" + db.getPayment(payment4_id).getName());


        //-------------- Vendor -------------
        Vendor vendor1 = new Vendor("MTR");
        Vendor vendor2 = new Vendor("McDonald's");
        Vendor vendor3 = new Vendor("Med Can");
        Vendor vendor4 = new Vendor("Park'n Shop");
        Vendor vendor5 = new Vendor("7-Eleven");
        Vendor vendor6 = new Vendor("KMB");
        Vendor vendor7 = new Vendor("Broadway");
        Vendor vendor8 = new Vendor("Genki");
        Vendor vendor9 = new Vendor("None");

        long vendor1_id = db.createVendor(vendor1);
        long vendor2_id = db.createVendor(vendor2);
        long vendor3_id = db.createVendor(vendor3);
        long vendor4_id = db.createVendor(vendor4);
        long vendor5_id = db.createVendor(vendor5);
        long vendor6_id = db.createVendor(vendor6);
        long vendor7_id = db.createVendor(vendor7);
        long vendor8_id = db.createVendor(vendor8);
        long vendor9_id = db.createVendor(vendor9);

        db.deleteVendor(vendor9_id);

//        Log.d("Vendor Count", "Vendor Count: " + db.getVendorCount());
//        Log.d("Vendor Count", "Vendor All Count: " + db.getAllVendors().size());
//        Log.d("Vendor", "Vendor 6:" + db.getVendor(vendor6_id).getName());


        //-------------- Record -------------
        Record record1 = new Record(10.5,"Expense");
        Record record2 = new Record(208,"Expense");
        Record record3 = new Record(3000,"Income");
        Record record4 = new Record(50,"Income");
        Record record5 = new Record(44.5,"Expense");
        Record record6 = new Record(13,"Expense");
        Record record7 = new Record(2.5,"Expense");
        Record record8 = new Record(3.7,"Expense");
        Record record9 = new Record(400,"Expense");
        Record record10 = new Record(25.5,"Expense");
        Record record11 = new Record(33,"Expense");

        long record1_id = db.createRecord(record1, vendor5_id, subCat1_id, payment1_id, currency3_id);//10.5
        long record2_id = db.createRecord(record2, vendor4_id, subCat1_id, payment1_id, currency3_id);//208
        long record3_id = db.createRecord(record3, vendor9_id, subCat8_id, payment3_id, currency3_id);//3000
        long record4_id = db.createRecord(record4, vendor9_id, subCat8_id, payment3_id, currency1_id);//50
        long record5_id = db.createRecord(record5, vendor3_id, subCat2_id, payment1_id, currency3_id);//44.5
        long record6_id = db.createRecord(record6, vendor1_id, subCat4_id, payment2_id, currency3_id);//13
        long record7_id = db.createRecord(record7, vendor6_id, subCat5_id, payment2_id, currency3_id);//2.5
        long record8_id = db.createRecord(record8, vendor6_id, subCat5_id, payment2_id, currency3_id);//3.7
        long record9_id = db.createRecord(record9, vendor8_id, subCat3_id, payment4_id, currency2_id);//400
        long record10_id = db.createRecord(record10, vendor3_id, subCat2_id, payment1_id, currency3_id);//25.5
        long record11_id = db.createRecord(record11, vendor7_id, subCat9_id, payment1_id, currency3_id);//33

//        Log.d("Record Count", "Record Count: " +db.getRecordCount());
//        Log.d("Record Count", "Record All Count: " + db.getAllRecords().size());
//        Log.d("Record", "Record 3:" + db.getRecord(record3_id).getPrice());
//        List<Record> results0 = db.getAllRecordsBySubcat("Breakfast");
//        for (Record result : results0) {
//            Log.d("Record", "Breakfast record: " + result.getPrice());
//        }
//        List<Record> results1 = db.getAllRecordsByPayment("Octopus");
//        for (Record result : results1) {
//            Log.d("Record", "Octopus record: " + result.getPrice());
//        }
//        List<Record> results2 = db.getAllRecordsByCurrency("HKD");
//        for (Record result : results2) {
//            Log.d("Record", "HKD record: " + result.getPrice());
//        }
//        List<Record> results3 = db.getAllRecordsByVendor("None");
//        for (Record result : results3) {
//            Log.d("Record", "Vendor 'None' record: " + result.getPrice());
//        }

        //close database
        db.closeDB();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Today")).commit();
        } else if (id == R.id.nav_week) {
            actionBar.setTitle(getString(R.string.week));
            // TODO: Retrieve database records to determine which fragment to show
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EmptyFragment.newInstance("No Record For Week")).commit();
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

}
