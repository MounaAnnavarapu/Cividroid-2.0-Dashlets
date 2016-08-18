package plus.dashlet.dashlets.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OfflineReport extends RealmObject {

    String instanceID;
    String saveDate;
    RealmList<OfflineReportData> offlineReportDatas;

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public RealmList<OfflineReportData> getOfflineReportDatas() {
        return offlineReportDatas;
    }

    public void setOfflineReportDatas(RealmList<OfflineReportData> offlineReportDatas) {
        this.offlineReportDatas = offlineReportDatas;
    }
}
