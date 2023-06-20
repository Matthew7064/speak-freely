package com.speakfreely.speakfreely.repositoryTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CourseRepositoryTests {
/*
    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Course course1;
    private Course course2;

    @BeforeEach
    public void setUp() {
        // Create and save sample courses
        course1 = new Course();
        course1.setName("Course 1");
        entityManager.persist(course1);

        course2 = new Course();
        course2.setName("Course 2");
        entityManager.persist(course2);
    }

    @Test
    public void testFindById() {
        // Find an existing course by ID
        Optional<Course> foundCourse = courseRepository.findById(course1.getId());

        // Check if the course is found and has the correct name
        assertTrue(foundCourse.isPresent());
        assertEquals(course1.getName(), foundCourse.get().getName());
    }

    @Test
    public void testFindAll() {
        // Find all courses
        List<Course> allCourses = courseRepository.findAll();

        // Check if all saved courses are returned
        assertEquals(2, allCourses.size());
        assertTrue(allCourses.contains(course1));
        assertTrue(allCourses.contains(course2));
    }

    @Test
    public void testSave() {
        // Create a new course
        Course newCourse = new Course();
        newCourse.setName("New Course");

        // Save the new course
        Course savedCourse = courseRepository.save(newCourse);

        // Check if the new course is saved and has a generated ID
        assertNotNull(savedCourse.getId());

        // Check if the new course can be found by ID
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent());
        assertEquals(newCourse.getName(), foundCourse.get().getName());
    }

    @Test
    public void testDelete() {
        // Delete a course
        courseRepository.delete(course1);

        // Check if the deleted course is no longer found by ID
        Optional<Course> foundCourse = courseRepository.findById(course1.getId());
        assertFalse(foundCourse.isPresent());
    }

 */
}
