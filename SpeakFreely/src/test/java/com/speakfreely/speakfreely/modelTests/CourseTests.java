package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTests {

    private Course course;
    private List<Participant> participants;
    private List<Grade> grades;
    private Tutor tutor;

    @BeforeEach
    public void setUp() {
        course = new Course();
        participants = new ArrayList<>();
        grades = new ArrayList<>();
        tutor = new Tutor();

        course.setName("English");
        course.setDescription("Learn English language");
        course.setEnrolledParticipants(participants);
        course.setGrades(grades);
        course.setTutor(tutor);
    }

    @Test
    public void testEnrollParticipant() {
        Participant participant1 = new Participant();

        // Enroll the first participant
        course.enrollParticipant(participant1);

        // Check if the participant is enrolled
        assertTrue(course.getEnrolledParticipants().contains(participant1));

        // Enroll the same participant again
        course.enrollParticipant(participant1);

        // Check if the participant is still enrolled only once
        assertEquals(1, course.getEnrolledParticipants().size());
    }

    @Test
    public void testAddGrade() {
        Grade grade = new Grade();

        // Add a grade to the course
        course.addGrade(grade);

        // Check if the grade is added
        assertTrue(course.getGrades().contains(grade));
    }

    @Test
    public void testDeleteGrade() {
        Grade grade = new Grade();
        course.addGrade(grade);

        // Delete the grade from the course
        course.deleteGrade(grade);

        // Check if the grade is deleted
        assertFalse(course.getGrades().contains(grade));
    }

    @Test
    public void testDeleteGrades() {
        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        course.addGrade(grade1);
        course.addGrade(grade2);

        // Delete all grades from the course
        course.deleteGrades();

        // Check if all grades are deleted
        assertTrue(course.getGrades().isEmpty());
    }

    @Test
    public void testAssignTutor() {
        Tutor newTutor = new Tutor();

        // Assign a new tutor to the course
        course.assignTutor(newTutor);

        // Check if the tutor is assigned
        assertEquals(newTutor, course.getTutor());
    }

    @Test
    public void testDeleteTutor() {
        // Delete the tutor from the course
        course.deleteTutor();

        // Check if the tutor is null
        assertNull(course.getTutor());
    }

    @Test
    public void testDeleteParticipants() {
        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        course.enrollParticipant(participant1);
        course.enrollParticipant(participant2);

        // Delete all participants from the course
        course.deleteParticipants();

        // Check if all participants are deleted
        assertTrue(course.getEnrolledParticipants().isEmpty());
    }

    @Test
    public void testDeleteParticipant() {
        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        course.enrollParticipant(participant1);
        course.enrollParticipant(participant2);

        // Delete a specific participant from the course
        course.deleteParticipant(participant1);

        // Check if the participant is deleted
        assertFalse(course.getEnrolledParticipants().contains(participant1));
        assertTrue(course.getEnrolledParticipants().contains(participant2));
    }
}
