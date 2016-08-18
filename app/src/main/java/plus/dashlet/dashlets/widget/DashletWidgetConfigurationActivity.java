package plus.dashlet.dashlets.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import plus.dashlet.dashlets.R;
import plus.dashlet.dashlets.model.offline.ChartsForWidgets;


public class DashletWidgetConfigurationActivity extends AppCompatActivity {

    private int widgetID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);


        setTitle(getString(R.string.choose_chart_for_widget));

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        final Realm realm = Realm.getDefaultInstance();

        RealmResults<ChartsForWidgets> chartsForWidgetses = realm.where(ChartsForWidgets.class).findAll();

        if(chartsForWidgetses.size()==0) {
            TextView empty = new TextView(this);
            empty.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            empty.setText(getText(R.string.no_chart_available));
            empty.setGravity(Gravity.CENTER);
            setContentView(empty);
            return;
        }

        ListView charts = new ListView(this);
        setContentView(charts);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            widgetID = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.dashlet_widget_panel);


        List<String> listItems = new ArrayList<>();





        for(ChartsForWidgets chartsForWidgets : chartsForWidgetses) {
            listItems.add(chartsForWidgets.getChartName());
        }




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);

        charts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chartName = (String) parent.getItemAtPosition(position);
                ChartsForWidgets chartsForWidgets = realm.where(ChartsForWidgets.class).equalTo("chartName",chartName).findFirst();
                byte[] byteArray = chartsForWidgets.getImageByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                remoteViews.setImageViewBitmap(R.id.widgetPanel,bitmap);
                appWidgetManager.updateAppWidget(widgetID,remoteViews);
                Intent done = new Intent();
                done.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetID);
                setResult(RESULT_OK,done);
                finish();
            }
        });
        charts.setAdapter(adapter);

    }
}
