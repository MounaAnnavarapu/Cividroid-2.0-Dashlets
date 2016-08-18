
package plus.dashlet.dashlets.model.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "is_error",
        "version",
        "count",
        "values"
})
public class AvailableReports {

    @JsonProperty("is_error")
    private Integer isError;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("values")
    private List<Value> values = new ArrayList<Value>();

    /**
     * @return The isError
     */
    @JsonProperty("is_error")
    public Integer getIsError() {
        return isError;
    }

    /**
     * @param isError The is_error
     */
    @JsonProperty("is_error")
    public void setIsError(Integer isError) {
        this.isError = isError;
    }

    /**
     * @return The version
     */
    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return The count
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return The values
     */
    @JsonProperty("values")
    public List<Value> getValues() {
        return values;
    }

    /**
     * @param values The values
     */
    @JsonProperty("values")
    public void setValues(List<Value> values) {
        this.values = values;
    }

}