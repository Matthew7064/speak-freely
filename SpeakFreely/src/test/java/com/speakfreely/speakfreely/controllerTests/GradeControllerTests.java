package com.speakfreely.speakfreely.controllerTests;

import com.speakfreely.speakfreely.controller.GradeController;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeControllerTests {

    @Test
    void testFindAllGrades() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        List<Grade> grades = new ArrayList<>();
        Grade grade1 = new Grade();
        //grade1.setId(1L);
        grade1.setGrade(90);
        grade1.setWeight(2);
        grade1.setDescription("Math");
        grade1.setDate(new Date());
        grades.add(grade1);

        Grade grade2 = new Grade();
        // Ustaw pozostałe właściwości dla grade2

        grades.add(grade2);

        when(gradeRepository.findAll()).thenReturn(grades);
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        List<Grade> result = gradeController.findAllGrades();

        // Assert
        assertEquals(grades, result);
    }

    @Test
    void testFindGrade_existingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        Grade grade = new Grade();
        //grade.setId(1L);
        grade.setGrade(90);
        grade.setWeight(2);
        grade.setDescription("Math");
        grade.setDate(new Date());
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        Optional<Grade> result = gradeController.findGrade(1L);

        // Assert
        assertEquals(Optional.of(grade), result);
    }

    @Test
    void testFindGrade_nonExistingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        Optional<Grade> result = gradeController.findGrade(1L);

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testDeleteGrade_existingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        Grade grade = new Grade();
        //grade.setId(1L);
        grade.setGrade(90);
        grade.setWeight(2);
        grade.setDescription("Math");
        grade.setDate(new Date());
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        ResponseEntity<Grade> response = gradeController.deleteGrade(1L);

        // Assert
        verify(gradeRepository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteGrade_nonExistingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        ResponseEntity<Grade> response = gradeController.deleteGrade(1L);

        // Assert
        verify(gradeRepository, never()).deleteById(Mockito.anyLong());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteAllGrades() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        ResponseEntity<Grade> response = gradeController.deleteAllGrades();

        // Assert
        verify(gradeRepository, times(1)).deleteAll();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdatePartOfGrade_existingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        Grade grade = new Grade();
        //grade.setId(1L);
        grade.setGrade(90);
        grade.setWeight(2);
        grade.setDescription("Math");
        grade.setDate(new Date());
        Map<String, String> updates = new HashMap<>();
        updates.put("grade", "95");
        updates.put("weight", "3");
        updates.put("description", "Mathematics");
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        ResponseEntity<Grade> response = gradeController.updatePartOfGrade(updates, 1L);

        // Assert
        verify(gradeRepository, times(1)).save(grade);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(95, grade.getGrade());
        assertEquals(3, grade.getWeight());
        assertEquals("Mathematics", grade.getDescription());
    }

    @Test
    void testUpdatePartOfGrade_nonExistingId() {
        // Arrange
        GradeRepository gradeRepository = mock(GradeRepository.class);
        Map<String, String> updates = new HashMap<>();
        updates.put("grade", "95");
        updates.put("weight", "3");
        updates.put("description", "Mathematics");
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        GradeController gradeController = new GradeController(gradeRepository);

        // Act
        ResponseEntity<Grade> response = gradeController.updatePartOfGrade(updates, 1L);

        // Assert
        verify(gradeRepository, never()).save(Mockito.any(Grade.class));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
