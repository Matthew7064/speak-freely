package com.speakfreely.speakfreely.controllerTests;

import com.speakfreely.speakfreely.controller.ParticipantController;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipantController.class)
public class ParticipantControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private ParticipantRepository participantRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllParticipants() throws Exception {
        Participant participant1 = new Participant();
        participant1.setName("John");
        participant1.setSurname("Doe");
        participant1.setEmail("john.doe@example.com");

        Participant participant2 = new Participant();
        participant2.setName("Jane");
        participant2.setSurname("Smith");
        participant2.setEmail("jane.smith@example.com");

        List<Participant> participants = Arrays.asList(participant1, participant2);

        given(participantRepository.findAll()).willReturn(participants);

        mockMvc.perform(get("/participants"))
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
    public void testFindParticipantById() throws Exception {
        Participant participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");

        given(participantRepository.findById(1L)).willReturn(Optional.of(participant));

        mockMvc.perform(get("/participants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testCreateParticipant() throws Exception {
        Participant participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");

        given(participantRepository.save(any(Participant.class))).willReturn(participant);

        String requestBody = "{\"name\":\"John\",\"surname\":\"Doe\",\"email\":\"john.doe@example.com\"}";

        mockMvc.perform(post("/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testUpdateParticipant() throws Exception {
        Participant participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");

        given(participantRepository.findById(1L)).willReturn(Optional.of(participant));
        given(participantRepository.save(any(Participant.class))).willReturn(participant);

        String requestBody = "{\"name\":\"John\",\"surname\":\"Doe\",\"email\":\"john.doe@example.com\"}";

        mockMvc.perform(put("/participants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testDeleteParticipant() throws Exception {
        Participant participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");

        given(participantRepository.findById(1L)).willReturn(Optional.of(participant));
        doNothing().when(participantRepository).delete(participant);

        mockMvc.perform(delete("/participants/1"))
                .andExpect(status().isOk());

        verify(participantRepository).delete(participant);
    }
}
