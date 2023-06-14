package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class GradeTests {

    private Grade grade;
    private Participant participant;
    private Course course;

    @BeforeEach
    public void setUp() {
        grade = new Grade();
        participant = new Participant();
        course = new Course();

        grade.setGrade(4.5f);
        grade.setWeight(10);
        grade.setDescription("Final Exam");
        grade.setDate(new Date());
        grade.setParticipant(participant);
        grade.setCourse(course);
    }

    @Test
    public void testGetGrade() {
        // Check if the correct grade is returned
        assertEquals(4.5f, grade.getGrade());
    }

    @Test
    public void testSetGrade() {
        // Set a new grade for the grade
        grade.setGrade(3.8f);

        // Check if the grade is updated
        assertEquals(3.8f, grade.getGrade());
    }

    @Test
    public void testGetWeight() {
        // Check if the correct weight is returned
        assertEquals(10, grade.getWeight());
    }

    @Test
    public void testSetWeight() {
        // Set a new weight for the grade
        grade.setWeight(8);

        // Check if the weight is updated
        assertEquals(8, grade.getWeight());
    }

    @Test
    public void testGetDescription() {
        // Check if the correct description is returned
        assertEquals("Final Exam", grade.getDescription());
    }

    @Test
    public void testSetDescription() {
        // Set a new description for the grade
        grade.setDescription("Midterm Exam");

        // Check if the description is updated
        assertEquals("Midterm Exam", grade.getDescription());
    }

    @Test
    public void testGetDate() {
        // Check if the correct date is returned
        assertNotNull(grade.getDate());
    }

    @Test
    public void testSetDate() {
        // Set a new date for the grade
        Date newDate = new Date();
        grade.setDate(newDate);

        // Check if the date is updated
        assertEquals(newDate, grade.getDate());
    }

    @Test
    public void testGetParticipant() {
        // Check if the correct participant is returned
        assertEquals(participant, grade.getParticipant());
    }

    @Test
    public void testSetParticipant() {
        Participant newParticipant = new Participant();

        // Set a new participant for the grade
        grade.setParticipant(newParticipant);

        // Check if the participant is updated
        assertEquals(newParticipant, grade.getParticipant());
    }

    @Test
    public void testGetCourse() {
        // Check if the correct course is returned
        assertEquals(course, grade.getCourse());
    }

    @Test
    public void testSetCourse() {
        Course newCourse = new Course();

        // Set a new course for the grade
        grade.setCourse(newCourse);

        // Check if the course is updated
        assertEquals(newCourse, grade.getCourse());
    }
}
