package com.speakfreely.speakfreely.controllerTests;


import com.speakfreely.speakfreely.controller.CourseController;
import com.speakfreely.speakfreely.controller.TaskController;
import com.speakfreely.speakfreely.model.*;
import com.speakfreely.speakfreely.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskControllerTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private FlashCardRepository flashCardRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TaskController taskController;

    private Course course;
    private Participant participant;
    private Grade grade;
    private Task task;
    private FlashCard flashCard;
    private List<FlashCard> flashCardList;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setName("English");
        course.setDescription("Learn English language");
        participant = new Participant();
        participant.setName("John");
        participant.setSurname("Doe");
        grade = new Grade();
        grade.setGrade(5);
        grade.setWeight(2);
        grade.setDescription("Grade for Task 2");
        flashCardList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            FlashCard flashCard = new FlashCard();
            flashCard.setKey("word key " + i);
            flashCard.setValue("word value " + i*2);
            flashCardList.add(flashCard);
        }
        flashCard = new FlashCard();
        flashCard.setKey("Mock flash key");
        flashCard.setValue("Mock flash value");
        task = new Task();
        task.setName("Task 2");
        task.setCourse(course);
        task.setGrade(grade);
        task.setFlashCards(flashCardList);
        

        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(participantRepository.findById(2L)).thenReturn(Optional.empty());
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        when(gradeRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void findAllTasks_shouldReturnListOfTasks() {
        List<Task> result = taskController.findAllTasks();
        assertEquals(Collections.singletonList(task), result);
    }

    @Test
    void findTask_withValidId_shouldReturnTask() {
        Optional<Task> result = taskController.findTask(1L);
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void findTask_withInvalidId_shouldReturnEmptyOptional() {
        Optional<Task> result = taskController.findTask(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void addFlashCard_withValidFlashCard_shouldReturnCreatedResponse() {
        var previousFlashCardsSize = task.getFlashCards().size();
        ResponseEntity<FlashCard> response = taskController.addFlashCard(1L, flashCard);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(flashCard, response.getBody());
        assertEquals(1, task.getFlashCards().size() - previousFlashCardsSize);
    }

    @Test
    void deleteTask_withValidId_shouldReturnNoContentResponse() {
        ResponseEntity<Task> response = taskController.deleteTask(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void viewFlashCards_shouldReturnCorrectFlashCards(){
        ResponseEntity<List<FlashCard>> response = taskController.viewAllFlashCards(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flashCardList, response.getBody());
    }

    @Test
    void sendFlashCards_halfCorrect_shouldGiveHalfGrade(){
        List<FlashCard> flashCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            FlashCard flashCard = new FlashCard();
            flashCard.setKey("word key " + i);
            if(i%2 == 1)
                flashCard.setValue("word value " + i*2);
            else
                flashCard.setValue("wrong value " + i*2);
            flashCards.add(flashCard);
        }
        ResponseEntity<Grade> response = taskController.gradeFlashCards(1L, 1L, flashCards);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Grade scaledGrade = response.getBody();
        assertNotNull(scaledGrade);
        assertEquals(grade.getGrade() * 0.5, scaledGrade.getGrade());
        assertEquals(grade.getDescription(), scaledGrade.getDescription());
        assertEquals(grade.getWeight(), scaledGrade.getWeight());
        assertEquals(participant, scaledGrade.getParticipant());
        assertEquals(course, scaledGrade.getCourse());
    }

}