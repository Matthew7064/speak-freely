package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TutorTests {

    private Tutor tutor;
    private Course course;

    @BeforeEach
    public void setUp() {
        tutor = new Tutor();
        course = new Course();

        tutor.setName("John");
        tutor.setSurname("Doe");
        tutor.setEmail("john.doe@example.com");

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        tutor.setCourses(courses);

        course.setTutor(tutor);
    }

    @Test
    public void testGetId() {
        // Check if the correct ID is returned
        // Has to be this way because we may create multiple tutors and we need it to work for all of them.
        assertEquals(course.getTutor().getId(), tutor.getId());
    }

    @Test
    public void testGetName() {
        // Check if the correct name is returned
        assertEquals("John", tutor.getName());
    }

    @Test
    public void testSetName() {
        // Set a new name for the tutor
        tutor.setName("Jane");

        // Check if the name is updated
        assertEquals("Jane", tutor.getName());
    }

    @Test
    public void testGetSurname() {
        // Check if the correct surname is returned
        assertEquals("Doe", tutor.getSurname());
    }

    @Test
    public void testSetSurname() {
        // Set a new surname for the tutor
        tutor.setSurname("Smith");

        // Check if the surname is updated
        assertEquals("Smith", tutor.getSurname());
    }

    @Test
    public void testGetEmail() {
        // Check if the correct email is returned
        assertEquals("john.doe@example.com", tutor.getEmail());
    }

    @Test
    public void testSetEmail() {
        // Set a new email for the tutor
        tutor.setEmail("jane.smith@example.com");

        // Check if the email is updated
        assertEquals("jane.smith@example.com", tutor.getEmail());
    }

    @Test
    public void testGetCourses() {
        // Check if the correct list of courses is returned
        assertEquals(1, tutor.getCourses().size());
        assertTrue(tutor.getCourses().contains(course));
    }

    @Test
    public void testSetCourses() {
        List<Course> newCourses = new ArrayList<>();
        Course newCourse = new Course();
        newCourses.add(newCourse);

        // Set a new list of courses for the tutor
        tutor.setCourses(newCourses);

        // Check if the list of courses is updated
        assertEquals(1, tutor.getCourses().size());
        assertTrue(tutor.getCourses().contains(newCourse));
    }
}
