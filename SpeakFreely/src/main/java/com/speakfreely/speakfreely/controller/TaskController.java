package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.*;
import com.speakfreely.speakfreely.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final FlashCardRepository flashCardRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, FlashCardRepository flashCardRepository, GradeRepository gradeRepository) {
        this.taskRepository = taskRepository;
        this.flashCardRepository = flashCardRepository;
        this.gradeRepository = gradeRepository;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{id}")
    public Optional<Task> findTask(@PathVariable("id") Long id) {
        return taskRepository.findById(id);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable("id") Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            System.out.println("Attempt to delete non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Task> deleteAllTasks() {
        taskRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskName(@RequestBody String taskName, @PathVariable("id") Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            System.out.println("Attempt to update non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var updatedTask = task.get();
        updatedTask.setName(taskName);
        return new ResponseEntity<>(updatedTask, HttpStatus.NO_CONTENT);
    }

    //@PreAuthorize("hasRole('TUTOR')")
    @PostMapping("/{taskId}/grade")
    public ResponseEntity<Grade> setGrade(@PathVariable Long taskId, @RequestBody Grade grade) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            System.out.println("Giving grade for task: attempt to use non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var updatedTask = task.get();
        updatedTask.setGrade(grade);
        grade.setParticipant(null);
        grade.setCourse(updatedTask.getCourse());
        taskRepository.save(updatedTask);
        gradeRepository.save(grade);
        return new ResponseEntity<>(grade, HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('TUTOR')")
    @PatchMapping("/{taskId}/grade")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long taskId, @RequestBody Map<String, Object> updates) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            System.out.println("Updating grade of task: attempt to use non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var grade = task.get().getGrade();
        if (grade == null){
            System.out.println("Updating grade of task: task doesn't have a grade");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        partialGradeUpdate(grade, updates);
        return new ResponseEntity<>(grade, HttpStatus.NO_CONTENT);
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

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{taskId}/flash-cards")
    public ResponseEntity<List<FlashCard>> viewAllFlashCards(@PathVariable Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            System.out.println("View all flash cards of the task: trying to access non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task.get().getFlashCards(), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('TUTOR')")
    @GetMapping("/{taskId}/flash-cards/{cardId}")
    public ResponseEntity<FlashCard> viewSpecificFlashCard(@PathVariable Long taskId, @PathVariable Long cardId) {
        Optional<Task> task = taskRepository.findById(taskId);
        Optional<FlashCard> flashCard = flashCardRepository.findById(cardId);
        if (task.isEmpty() || flashCard.isEmpty()) {
            System.out.println("View flash card of the task: trying to access non existing task or flash card.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (task.get().getFlashCards().stream()
                .mapToLong(FlashCard::getId).anyMatch(cardId::equals)) {
            return new ResponseEntity<>(flashCard.get(), HttpStatus.OK);
        } else {
            System.out.println("View flash card of the task: trying to access flash card that isn't part of the task.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{taskId}/flash-cards")
    public ResponseEntity<FlashCard> addFlashCard(@PathVariable Long taskId, @RequestBody FlashCard flashCard) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            System.out.println("Adding flash card to task: attempt to use non existing task.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        task.get().addFlashCard(flashCard);
        taskRepository.save(task.get());
        flashCardRepository.save(flashCard);
        return new ResponseEntity<>(flashCard, HttpStatus.CREATED);
    }

    @DeleteMapping("/{taskId}/flash-cards/{cardId}")
    public ResponseEntity<FlashCard> deleteFlashCard(@PathVariable Long taskId, @PathVariable Long cardId) {
        Optional<Task> task = taskRepository.findById(taskId);
        Optional<FlashCard> flashCard = flashCardRepository.findById(cardId);
        if (task.isEmpty() || flashCard.isEmpty()) {
            System.out.println("Delete flash card from task: attempt to use non existing task or flash card.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (task.get().getFlashCards().stream()
                .mapToLong(FlashCard::getId).noneMatch(cardId::equals)) {
            System.out.println("Delete flash card from task: trying to delete flash card that isn't part of the task.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        task.get().removeFlashCard(flashCard.get());
        flashCard.get().setTask(null);
        flashCardRepository.deleteById(cardId);
        taskRepository.save(task.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{taskId}/flash-cards/{cardId}")
    public ResponseEntity<FlashCard> updateFlashCard(@PathVariable Long taskId, @PathVariable Long cardId, @RequestBody Map<String, Object> updates) {
        Optional<Task> task = taskRepository.findById(taskId);
        Optional<FlashCard> flashCard = flashCardRepository.findById(cardId);
        if (task.isEmpty() || flashCard.isEmpty()) {
            System.out.println("Update flash card from task: attempt to use non existing task or flash card.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (task.get().getFlashCards().stream()
                .mapToLong(FlashCard::getId).noneMatch(cardId::equals)) {
            System.out.println("Update flash card from task: trying to update flash card that isn't part of the task.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var updatedFlashCard = flashCard.get();
        if (updates.containsKey("key")) {
            updatedFlashCard.setKey((String) updates.get("key"));
        }
        if (updates.containsKey("value")) {
            updatedFlashCard.setValue((String) updates.get("value"));
        }
        flashCardRepository.save(updatedFlashCard);
        return new ResponseEntity<>(updatedFlashCard, HttpStatus.NO_CONTENT);
    }

}
