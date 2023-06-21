package com.speakfreely.speakfreely.controllerTests;


import com.speakfreely.speakfreely.controller.CourseController;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.GradeRepository;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseControllerTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private CourseController courseController;

    private Course course;
    private Participant participant;
    private Tutor tutor;
    private Grade grade;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setName("English");
        course.setDescription("Learn English language");
        participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        tutor = new Tutor();
        tutor.setName("Jane");
        tutor.setSurname("Smith");
        grade = new Grade();
        grade.setGrade(5);
        grade.setCourse(course);
        grade.setParticipant(participant);

        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(participantRepository.findById(2L)).thenReturn(Optional.empty());
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(tutorRepository.findById(2L)).thenReturn(Optional.empty());
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        when(gradeRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void findAllCourses_shouldReturnListOfCourses() {
        List<Course> result = courseController.findAllCourses();
        assertEquals(Collections.singletonList(course), result);
    }

    @Test
    void findCourse_withValidId_shouldReturnCourse() {
        Optional<Course> result = courseController.findCourse(1L);
        assertTrue(result.isPresent());
        assertEquals(course, result.get());
    }

    @Test
    void findCourse_withInvalidId_shouldReturnEmptyOptional() {
        Optional<Course> result = courseController.findCourse(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void addCourse_withValidCourse_shouldReturnCreatedResponse() {
        ResponseEntity<Course> response = courseController.addCourse(course);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    void deleteCourse_withValidId_shouldReturnNoContentResponse() {
        ResponseEntity<Course> response = courseController.deleteCourse(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
//        verify(gradeRepository).deleteById(grade.getId());
//        verify(courseRepository).deleteById(1L);
    }

}