package com.speakfreely.speakfreely.controllerTests;

import com.speakfreely.speakfreely.controller.TutorController;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class TutorControllerTests {

    @Test
    public void testFindAllTutors() {
        // Tworzenie mocka repozytorium
        TutorRepository tutorRepository = mock(TutorRepository.class);
        List<Tutor> tutors = new ArrayList<>();
        tutors.add(new Tutor());
        Mockito.when(tutorRepository.findAll()).thenReturn(tutors);

        // Inicjalizacja kontrolera
        TutorController tutorController = new TutorController(tutorRepository, null);

        // Wywołanie metody kontrolera
        List<Tutor> result = tutorController.findAllTutors();

        // Sprawdzenie wyniku
        assertEquals(1, result.size());
    }

    @Test
    public void testFindTutor() {
        // Tworzenie mocka repozytorium
        TutorRepository tutorRepository = mock(TutorRepository.class);
        Tutor tutor = new Tutor();
        Mockito.when(tutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));

        // Inicjalizacja kontrolera
        TutorController tutorController = new TutorController(tutorRepository, null);

        // Wywołanie metody kontrolera
        Optional<Tutor> result = tutorController.findTutor(1L);

        // Sprawdzenie wyniku
        assertTrue(result.isPresent());
        assertEquals(tutor, result.get());
    }

    @Test
    public void testAddTutor() {
        // Tworzenie mocka repozytorium
        TutorRepository tutorRepository = mock(TutorRepository.class);
        Tutor tutor = new Tutor();

        // Inicjalizacja kontrolera
        TutorController tutorController = new TutorController(tutorRepository, null);

        // Wywołanie metody kontrolera
        ResponseEntity<Tutor> response = tutorController.addTutor(tutor);

        // Sprawdzenie wyniku
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tutor, response.getBody());
    }

    @Test
    public void testDeleteTutor_WithExistingId_ShouldDeleteTutorAndReturnNoContentStatus() {
        // Tworzenie mocka repozytorium
        TutorRepository tutorRepository = mock(TutorRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        Tutor tutor = mock(Tutor.class); // Tworzenie atrapy dla obiektu Tutor

        // Konfiguracja zachowania atrap
        tutor.setId(1L);
        Mockito.when(tutorRepository.findById(anyLong())).thenReturn(Optional.of(tutor));
        Mockito.when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        Mockito.when(tutor.getCourses()).thenReturn(new ArrayList<>());

        // Inicjalizacja kontrolera
        TutorController tutorController = new TutorController(tutorRepository, courseRepository);

        // Wywołanie metody kontrolera
        ResponseEntity<Tutor> response = tutorController.deleteTutor(1L);

        // Sprawdzenie wyniku
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }



    @Test
    public void testDeleteTutor_WithNonExistingId_ShouldReturnNotFoundStatus() {
        // Tworzenie mocka repozytorium
        TutorRepository tutorRepository = mock(TutorRepository.class);
        TutorController tutorController = new TutorController(tutorRepository, null);

        // Wywołanie metody kontrolera
        ResponseEntity<Tutor> response = tutorController.deleteTutor(1L);

        // Sprawdzenie wyniku
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Dodaj pozostałe testy dla pozostałych metod w klasie TutorController...

}