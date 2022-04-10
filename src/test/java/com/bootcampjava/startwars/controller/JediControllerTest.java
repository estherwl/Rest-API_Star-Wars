package com.bootcampjava.startwars.controller;

import com.bootcampjava.startwars.model.Jedi;
import com.bootcampjava.startwars.service.JediService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {
        // cenario
        Mockito.doReturn(Optional.of(mockJedi())).when(jediService).findById(1);

        // execucao
        mockMvc.perform(get("/jedi/{id}", 1))

                // asserts
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /jedi/1 - Not Found")
    public void testGetJediByIdNotFound() throws Exception {
        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}", 1))
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do POST com sucesso
    @Test
    @DisplayName("POST /jedi- SUCCESS")
    public void testCreateJediWithSuccess() throws Exception {
        Mockito.when(jediService.save(any(Jedi.class))).thenReturn(mockJedi());

        mockMvc.perform(post("/jedi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockJedi()))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    // TODO: Teste do PUT com sucesso
//    @Test
//    @DisplayName("PUT /jedi/update/1- SUCCESS")
//    public void testPutJediWithSuccess() throws Exception {
//        Jedi jediChanged = new Jedi("Luke Skywalker", 10);
//
//        Mockito.doReturn(Optional.of(jediChanged)).when(jediService).findById(1);
//        Mockito.doReturn(true).when(jediService).update(mockJedi());
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .put("/jedi/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jediChanged.toString())
//                        .accept(MediaType.APPLICATION_JSON))
//
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
//                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))
//
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Luke Skywalker")))
//                .andExpect(jsonPath("$.strength", is(10)))
//                .andExpect(jsonPath("$.version", is(1)));
//    }

    // TODO: Teste do PUT com uma versao igual da ja existente - deve retornar um conflito

    // TODO: Teste do PUT com erro - not found

    // TODO: Teste do delete com sucesso
    @Test
    @DisplayName("DELETE /jedi- SUCCESS")
    public void deleteJediWithSuccess() throws Exception {
        Mockito.doReturn(Optional.of(mockJedi())).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).delete(1);

        mockMvc.perform(delete("/jedi/{id}", 1))
                .andExpect(status().isOk());
    }

    // TODO: Teste do delete com erro - deletar um id ja deletado
    @Test
    @DisplayName("DELETE /jedi/1- NOT FOUND")
    public void deleteJediWithError() throws Exception {
        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(delete("/jedi/{id}", 1))
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do delete com erro  - internal server error
    @Test
    @DisplayName("DELETE /jedi/1- INTERNAL_SERVER_ERROR")
    public void deleteJediWithServerError() throws Exception {
        Mockito.doReturn(Optional.of(mockJedi())).when(jediService).findById(1);
        Mockito.doReturn(false).when(jediService).delete(1);

        mockMvc.perform(delete("/jedi/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Jedi mockJedi() {
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        return mockJedi;
    }

}
