package plus.dashlet.dashlets.query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ReportInstanceQuery {
    public static Map<String,String> buildQueryParams(String apikey, String sitekey, int instanceId) {
        Map<String, String> map = new HashMap<>();
        map.put("entity", "ReportTemplate");
        map.put("action", "getrows");
        map.put("api_key", apikey);
        map.put("key", sitekey);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sequential", 1);
            jsonObject.put("instance_id", instanceId);
            JSONObject options = new JSONObject();
            JSONObject metaData = new JSONObject();
            metaData.put("0", "title");
            metaData.put("1", "labels");
            options.put("metadata", metaData);
            jsonObject.put("options", options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("json", jsonObject.toString());
        return map;
    }
}
