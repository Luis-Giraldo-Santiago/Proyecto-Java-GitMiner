package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
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

@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IssueRepository issueRepository;


    @Operation(
            summary = "Retrieve a issues list",
            description = "Get a list of issues ",
            tags = {"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista con todos las issues realizadas",
                    content = {@Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado issue alguna",
                    content = {@Content(schema = @Schema())})
    })
    // GET http://localhost:8080/gitminer/issues
    @GetMapping
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    // GET http://localhost:8080/api/projects/{projectId}/issues
    /*@GetMapping("/projects/{projectId}/issues")
    public List<Issue> getAllIssuesByProjectId(@PathVariable(value="projectId") String projectId)
            throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (!project.isPresent())
            throw new ProjectNotFoundException();
        return project.get().getIssues();
    }*/


    @Operation(
            summary = "Retrieve a issue",
            description = "Get a issue",
            tags = {"issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue realizada cuyo Id coincida con el Id"
                    + " pasado por parámetro",
                    content = {@Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado issue alguna que tenga el Id pasado por parámetro"
                    ,content = {@Content(schema = @Schema())})})
    // GET http://localhost:8080/gitminer/issues/{id}
    @GetMapping("/{id}")
    public Issue findById(@Parameter(description = "Id de la issue que queremos obtener")
                              @PathVariable(value="id") String id) throws CommitNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent())
            throw new CommitNotFoundException();
        return issue.get();
    }

    @Operation(
            summary = "Retrieve comments list",
            description = "Get list of comments",
            tags = {"comments", "issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista con los comentarios realizados en la Issue" +
                    "   obtenida por el Id pasado por parámetro",
                    content = {@Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado issue alguna que tenga el Id pasado por parámetro y por " +
                            "tanto no es posible obtener los comentarios de la misma"
                    ,content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}/comments")
    public List<Comment> getIssueComments(@Parameter(description = "Id de la Issue que queremos obtener")
                                              @PathVariable(value="id") String issueId) throws ProjectNotFoundException {
        Optional<Issue> issue = issueRepository.findById(issueId);
        if (!issue.isPresent())
            throw new ProjectNotFoundException();
        return issue.get().getComments();
    }

    @Operation(
            summary = "Insert a Issue",
            description = "Add a new issue whose data is parsed in the body of the request in JSON format",
            tags = {"issue", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "La issue ha sido creada (publicada) con éxito",
                    content = {@Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible publicar la issue, por favor, revise las credenciales, es muy" +
                            "probable que usted se haya equivocado al realizar el requerimiento",
                    content = {@Content(schema = @Schema())})})

    // POST http://localhost:8080/gitminer/projects/{projectId}/issues
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/projects/{projectId}/issues")
    public Issue createIssue(@Parameter(description = "Issue que queremos crear") @RequestBody @Valid Issue issue,
                             @Parameter(description = "Id del proyecto sobre el que queremos crear la Issue")
                             @PathVariable("projectId") String projectId) {
        Issue newIssue = issueRepository.save(new Issue(
                issue.getId(),
                issue.getRefId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getState(),
                issue.getCreatedAt(),
                null,
                null,
                issue.getLabels(),
                issue.getAuthor(),
                issue.getAssignee(),
                issue.getUpvotes(),
                issue.getDownvotes(),
                issue.getWebUrl(),
                issue.getComments()));
        projectRepository.findById(projectId).get().getIssues().add(newIssue);
        return newIssue;
    }

    @Operation(
            summary = "Delete a issue",
            description = "Delete a issue whose Id is equal to parameter´s Id",
            tags = {"issue", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "La issue ha sido eliminada con éxito",
                    content = {@Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible eliminar la issue, por favor, asegurese de que " +
                            "el Id pasado por parámetro sea el correcto",
                    content = {@Content(schema = @Schema())})})

    // DELETE http://localhost:8080/gitminer/commits/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/issues/{id}")
    public void deleteIssue(@Parameter(description = "Id de la issue que queremos borrar") @PathVariable String id) {
        if (issueRepository.existsById(id))
            issueRepository.deleteById(id);
    }

}


