package aiss.github.service;

import aiss.github.models.commet.Comment;
import aiss.github.models.issue.Issue;
import aiss.github.models.issue.Label;
import aiss.github.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    public List<Issue> findAllIssue(String fullName){
        String uri = "https://api.github.com/repos/"+fullName+"/issues?per_page=2";
        ResponseEntity<Issue[]> response = restTemplate.exchange(uri, HttpMethod.GET, null, Issue[].class);
        List<Issue> issues = Arrays.asList(response.getBody());
        for (Issue issue: issues){
            issue.setUpVotes(issue.getReactions().getUpVotes());
            issue.setDownVotes(issue.getReactions().getDownVotes());
            Integer id = issue.getAuthor__1().getId();
            String uriUser = "https://api.github.com/user/"+id;
            ResponseEntity<User> responseUser = restTemplate.exchange(uriUser, HttpMethod.GET, null, User.class);
            issue.setUsuario(responseUser.getBody());
            if (issue.getAssignee() != null){
                Integer idAssig = issue.getAssignee().getId();
                String uriAssig = "https://api.github.com/user/"+idAssig;
                ResponseEntity<User> responseAssig = restTemplate.exchange(uriAssig, HttpMethod.GET, null, User.class);
                issue.setAsignado(responseUser.getBody());
            }
            String number = issue.getRef_id();
            String uriComment = "https://api.github.com/repos/"+fullName+"/issues/"+number+"/comments";
            ResponseEntity<Comment[]> responseCommit = restTemplate.exchange(uriComment, HttpMethod.GET, null,
                    Comment[].class);
            issue.setComentarios((Arrays.asList(responseCommit.getBody())));
            issue.setEtiqueta(issue.getLabels().stream().map(Label::getName).toList());
        }
        return issues;
    }
}
