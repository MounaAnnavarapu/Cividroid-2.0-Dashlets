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


public class ChartCreationDialog extends DialogFragment {

    private ChartSelectionListener chartSelectionListener;

    public static final ChartCreationDialog getInstance(Bundle bundle, ChartSelectionListener chartSelectionListener) {
        ChartCreationDialog chartCreationDialog = new ChartCreationDialog();
        chartCreationDialog.setArguments(bundle);
        chartCreationDialog.chartSelectionListener = chartSelectionListener;
        return chartCreationDialog;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

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
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(ySpinner.getSelectedItem()==null || xSpinner.getSelectedItem()==null) {
                            Toast.makeText(getActivity(),getString(R.string.invalid_config),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String yColumn, xColumn;

                        yColumn = ySpinner.getSelectedItem().toString();
                        xColumn = xSpinner.getSelectedItem().toString();
                        if(yColumn.equalsIgnoreCase(xColumn)) {
                            Toast.makeText(getActivity(),getString(R.string.same_column_selected),Toast.LENGTH_SHORT).show();
                        } else {
                            chartSelectionListener.chartSelected(yColumn,xColumn, chartSpinner.getSelectedItem().toString());
                        }
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
