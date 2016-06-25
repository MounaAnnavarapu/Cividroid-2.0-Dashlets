package plus.dashlet.dashlets;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class ChartDialog extends DialogFragment {

    private ChartSelectionListener chartSelectionListener;

    public static final ChartDialog getInstance(Bundle bundle, ChartSelectionListener chartSelectionListener) {
        ChartDialog chartDialog = new ChartDialog();
        chartDialog.setArguments(bundle);
        chartDialog.chartSelectionListener = chartSelectionListener;
        return chartDialog;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        Bundle bundle = getArguments();
        ArrayList<String> yAxis = bundle.getStringArrayList("yaxis");
        ArrayList<String> xAxis = bundle.getStringArrayList("xaxis");

        View chartView = getActivity().getLayoutInflater().inflate(R.layout.chart_dialog,null);
        final Spinner ySpinner, xSpinner, chartSpinner;

        ySpinner = (Spinner) chartView.findViewById(R.id.ySpinner);
        xSpinner = (Spinner) chartView.findViewById(R.id.xSpinner);
        chartSpinner = (Spinner) chartView.findViewById(R.id.chartSpinner);

        ArrayAdapter<String> yArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_text_view);
        yArrayAdapter.addAll(yAxis);
        ySpinner.setAdapter(yArrayAdapter);

        ArrayAdapter<String> xArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_text_view);
        xArrayAdapter.addAll(xAxis);
        xSpinner.setAdapter(xArrayAdapter);

        ArrayAdapter<String> chartAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_text_view);
        chartAdapter.add("Bar Chart");
        chartAdapter.add("Horizontal Bar Chart");
        chartAdapter.add("Pie Chart");
        chartAdapter.add("Cubic Line Chart");
        chartSpinner.setAdapter(chartAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Generate a Chart").setView(chartView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String yColumn, xColumn;
                        yColumn = ySpinner.getSelectedItem().toString();
                        xColumn = xSpinner.getSelectedItem().toString();
                        if(yColumn.equalsIgnoreCase(xColumn)) {
                            Toast.makeText(getActivity(),"X-Axis and Y-Axis have same column selected!",Toast.LENGTH_SHORT).show();
                        } else {
                            chartSelectionListener.chartSelected(yColumn,xColumn, chartSpinner.getSelectedItem().toString());
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
