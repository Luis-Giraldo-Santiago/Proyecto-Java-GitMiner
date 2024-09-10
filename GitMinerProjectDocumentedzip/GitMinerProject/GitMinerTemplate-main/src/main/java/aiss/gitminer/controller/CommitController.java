package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommitRepository;
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

@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CommitRepository commitRepository;


    @Operation(
            summary = "Retrieve a commits list",
            description = "Get a list of commits ",
            tags = {"commits", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista con todos los commits realizados",
                    content = {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado commit alguno",
                    content = {@Content(schema = @Schema())})
    })
    // GET http://localhost:8080/gitminer/commits
    @GetMapping
    public List<Commit> getAllCommits() {
        return commitRepository.findAll();
    }

    // GET http://localhost:8080/api/projects/{projectId}/commits
    /* @GetMapping("/projects/{projectId}/commits")
    public List<Commit> getAllCommitsByProjectId(@PathVariable(value="projectId") String projectId)
            throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (!project.isPresent())
            throw new ProjectNotFoundException();
        return project.get().getCommits();
    }*/

    @Operation(
            summary = "Retrieve a commit",
            description = "Get a commit",
            tags = {"commit", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit realizado cuyo Id coincida con el Id"
                    + " pasado por parámetro",
                    content = {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado commit alguno que tenga el Id pasado por parámetro"
                    ,content = {@Content(schema = @Schema())})})
    // GET http://localhost:8080/api/commits/{id}
    @GetMapping("/{id}")
    public Commit findById(@Parameter(description = "Id del commit realizado")
                               @PathVariable(value="id") String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if (!commit.isPresent())
            throw new CommitNotFoundException();
        return commit.get();
    }

    @Operation(
            summary = "Insert a commit",
            description = "Add a new commit whose data is parsed in the body of the request in JSON format",
            tags = {"commit", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "El commit ha sido creado (publicado) con éxito",
                    content = {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible publicar el commit, por favor, revise las credenciales, es muy" +
                            "probable que usted se haya equivocado al realizar el requerimiento",
                    content = {@Content(schema = @Schema())})})
    // POST http://localhost:8080/api/projects/{projectId}/commits
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/projects/{projectId}/commits")
    public Commit createCommit(@Parameter(description = "Commit que queremos crear") @RequestBody @Valid Commit commit,
                               @Parameter(description = "id del proyecto sobre el que se hace el commit")
                               @PathVariable("projectId") String projectId) {
        Commit newCommit = commitRepository.save(new Commit(
                commit.getId(),
                commit.getTitle(),
                commit.getMessage(),
                commit.getAuthorName(),
                commit.getAuthorEmail(),
                commit.getAuthoredDate(),
                commit.getCommitterName(),
                commit.getCommitterEmail(),
                commit.getCommittedDate(),
                commit.getWebUrl()));
        projectRepository.findById(projectId).get().getCommits().add(newCommit);
        return newCommit;
    }


    @Operation(
            summary = "Delete a commit",
            description = "Delete a commit whose Id is equal to parameter´s Id",
            tags = {"commit", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "El commit ha sido eliminado con éxito",
                    content = {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible eliminar el commit, por favor, asegurese de que " +
                            "el Id pasado por parámetro sea el correcto",
                    content = {@Content(schema = @Schema())})})
    // DELETE http://localhost:8080/api/commits/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/commits/{id}")
    public void deleteCommit(@Parameter(description = "Id del commit perteneciente " +
            "al proyecto el cual queremos borrar") @PathVariable String id) {
        if (commitRepository.existsById(id))
            commitRepository.deleteById(id);
    }

}

