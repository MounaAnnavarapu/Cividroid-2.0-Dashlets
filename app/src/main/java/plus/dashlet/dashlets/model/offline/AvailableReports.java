package plus.dashlet.dashlets.model.offline;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class AvailableReports extends RealmObject {

    @PrimaryKey
    String instanceID;
    String reportName;


    public AvailableReports() {}

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
