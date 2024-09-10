package aiss.github.models.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reactions {

    @JsonProperty("+1")
    private Integer upVotes;

    @JsonProperty("-1")
    private Integer downVotes;

    public Reactions(Integer upVotes, Integer downVotes) {
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Integer getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Integer upVotes) {
        this.upVotes = upVotes;
    }

    public Integer getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Integer downVotes) {
        this.downVotes = downVotes;
    }

    @Override
    public String toString() {
        return "Reactions{" +
                "upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                '}';
    }
}
