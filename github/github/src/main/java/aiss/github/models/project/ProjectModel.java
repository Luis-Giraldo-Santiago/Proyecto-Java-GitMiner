package aiss.github.models.project;

import aiss.github.models.commit.Commits;
import aiss.github.models.issue.IssueModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("web_url")
    private String web_url;

    @JsonProperty("commits")
    private List<Commits> commits;

    @JsonProperty("issues")
    private List<IssueModel> issues;

    public ProjectModel(String id, String name, String web_url, List<Commits> commits, List<IssueModel> issues) {
        this.id = id;
        this.name = name;
        this.web_url = web_url;
        this.commits = commits;
        this.issues = issues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<Commits> getCommits() {
        return commits;
    }

    public void setCommits(List<Commits> commits) {
        this.commits = commits;
    }

    public List<IssueModel> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueModel> issues) {
        this.issues = issues;
    }
}
