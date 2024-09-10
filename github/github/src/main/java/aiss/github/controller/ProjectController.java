package aiss.github.controller;

import aiss.github.models.project.Project;
import aiss.github.models.project.ProjectModel;
import aiss.github.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.TableGenerator;
import javax.websocket.server.PathParam;



@Tag(name = "Project", description = "Project management GitMiner API")
@RestController
@RequestMapping("/github")
public class ProjectController {


    private final ProjectService service;

    public ProjectController(ProjectService projectRepository){
        this.service = projectRepository;
    }


    // GET http://localhost:8082/github/{owner}/{repoName}[?sinceCommit={numero1}&sinceIssues={numero2}&max_Pages={numero3}]
    @Operation(
            summary = "Retrieve a Project by function´s parameters",
            description = "Get a Project Object by specifying its id",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Características del proyecto filtrado ",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Proyecto no encontrado, revise las credenciales del mismo" +
                            " y asegurese de que sean correctas",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{owner}/{repoName}")
    public ProjectModel findOneProject(@Parameter(description = "Autor del proyecto") @PathVariable String owner,
                                       @Parameter(description = "Nombre del repositorio")
                                       @PathVariable String repoName,
                                       @Parameter(description = "Número de commits enviados en los últimos X días")
                                       @RequestParam(required = false) Integer sinceCommit,
                                       @Parameter(description = "Número de issues actualizadas en los últimos X días")
                                       @RequestParam(required = false) Integer sinceIssues,
                                       @Parameter(description = "Número máximo de páginas del proyecto")
                                       @RequestParam(required = false) Integer max_Pages){
        return service.findOneProject(owner, repoName, sinceCommit, sinceIssues, max_Pages);
    }



    @Operation(
            summary = "Insert a project",
            description = "Add a new project whose data is parsed in the body of the request in JSON format",
            tags = {"projects", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "El proyecto ha sido creado con éxito",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible crear el proyecto, por favor, revise las credenciales, es muy" +
                            "probable que usted se haya equivocado al realizar el requerimiento",
                    content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}/{repoName}")
    public ProjectModel createdProject(@Parameter(description = "Autor del proyecto") @PathVariable String owner,
                                       @Parameter(description = "Nombre del repositorio")
                                       @PathVariable String repoName,
                                       @Parameter(description = "Número de commits enviados en los últimos X días")
                                           @RequestParam(required = false) Integer sinceCommit,
                                       @Parameter(description = "Número de issues actualizadas en los últimos X días")
                                           @RequestParam(required = false) Integer sinceIssues,
                                       @Parameter(description = "Número máximo de páginas del proyecto")
                                           @RequestParam(required = false) Integer max_Pages){
        ProjectModel project = service.createProject(owner, repoName, sinceCommit, sinceIssues, max_Pages);
        return project;
    }





}
