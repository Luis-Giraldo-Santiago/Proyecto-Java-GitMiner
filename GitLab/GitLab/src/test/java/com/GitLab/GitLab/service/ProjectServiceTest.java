package com.GitLab.GitLab.service;

import com.GitLab.GitLab.model.project.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("getProject")
    void getProject() {
        Project project = service.findProyect("4207231",2,3,4);
        System.out.println(project);

    }


}