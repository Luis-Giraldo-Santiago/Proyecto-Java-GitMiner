package com.GitLab.GitLab.service;

import com.GitLab.GitLab.model.comment.Comment;
import com.GitLab.GitLab.model.commit.Commit;
import com.GitLab.GitLab.model.issue.Issue;
import com.GitLab.GitLab.model.issue.findIssue;
import com.GitLab.GitLab.model.project.Project;
import com.GitLab.GitLab.model.user.User;
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

    public Project findProyect(String id, Integer sinceCommit,Integer sinceIssues,Integer max_Pages){
        if(sinceCommit==null) sinceCommit = 10;
        if(sinceIssues==null) sinceIssues = 10;
        if(max_Pages==null) max_Pages = 1;
        User assignee = null;

        String token = "glpat-Grv1GWWpaAyHTB-kv12J";

        List<Issue> issues = new ArrayList<>();


        String uri = "https://gitlab.com/api/v4/projects/"+id;


        ResponseEntity<Project> response =restTemplate.exchange(uri, HttpMethod.GET, null, Project.class);


        Project project = response.getBody();


        String uriCommit = "https://gitlab.com/api/v4/projects/"+id
                +"/repository/commits?per_page="+sinceCommit+"&page="+max_Pages;

        ResponseEntity<Commit[]> responseCommit = restTemplate.exchange(uriCommit, HttpMethod.GET,
                null, Commit[].class);

        List<Commit> commits = Arrays.asList(responseCommit.getBody());

        String uriIssue = "https://gitlab.com/api/v4/projects/"+id
                +"/issues?per_page="+sinceIssues+"&page="+max_Pages;

        ResponseEntity<findIssue[]> responseIssue = restTemplate.exchange(uriIssue, HttpMethod.GET,
                null, findIssue[].class);

        List<findIssue> findIssues = Arrays.asList(responseIssue.getBody());

        for(findIssue issue: findIssues){
            String uriComment = "https://gitlab.com/api/v4/projects/"+id+"/issues/"+issue.getIid()+"/notes";
            HttpHeaders headers =new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Comment[]> request =new HttpEntity<>(null, headers);
            ResponseEntity<Comment[]> responseComment = restTemplate.exchange(uriComment, HttpMethod.GET, request,
                    Comment[].class);


            User author = new User(issue.getAuthor().getId(), issue.getAuthor().getUsername(), issue.getAuthor().getName(),
                    issue.getAuthor().getAvatar_url(), issue.getAuthor().getWeb_url());

            if (issue.getAssignee()!=null){
                assignee = new User(issue.getAssignee().getId(), issue.getAssignee().getUsername(),
                        issue.getAssignee().getName(), issue.getAssignee().getAvatar_url(), issue.getAssignee().getWeb_url());
            }

            List<Comment> comment = Arrays.asList(responseComment.getBody());
            Issue issueModel = new Issue(issue.getId(),issue.getRef_id(), issue.getTitle(), issue.getDescription(),
                    issue.getState(), issue.getCreated_at(), issue.getUpdated_at(), issue.getClosed_at(),
                    issue.getLabels(), author, assignee, issue.getUpvotes(), issue.getDownvotes(),
                    issue.getWeb_url(), comment);
            issues.add(issueModel);
        }


        project.setCommits(commits);
        project.setIssues(issues);

        return project;
    }

    public Project createdProject(String id, Integer sinceCommit,Integer sinceIssues,Integer max_Pages){
        if(sinceCommit==null) sinceCommit = 10;
        if(sinceIssues==null) sinceIssues = 10;
        if(max_Pages==null) max_Pages = 1;
        User assignee = null;

        String token = "glpat-Grv1GWWpaAyHTB-kv12J";

        List<Issue> issues = new ArrayList<>();

        String uri = "https://gitlab.com/api/v4/projects/"+id;


        ResponseEntity<Project> response =restTemplate.exchange(uri, HttpMethod.GET, null, Project.class);

        Project project = response.getBody();
        String uriCommit = "https://gitlab.com/api/v4/projects/"+id
                +"/repository/commits?per_page="+sinceCommit+"&page="+max_Pages;

        ResponseEntity<Commit[]> responseCommit = restTemplate.exchange(uriCommit, HttpMethod.GET,
                null, Commit[].class);

        List<Commit> commits = Arrays.asList(responseCommit.getBody());

        String uriIssue = "https://gitlab.com/api/v4/projects/"+id
                +"/issues?per_page="+sinceIssues+"&page="+max_Pages;

        ResponseEntity<findIssue[]> responseIssue = restTemplate.exchange(uriIssue, HttpMethod.GET,
                null, findIssue[].class);

        List<findIssue> findIssues = Arrays.asList(responseIssue.getBody());

        for(findIssue issue: findIssues){
            String uriComment = "https://gitlab.com/api/v4/projects/"+id+"/issues/"+issue.getIid()+"/notes";
            HttpHeaders headers =new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Comment[]> request =new HttpEntity<>(null, headers);
            ResponseEntity<Comment[]> responseComment = restTemplate.exchange(uriComment, HttpMethod.GET, request,
                    Comment[].class);
            User author = new User(issue.getAuthor().getId(), issue.getAuthor().getUsername(), issue.getAuthor().getName(),
                    issue.getAuthor().getAvatar_url(), issue.getAuthor().getWeb_url());
            if (issue.getAssignee()!=null){
               assignee = new User(issue.getAssignee().getId(), issue.getAssignee().getUsername(),
                        issue.getAssignee().getName(), issue.getAssignee().getAvatar_url(), issue.getAssignee().getWeb_url());
            }

            List<Comment> comment = Arrays.asList(responseComment.getBody());
            Issue issueModel = new Issue(issue.getId(),issue.getRef_id(), issue.getTitle(), issue.getDescription(),
                    issue.getState(), issue.getCreated_at(), issue.getUpdated_at(), issue.getClosed_at(),
                    issue.getLabels(), author, assignee, issue.getUpvotes(), issue.getDownvotes(),
                    issue.getWeb_url(), comment);
            issues.add(issueModel);
        }
        project.setCommits(commits);
        project.setIssues(issues);
        String uriGitMiner = "http://localhost:8080/gitminer/projects";
        restTemplate.postForObject(uriGitMiner, project, Project.class);
        return project;
    }
}
