package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTests {

    private Participant participant;
    private Grade grade;
    private Course course;

    @BeforeEach
    public void setUp() {
        participant = new Participant();
        grade = new Grade();
        course = new Course();

        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");
        participant.setPayment(true);

        List<Grade> grades = new ArrayList<>();
        grades.add(grade);
        participant.setGrades(grades);

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        participant.setCourses(courses);

        grade.setParticipant(participant);
        course.enrollParticipant(participant);
//        course.getEnrolledParticipants().add(participant);
    }

    @Test
    public void testGetId() {
        // Check if the correct ID is returned
        assertEquals(1L, participant.getId());
    }

    @Test
    public void testGetName() {
        // Check if the correct name is returned
        assertEquals("John", participant.getName());
    }

    @Test
    public void testSetName() {
        // Set a new name for the participant
        participant.setName("Jane");

        // Check if the name is updated
        assertEquals("Jane", participant.getName());
    }

    @Test
    public void testGetSurname() {
        // Check if the correct surname is returned
        assertEquals("Doe", participant.getSurname());
    }

    @Test
    public void testSetSurname() {
        // Set a new surname for the participant
        participant.setSurname("Smith");

        // Check if the surname is updated
        assertEquals("Smith", participant.getSurname());
    }

    @Test
    public void testGetEmail() {
        // Check if the correct email is returned
        assertEquals("john.doe@example.com", participant.getEmail());
    }

    @Test
    public void testSetEmail() {
        // Set a new email for the participant
        participant.setEmail("jane.smith@example.com");

        // Check if the email is updated
        assertEquals("jane.smith@example.com", participant.getEmail());
    }

    @Test
    public void testIsPayment() {
        // Check if the correct payment status is returned
        assertTrue(participant.isPayment());
    }

    @Test
    public void testSetPayment() {
        // Set a new payment status for the participant
        participant.setPayment(false);

        // Check if the payment status is updated
        assertFalse(participant.isPayment());
    }

    @Test
    public void testGetGrades() {
        // Check if the correct list of grades is returned
        assertEquals(1, participant.getGrades().size());
        assertTrue(participant.getGrades().contains(grade));
    }

    @Test
    public void testSetGrades() {
        List<Grade> newGrades = new ArrayList<>();
        Grade newGrade = new Grade();
        newGrades.add(newGrade);

        // Set a new list of grades for the participant
        participant.setGrades(newGrades);

        // Check if the list of grades is updated
        assertEquals(1, participant.getGrades().size());
        assertTrue(participant.getGrades().contains(newGrade));
    }

    @Test
    public void testGetCourses() {
        // Check if the correct list of courses is returned
        assertEquals(1, participant.getCourses().size());
        assertTrue(participant.getCourses().contains(course));
    }

    @Test
    public void testSetCourses() {
        List<Course> newCourses = new ArrayList<>();
        Course newCourse = new Course();
        newCourses.add(newCourse);

        // Set a new list of courses for the participant
        participant.setCourses(newCourses);

        // Check if the list of courses is updated
        assertEquals(1, participant.getCourses().size());
        assertTrue(participant.getCourses().contains(newCourse));
    }
}
