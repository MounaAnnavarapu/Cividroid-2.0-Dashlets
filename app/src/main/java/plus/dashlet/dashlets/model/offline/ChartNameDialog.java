package plus.dashlet.dashlets.model.offline;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import plus.dashlet.dashlets.R;

public class ChartNameDialog extends DialogFragment {

    EditText chartName;
    GetChartName getChartName;


    public static ChartNameDialog newInstance(GetChartName getChartName)
    {
        ChartNameDialog fragment = new ChartNameDialog();
        fragment.getChartName = getChartName;
        return fragment ;
    }

    public ChartNameDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        chartName = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(chartName).setTitle(getString(R.string.get_chart_name))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getChartName.gotChartName(chartName.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}