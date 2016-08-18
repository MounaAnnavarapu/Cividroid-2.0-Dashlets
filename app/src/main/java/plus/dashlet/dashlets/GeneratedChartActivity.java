package plus.dashlet.dashlets;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import plus.dashlet.dashlets.model.offline.ChartNameDialog;
import plus.dashlet.dashlets.model.offline.ChartsForWidgets;
import plus.dashlet.dashlets.model.offline.GetChartName;


public class GeneratedChartActivity extends AppCompatActivity implements GetChartName {

    Chart chart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        LinearLayout chartHolder = (LinearLayout) findViewById(R.id.chartHolder);
        Bundle bundle = getIntent().getExtras();
        String chartType = bundle.getString("charttype");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        setTitle(chartType);

        List<String> xVals = bundle.getStringArrayList("xvalues");
        float[] yVals = bundle.getFloatArray("yvalues");



        if(chartType.equalsIgnoreCase("bar chart")) {

            List<BarEntry> yEntries = new ArrayList<>();

            int yValsSize = yVals.length;
            for (int i = 0; i < yValsSize; i++) {
                yEntries.add(new BarEntry(yVals[i], i));
            }

            BarDataSet iBarDataSet = new BarDataSet(yEntries, "chart");

            BarChart barChart = new BarChart(this);
            chartHolder.addView(barChart, params);


            barChart.setData(new BarData(xVals, iBarDataSet));
            chart = barChart;


            barChart.invalidate();


        } else if(chartType.equalsIgnoreCase("horizontal bar chart")) {

            List<BarEntry> yEntries = new ArrayList<>();

            int yValsSize = yVals.length;
            for (int i = 0; i < yValsSize; i++) {
                yEntries.add(new BarEntry(yVals[i], i));
            }


            BarDataSet iBarDataSet = new BarDataSet(yEntries, "chart");

            HorizontalBarChart horizontalBarChart = new HorizontalBarChart(this);
            chartHolder.addView(horizontalBarChart, params);


            horizontalBarChart.setData(new BarData(xVals, iBarDataSet));
            chart = horizontalBarChart;
            horizontalBarChart.invalidate();

        } else if(chartType.equalsIgnoreCase("pie chart")) {


            List<Entry> yEntries = new ArrayList<>();

            int yValsSize = yVals.length;
            for (int i = 0; i < yValsSize; i++) {
                yEntries.add(new BarEntry(yVals[i], i));
            }

            PieDataSet pieDataSet = new PieDataSet(yEntries,"pie");

            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(5f);

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            pieDataSet.setColors(colors);

            PieChart pieChart = new PieChart(this);
            chartHolder.addView(pieChart,params);

            PieData pieData = new PieData(xVals,pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);

            pieChart.setData(pieData);

            chart = pieChart;

            pieChart.invalidate();



        } else if(chartType.equalsIgnoreCase("cubic line chart")) {
            List<Entry> yEntries = new ArrayList<>();

            int yValsSize = yVals.length;
            for (int i = 0; i < yValsSize; i++) {
                yEntries.add(new BarEntry(yVals[i], i));
            }

            LineDataSet lineDataSet = new LineDataSet(yEntries,"cubic");

            lineDataSet.setDrawCubic(true);
            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(1.8f);
            lineDataSet.setCircleRadius(4f);
            lineDataSet.setCircleColor(Color.WHITE);
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            lineDataSet.setColor(ContextCompat.getColor(this,R.color.deepOrange));
            lineDataSet.setFillColor(ContextCompat.getColor(this,R.color.yellow));
            lineDataSet.setFillAlpha(200);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);

            LineData lineData = new LineData(xVals,lineDataSet);

            LineChart lineChart = new LineChart(this);
            chartHolder.addView(lineChart,params);

            lineChart.setData(lineData);
            chart = lineChart;
            lineChart.invalidate();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save_for_widgets) {
            ChartNameDialog chartNameDialog = ChartNameDialog.newInstance(this);
            chartNameDialog.show(getSupportFragmentManager(),"get chart name");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void gotChartName(String chartName) {
        ChartsForWidgets chartsForWidgets = new ChartsForWidgets();
        chartsForWidgets.setChartName(chartName);
        Calendar c = Calendar.getInstance();
        long timeStampInSeconds = c.getTimeInMillis();
        chartsForWidgets.setCreationTime(String.valueOf(timeStampInSeconds));
        Bitmap chartBitmap = chart.getChartBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        chartBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        chartsForWidgets.setImageByteArray(byteArray);
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(chartsForWidgets);
        realm.commitTransaction();
    }
}
