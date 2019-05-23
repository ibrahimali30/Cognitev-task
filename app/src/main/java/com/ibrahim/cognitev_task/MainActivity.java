package com.ibrahim.cognitev_task;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private static final String TAG = "MainActivity";

    //views
    private BarChart chart;
    private RadioGroup mRadioGroupY;
    private RadioGroup mRadioGroupX;

    //variables
    List<Campaign> mCampaignList;
    private String[] fields_y;
    private String[] fields_x;
    private String yAxisField;
    private String xAxisField;
    private int[] color;
    public static SectionedRecyclerViewAdapter sectionAdapter;
    private CampaignViewModel mCampaignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        mCampaignViewModel = ViewModelProviders.of(this).get(CampaignViewModel.class);
        color = new int[]{Color.rgb(50, 153, 255),
                Color.rgb(255, 102, 102),
                Color.rgb(55, 102, 120)};
        mCampaignList = new ArrayList<>();

        //initialize recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);


        initChart();

        parseJsonCampaigns();

       initRadioGroups();






        mRadioGroupX.check(R.id.country_x);
        mRadioGroupY.check(R.id.category);

    }


    private void groupCampaignsRecyclerView() {
        TextView textureView = findViewById(R.id.group_by_textview);
        textureView.setText("group Campaigns By "+xAxisField);

        sectionAdapter.removeAllSections();
        for (int i=0;i<fields_x.length;i++){
            String title = fields_x[i];
            List<Campaign> campaignList = mCampaignViewModel.getCampaigns(xAxisField , title);
            Log.d(TAG, "groupCampaignsRecyclerView: "+title+" "+campaignList+'\n');
            ExpandableSection section = new ExpandableSection(title+"  ("+campaignList.size()+" )" , campaignList);
            section.expanded=false;
            sectionAdapter.addSection(section);
            Log.d(TAG, "groupCampaignsRecyclerView: called for i "+i+" title "+title);
        }
        sectionAdapter.notifyDataSetChanged();
    }



    private void parseJsonCampaigns() {
        String jsonData = getResources().getString(R.string.json_string);
        Log.d(TAG, "onCreate: "+jsonData);

        try {
            JSONArray campaignsJsonArray = new JSONArray(jsonData);

            for (int i=0 ; i<campaignsJsonArray.length() ; i++){
                JSONObject campaignObject = campaignsJsonArray.getJSONObject(i);

                Campaign campaign = new Campaign();

                campaign.setName(campaignObject.getString("name"));
                campaign.setCountry(campaignObject.getString("country"));
                campaign.setBudget(campaignObject.getString("budget"));
                campaign.setGoal(campaignObject.getString("goal"));
                campaign.setCategory(campaignObject.getString("category"));

                mCampaignList.add(campaign);
            }

            mCampaignViewModel.insertCampaigns(mCampaignList);


        } catch (JSONException e) {
            Log.d(TAG, "onCreate: json error// "+e.getMessage());
            e.printStackTrace();
        }
    }


    private void drawChart() {//this method wil be called each time a radio button selected
        if (fields_y ==null || fields_x ==null)return;

        float groupSpace = 10f;
        float barSpace = 0.05f; // x4 DataSet
        float barWidth = 10f; // x4 DataSet
        int groupCount = fields_x.length;
        int startAt = 1;


        BarData data = new BarData();

        for (int j = 0; j< fields_y.length ; j++) {
            ArrayList<BarEntry> values1 = new ArrayList<>();

            for (int i = 0; i < fields_x.length; i++) {

                    values1.add(new BarEntry(i, mCampaignViewModel.getCount(yAxisField , fields_y[j] , xAxisField , fields_x[i])));
            }

            BarDataSet dataSet = new BarDataSet(values1 , fields_y[j]);
            dataSet.setColor(color[j]);
            data.addDataSet(dataSet);
        }

        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startAt);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startAt + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startAt, groupSpace, barSpace);
        chart.invalidate();
        chart.animateY(800);
    }

    private void initChart() {

        //init views
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

        //chart key
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(30f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int position = (int) (value/30);
                if (position>fields_x.length-1){
                    return "";
                }else {
                    return fields_x[(int) (value/30)]==null?"":fields_x[(int) (value/30)];

                }
            }
        });

        YAxis leftAxis = chart.getAxisLeft();

        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        chart.getAxisRight().setEnabled(false);
    }
    private void initRadioGroups() {
        mRadioGroupX=findViewById(R.id.radio_group_x);
        mRadioGroupX.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View view = findViewById(checkedId);
                fields_x = mCampaignViewModel.getFields(view.getTag().toString());
                xAxisField = view.getTag().toString();

                groupCampaignsRecyclerView();//group campaign based on selected field

                drawChart();
            }
        });

        mRadioGroupY =findViewById(R.id.radio_group);
        mRadioGroupY.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View view = findViewById(checkedId);
                fields_y = mCampaignViewModel.getFields(view.getTag().toString());
                yAxisField = view.getTag().toString();

                drawChart();
            }
        });
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");

    }
}
