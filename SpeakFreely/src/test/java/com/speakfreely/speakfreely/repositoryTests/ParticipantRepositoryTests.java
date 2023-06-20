package com.speakfreely.speakfreely.repositoryTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ParticipantRepositoryTests {

    @Autowired
    private ParticipantRepository participantRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Participant participant1;
    private Participant participant2;
    private Course course;

    @BeforeEach
    public void setUp() {
        // Create and save sample participants
        participant1 = new Participant();
        participant1.setName("John");
        participant1.setSurname("Doe");
        participant1.setEmail("john.doe@example.com");
        entityManager.persist(participant1);

        participant2 = new Participant();
        participant2.setName("Jane");
        participant2.setSurname("Smith");
        participant2.setEmail("jane.smith@example.com");
        entityManager.persist(participant2);

        // Create and save a course
        course = new Course();
        course.setName("Course 1");
        entityManager.persist(course);

        // Associate participants with the course
        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        course.setEnrolledParticipants(participants);
        entityManager.persist(course);

        // Create and save sample grades for participant1
        Grade grade1 = new Grade();
        grade1.setGrade(4.5f);
        grade1.setWeight(1);
        grade1.setParticipant(participant1);
        grade1.setCourse(course);
        entityManager.persist(grade1);

        Grade grade2 = new Grade();
        grade2.setGrade(3.8f);
        grade2.setWeight(2);
        grade2.setParticipant(participant1);
        grade2.setCourse(course);
        entityManager.persist(grade2);
    }

    @Test
    public void testFindById() {
        // Find an existing participant by ID
        Optional<Participant> foundParticipant = participantRepository.findById(participant1.getId());

        // Check if the participant is found and has the correct name
        assertTrue(foundParticipant.isPresent());
        assertEquals(participant1.getName(), foundParticipant.get().getName());
    }

    @Test
    public void testFindByEmail() {
        // Find a participant by email using custom query
        Query query = entityManager.createQuery("SELECT p FROM Participant p WHERE p.email = :email");
        query.setParameter("email", "john.doe@example.com");
        List<Participant> foundParticipants = query.getResultList();

        // Check if the participant is found and has the correct name
        assertEquals(1, foundParticipants.size());
        assertEquals(participant1.getName(), foundParticipants.get(0).getName());
    }

    @Test
    public void testFindByCourse() {
        List<Participant> foundParticipants = new ArrayList<>();

        // Iterate over all participants
        List<Participant> allParticipants = participantRepository.findAll();
        for (Participant participant : allParticipants) {
            // Check if the participant is enrolled in the specified course
            List<Course> enrolledCourses = participant.getCourses();
            if (enrolledCourses.contains(course)) {
                foundParticipants.add(participant);
            }
        }

        // Check if all participants enrolled in the course are found
        assertEquals(2, foundParticipants.size());
        assertTrue(foundParticipants.contains(participant1));
        assertTrue(foundParticipants.contains(participant2));
    }

    @Test
    public void testSave() {
        // Create a new participant
        Participant newParticipant = new Participant();
        newParticipant.setName("Alice");
        newParticipant.setSurname("Johnson");
        newParticipant.setEmail("alice.johnson@example.com");

        // Save the new participant
        participantRepository.save(newParticipant);

        // Check if the participant is saved with a generated ID
        assertNotNull(newParticipant.getId());

        // Verify that the participant can be found by ID
        Optional<Participant> foundParticipant = participantRepository.findById(newParticipant.getId());
        assertTrue(foundParticipant.isPresent());
        assertEquals(newParticipant.getName(), foundParticipant.get().getName());
    }

    @Test
    public void testDelete() {
        // Delete a participant
        participantRepository.delete(participant1);

        // Verify that the participant is no longer found
        Optional<Participant> foundParticipant = participantRepository.findById(participant1.getId());
        assertFalse(foundParticipant.isPresent());
    }
}
