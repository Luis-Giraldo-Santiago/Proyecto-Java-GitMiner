package aiss.github.models.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commits {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("message")
    private String message;

    @JsonProperty("author_name")
    private String authors_name;

    @JsonProperty("author_email")
    private String author_email;

    @JsonProperty("authored_date")
    private String authored_date;

    @JsonProperty("committer_name")
    private String committer_name;

    @JsonProperty("committer_email")
    private String committer_email;

    @JsonProperty("committed_date")
    private String committed_date;

    @JsonProperty("web_url")
    private String web_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage(String message) {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthors_name() {
        return authors_name;
    }

    public void setAuthors_name(String authors_name) {
        this.authors_name = authors_name;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthored_date() {
        return authored_date;
    }

    public void setAuthored_date(String authored_date) {
        this.authored_date = authored_date;
    }

    public String getCommitter_name() {
        return committer_name;
    }

    public void setCommitter_name(String committer_name) {
        this.committer_name = committer_name;
    }

    public String getCommitter_email() {
        return committer_email;
    }

    public void setCommitter_email(String committer_email) {
        this.committer_email = committer_email;
    }

    public String getCommitted_date() {
        return committed_date;
    }

    public void setCommitted_date(String committed_date) {
        this.committed_date = committed_date;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Commits(String id, String title, String message, String authors_name, String author_email, String authored_date, String committer_name, String committer_email, String committed_date, String web_url) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.authors_name = authors_name;
        this.author_email = author_email;
        this.authored_date = authored_date;
        this.committer_name = committer_name;
        this.committer_email = committer_email;
        this.committed_date = committed_date;
        this.web_url = web_url;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", authors_name='" + authors_name + '\'' +
                ", author_email='" + author_email + '\'' +
                ", authored_date='" + authored_date + '\'' +
                ", committer_name='" + committer_name + '\'' +
                ", committer_email='" + committer_email + '\'' +
                ", committed_date='" + committed_date + '\'' +
                ", web_url='" + web_url + '\'' +
                '}';
    }
}
