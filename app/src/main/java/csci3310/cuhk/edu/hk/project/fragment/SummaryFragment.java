package csci3310.cuhk.edu.hk.project.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csci3310.cuhk.edu.hk.project.MainActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.SummaryDetailsActivity;
import csci3310.cuhk.edu.hk.project.bean.Record;
import csci3310.cuhk.edu.hk.project.dataModel.Category;
import csci3310.cuhk.edu.hk.project.db.RecordTable;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;

public class SummaryFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart mChart;
    private float[] yData;
    private String[] xData;

    private RecordsDataHelper mDataHelper;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    private boolean noSummaryFlag = false;
    private Record.RecordType recordType = Record.RecordType.Income;
    private ViewGroup emptyContainer;
    private ArrayList<String> xVals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        xData = getResources().getStringArray(R.array.category);
        mDataHelper = new RecordsDataHelper(getActivity());

        getData();
    }

    private void getData() {
        Cursor cursor = mDataHelper.queryRaw("select category, sum(amount) as amount from record where type = '" + recordType.toString() + "' group by category");
        yData = new float[xData.length];
        if (cursor.moveToFirst()) {
            noSummaryFlag = false;
            String category = cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_CATEGORY));
            double amount = cursor.getDouble(cursor.getColumnIndex(RecordTable.COLUMN_AMOUNT));
            int index = Arrays.asList(xData).indexOf(category);
            yData[index] = (float) amount;

            while (cursor.moveToNext()) {
                category = cursor.getString(cursor.getColumnIndex(RecordTable.COLUMN_CATEGORY));
                amount = cursor.getDouble(cursor.getColumnIndex(RecordTable.COLUMN_AMOUNT));
                index = Arrays.asList(xData).indexOf(category);
                yData[index] = (float) amount;
            }
        } else {
            noSummaryFlag = true;
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        final RelativeLayout mainLayout = (RelativeLayout) rootView.findViewById(R.id.mainLayout);
        mChart = (PieChart) rootView.findViewById(R.id.mChart);
        emptyContainer = (ViewGroup) rootView.findViewById(R.id.empty_container);

        updateVisibility();
        configureChart();

        return rootView;
    }

    private void configureChart() {
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setCenterText(recordType.toString());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        // Add data
        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void updateVisibility() {
        if (noSummaryFlag) {
            emptyContainer.setVisibility(View.VISIBLE);
            ((TextView) emptyContainer.findViewById(R.id.emptyFragment_text)).setText("No Summary");
            mChart.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_income) {
            recordType = Record.RecordType.Income;
            getData();
            updateVisibility();
            setData();
            return true;
        } else if (id == R.id.menu_expense) {
            recordType = Record.RecordType.Expense;
            getData();
            updateVisibility();
            setData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        mChart.setCenterText(recordType.toString());
        xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            int count = yVals.size();
            if (yData[i] != 0) {
                yVals.add(new Entry(yData[i], count));
                xVals.add(xData[i]);
            }
        }

        PieDataSet pieDataSet = new PieDataSet(yVals, "Category");
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);

        List<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);


        PieData data = new PieData(xVals, pieDataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        mChart.highlightValue(null);

        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null) {
            return;
        }
        Intent intent = new Intent(mDataHelper.getContext(), SummaryDetailsActivity.class);
        intent.putExtra(ItemsFragment.LIST_TYPE, ItemsFragment.ListType.Category.toString());
        intent.putExtra(RecordTable.COLUMN_CATEGORY, xVals.get(e.getXIndex()));
        intent.putExtra(RecordTable.COLUMN_TYPE, recordType.toString());
        mDataHelper.getContext().startActivity(intent);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
