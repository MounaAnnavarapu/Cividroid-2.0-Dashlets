package plus.dashlet.dashlets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import plus.dashlet.dashlets.model.offline.AvailableReports;
import plus.dashlet.dashlets.model.offline.ColumnValues;
import plus.dashlet.dashlets.model.offline.OfflineReport;
import plus.dashlet.dashlets.model.offline.OfflineReportData;
import plus.dashlet.dashlets.model.reportinstance.ReportInstance;
import plus.dashlet.dashlets.model.reports.Value;
import plus.dashlet.dashlets.query.AvailableReportsQuery;
import plus.dashlet.dashlets.query.ReportInstanceQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DashletsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChartSelectionListener {

    private NavigationView navigationView;
    private Realm realm;
    private int instanceID;
    private boolean offline = false;
    private boolean configurationValid;
    private int currentOfflinePageCount;
    private int page;
    private OfflineReport currentPageData;
    private int currentPageRowCount;
    private String baseURL = "", apiKey = "", siteKey = "";
    private final static String DEVELOPMENT_API_KEY = "apikey";
    private final static String DEVELOPMENT_BASE_URL = "http://10.0.2.2/wordpress/wp-content/plugins/civicrm/civicrm/extern/";
    private final static String DEVELOPMENT_SITE_API_KEY = "374d188b38c25ea3b103dee580bb5946";
    private ICiviApi api;
    private GridLayout gridLayout;

    private ArrayList<String> yAxis;
    private ReportInstance reportInstance = null;
    private SharedPreferences preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();




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
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("yaxis", yAxis);
                    ArrayList<String> labelList = new ArrayList<String>();
                    if (!offline) {
                        Map<String, String> labelMap = reportInstance.getMetadata().getLabels().getLabelMap();
                        for (Map.Entry<String, String> label : labelMap.entrySet()) {
                            labelList.add(label.getValue());
                        }
                    } else if(currentOfflinePageCount!=0){
                        RealmList<OfflineReportData> columnNames = realm.where(OfflineReport.class).equalTo("instanceID", String.valueOf(instanceID)).findAllSorted("saveDate").first().getOfflineReportDatas();
                        for (OfflineReportData columnName : columnNames) {
                            labelList.add(columnName.getColumnName());
                        }

                    } else {
                        return;
                    }
                    bundle.putStringArrayList("xaxis", labelList);
                    ChartCreationDialog chartCreationDialog = ChartCreationDialog.getInstance(bundle, DashletsActivity.this);
                    chartCreationDialog.show(getFragmentManager(), "Chart");
                }
            }
        });

        FloatingActionButton prevFab, nextFab;
        prevFab = (FloatingActionButton) findViewById(R.id.prev);
        nextFab = (FloatingActionButton) findViewById(R.id.next);

        View.OnClickListener navigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.prev: if(page==0) {
                        Toast.makeText(DashletsActivity.this,getString(R.string.page1Prompt),Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        page--;
                    }
                        break;
                    case R.id.next:
                        if(offline) {
                            if(page<(currentOfflinePageCount-1)) {
                                page++;
                            } else {
                                Toast.makeText(DashletsActivity.this, getString(R.string.lastPagePrompt), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else
                        if(currentPageRowCount<25) {
                            Toast.makeText(DashletsActivity.this,getString(R.string.lastPagePrompt),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            page++;
                        }
                        break;
                }
                if(!offline) {
                    loadPage();
                } else {
                    loadOfflinePage();
                }
            }};

        prevFab.setOnClickListener(navigationListener);
        nextFab.setOnClickListener(navigationListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ((TextView) findViewById(R.id.notification_textview)).setText(getString(R.string.config_needed));

    }

    @Override
    protected void onResume() {
        super.onResume();

        String baseURL = preferenceManager.getString("rest","");
        String apiKey = preferenceManager.getString("apikey","");
        String siteKey = preferenceManager.getString("key","");

        if(!baseURL.equalsIgnoreCase(this.baseURL) || !apiKey.equalsIgnoreCase(this.apiKey) || !siteKey.equalsIgnoreCase(this.siteKey))
        {
            configurationValid=false;
            this.baseURL = baseURL;
            this.apiKey = apiKey;
            this.siteKey = siteKey;

            //Logging
            /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);*/

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(this.baseURL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    // .client(httpClient.build())
                    .build();

            api = retrofit.create(ICiviApi.class);

            loadAvailableReports();
        }

    }

    private void loadAvailableReports() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Available Reports");
        progressDialog.setMessage("Shouldn't take more than a few seconds :)");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        Call<plus.dashlet.dashlets.model.reports.AvailableReports> reportCall = api.getAvailableReports(AvailableReportsQuery.buildQueryParams(apiKey, siteKey, 0));
        reportCall.enqueue(new Callback<plus.dashlet.dashlets.model.reports.AvailableReports>() {
            @Override
            public void onResponse(Call<plus.dashlet.dashlets.model.reports.AvailableReports> call, Response<plus.dashlet.dashlets.model.reports.AvailableReports> response) {
                plus.dashlet.dashlets.model.reports.AvailableReports reports = response.body();
                if(reports!=null) {
                    int count = reports.getCount();

                    if (count > 0) {
                        configurationValid = true;
                        ((TextView) findViewById(R.id.notification_textview)).setText(getString(R.string.select_dashlet));
                        List<Value> values = reports.getValues();
                        for (Value value : values) {
                            AvailableReports offlineReportModel = new AvailableReports();
                            offlineReportModel.setInstanceID(value.getId());
                            offlineReportModel.setReportName(value.getTitle());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(offlineReportModel);
                            realm.commitTransaction();
                        }
                    }
                    if (count == 25) {
                        int lastID = Integer.valueOf(reports.getValues().get(count - 1).getId());
                        api.getAvailableReports(AvailableReportsQuery.buildQueryParams(apiKey, siteKey, lastID)).enqueue(this);
                    } else {
                        progressDialog.dismiss();
                        Menu menu = navigationView.getMenu();
                        RealmResults<AvailableReports> availableReports = realm.where(AvailableReports.class).findAll();
                        for (AvailableReports report : availableReports) {
                            menu.add(report.getReportName());
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    ((TextView) findViewById(R.id.notification_textview)).setText("Communication error! Configuration is correct?");
                }
            }

            @Override
            public void onFailure(Call<plus.dashlet.dashlets.model.reports.AvailableReports> call, Throwable t) {
                progressDialog.dismiss();
                ((TextView) findViewById(R.id.notification_textview)).setText("Communication error! Configuration is correct?");
            }
        });
    }

    private void loadPage() {
        gridLayout.setVisibility(View.GONE);
        String offset = String.valueOf((page * 25));
        api.getReportInstance(ReportInstanceQuery.buildQueryParams(apiKey, siteKey, instanceID, offset)).enqueue(new Callback<ReportInstance>() {
            @Override
            public void onResponse(Call<ReportInstance> call, Response<ReportInstance> response) {
                yAxis.clear();
                gridLayout.removeAllViews();
                reportInstance = response.body();
                if (reportInstance == null) {
                    return;
                }
                int c = reportInstance.getCount();
                if (c == 0) {
                    c = 1;
                }
                gridLayout.setRowCount(c + 1);

                gridLayout.setColumnCount(reportInstance.getMetadata().getLabels().getLabelMap().size());


                currentPageData = new OfflineReport();
                currentPageData.setInstanceID(String.valueOf(instanceID));
                RealmList<OfflineReportData> offlineReportDatas = new RealmList<OfflineReportData>();


                Map<String, String> labelMap = reportInstance.getMetadata().getLabels().getLabelMap();
                if (labelMap != null) {
                    gridLayout.setVisibility(View.VISIBLE);

                    int col = 0;
                    for (Map.Entry<String, String> label : labelMap.entrySet()) {


                        OfflineReportData offlineReportData = new OfflineReportData();
                        offlineReportData.setColumnName(label.getValue());
                        RealmList<ColumnValues> columnValues = new RealmList<ColumnValues>();


                        TextView headerCell = (TextView) DashletsActivity.this.getLayoutInflater().inflate(R.layout.table_header_cell, null);
                        headerCell.setText(label.getValue().toUpperCase());
                        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                        param.rowSpec = GridLayout.spec(0);
                        param.columnSpec = GridLayout.spec(col);
                        gridLayout.addView(headerCell, param);

                        boolean checkForDigit = true;
                        boolean removeHeaderForEmptyColumn = true;

                        currentPageRowCount = reportInstance.getCount();

                        for (int i = 1; i <= currentPageRowCount; i++) {


                            String data = (String) reportInstance.getValues().get(i - 1).getValueMap().get(label.getKey());

                            if (checkForDigit) {
                                if (data != null && !data.isEmpty()) {
                                    if (NumberUtils.isParsable(data)) {
                                        checkForDigit = false;
                                        yAxis.add(label.getValue());
                                    }
                                }
                            }

                            if (data == null || data.isEmpty() || data.equals("null")) {
                                columnValues.add(new ColumnValues("null"));
                                continue;
                            }
                            columnValues.add(new ColumnValues(data));

                            removeHeaderForEmptyColumn = false;
                            TextView dataCell = (TextView) DashletsActivity.this.getLayoutInflater().inflate(R.layout.table_row_cell, null);
                            dataCell.setText(data);
                            GridLayout.LayoutParams cellParam = new GridLayout.LayoutParams();
                            cellParam.rowSpec = GridLayout.spec(i);
                            cellParam.columnSpec = GridLayout.spec(col);
                            gridLayout.addView(dataCell, cellParam);

                        }


                        col++;
                        if (removeHeaderForEmptyColumn) {
                            gridLayout.removeView(headerCell);
                        } else {
                            offlineReportData.setValues(columnValues);
                            offlineReportDatas.add(offlineReportData);

                        }

                    }

                    currentPageData.setOfflineReportDatas(offlineReportDatas);


                    if (gridLayout.getChildCount() == 0) {
                        gridLayout.setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.notification_textview)).setText(getString(R.string.no_data_loaded));
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportInstance> call, Throwable t) {
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
        getMenuInflater().inflate(R.menu.main, menu);
        if(offline) {
            menu.findItem(R.id.save_offline).setVisible(false);
            menu.findItem(R.id.show_saved_pages).setVisible(false);
            menu.findItem(R.id.goLive).setVisible(true);
            menu.findItem(R.id.deletePage).setVisible(true);
        } else {
            menu.findItem(R.id.save_offline).setVisible(true);
            menu.findItem(R.id.show_saved_pages).setVisible(true);
            menu.findItem(R.id.goLive).setVisible(false);
            menu.findItem(R.id.deletePage).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,AppSettings.class));
            return true;
        }else if(configurationValid) {
            if (id == R.id.save_offline) {
                if (currentPageRowCount > 0) {
                    Calendar c = Calendar.getInstance();
                    long timeStampInSeconds = c.getTimeInMillis();
                    currentPageData.setSaveDate(String.valueOf(timeStampInSeconds));
                    realm.beginTransaction();
                    realm.copyToRealm(currentPageData);
                    realm.commitTransaction();
                    Toast.makeText(DashletsActivity.this, getString(R.string.page_saved), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashletsActivity.this, getString(R.string.no_data_to_save), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.show_saved_pages) {
                Toast.makeText(DashletsActivity.this, getString(R.string.offline_mode), Toast.LENGTH_LONG).show();
                offline = true;
                invalidateOptionsMenu();
                page = 0;
                loadOfflinePage();
            } else if (id == R.id.goLive) {
                Toast.makeText(DashletsActivity.this, getString(R.string.back_online), Toast.LENGTH_LONG).show();
                offline = false;
                invalidateOptionsMenu();
                page = 0;
                loadPage();
            } else if (id == R.id.deletePage) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Toast.makeText(DashletsActivity.this, getString(R.string.page_deleted), Toast.LENGTH_SHORT).show();
                        OfflineReport offlineReport = realm.where(OfflineReport.class).equalTo("instanceID", String.valueOf(instanceID)).findAllSorted("saveDate").get(page);
                        offlineReport.deleteFromRealm();
                        loadOfflinePage();
                    }
                });


            }
        } else {
            Toast.makeText(this,getString(R.string.invalid_config),Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadOfflinePage() {


        gridLayout.removeAllViews();
        gridLayout.setVisibility(View.GONE);


        RealmResults<OfflineReport> offlineReportRealmResults = realm.where(OfflineReport.class).equalTo("instanceID",String.valueOf(instanceID)).findAllSorted("saveDate");

        currentOfflinePageCount = offlineReportRealmResults.size();

        if(currentOfflinePageCount==0) {
            Toast.makeText(DashletsActivity.this,getString(R.string.no_saved_pages),Toast.LENGTH_SHORT).show();
            return;
        }

        yAxis.clear();

        if(page>=currentOfflinePageCount) {
            page = currentOfflinePageCount - 1;
        }


        int c = offlineReportRealmResults.get(page).getOfflineReportDatas().get(0).getValues().size();
        if(c==0) {
            c=1;
        }
        gridLayout.setRowCount(c+1);

        RealmList<OfflineReportData> labels = offlineReportRealmResults.get(page).getOfflineReportDatas();

        gridLayout.setColumnCount(labels.size());
        gridLayout.setVisibility(View.VISIBLE);


        int col = 0;

        for(OfflineReportData label : labels) {
            TextView headerCell = (TextView) DashletsActivity.this.getLayoutInflater().inflate(R.layout.table_header_cell,null);
            headerCell.setText(label.getColumnName().toUpperCase());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.rowSpec = GridLayout.spec(0);
            param.columnSpec = GridLayout.spec(col);
            gridLayout.addView(headerCell,param);
            boolean checkForDigit = true;
            boolean removeHeaderForEmptyColumn = true;

            RealmList<ColumnValues> columnValues = label.getValues();

            int i = 1;

            for(ColumnValues value : columnValues) {
                String data = value.getValue();

                if(checkForDigit) {
                    if(data!=null && !data.isEmpty()) {
                        if(NumberUtils.isParsable(data)) {
                            checkForDigit= false;
                            yAxis.add(label.getColumnName());
                        }
                    }
                }

                if(data==null||data.isEmpty()||data.equals("null")) {
                    continue;
                }

                removeHeaderForEmptyColumn = false;
                TextView dataCell = (TextView) DashletsActivity.this.getLayoutInflater().inflate(R.layout.table_row_cell,null);
                dataCell.setText(data);
                GridLayout.LayoutParams cellParam = new GridLayout.LayoutParams();
                cellParam.rowSpec = GridLayout.spec(i);
                cellParam.columnSpec = GridLayout.spec(col);
                gridLayout.addView(dataCell,cellParam);
                i++;


            }

            col++;
            if(removeHeaderForEmptyColumn) {
                gridLayout.removeView(headerCell);
            }

        }

        if(gridLayout.getChildCount()==0) {
            gridLayout.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.notification_textview)).setText(getString(R.string.no_data_loaded));
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final String itemName = item.getTitle().toString();
        final RealmResults<AvailableReports> reports = realm.where(AvailableReports.class).equalTo("reportName",itemName).findAll();
        instanceID = Integer.valueOf(reports.get(0).getInstanceID());
        page = 0;
        setTitle(itemName);
        gridLayout.setVisibility(View.GONE);
        if(!offline) {
            loadPage();
        } else {
            loadOfflinePage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void chartSelected(String yColumn, String xColumn, String chartType) {
        Toast.makeText(this,yColumn+" "+xColumn+" "+chartType,Toast.LENGTH_SHORT).show();
        String yColumnName = null, xColumnName = null;

        if(!offline) {

            for (Map.Entry<String, String> label : reportInstance.getMetadata().getLabels().getLabelMap().entrySet()) {
                if (label.getValue().equalsIgnoreCase(yColumn)) {
                    yColumnName = label.getKey();
                } else if (label.getValue().equalsIgnoreCase(xColumn)) {
                    xColumnName = label.getKey();
                }
                if (yColumnName != null && xColumnName != null) {
                    break;
                }
            }
        }

        Bundle bundle = new Bundle();

        ArrayList<String> xColumnValues = new ArrayList<>();
        if(!offline) {
            List<plus.dashlet.dashlets.model.reportinstance.Value> values = reportInstance.getValues();
            int listCount = values.size();
            float[] yColumnValues = new float[listCount];

            for (int i = 0; i < listCount; i++) {
                Map<String, Object> valueMap = values.get(i).getValueMap();
                String yValue = (String) valueMap.get(yColumnName);
                if (!yValue.isEmpty() && NumberUtils.isParsable(yValue)) {
                    yColumnValues[i] = Float.valueOf(yValue);
                }
                xColumnValues.add((String) valueMap.get(xColumnName));
            }
            bundle.putFloatArray("yvalues",yColumnValues);


        } else {
            OfflineReport result = realm.where(OfflineReport.class).equalTo("instanceID",String.valueOf(instanceID)).findAllSorted("saveDate").get(page);
            float[] yColumnValues = new float[result.getOfflineReportDatas().first().getValues().size()];
            RealmList<OfflineReportData> columns = result.getOfflineReportDatas();
            int i = 0;
            for(OfflineReportData column : columns) {
                if(column.getColumnName().equalsIgnoreCase(xColumn)) {
                    RealmList<ColumnValues> columnValues = column.getValues();
                    for(ColumnValues columnValues1 : columnValues) {
                        xColumnValues.add(columnValues1.getValue());
                    }
                } else if(column.getColumnName().equalsIgnoreCase(yColumn)) {
                    RealmList<ColumnValues> columnValues = column.getValues();
                    for(ColumnValues columnValues1 : columnValues) {

                        if (!columnValues1.getValue().isEmpty() && NumberUtils.isParsable(columnValues1.getValue())) {
                            yColumnValues[i] = Float.valueOf(columnValues1.getValue());
                        } else {
                        }
                        i++;
                    }
                }
            }
            bundle.putFloatArray("yvalues",yColumnValues);


        }

        bundle.putStringArrayList("xvalues",xColumnValues);
        bundle.putString("charttype",chartType);

        Intent intent = new Intent(this,GeneratedChartActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

    }
}
