package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/tutors")
public class TutorController {
    private final TutorRepository tutorRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TutorController(TutorRepository tutorRepository, CourseRepository courseRepository) {
        this.tutorRepository = tutorRepository;
        this.courseRepository = courseRepository;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping
    public List<Tutor> findAllTutors() {
        return tutorRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{id}")
    public Optional<Tutor> findTutor(@PathVariable("id") Long id) {
        return tutorRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Tutor> addTutor(@RequestBody Tutor tutor) {
        tutorRepository.save(tutor);
        return new ResponseEntity<>(tutor, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Tutor> deleteTutor(@PathVariable("id") Long id) {
        Optional<Tutor> tutor = tutorRepository.findById(id);
        if (tutor.isEmpty()) {
            System.out.println("Attempt to delete non existing tutor.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tutor.get().getCourses().parallelStream().forEach(Course::deleteTutor);
        tutorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Tutor> deleteAllTutors() {
        tutorRepository.findAll().parallelStream()
                .flatMap(tutor -> tutor.getCourses().stream())
                .forEach(Course::deleteTutor);
        tutorRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Tutor> updateTutor(@RequestBody Tutor tutor, @PathVariable("id") Long id) {
        Optional<Tutor> updatedTutor = tutorRepository.findById(id);
        if (updatedTutor.isPresent()) {
            updatedTutor.get().setName(tutor.getName());
            updatedTutor.get().setSurname(tutor.getSurname());
            updatedTutor.get().setEmail(tutor.getEmail());
            tutorRepository.save(updatedTutor.get());
            return new ResponseEntity<>(updatedTutor.get(), HttpStatus.NO_CONTENT);
        } else {
            tutorRepository.save(tutor);
        }
        return new ResponseEntity<>(tutor, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<List<Tutor>> updateAllTutors(@RequestBody List<Tutor> tutors) {
        tutors.parallelStream().forEach(tutorRepository::save);
        return new ResponseEntity<>(tutors, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Tutor> updatePartOfTutor(@RequestBody Map<String, Object> updates, @PathVariable("id") Long id) {
        Optional<Tutor> tutor = tutorRepository.findById(id);
        if (tutor.isEmpty()) {
            System.out.println("Attempt to update non existing tutor.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(tutor.get(), updates);
        return new ResponseEntity<>(tutor.get(), HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(Tutor tutor, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            tutor.setName((String) updates.get("name"));
        }
        if (updates.containsKey("surname")) {
            tutor.setSurname((String) updates.get("surname"));
        }
        if (updates.containsKey("email")) {
            tutor.setEmail((String) updates.get("email"));
        }
        tutorRepository.save(tutor);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{tutorId}/courses/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable Long tutorId, @PathVariable Long courseId) {
        Optional<Tutor> tutor = tutorRepository.findById(tutorId);
        if (tutor.isEmpty() || courseRepository.findById(courseId).isEmpty()) {
            System.out.println("Attempt to use non existing tutor or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var toSend = tutor.get().getCourses().stream()
                .filter(course -> courseId.equals(course.getId())).findFirst();
        return toSend.map(course -> new ResponseEntity<>(course, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{tutorId}/courses")
    public List<Course> getCourses(@PathVariable Long tutorId) {
        Optional<Tutor> tutor = tutorRepository.findById(tutorId);
        if (tutor.isPresent())
            return tutor.get().getCourses();
        System.out.println("Attempt to use non existing tutor.");
        return Collections.emptyList();
    }
}
