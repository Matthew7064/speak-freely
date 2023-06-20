package com.speakfreely.speakfreely.controllerTests;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.speakfreely.speakfreely.model.ChatMessage;
import com.speakfreely.speakfreely.repository.ChatRepository;
import com.speakfreely.speakfreely.controller.ChatController;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


class ChatControllerTest {
    private  ChatRepository chatRepository;
    private  SimpMessagingTemplate messagingTemplate;
    private  CourseRepository courseRepository;
    private  TutorRepository tutorRepository;
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        chatRepository = mock(ChatRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        chatController = new ChatController(chatRepository, messagingTemplate, courseRepository, tutorRepository);
    }

    @Test
    void processMessage_ShouldSaveMessageAndSendToSubscribers() {
        // Given
        ChatMessage chatMessage = new ChatMessage();

        // When
        chatController.processMessage(chatMessage);

        // Then
        verify(chatRepository).save(chatMessage);
        verify(messagingTemplate).convertAndSend(eq("/topic/chat"), eq(chatMessage));
    }

    @Test
    void getMessagesByCourse_ShouldReturnMessagesForCourse() {
        // Given
        Long courseId = 1L;
        ChatMessage chatMessage = new ChatMessage();
        Course course = new Course();
        chatMessage.setCourse(course);
        when(chatRepository.findById(courseId)).thenReturn(Optional.of(chatMessage));
        when(chatRepository.findByCourse(course)).thenReturn(Collections.singletonList(chatMessage));

        // When
        List<ChatMessage> messages = (List<ChatMessage>) chatController.getMessagesByCourse(courseId);

        // Then
        verify(chatRepository).findById(courseId);
        verify(chatRepository).findByCourse(course);
        // Additional assertions for messages if needed
    }

    @Test
    void getMessagesByTutor_ShouldReturnMessagesForTutor() {
        // Given
        Long tutorId = 1L;
        Tutor tutor = new Tutor();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setTutor(tutor);
        when(chatRepository.getOne(tutorId)).thenReturn(chatMessage);
        when(chatRepository.findByTutor(tutor)).thenReturn(Collections.singletonList(chatMessage));

        // When
        List<ChatMessage> messages = chatController.getMessagesByTutor(tutorId);

        // Then
        verify(chatRepository).getOne(tutorId);
        verify(chatRepository).findByTutor(tutor);
        // Additional assertions for messages if needed
    }
}

