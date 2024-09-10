package aiss.github.models.issue;

import aiss.github.models.commet.Comment;
import aiss.github.models.commit.Author__1;
import aiss.github.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String ref_id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String description;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private String created_at;

    @JsonProperty("updated_at")
    private String updated_at;

    @JsonProperty("closed_at")
    private String closed_at;

    @JsonProperty("labels")
    private List<Label> labels;

    @JsonProperty("user")
    private Author__1 author__1;

    @JsonProperty("assignee")
    private Author__1 assignee;

    @JsonProperty("reactions")
    private Reactions reactions;

    @JsonProperty("url")
    private String web_url;

    private Integer upVotes;

    private Integer downVotes;


    private List<Comment> comentarios;

    private User usuario;

    private User asignado;

    private List<String> etiqueta;

    public Issue(String id, String ref_id, String title, String description, String state, String created_at, String updated_at, String closed_at, List<Label> labels) {
        this.id = id;
        this.ref_id = ref_id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.closed_at = closed_at;
        this.labels = labels;
        this.upVotes = 0;
        this.downVotes = 0;
        this.etiqueta = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Author__1 getAuthor__1() {
        return author__1;
    }

    public void setAuthor__1(Author__1 author__1) {
        this.author__1 = author__1;
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

    public List<Comment> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comment> comments) {
        this.comentarios = comments;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Author__1 getAssignee() {
        return assignee;
    }

    public void setAssignee(Author__1 assignee) {
        this.assignee = assignee;
    }

    public User getAsignado() {
        return asignado;
    }

    public void setAsignado(User asignado) {
        this.asignado = asignado;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

    public List<String> getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(List<String> etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", ref_id='" + ref_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", closed_at='" + closed_at + '\'' +
                ", labels=" + etiqueta + '\'' +
                ", author=" + usuario + '\'' +
                ", assigner="+ asignado + '\'' +
                ", upVotes=" + upVotes + '\'' +
                ", downVotes=" + downVotes + '\'' +
                ", comments=" + comentarios + '\'' +
                ", web_url=" + web_url + '\'' +
                '}';
    }
}
