package plus.dashlet.dashlets.model.reportinstance;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Labels {


    @JsonIgnore
    private Map<String, String> labelMap = new HashMap<String, String>();


    public Labels(){}



    @JsonAnyGetter
    public Map<String, String> getLabelMap() {
        return this.labelMap;
    }

    @JsonAnySetter
    public void setLabelMap(String name, String value) {
        this.labelMap.put(name, value);
    }

}
