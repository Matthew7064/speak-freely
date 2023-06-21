package com.speakfreely.speakfreely.repositoryTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.repository.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GradeRepositoryTests {
/*
    @Autowired
    private GradeRepository gradeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Grade grade1;
    private Grade grade2;
    private Participant participant;
    private Course course;

    @BeforeEach
    public void setUp() {
        // Create and save a participant
        participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        participant.setEmail("john.doe@example.com");
        entityManager.persist(participant);

        // Create and save a course
        course = new Course();
        course.setName("Course 1");
        entityManager.persist(course);

        // Create and save sample grades
        grade1 = new Grade();
        grade1.setGrade(4.5f);
        grade1.setWeight(1);
        grade1.setDate(new Date());
        grade1.setParticipant(participant);
        grade1.setCourse(course);
        entityManager.persist(grade1);

        grade2 = new Grade();
        grade2.setGrade(3.8f);
        grade2.setWeight(2);
        grade2.setDate(new Date());
        grade2.setParticipant(participant);
        grade2.setCourse(course);
        entityManager.persist(grade2);
    }

    @Test
    public void testFindById() {
        // Find an existing grade by ID
        Optional<Grade> foundGrade = gradeRepository.findById(grade1.getId());

        // Check if the grade is found and has the correct grade value
        assertTrue(foundGrade.isPresent());
        assertEquals(grade1.getGrade(), foundGrade.get().getGrade());
    }

    @Test
    public void testFindByParticipantAndCourse() {
        // Find grades by participant and course using custom query
        Query query = entityManager.createQuery("SELECT g FROM Grade g WHERE g.participant = :participant AND g.course = :course");
        query.setParameter("participant", participant);
        query.setParameter("course", course);
        List<Grade> foundGrades = query.getResultList();

        // Check if all grades for the participant and course are found
        assertEquals(2, foundGrades.size());
        assertTrue(foundGrades.contains(grade1));
        assertTrue(foundGrades.contains(grade2));
    }

    @Test
    public void testSave() {
        // Create a new grade
        Grade newGrade = new Grade();
        newGrade.setGrade(4.0f);
        newGrade.setWeight(1);
        newGrade.setDate(new Date());
        newGrade.setParticipant(participant);
        newGrade.setCourse(course);

        // Save the new grade
        Grade savedGrade = gradeRepository.save(newGrade);

        // Check if the new grade is saved and has a generated ID
        assertNotNull(savedGrade.getId());

        // Check if the new grade can be found by ID
        Optional<Grade> foundGrade = gradeRepository.findById(savedGrade.getId());
        assertTrue(foundGrade.isPresent());
        assertEquals(newGrade.getGrade(), foundGrade.get().getGrade());
    }

    @Test
    public void testDelete() {
        // Delete a grade
        gradeRepository.delete(grade1);

        // Check if the deleted grade is no longer found by ID
        Optional<Grade> foundGrade = gradeRepository.findById(grade1.getId());
        assertFalse(foundGrade.isPresent());
    }

 */
}
