package com.ticarumproject.ticarum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticarumproject.ticarum.dto.CreateCompetitionRequest;
import com.ticarumproject.ticarum.dto.CreateTeamRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicarumApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void flujoCompleto_competicion_equipos_y_generacion() throws Exception {
        // Crear competición
        CreateCompetitionRequest req = new CreateCompetitionRequest();
        req.name = "Prueba";
        req.sport = "Futbol";
        req.startDate = LocalDate.now();
        req.endDate = LocalDate.now().plusDays(10);
        req.pistas = 2;
        req.reservedDays = Set.of(req.startDate, req.startDate.plusDays(1));

        String body = objectMapper.writeValueAsString(req);

        String location = mockMvc.perform(post("/api/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/competitions/")))
                .andReturn().getResponse().getHeader("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);

        // Registrar equipos
        for (int i = 1; i <= 5; i++) {
            CreateTeamRequest tr = new CreateTeamRequest();
            tr.name = "Equipo-" + i;
            mockMvc.perform(post("/api/competitions/" + id + "/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tr)))
                    .andExpect(status().isCreated());
        }

        // Generar primera jornada
        mockMvc.perform(post("/api/competitions/" + id + "/firstday/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedMatches", notNullValue()));

        // Consultar partidos
        mockMvc.perform(get("/api/competitions/" + id + "/firstday/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));

        // Consultar no asignados
        mockMvc.perform(get("/api/competitions/" + id + "/firstday/unassigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }
}
