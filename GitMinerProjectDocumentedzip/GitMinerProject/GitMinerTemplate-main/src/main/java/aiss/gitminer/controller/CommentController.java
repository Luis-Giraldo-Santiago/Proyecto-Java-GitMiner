package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @Operation(
            summary = "Retrieve a comments list",
            description = "Get a list of comments",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista con todos los comentarios realizados",
                    content = {@Content(schema = @Schema(implementation = Comment.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado algún que otro comentario",
                    content = {@Content(schema = @Schema())})})
    // GET http://localhost:8080/gitminer/comments
    @GetMapping
    public List<Comment> findAllComments(){ return repository.findAll(); }


    @Operation(
            summary = "Retrieve a comment",
            description = "Get a comment whose Id is equal to parameter´s Id",
            tags = {"comment", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentario cuyo Id coincide con el Id del parámetro",
                    content = {@Content(schema = @Schema(implementation = Comment.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra ningún comentario con el Id pasado por parámetro",
                    content = {@Content(schema = @Schema())})})
    // GET http://localhost:8080/gitminer/comments/{id}
    @GetMapping("/{id}")
    public Comment findById(@Parameter(description = "Id del comentario que queremos obtener") @PathVariable String id){
        Optional<Comment> comment = repository.findById(id);
        return comment.get();
    }



    /*@ResponseStatus
    @PostMapping
    public Comment createComment(@Valid @RequestBody Comment comment, @PathVariable("projectId") String projectId){
        Comment newComment = repository.save(new Comment(
                comment.getId(),
                comment.getBody(),
                comment.getAuthor(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()));
        return projectRepository.findById(projectId).get();
    }*/

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment whose Id is equal to parameter´s Id",
            tags = {"comment", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "El comenatario ha sido eliminado con éxito",
                    content = {@Content(schema = @Schema(implementation = Comment.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible eliminar el comentario, por favor, asegurese de que " +
                            "el Id pasado por parámetro sea el correcto",
                    content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "Id del comentario que queremos borrar") @PathVariable String id){
        if(repository.existsById(id))
            repository.deleteById(id);
    }

}
