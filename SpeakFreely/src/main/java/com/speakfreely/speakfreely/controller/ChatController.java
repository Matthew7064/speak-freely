package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.model.ChatMessage;
import com.speakfreely.speakfreely.repository.ChatRepository;
import com.speakfreely.speakfreely.repository.CourseRepository;
import com.speakfreely.speakfreely.repository.TutorRepository;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ChatController {
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CourseRepository courseRepository;
    private final TutorRepository tutorRepository;

    @Autowired
    public ChatController(ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate,
                          CourseRepository courseRepository, TutorRepository tutorRepository) {
        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
        this.courseRepository = courseRepository;
        this.tutorRepository = tutorRepository;
    }


    @GetMapping("/chatlist")
    public List<ChatMessage> findAllChats() {
        return chatRepository.findAll();
    }


    @PostMapping("/message")
    public ResponseEntity<String> addMessage(@RequestBody ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now()); // Ustaw bieżący czas jako znacznik czasu wiadomości
        chatRepository.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
        return ResponseEntity.ok("Wiadomość została dodana.");
    }

    @PostMapping("/message/course/{courseId}")
    public ResponseEntity<String> addMessageToCourse(
            @PathVariable Long courseId,
            @RequestBody ChatMessage chatMessage
    ) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            chatMessage.setCourse(course);
            return addMessage(chatMessage);
        } else {
            return ResponseEntity.badRequest().body("Nie znaleziono kursu o podanym identyfikatorze.");
        }
    }

    @PostMapping("/message/tutor/{tutorId}")
    public ResponseEntity<String> addMessageToTutor(
            @PathVariable Long tutorId,
            @RequestBody ChatMessage chatMessage
    ) {
        Optional<Tutor> optionalTutor = tutorRepository.findById(tutorId);
        if (optionalTutor.isPresent()) {
            Tutor tutor = optionalTutor.get();
            chatMessage.setTutor(tutor);
            return addMessage(chatMessage);
        } else {
            return ResponseEntity.badRequest().body("Nie znaleziono tutora o podanym identyfikatorze.");
        }
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        // Zapisz wiadomość czatu w repozytorium
        chatRepository.save(chatMessage);

        // Wysyłanie wiadomości do subskrybentów
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
    }

    @GetMapping("/messages/course/{courseId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getMessagesByCourse(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<ChatMessage> chatMessages = chatRepository.findByCourse(course);
            return ResponseEntity.ok(chatMessages);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/messages/tutor/{tutorId}")
    @ResponseBody
    public List<ChatMessage> getMessagesByTutor(@PathVariable Long tutorId) {
        Tutor tutor = chatRepository.getOne(tutorId).getTutor();
        return chatRepository.findByTutor(tutor);
    }
}
