package aiss.github.service;

import aiss.github.models.commet.Comment;
import aiss.github.models.commet.CommentModel;
import aiss.github.models.commit.Commits;
import aiss.github.models.commit.FindCommit;
import aiss.github.models.issue.Issue;
import aiss.github.models.issue.IssueModel;
import aiss.github.models.issue.Label;
import aiss.github.models.project.Project;
import aiss.github.models.project.ProjectModel;
import aiss.github.models.user.User;
import aiss.github.models.user.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    public List<Project> findAllProjects(){
        String uri = "https://api.github.com/repositories";
        ResponseEntity<Project[]> response = restTemplate.exchange(uri,
                HttpMethod.GET, null, Project[].class);
        return Arrays.asList(response.getBody()).stream().limit(5).toList();
    }

    public ProjectModel findOneProject(String orgs, String ORG, Integer numCommit, Integer numIssues, Integer numPages) {
        if(numCommit==null) numCommit = 10;
        if(numIssues==null) numIssues = 10;
        if(numPages==null) numPages = 1;
        UserModel assignee = null;
        List<IssueModel> issueModels = new ArrayList<>();
        List<CommentModel> commentModels = new ArrayList<>();
        String uri = "https://api.github.com/repos/"+orgs+"/"+ORG;
        HttpHeaders headersProject = new HttpHeaders();
        headersProject.set("Authorization", "Bearer " + "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");
        HttpEntity<Project> requestProject = new HttpEntity<>(null, headersProject);
        ResponseEntity<Project> responseProject = restTemplate.exchange(uri,
                HttpMethod.GET, requestProject, Project.class);
        Project project = responseProject.getBody();
        String uriCommit = "https://api.github.com/repos/"+orgs+"/"+ORG+"/commits?per_page="+numCommit+"&page="+numPages;
        HttpHeaders headersCommit = new HttpHeaders();
        headersCommit.set("Authorization", "Bearer " + "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");
        HttpEntity<Commits[]> requestCommit = new HttpEntity<>(null, headersCommit);
        ResponseEntity<FindCommit[]> responseCommit = restTemplate.exchange(uriCommit, HttpMethod.GET, requestCommit,
                FindCommit[].class);
        List<FindCommit> findCommits = Arrays.asList(responseCommit.getBody());
        List<Commits> commits = new ArrayList<>();
        String uriIssue = "https://api.github.com/repos/"+orgs+"/"+ORG+"/issues?per_page="+numIssues+"&page="+numPages;
        HttpHeaders headersIssue = new HttpHeaders();
        headersIssue.set("Authorization", "Bearer " + "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");
        HttpEntity<Issue[]> requestIssue = new HttpEntity<>(null, headersIssue);
        ResponseEntity<Issue[]> responseIssue = restTemplate.exchange(uriIssue, HttpMethod.GET, requestIssue, Issue[].class);
        List<Issue> issues = Arrays.asList(responseIssue.getBody());
        for(FindCommit findCommit: findCommits) {
            Commits commit = new Commits(findCommit.getSha(), "",
                    findCommit.getCommits().getMessage(),
                    findCommit.getCommits().getAuthor().getName(),
                    findCommit.getCommits().getAuthor().getEmail(),
                    findCommit.getCommits().getAuthor().getDate(),
                    findCommit.getCommits().getCommitter().getName(),
                    findCommit.getCommits().getCommitter().getEmail(),
                    findCommit.getCommits().getCommitter().getDate(),
                    findCommit.getHtmlUrl());
            commits.add(commit);
        }
        for (Issue issue: issues){
            issue.setUpVotes(issue.getReactions().getUpVotes());
            issue.setDownVotes(issue.getReactions().getDownVotes());
            Integer id = issue.getAuthor__1().getId();
            String uriUser = "https://api.github.com/user/"+id;
            HttpHeaders headersUser = new HttpHeaders();
            headersUser.set("Authorization", "Bearer " + "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");
            HttpEntity<User> requestUser = new HttpEntity<>(null, headersUser);
            ResponseEntity<User> responseUser = restTemplate.exchange(uriUser, HttpMethod.GET, requestUser, User.class);
            User usuario = responseUser.getBody();
            UserModel author = new UserModel(usuario.getId(), usuario.getLogin(), usuario.getName(), usuario.getAvatar_url(), usuario.getHtml_url());
            issue.setUsuario(responseUser.getBody());
            if (issue.getAssignee() != null){
                Integer idAssig = issue.getAssignee().getId();
                String uriAssig = "https://api.github.com/user/"+idAssig;
                ResponseEntity<User> responseAssig = restTemplate.exchange(uriAssig, HttpMethod.GET, requestUser, User.class);
                User asignado = responseAssig.getBody();
                assignee = new UserModel(asignado.getId(), asignado.getLogin(), asignado.getName(), asignado.getAvatar_url(), asignado.getHtml_url());
                issue.setAsignado(responseAssig.getBody());
            }
            String number = issue.getRef_id();
            String uriComment = "https://api.github.com/repos/"+orgs+"/"+ORG+"/issues/"+number+"/comments";
            HttpHeaders headersComment = new HttpHeaders();
            headersComment.set("Authorization", "Bearer " + "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");
            HttpEntity<Comment[]> requestComment = new HttpEntity<>(null, headersComment);
            ResponseEntity<Comment[]> responseComment = restTemplate.exchange(uriComment, HttpMethod.GET, requestComment,
                    Comment[].class);
            List<Comment> comments = Arrays.asList(responseComment.getBody());
            if(comments.size()>=1) {
                for (Comment comment : comments) {
                    String uriUserComment = "https://api.github.com/user/"+comment.getAuthor__1().getId();
                    HttpHeaders headersUserComment = new HttpHeaders();
                    headersUserComment.set("Authorization", "Bearer " + "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");
                    HttpEntity<User> requestUserComment = new HttpEntity<>(null, headersUserComment);
                    ResponseEntity<User> responseUserComment = restTemplate.exchange(uriUser, HttpMethod.GET, requestUser, User.class);
                    User userComment = responseUserComment.getBody();
                    UserModel usuarioComentario = new UserModel(userComment.getId(), userComment.getLogin(),
                            userComment.getName(), userComment.getAvatar_url(), userComment.getHtml_url());
                    CommentModel commentModel = new CommentModel(comment.getId(), comment.getBody(), usuarioComentario,
                            comment.getCreated_at(), comment.getUpdate_at());
                    commentModels.add(commentModel);
                }
            }
            issue.setComentarios((Arrays.asList(responseComment.getBody())));
            issue.setEtiqueta(issue.getLabels().stream().map(Label::getName).toList());
            List<String> label = new ArrayList<>();
            label.add(issue.getLabels().getClass().getName());
            IssueModel issueModel = new IssueModel(issue.getId(), issue.getRef_id(), issue.getTitle(), issue.getDescription(),
                    issue.getState(), issue.getCreated_at(), issue.getUpdated_at(), issue.getClosed_at(), label,
                    author, assignee, issue.getUpVotes(), issue.getDownVotes(), issue.getWeb_url(), commentModels);
            issueModels.add(issueModel);
        }
        ProjectModel projectModel = new ProjectModel(project.getId(), project.getName(), project.getWeb_url(), project.getCommit(), issueModels);
        return projectModel;
    }

    public ProjectModel createProject(String orgs, String ORG, Integer numCommit, Integer numIssues, Integer numPages) {
        UserModel assignee = null;
        List<IssueModel> issueModels = new ArrayList<>();
        List<CommentModel> commentModels = new ArrayList<>();
        String uri = "https://api.github.com/repos/"+orgs+"/"+ORG;
        HttpHeaders headersProject = new HttpHeaders();
        headersProject.set("Authorization", "Bearer " + "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");
        HttpEntity<Project> requestProject = new HttpEntity<>(null, headersProject);
        ResponseEntity<Project> responseProject = restTemplate.exchange(uri,
                HttpMethod.GET, requestProject, Project.class);
        Project project = responseProject.getBody();
        String uriCommit = "https://api.github.com/repos/"+orgs+"/"+ORG+"/commits?per_page="+numCommit+"&page="+numPages;
        HttpHeaders headersCommit = new HttpHeaders();
        headersCommit.set("Authorization", "Bearer " + "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");
        HttpEntity<Commits[]> requestCommit = new HttpEntity<>(null, headersCommit);
        ResponseEntity<FindCommit[]> responseCommit = restTemplate.exchange(uriCommit, HttpMethod.GET, requestCommit,
                FindCommit[].class);
        List<FindCommit> findCommits = Arrays.asList(responseCommit.getBody());
        List<Commits> commits = new ArrayList<>();
        String uriIssue = "https://api.github.com/repos/"+orgs+"/"+ORG+"/issues?per_page="+numIssues+"&page="+numPages;
        HttpHeaders headersIssue = new HttpHeaders();
        headersIssue.set("Authorization", "Bearer " + "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");
        HttpEntity<Issue[]> requestIssue = new HttpEntity<>(null, headersIssue);
        ResponseEntity<Issue[]> responseIssue = restTemplate.exchange(uriIssue, HttpMethod.GET, requestIssue, Issue[].class);
        List<Issue> issues = Arrays.asList(responseIssue.getBody());
        for(FindCommit findCommit: findCommits) {
            Commits commit = new Commits(findCommit.getSha(), "",
                    findCommit.getCommits().getMessage(),
                    findCommit.getCommits().getAuthor().getName(),
                    findCommit.getCommits().getAuthor().getEmail(),
                    findCommit.getCommits().getAuthor().getDate(),
                    findCommit.getCommits().getCommitter().getName(),
                    findCommit.getCommits().getCommitter().getEmail(),
                    findCommit.getCommits().getCommitter().getDate(),
                    findCommit.getHtmlUrl());
            commits.add(commit);
        }
        for (Issue issue: issues){
            issue.setUpVotes(issue.getReactions().getUpVotes());
            issue.setDownVotes(issue.getReactions().getDownVotes());
            Integer id = issue.getAuthor__1().getId();
            String uriUser = "https://api.github.com/user/"+id;
            HttpHeaders headersUser = new HttpHeaders();
            headersUser.set("Authorization", "Bearer " + "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");
            HttpEntity<User> requestUser = new HttpEntity<>(null, headersUser);
            ResponseEntity<User> responseUser = restTemplate.exchange(uriUser, HttpMethod.GET, requestUser, User.class);
            User usuario = responseUser.getBody();
            UserModel author = new UserModel(usuario.getId(), usuario.getLogin(), usuario.getName(), usuario.getAvatar_url(), usuario.getHtml_url());
            issue.setUsuario(responseUser.getBody());
            if (issue.getAssignee() != null){
                Integer idAssig = issue.getAssignee().getId();
                String uriAssig = "https://api.github.com/user/"+idAssig;
                ResponseEntity<User> responseAssig = restTemplate.exchange(uriAssig, HttpMethod.GET, requestUser, User.class);
                User asignado = responseAssig.getBody();
                assignee = new UserModel(asignado.getId(), asignado.getLogin(), asignado.getName(), asignado.getAvatar_url(), asignado.getHtml_url());
                issue.setAsignado(responseAssig.getBody());
            }
            String number = issue.getRef_id();
            String uriComment = "https://api.github.com/repos/"+orgs+"/"+ORG+"/issues/"+number+"/comments";
            HttpHeaders headersComment = new HttpHeaders();
            headersComment.set("Authorization", "Bearer " + "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");
            HttpEntity<Comment[]> requestComment = new HttpEntity<>(null, headersComment);
            ResponseEntity<Comment[]> responseComment = restTemplate.exchange(uriComment, HttpMethod.GET, requestComment,
                    Comment[].class);
            List<Comment> comments = Arrays.asList(responseComment.getBody());
            if(comments.size()>=1) {
                for (Comment comment : comments) {
                    String uriUserComment = "https://api.github.com/user/"+comment.getAuthor__1().getId();
                    HttpHeaders headersUserComment = new HttpHeaders();
                    headersUserComment.set("Authorization", "Bearer " + "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");
                    HttpEntity<User> requestUserComment = new HttpEntity<>(null, headersUserComment);
                    ResponseEntity<User> responseUserComment = restTemplate.exchange(uriUser, HttpMethod.GET, requestUser, User.class);
                    User userComment = responseUserComment.getBody();
                    UserModel usuarioComentario = new UserModel(userComment.getId(), userComment.getLogin(),
                            userComment.getName(), userComment.getAvatar_url(), userComment.getHtml_url());
                    CommentModel commentModel = new CommentModel(comment.getId(), comment.getBody(), usuarioComentario,
                            comment.getCreated_at(), comment.getUpdate_at());
                    commentModels.add(commentModel);
                }
            }
            issue.setComentarios((Arrays.asList(responseComment.getBody())));
            issue.setEtiqueta(issue.getLabels().stream().map(Label::getName).toList());
            List<String> label = new ArrayList<>();
            label.add(issue.getLabels().getClass().getName());
            IssueModel issueModel = new IssueModel(issue.getId(), issue.getRef_id(), issue.getTitle(), issue.getDescription(),
                    issue.getState(), issue.getCreated_at(), issue.getUpdated_at(), issue.getClosed_at(), label,
                    author, assignee, issue.getUpVotes(), issue.getDownVotes(), issue.getWeb_url(), commentModels);
            issueModels.add(issueModel);
        }
        ProjectModel projectModel = new ProjectModel(project.getId(), project.getName(), project.getWeb_url(), project.getCommit(), issueModels);
        String uriGitMiner = "http://localhost:8080/gitminer/projects";
        restTemplate.postForObject(uriGitMiner, projectModel, ProjectModel.class);
        return projectModel;
    }

}
