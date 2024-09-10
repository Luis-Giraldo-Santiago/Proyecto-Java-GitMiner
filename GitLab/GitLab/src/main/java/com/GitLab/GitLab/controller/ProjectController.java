package com.GitLab.GitLab.controller;

import com.GitLab.GitLab.model.project.Project;
import com.GitLab.GitLab.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Tag(name = "Project", description = "Project management API of GitLab")
@RestController
@RequestMapping("/gitlab")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService projectService){
        this.service = projectService;
    }

    // GET http://localhost:8081/gitlab/{id}[?sinceCommit={numero1}&sinceIssues={numero2}&max_Pages={numero3}]
    @Operation(
            summary = "Retrieve a project by Id",
            description = "Get a project object by specifying itd Id",
            tags = {"projects", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Características del proyecto filtrado ",
                        content = {@Content(schema = @Schema(implementation = Project.class),
                                mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Proyecto no encontrado, revise las credenciales del mismo" +
                            " y asegurese de que sean correctas",
            content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Project findProject(@Parameter(description = "Id del proyecto") @PathVariable String id,
                               @Parameter(description = "Número de Commits enviados en los últimos X días")
                               @RequestParam(required = false) Integer sinceCommit,
                               @Parameter(description = "Número de issues actualizados en los últimos X días")
                               @RequestParam(required = false) Integer sinceIssues,
                               @Parameter(description = "Número de páginas máximas del proyecto que vamos a filtrar")
                               @RequestParam(required = false) Integer max_Pages){
        return service.findProyect(id,sinceCommit,sinceIssues,max_Pages);
    }

    Pageable paging;



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
    @PostMapping("/{id}")
    public Project createdProject(@Parameter(description = "Id del proyecto") @PathVariable String id,
                                  @Parameter(description = "Número de Commits enviados en los últimos" +
                                          "X días")

                                  @RequestParam(required = false) Integer sinceCommit,
                                  @Parameter(description = "Número de issues actualizados en los últimos X días")

                                      @RequestParam(required = false) Integer sinceIssues,
                                  @Parameter(description = "Número máximo de páginas que queremos que tenga nuestro" +
                                          "proyecto")
                                      @RequestParam(required = false) Integer max_Pages){
        return service.createdProject(id,sinceCommit,sinceIssues,max_Pages);
    }


}
