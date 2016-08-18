package plus.dashlet.dashlets.model.reportinstance;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Value {


    @JsonIgnore
    private Map<String, Object> valueMap = new HashMap<>();

    public Value() {

    }

    @JsonAnyGetter
    public Map<String, Object> getValueMap() {
        return this.valueMap;
    }

    @JsonAnySetter
    public void setValueMap(String name, Object value) {
        this.valueMap.put(name, value);
    }
}