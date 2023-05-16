package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class GradeController {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/grades")
    public List<Grade> findAllGrades() {
        return gradeRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/grades/{id}")
    public Optional<Grade> findGrade(@PathVariable("id") Long id) {
        return gradeRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @DeleteMapping("/grades/{id}")
    public ResponseEntity<Grade> deleteGrade(@PathVariable("id") Long id) {
        Optional<Grade> grade = gradeRepository.findById(id);
        if (grade.isEmpty()) {
            System.out.println("Attempt to delete non existing grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        gradeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/grades")
    public ResponseEntity<Grade> deleteAllGrades() {
        gradeRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @PatchMapping("/grades/{id}")
    public ResponseEntity<Grade> updatePartOfGrade(@RequestBody Map<String, String> updates, @PathVariable("id") Long id) {
        Optional<Grade> grade = gradeRepository.findById(id);
        if (grade.isEmpty()) {
            System.out.println("Attempt to update non existing grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(grade.get(), updates);
        return new ResponseEntity<>(grade.get(), HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(Grade grade, Map<String, String> updates) {
        if (updates.containsKey("grade")) {
            grade.setGrade(Float.parseFloat(updates.get("grade")));
        }
        if (updates.containsKey("weight")) {
            grade.setWeight(Integer.parseInt(updates.get("weight")));
        }
        if (updates.containsKey("description")) {
            grade.setDescription((String) updates.get("description"));
        }
        grade.setDate(new Date());
        gradeRepository.save(grade);
    }
}
