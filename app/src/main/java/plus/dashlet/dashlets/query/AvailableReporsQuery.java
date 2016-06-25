package plus.dashlet.dashlets.query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AvailableReporsQuery {
    public static Map<String,String> buildQueryParams(String apikey, String sitekey, int offset) {
        Map<String, String> map = new HashMap<>();
        map.put("entity", "ReportInstance");
        map.put("action", "get");
        map.put("api_key", apikey);
        map.put("key", sitekey);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sequential", 1);
            jsonObject.put("return","id,title");
            JSONObject options = new JSONObject();
            options.put("offset",offset);
            jsonObject.put("options",options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("json", jsonObject.toString());
        return map;
    }
}
