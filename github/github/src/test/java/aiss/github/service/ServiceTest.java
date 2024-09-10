package aiss.github.service;

import aiss.github.models.commet.Comment;
import aiss.github.models.issue.Issue;
import aiss.github.models.project.Project;
import aiss.github.models.project.ProjectModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServiceTest {

    @Autowired
    ProjectService projectService;
    @Autowired
    CommitService commitService;

    @Autowired
    CommentService commentService;

    @Autowired
    IssueService issueService;

    @Test
    @DisplayName("Get One Project")
    void findOneProject(){
        ProjectModel project = projectService.findOneProject("spring-projects", "spring-framework",3,4,5);
        System.out.println(project);
    }

    @Test
    @DisplayName("Get Comments Issue")
    void findAllCommentIssue(){
        List<Comment> comments = commentService.findAllCommentIssue("spring-projects/spring-framework","30380");
        System.out.println(comments);
    }

    @Test
    @DisplayName("Get Issue Project")
    void findAllIssue(){
        List<Issue> issues = issueService.findAllIssue("spring-projects/spring-framework");
        System.out.println(issues);
    }


}