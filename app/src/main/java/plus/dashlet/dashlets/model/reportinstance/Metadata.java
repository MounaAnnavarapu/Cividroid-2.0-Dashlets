package plus.dashlet.dashlets.model.reportinstance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "labels"
})
public class Metadata {

    @JsonProperty("title")
    private String title;
    @JsonProperty("labels")
    private Labels labels;

    public Metadata() {}

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }


    /**
     * @return The labels
     */
    @JsonProperty("labels")
    public Labels getLabels() {
        return labels;
    }

}