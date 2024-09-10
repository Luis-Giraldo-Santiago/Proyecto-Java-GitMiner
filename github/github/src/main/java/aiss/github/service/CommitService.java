package aiss.github.service;

import aiss.github.models.commit.Commits;
import aiss.github.models.commit.FindCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    public List<Commits> findAllCommitProject(String full_name){
        String uri = "https://api.github.com/repos/"+full_name+"/commits";
        ResponseEntity<FindCommit[]> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                FindCommit[].class);
        List<FindCommit> findCommits = Arrays.asList(response.getBody());
        List<Commits> commits = new ArrayList<>();
        for(FindCommit findCommit: findCommits){
            Commits commit = null;
            /*Commit commit = new Commit(findCommit.getSha(), "",
                    findCommit.getCommits().getMessage(),
                    findCommit.getCommits().getAuthor().getName(),
                    findCommit.getCommits().getAuthor().getEmail(),
                    findCommit.getCommits().getAuthor().getDate(),
                    findCommit.getCommits().getCommitter().getName(),
                    findCommit.getCommits().getCommitter().getEmail(),
                    findCommit.getCommits().getCommitter().getDate(),
                    findCommit.getHtmlUrl());*/
            commit.setId(findCommit.getSha());
            commit.setMessage(findCommit.getCommits().getMessage());
            commit.setAuthors_name(findCommit.getCommits().getAuthor().getName());
            commit.setAuthor_email(findCommit.getCommits().getAuthor().getEmail());
            commit.setAuthored_date(findCommit.getCommits().getAuthor().getDate());
            commit.setCommitter_name(findCommit.getCommits().getCommitter().getName());
            commit.setCommitter_email(findCommit.getCommits().getCommitter().getEmail());
            commit.setCommitted_date(findCommit.getCommits().getCommitter().getDate());
            commit.setWeb_url(findCommit.getHtmlUrl());
            commits.add(commit);
        }
        return commits.stream().limit(5).toList();
    }
}
