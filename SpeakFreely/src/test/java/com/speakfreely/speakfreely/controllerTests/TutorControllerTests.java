package com.speakfreely.speakfreely.controllerTests;

import com.speakfreely.speakfreely.controller.TutorController;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TutorController.class)
@AutoConfigureMockMvc
public class TutorControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private TutorRepository tutorRepository;

    @Test
    public void testFindAllTutors() throws Exception {
        Tutor tutor1 = new Tutor();
        tutor1.setName("John");
        tutor1.setSurname("Doe");
        tutor1.setEmail("john.doe@example.com");

        Tutor tutor2 = new Tutor();
        tutor2.setName("Jane");
        tutor2.setSurname("Smith");
        tutor2.setEmail("jane.smith@example.com");

        List<Tutor> tutors = Arrays.asList(tutor1, tutor2);

        when(tutorRepository.findAll()).thenReturn(tutors);

        mockMvc.perform(MockMvcRequestBuilders.get("/tutors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].surname", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].name", is("Jane")))
                .andExpect(jsonPath("$[1].surname", is("Smith")))
                .andExpect(jsonPath("$[1].email", is("jane.smith@example.com")));
    }

    @Test
    public void testFindTutor() throws Exception {
        Tutor tutor = new Tutor();
        tutor.setName("John");
        tutor.setSurname("Doe");
        tutor.setEmail("john.doe@example.com");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        mockMvc.perform(MockMvcRequestBuilders.get("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testFindTutor_NonExistingTutor() throws Exception {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTutor() throws Exception {
        Tutor tutor = new Tutor();
        tutor.setName("John");
        tutor.setSurname("Doe");
        tutor.setEmail("john.doe@example.com");

        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);

        String tutorJson = "{\"name\":\"John\",\"surname\":\"Doe\",\"email\":\"john.doe@example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/tutors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tutorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testUpdateTutor() throws Exception {
        Tutor tutor = new Tutor();
        tutor.setName("John");
        tutor.setSurname("Doe");
        tutor.setEmail("john.doe@example.com");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);

        String tutorJson = "{\"name\":\"John\",\"surname\":\"Doe\",\"email\":\"john.doe@example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tutorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testUpdateTutor_NonExistingTutor() throws Exception {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        String tutorJson = "{\"name\":\"John\",\"surname\":\"Doe\",\"email\":\"john.doe@example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tutorJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTutor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTutor_NonExistingTutor() throws Exception {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tutors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
