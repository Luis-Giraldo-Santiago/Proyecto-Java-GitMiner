package aiss.github.service;

import aiss.github.models.commet.Comment;
import aiss.github.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    public List<Comment> findAllCommentIssue(String full_name, String number){
        String uri = "https://api.github.com/repos/"+full_name+"/issues/"+number+"/comments";
        ResponseEntity<Comment[]> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                Comment[].class);
        List<Comment> comments = Arrays.asList(response.getBody());
        for(Comment comment: comments){
            Integer id = comment.getAuthor__1().getId();
            String uriUser = "https://api.github.com/user/"+id;
            ResponseEntity<User> responseUser = restTemplate.exchange(uriUser, HttpMethod.GET, null, User.class);
            comment.setUsuario(responseUser.getBody());
        }
        return comments;
    }
}
