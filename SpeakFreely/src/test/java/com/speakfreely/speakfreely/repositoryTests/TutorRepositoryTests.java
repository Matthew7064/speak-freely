package com.speakfreely.speakfreely.repositoryTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TutorRepositoryTests {
/*
    @Autowired
    private TutorRepository tutorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Tutor tutor1;
    private Tutor tutor2;
    private Course course;

    @BeforeEach
    public void setUp() {
        // Create and save sample tutors
        tutor1 = new Tutor();
        tutor1.setName("John");
        tutor1.setSurname("Doe");
        tutor1.setEmail("john.doe@example.com");
        entityManager.persist(tutor1);

        tutor2 = new Tutor();
        tutor2.setName("Jane");
        tutor2.setSurname("Smith");
        tutor2.setEmail("jane.smith@example.com");
        entityManager.persist(tutor2);

        // Create and save a course
        course = new Course();
        course.setName("Course 1");
        course.setTutor(tutor1);
        entityManager.persist(course);

        // Associate courses with tutors
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        tutor1.setCourses(courses);
        entityManager.persist(tutor1);
    }

    @Test
    public void testFindById() {
        // Find an existing tutor by ID
        Optional<Tutor> foundTutor = tutorRepository.findById(tutor1.getId());

        // Check if the tutor is found and has the correct name
        assertTrue(foundTutor.isPresent());
        assertEquals(tutor1.getName(), foundTutor.get().getName());
    }

    @Test
    public void testFindByEmail() {
        // Find a tutor by email using custom query
        Query query = entityManager.createQuery("SELECT t FROM Tutor t WHERE t.email = :email");
        query.setParameter("email", "john.doe@example.com");
        Tutor foundTutor = (Tutor) query.getSingleResult();

        // Check if the tutor is found and has the correct name
        assertNotNull(foundTutor);
        assertEquals(tutor1.getName(), foundTutor.getName());
    }

    @Test
    public void testSave() {
        // Create a new tutor
        Tutor newTutor = new Tutor();
        newTutor.setName("Alice");
        newTutor.setSurname("Johnson");
        newTutor.setEmail("alice.johnson@example.com");

        // Save the new tutor
        tutorRepository.save(newTutor);

        // Check if the tutor is saved with a generated ID
        assertNotNull(newTutor.getId());

        // Verify that the tutor can be found by ID
        Optional<Tutor> foundTutor = tutorRepository.findById(newTutor.getId());
        assertTrue(foundTutor.isPresent());
        assertEquals(newTutor.getName(), foundTutor.get().getName());
    }

    @Test
    public void testDelete() {
        // Delete a tutor
        tutorRepository.delete(tutor1);

        // Verify that the tutor is no longer found
        Optional<Tutor> foundTutor = tutorRepository.findById(tutor1.getId());
        assertFalse(foundTutor.isPresent());
    }

 */
}
