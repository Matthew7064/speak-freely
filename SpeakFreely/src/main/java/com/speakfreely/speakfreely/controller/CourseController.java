package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Grade;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.GradeRepository;
import com.speakfreely.speakfreely.repository.ParticipantRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final ParticipantRepository participantRepository;
    private final TutorRepository tutorRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, ParticipantRepository participantRepository, TutorRepository tutorRepository, GradeRepository gradeRepository) {
        this.courseRepository = courseRepository;
        this.participantRepository = participantRepository;
        this.tutorRepository = tutorRepository;
        this.gradeRepository = gradeRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{id}")
    public Optional<Course> findCourse(@PathVariable("id") Long id) {
        return courseRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        courseRepository.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable("id") Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            System.out.println("Attempt to delete non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().getGrades().parallelStream().
                mapToLong(Grade::getId).forEach(gradeRepository::deleteById);
        course.get().deleteParticipants();
        course.get().deleteTutor();
        courseRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Course> deleteAllCourses() {
        courseRepository.findAll().forEach(course -> {
            course.deleteParticipants();
            course.deleteTutor();
        });
        courseRepository.findAll().stream()
                .flatMap(course -> course.getGrades().parallelStream())
                .mapToLong(Grade::getId).forEach(gradeRepository::deleteById);
        courseRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course, @PathVariable("id") Long id) {
        Optional<Course> updatedCourse = courseRepository.findById(id);
        if (updatedCourse.isPresent()) {
            updatedCourse.get().setName(course.getName());
            updatedCourse.get().setDescription(course.getDescription());
            courseRepository.save(updatedCourse.get());
            return new ResponseEntity<>(updatedCourse.get(), HttpStatus.NO_CONTENT);
        } else {
            courseRepository.save(course);
        }
        return new ResponseEntity<>(course, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<List<Course>> updateAllCourses(@RequestBody List<Course> courses) {
        courses.parallelStream().forEach(courseRepository::save);
        return new ResponseEntity<>(courses, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Course> updatePartOfCourse(@RequestBody Map<String, Object> updates, @PathVariable("id") Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            System.out.println("Attempt to update non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        partialUpdate(course.get(), updates);
        return new ResponseEntity<>(course.get(), HttpStatus.NO_CONTENT);
    }

    private void partialUpdate(Course course, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            course.setName((String) updates.get("name"));
        }
        if (updates.containsKey("description")) {
            course.setDescription((String) updates.get("description"));
        }
        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}/participants/{participantId}")
    public ResponseEntity<Course> enrollParticipantToCourse(@PathVariable Long courseId, @PathVariable Long participantId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (course.isEmpty() || participant.isEmpty()) {
            System.out.println("Enrollment participant to course: attempt to use non existing participant or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().enrollParticipant(participant.get());
        courseRepository.save(course.get());
        return new ResponseEntity<>(course.get(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}/participants")
    public ResponseEntity<Course> enrollParticipantsToCourse(@PathVariable Long courseId, @RequestBody List<Long> indices) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            System.out.println("Enrollment participant to course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var course = optionalCourse.get();
        indices.stream().map(participantRepository::findById)
                .filter(Optional::isPresent).map(Optional::get)
                .forEach(course::enrollParticipant);
        courseRepository.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}/tutors/{tutorId}")
    public ResponseEntity<Course> assignTutorToCourse(@PathVariable Long courseId, @PathVariable Long tutorId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Tutor> tutor = tutorRepository.findById(tutorId);
        if (course.isEmpty() || tutor.isEmpty()) {
            System.out.println("Assignment tutor to course: attempt to use non existing tutor or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().assignTutor(tutor.get());
        courseRepository.save(course.get());
        return new ResponseEntity<>(course.get(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}/tutors")
    public ResponseEntity<Course> deleteTutor(@PathVariable Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            System.out.println("Deleting tutor for course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().deleteTutor();
        courseRepository.save(course.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/tutors")
    public ResponseEntity<Tutor> getTutor(@PathVariable Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            System.out.println("Getting tutor for course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Tutor tutor = course.get().getTutor();
        return new ResponseEntity<>(tutor, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}/participants")
    public ResponseEntity<Course> signOutParticipants(@PathVariable Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            System.out.println("Deleting participants for course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().deleteParticipants();
        courseRepository.save(course.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}/participants/{participantId}")
    public ResponseEntity<Course> signOutParticipant(@PathVariable Long courseId, @PathVariable Long participantId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (course.isEmpty() || participant.isEmpty()) {
            System.out.println("Deleting participant from course: attempt to use non existing participant or course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.get().deleteParticipant(participant.get());
        courseRepository.save(course.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/participants")
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            System.out.println("Getting participants from course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course.get().getEnrolledParticipants(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{courseId}/participants")
    public ResponseEntity<Course> signOutParticipants(@PathVariable Long courseId, @RequestBody List<Long> indices) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            System.out.println("Deleting participant from course: attempt to use non existing course.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        indices.stream().map(participantRepository::findById)
                .filter(Optional::isPresent).map(Optional::get)
                .forEach(course.get()::deleteParticipant);
        courseRepository.save(course.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/participants/{participantId}")
    public ResponseEntity<Participant> getParticipant(@PathVariable Long courseId, @PathVariable Long participantId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty()) {
            System.out.println("Getting participant from course: attempt to use non existing course or participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var foundParticipant = course.get().getEnrolledParticipants()
                .stream().filter(participant -> participantId.equals(participant.getId()))
                .limit(1).findFirst();
        if (foundParticipant.isEmpty()) {
            System.out.println("Getting participant from course: attempt to view not enrolled participant.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(foundParticipant.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/participants/{participantId}/grades")
    public ResponseEntity<List<Grade>> getParticipantGrades(@PathVariable Long courseId, @PathVariable Long participantId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty()) {
            System.out.println("Getting grades of course participant: attempt to use non existing course or participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var enrolledParticipant = course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId())).findFirst();
        if (enrolledParticipant.isEmpty()) {
            System.out.println("Getting grades of course participant: attempt to use not enrolled participant.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var gradesToDisplay = enrolledParticipant.get().getGrades().stream()
                .filter(grade -> courseId.equals(grade.getCourse().getId()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(gradesToDisplay, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/participants/{participantId}/courseGrades")
    public Map<Participant, List<Grade>> getParticipantWithGrades(@PathVariable Long courseId, @PathVariable Long participantId) {
        Map<Participant, List<Grade>> toSend = new HashMap<>();
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty()) {
            System.out.println("Getting grades of course participant: attempt to use non existing course or participant.");
            return toSend;
        }
        course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId()))
                .forEach(participant -> toSend.put(participant, participant.getGrades().stream()
                        .filter(grade -> courseId.equals(grade.getCourse().getId()))
                        .collect(Collectors.toList())));
        return toSend;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{courseId}/participants/{participantId}/grades/{gradeId}")
    public ResponseEntity<Grade> getParticipantGrade(@PathVariable Long courseId, @PathVariable Long participantId, @PathVariable Long gradeId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty() || gradeRepository.findById(gradeId).isEmpty()) {
            System.out.println("Getting grade of course participant: attempt to use non existing course or participant or grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var foundGrade = course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId()))
                .flatMap(participant -> participant.getGrades().stream())
                .filter(grade -> gradeId.equals(grade.getId()) && courseId.equals(grade.getCourse().getId())).findFirst();
        if (foundGrade.isEmpty()) {
            System.out.println("Getting grade of course participant: attempt to use not enrolled participant.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(foundGrade.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @DeleteMapping("/{courseId}/participants/{participantId}/grades/{gradeId}")
    public ResponseEntity<Grade> deleteParticipantGrade(@PathVariable Long courseId, @PathVariable Long participantId, @PathVariable Long gradeId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty() || gradeRepository.findById(gradeId).isEmpty()) {
            System.out.println("Deleting grade of course participant: attempt to use non existing course or participant or grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var foundParticipant = course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId())).findFirst();
        if (foundParticipant.isEmpty()) {
            System.out.println("Deleting grade of course participant: attempt to use not enrolled participant.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var foundGrade = foundParticipant.get().getGrades().stream()
                .filter(grade -> gradeId.equals(grade.getId()) && courseId.equals(grade.getCourse().getId()))
                .findFirst();
        foundGrade.ifPresent(grade -> gradeRepository.deleteById(grade.getId()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PreAuthorize("hasRole('TUTOR')")
    @PostMapping("/{courseId}/participants/{participantId}/grades")
    public ResponseEntity<Grade> giveGrade(@PathVariable Long courseId, @PathVariable Long participantId, @RequestBody Grade grade) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty()) {
            System.out.println("Giving grade for course participant: attempt to use non existing course or participant.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var foundParticipant = course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId())).findFirst();
        if (foundParticipant.isEmpty()) {
            System.out.println("Giving grade for course participant: attempt to use not enrolled participant.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var participant = foundParticipant.get();
        participant.receiveGrade(grade);
        course.get().addGrade(grade);
        grade.setParticipant(participant);
        grade.setCourse(course.get());
        courseRepository.save(course.get());
        participantRepository.save(participant);
        gradeRepository.save(grade);
        return new ResponseEntity<>(grade, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @PatchMapping("/{courseId}/participants/{participantId}/grades/{gradeId}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long courseId, @PathVariable Long participantId, @PathVariable Long gradeId, @RequestBody Map<String, Object> updates) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty() || participantRepository.findById(participantId).isEmpty() || gradeRepository.findById(gradeId).isEmpty()) {
            System.out.println("Updating grade of course participant: attempt to use non existing course or participant or grade.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var foundGrade = course.get().getEnrolledParticipants().stream()
                .filter(participant -> participantId.equals(participant.getId()))
                .flatMap(participant -> participant.getGrades().stream())
                .filter(grade -> gradeId.equals(grade.getId()) && courseId.equals(grade.getCourse().getId()))
                .findFirst();
        if (foundGrade.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        partialGradeUpdate(foundGrade.get(), updates);
        return new ResponseEntity<>(foundGrade.get(), HttpStatus.NO_CONTENT);
    }

    private void partialGradeUpdate(Grade grade, Map<String, Object> updates) {
        if (updates.containsKey("grade")) {
            grade.setGrade(((Double) updates.get("grade")).floatValue());
        }
        if (updates.containsKey("weight")) {
            grade.setWeight((int) updates.get("weight"));
        }
        if (updates.containsKey("description")) {
            grade.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("date")) {
            grade.setDate((Date) updates.get("date"));
        }
        gradeRepository.save(grade);
    }
}
