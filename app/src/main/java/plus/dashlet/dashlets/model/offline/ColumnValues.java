package plus.dashlet.dashlets.model.offline;

import io.realm.RealmObject;

public class ColumnValues extends RealmObject {

   String value;

    public ColumnValues(){}

    public ColumnValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
