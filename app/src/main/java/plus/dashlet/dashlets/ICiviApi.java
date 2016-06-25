package plus.dashlet.dashlets;

import java.util.Map;

import plus.dashlet.dashlets.model.reportinstance.ReportInstance;
import plus.dashlet.dashlets.model.reports.AvailableReports;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;



public interface ICiviApi {

    @POST("rest.php")
    Call<ReportInstance> getReportInstance(@QueryMap Map<String, String> params);

    @POST("rest.php")
    Call<AvailableReports> getAvailableReports(@QueryMap Map<String, String> params);
}
