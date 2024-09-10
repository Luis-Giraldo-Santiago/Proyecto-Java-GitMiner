package aiss.gitminer.controller;

import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
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

import javax.management.DescriptorKey;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;


    @Operation(
            summary = "Retrieve a project list",
            description = "Get a list of projects ",
            tags = {"projects", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista con todos los proyectos realizados",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado proyecto alguno",
                    content = {@Content(schema = @Schema())})
    })
    // GET http://localhost:8080/gitminer/projects
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }


    @Operation(
            summary = "Retrieve a project",
            description = "Get a project",
            tags = {"project", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto realizado cuyo Id coincide con el Id"
                    + " pasado por parámetro",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario no ha realizado proyecto alguno que tenga el Id pasado por parámetro"
                    ,content = {@Content(schema = @Schema())})})
    // GET http://localhost:8080/gitminer/projects/{id}
    @GetMapping("/{id}")
    public Project findById(@Parameter(description = "Id del proyecto que queremos filtrar") @PathVariable String id) {
        return projectRepository.findById(id).get();
    }



    @Operation(
            summary = "Insert a project",
            description = "Add a new project whose data is parsed in the body of the request in JSON format",
            tags = {"project", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "El proyecto ha sido creado (publicado) con éxito",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible publicar el proyecto, por favor, revise las credenciales, es muy" +
                            "probable que usted se haya equivocado al realizar el requerimiento",
                    content = {@Content(schema = @Schema())})})
    // POST http://localhost:8080/gitminer/projects
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createProject(@Parameter(description = "Proyecto que queremos crear")
                                     @RequestBody @Valid Project project) {
        Project newProject = projectRepository.save(new Project(
                project.getId(),
                project.getName(),
                project.getWebUrl(),
                project.getCommits(),
                project.getIssues()));
        return newProject;
    }

    @Operation(
            summary = "Update a project",
            description = "Update a  project whose data is parsed in the body of the request in JSON format",
            tags = {"project", "put"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "El proyecto ha sido actualizado con éxito",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible actualizar el proyecto, por favor, asegurese de que " +
                            "el Id pasado por parámetro sea el correcto",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})})



    // PUT http://localhost:8080/gitminer/projects/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateProject(@Parameter(description = "Proyecto resultante")
                                  @RequestBody @Valid Project updatedProject,
                              @Parameter(description = "Id del proyecto que queremos actualizar")
                              @PathVariable String id) {
        Project project = projectRepository.findById(id).get();
        project.setName(updatedProject.getName());
        project.setWebUrl(updatedProject.getWebUrl());
        projectRepository.save(project);
    }



    @Operation(
            summary = "Delete a project",
            description = "Delete a project whose Id is equal to parameter´s Id",
            tags = {"project", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "El proyecto ha sido eliminado con éxito",
                    content = {@Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "No ha sido posible eliminar el proyecto, por favor, asegurese de que " +
                            "el Id pasado por parámetro sea el correcto",
                    content = {@Content(schema = @Schema())})})
    // DELETE http://localhost:8080/gitminer/projects/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProject(@Parameter(description = "Id del proyecto que queremos borrar")
                                  @PathVariable String id) {
        if (projectRepository.existsById(id))
            projectRepository.deleteById(id);
    }

}

