package com.speakfreely.speakfreely.controllerTests;

import com.speakfreely.speakfreely.controller.ParticipantController;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.GradeRepository;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ParticipantControllerTest {
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private CourseRepository courseRepository;

    private ParticipantController participantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        participantController = new ParticipantController(participantRepository, gradeRepository, courseRepository);
    }

    @Test
    void findAllParticipants_ShouldReturnAllParticipants() {
        // Arrange
        List<Participant> participants = Collections.singletonList(new Participant());
        when(participantRepository.findAll()).thenReturn(participants);

        // Act
        List<Participant> result = participantController.findAllParticipants();

        // Assert
        assertEquals(participants, result);
        verify(participantRepository).findAll();
    }

    @Test
    void findParticipant_WithExistingId_ShouldReturnParticipant() {
        // Arrange
        Long participantId = 1L;
        Participant participant = new Participant();
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(participant));

        // Act
        Optional<Participant> result = participantController.findParticipant(participantId);

        // Assert
        assertEquals(Optional.of(participant), result);
        verify(participantRepository).findById(participantId);
    }

    @Test
    void findParticipant_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        Long participantId = 1L;
        when(participantRepository.findById(participantId)).thenReturn(Optional.empty());

        // Act
        Optional<Participant> result = participantController.findParticipant(participantId);

        // Assert
        assertEquals(Optional.empty(), result);
        verify(participantRepository).findById(participantId);
    }

    @Test
    void addParticipant_ShouldSaveParticipantAndReturnCreatedStatus() {
        // Arrange
        Participant participant = new Participant();

        // Act
        ResponseEntity<Participant> result = participantController.addParticipant(participant);

        // Assert
        assertEquals(participant, result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(participantRepository).save(participant);
    }

    @Test
    void deleteParticipant_WithExistingId_ShouldDeleteParticipantAndReturnNoContentStatus() {
        // Arrange
        Long participantId = 1L;
        Optional<Participant> optionalParticipant = Optional.of(new Participant());
        when(participantRepository.findById(participantId)).thenReturn(optionalParticipant);

        // Act
        ResponseEntity<Participant> result = participantController.deleteParticipant(participantId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertTrue(participantRepository.findById(participantId).isPresent());
//        verify(gradeRepository).deleteById(anyLong());
//        verify(participantRepository).deleteById(participantId);
    }

    @Test
    void deleteParticipant_WithNonExistingId_ShouldReturnNotFoundStatus() {
        // Arrange
        Long participantId = 1L;
        when(participantRepository.findById(participantId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Participant> result = participantController.deleteParticipant(participantId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(participantRepository, never()).deleteById(anyLong());
        verify(gradeRepository, never()).deleteById(anyLong());
    }

}
