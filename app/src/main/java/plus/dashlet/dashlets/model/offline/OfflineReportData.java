package plus.dashlet.dashlets.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OfflineReportData extends RealmObject {

    String columnName;
    RealmList<ColumnValues> values;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public RealmList<ColumnValues> getValues() {
        return values;
    }

    public void setValues(RealmList<ColumnValues> values) {
        this.values = values;
    }
}
