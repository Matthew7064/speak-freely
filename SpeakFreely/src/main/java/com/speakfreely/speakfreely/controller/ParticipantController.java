package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.GradeRepository;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantRepository participantRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ParticipantController(ParticipantRepository participantRepository, GradeRepository gradeRepository, CourseRepository courseRepository) {
        this.participantRepository = participantRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Participant> findAllParticipants() {
        return participantRepository.findAll();
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{id}")
    public Optional<Participant> findParticipant(@PathVariable("id") Long id) {
        return participantRepository.findById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Participant> addParticipant(@RequestBody Participant participant) {
        participantRepository.save(participant);
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable("id") Long id) {
        Optional<Participant> optionalParticipant = participantRepository.findById(id);
        if (optionalParticipant.isEmpty()) {
            System.out.println("Attempt to delete non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var participant = optionalParticipant.get();
        participant.getGrades().stream()
                .mapToLong(Grade::getId).forEach(gradeRepository::deleteById);
        participant.getCourses()
                .forEach(course -> course.deleteParticipant(participant));
        participantRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Participant> deleteAllParticipants() {
        participantRepository.findAll().stream().flatMap(participant -> participant.getGrades().stream())
                .mapToLong(Grade::getId).forEach(gradeRepository::deleteById);
        participantRepository.findAll().forEach(participant -> participant.getCourses().stream().
                forEach(course -> course.deleteParticipant(participant)));
        participantRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@RequestBody Participant participant, @PathVariable("id") Long id) {
        Optional<Participant> updatedParticipant = participantRepository.findById(id);
        if (updatedParticipant.isPresent()) {
            updatedParticipant.get().setName(participant.getName());
            updatedParticipant.get().setSurname(participant.getSurname());
            updatedParticipant.get().setEmail(participant.getEmail());
            participantRepository.save(updatedParticipant.get());
            return new ResponseEntity<>(updatedParticipant.get(), HttpStatus.NO_CONTENT);
        } else {
            participantRepository.save(participant);
        }
        return new ResponseEntity<>(participant, HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<List<Participant>> updateAllParticipants(@RequestBody List<Participant> participants) {
        participantRepository.saveAll(participants);
        return new ResponseEntity<>(participants, HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Participant> updatePartOfParticipant(@RequestBody Map<String, Object> updates, @PathVariable("id") Long id) {
        Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isEmpty()) {
            System.out.println("Attempt to update non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(participant.get(), updates);
        return new ResponseEntity<>(participant.get(), HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(Participant participant, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            participant.setName((String) updates.get("name"));
        }
        if (updates.containsKey("surname")) {
            participant.setSurname((String) updates.get("surname"));
        }
        if (updates.containsKey("email")) {
            participant.setEmail((String) updates.get("email"));
        }
        participantRepository.save(participant);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{participantId}/grades")
    public ResponseEntity<List<Grade>> viewAllGrades(@PathVariable Long participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            System.out.println("View all grades of the participant: trying to access non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(participant.get().getGrades(), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{participantId}/grades/{gradeId}")
    public ResponseEntity<Grade> viewSpecificGrade(@PathVariable Long participantId, @PathVariable Long gradeId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        Optional<Grade> grade = gradeRepository.findById(gradeId);
        if (participant.isEmpty() || grade.isEmpty()) {
            System.out.println("View grade of the participant: trying to access non existing participant or grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (participant.get().getGrades().stream()
                .mapToLong(Grade::getId).anyMatch(gradeId::equals)) {
            return new ResponseEntity<>(grade.get(), HttpStatus.OK);
        } else {
            System.out.println("View grade of the participant: trying to access grade that belongs to someone else.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{participantId}/courses/{courseId}/grades")
    public ResponseEntity<List<Grade>> viewAllCourseGrades(@PathVariable Long participantId, @PathVariable Long courseId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (participant.isEmpty() || course.isEmpty()) {
            System.out.println("View course grades of the participant: trying to access non existing participant or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (participant.get().getCourses().stream().
                mapToLong(Course::getId).noneMatch(courseId::equals)) {
            System.out.println("View course grades of the participant: trying to access the course that user is not enrolled to.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(participant.get().getGrades().stream()
                .filter(grade -> courseId.equals(grade.getCourse().getId()))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("{participantId}/courses")
    public ResponseEntity<List<Course>> viewCourses(@PathVariable Long participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            System.out.println("View all courses of the participant: trying to access non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(participant.get().getCourses(), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{participantId}/courses/{courseId}")
    public ResponseEntity<Course> viewCourse(@PathVariable Long participantId, @PathVariable Long courseId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (participant.isEmpty() || course.isEmpty()) {
            System.out.println("View course of the participant: trying to access non existing participant or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (participant.get().getCourses().stream().map(Course::getId).noneMatch(courseId::equals)) {
            System.out.println("View course of the participant: trying to access course that participant is not assigned to.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(course.get(), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{participantId}/addPayment")
    public ResponseEntity<Participant> addPayment(@PathVariable Long participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            System.out.println("Adding payment: attempt to use non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        participant.get().setPayment(true);
        participantRepository.save(participant.get());
        return new ResponseEntity<>(participant.get(), HttpStatus.ACCEPTED);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{participantId}/deletePayment")
    public ResponseEntity<Participant> deletePayment(@PathVariable Long participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            System.out.println("Deleting payment: attempt to use non existing participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        participant.get().setPayment(false);
        participantRepository.save(participant.get());
        return new ResponseEntity<>(participant.get(), HttpStatus.ACCEPTED);
    }
}
