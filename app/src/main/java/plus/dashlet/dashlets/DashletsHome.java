package plus.dashlet.dashlets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import plus.dashlet.dashlets.model.reportinstance.ReportInstance;
import plus.dashlet.dashlets.model.reports.AvailableReports;
import plus.dashlet.dashlets.model.reports.Value;
import plus.dashlet.dashlets.query.AvailableReporsQuery;
import plus.dashlet.dashlets.query.ReportInstanceQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DashletsHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChartSelectionListener {

    NavigationView navigationView;
    private String baseURL, apiKey, siteKey;
    private final static String DEVELOPMENT_API_KEY = "apikey";
    private final static String DEVELOPMENT_BASE_URL = "http://10.0.3.2/wordpress/wp-content/plugins/civicrm/civicrm/extern/";
    private final static String DEVELOPMENT_SITE_API_KEY = "6fbdf50f38171543c79ab37ee72c2f4c";
    private Map<String,Integer> navMenu = new HashMap<>();
    private ICiviApi api;
    private GridLayout gridLayout;

    private ArrayList<String> yAxis;
    private ReportInstance reportInstance = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);




        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        yAxis = new ArrayList<String>();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reportInstance!=null) {
                    if(yAxis.size()>0) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("yaxis", yAxis);
                        Map<String, String> labelMap = reportInstance.getMetadata().getLabels().getLabelMap();
                        ArrayList<String> labelList = new ArrayList<String>();
                        for (Map.Entry<String, String> label : labelMap.entrySet()) {
                            labelList.add(label.getValue());
                        }
                        bundle.putStringArrayList("xaxis", labelList);
                        ChartDialog chartDialog = ChartDialog.getInstance(bundle, DashletsHome.this);
                        chartDialog.show(getFragmentManager(), "Chart");
                    } else {
                        Toast.makeText(DashletsHome.this,"No eligible column for charting!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                        Toast.makeText(DashletsHome.this,"No data loaded!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        if(preferenceManager.getString("rest","").isEmpty()||
                preferenceManager.getString("apikey","").isEmpty()||preferenceManager.getString("key","").isEmpty()) {
            ((TextView) findViewById(R.id.notification_textview)).setText("Configuration needed!");
            //startActivity(new Intent(this,SettingsActivity.class));
            preferenceManager.edit().putString("rest",DEVELOPMENT_BASE_URL).apply();
            preferenceManager.edit().putString("apikey",DEVELOPMENT_API_KEY).apply();
            preferenceManager.edit().putString("key",DEVELOPMENT_SITE_API_KEY).apply();
        }

        ((TextView) findViewById(R.id.notification_textview)).setText("Select a Dashlet from menu!");

            baseURL = preferenceManager.getString("rest",DEVELOPMENT_BASE_URL);
            apiKey = preferenceManager.getString("apikey",DEVELOPMENT_API_KEY);
            siteKey = preferenceManager.getString("key",DEVELOPMENT_SITE_API_KEY);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            api = retrofit.create(ICiviApi.class);


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Fetching Available Reports");
            progressDialog.setMessage("Shouldn't take more than a few seconds :)");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();


            Call<AvailableReports> reportCall = api.getAvailableReports(AvailableReporsQuery.buildQueryParams(apiKey, siteKey, 0));
            reportCall.enqueue(new Callback<AvailableReports>() {
                @Override
                public void onResponse(Call<AvailableReports> call, Response<AvailableReports> response) {
                    AvailableReports reports = response.body();
                    int count = reports.getCount();

                    if (count > 0) {
                        List<Value> values = reports.getValues();
                        for (Value value : values) {
                            navMenu.put(value.getTitle(), new Integer(value.getId()));
                            Log.v("Report", "Instance ID: " + value.getId() + "  Report title: " + value.getTitle());
                        }
                    }
                    if (count == 25) {
                        int lastID = Integer.valueOf(reports.getValues().get(count - 1).getId());
                        api.getAvailableReports(AvailableReporsQuery.buildQueryParams(apiKey, siteKey, lastID)).enqueue(this);
                    } else {
                        progressDialog.dismiss();
                        Menu menu = navigationView.getMenu();
                        for (Map.Entry<String, Integer> entry : navMenu.entrySet()) {
                            menu.add(entry.getKey());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AvailableReports> call, Throwable t) {
                    Log.v("error", t.getLocalizedMessage());
                }
            });


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
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final String itemName = item.getTitle().toString();
        if(navMenu.containsKey(itemName)) {
            int instanceId = navMenu.get(itemName);
            gridLayout.setVisibility(View.GONE);
            api.getReportInstance(ReportInstanceQuery.buildQueryParams(apiKey,siteKey,instanceId)).enqueue(new Callback<ReportInstance>() {
                @Override
                public void onResponse(Call<ReportInstance> call, Response<ReportInstance> response) {
                    //Log.v("report instance",response.body().toString());
                    Log.v("report instance","fetch successful");


                    setTitle(itemName);
                    yAxis.clear();


                        gridLayout.removeAllViews();
                        reportInstance = response.body();
                        int c = reportInstance.getCount();
                        if(c==0) {
                            c=1;
                        }
                        gridLayout.setRowCount(c+1);

                        gridLayout.setColumnCount(reportInstance.getMetadata().getLabels().getLabelMap().size());

                        Log.v("grid","row: "+(reportInstance.getCount()+1)+" col: "+reportInstance.getMetadata().getLabels().getLabelMap().size());
                        Map<String, String> labelMap = reportInstance.getMetadata().getLabels().getLabelMap();
                        if(labelMap!=null) {
                            gridLayout.setVisibility(View.VISIBLE);

                            int col = 0;
                            for (Map.Entry<String, String> label : labelMap.entrySet()) {
                                TextView headerCell = (TextView) DashletsHome.this.getLayoutInflater().inflate(R.layout.table_header_cell,null);
                                //headerCell.setTypeface(null, Typeface.BOLD);
                                //headerCell.setTextColor(ContextCompat.getColor(DashletsHome.this,android.R.color.black));
                                //headerCell.setBackgroundColor(ContextCompat.getColor(DashletsHome.this,R.color.colorPrimary));
                                //int padding = (int) getResources().getDimension(R.dimen.cellPadding);
                                //headerCell.setPadding(padding, padding, padding, padding);
                                //headerCell.setTextColor(ContextCompat.getColor(DashletsHome.this, android.R.color.white));
                                headerCell.setText(label.getValue().toUpperCase());
                                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                                //param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                //param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                                //param.leftMargin = padding;
                                //param.rightMargin = padding;
                                //param.setGravity(Gravity.CENTER);
                                param.rowSpec = GridLayout.spec(0);
                                param.columnSpec = GridLayout.spec(col);
                                //headerCell.setLayoutParams(param);
                                gridLayout.addView(headerCell,param);

                                boolean checkForDigit = true;
                                boolean removeHeaderForEmptyColumn = true;

                                for(int i = 1;i <= reportInstance.getCount() ;i++ ) {

                                    String data = (String) reportInstance.getValues().get(i-1).getValueMap().get(label.getKey());

                                    if(checkForDigit) {
                                        if(data!=null && !data.isEmpty()) {
                                            if(NumberUtils.isParsable(data)) {
                                                checkForDigit= false;
                                                yAxis.add(label.getValue());
                                            }
                                        }
                                    }

                                    if(data==null||data.isEmpty()||data.equals("null")) {
                                        continue;
                                    }
                                    removeHeaderForEmptyColumn = false;
                                    TextView dataCell = (TextView) DashletsHome.this.getLayoutInflater().inflate(R.layout.table_row_cell,null);
                                    //if(i%2==0) {
                                        //dataCell.setTextColor(ContextCompat.getColor(DashletsHome.this,R.color.evenGrey));
                                    //}
                                    //dataCell.setPadding(padding, padding, padding, padding);
                                    //dataCell.setTextColor(ContextCompat.getColor(DashletsHome.this, android.R.color.black));
                                    dataCell.setText(data);
                                    GridLayout.LayoutParams cellParam = new GridLayout.LayoutParams();
                                    //cellParam.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                    //cellParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
                                    //cellParam.leftMargin = padding;
                                    //cellParam.rightMargin = padding;
                                    //cellParam.setGravity(Gravity.CENTER);
                                    cellParam.rowSpec = GridLayout.spec(i);
                                    cellParam.columnSpec = GridLayout.spec(col);
                                    Log.v("matrix",col+" "+i);
                                    //dataCell.setLayoutParams(cellParam);
                                    gridLayout.addView(dataCell,cellParam);

                                }
                                col++;
                                if(removeHeaderForEmptyColumn) {
                                    gridLayout.removeView(headerCell);
                                }

                            }

                            if(gridLayout.getChildCount()==0) {
                                gridLayout.setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.notification_textview)).setText("No data loaded!");
                            }

                        }
                }

                @Override
                public void onFailure(Call<ReportInstance> call, Throwable t) {
                    Log.v("report instance error",t.getLocalizedMessage());
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void chartSelected(String yColumn, String xColumn, String chartType) {
        Toast.makeText(this,yColumn+" "+xColumn+" "+chartType,Toast.LENGTH_SHORT).show();
        String yColumnName = null, xColumnName = null;

        for(Map.Entry<String, String> label : reportInstance.getMetadata().getLabels().getLabelMap().entrySet()) {
            if(label.getValue().equalsIgnoreCase(yColumn)) {
                yColumnName = label.getKey();
            } else if(label.getValue().equalsIgnoreCase(xColumn)) {
                xColumnName = label.getKey();
            }
            if(yColumnName!=null && xColumnName!=null) {
                break;
            }
        }

        ArrayList<String> xColumnValues = new ArrayList<>();
        List<plus.dashlet.dashlets.model.reportinstance.Value> values = reportInstance.getValues();
        int listCount = values.size();
        float[] yColumnValues = new float[listCount];

        for(int i = 0;i<listCount;i++) {
            Map<String, Object> valueMap = values.get(i).getValueMap();
            String yValue = (String) valueMap.get(yColumnName);
            if(!yValue.isEmpty() && NumberUtils.isParsable(yValue)) {
                yColumnValues[i] = Float.valueOf(yValue);
            }
            xColumnValues.add((String) valueMap.get(xColumnName));
        }

        Bundle bundle = new Bundle();
        bundle.putFloatArray("yvalues",yColumnValues);
        bundle.putStringArrayList("xvalues",xColumnValues);
        bundle.putString("charttype",chartType);

        Intent intent = new Intent(this,ChartActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

    }
}
