package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.chat.ChatMessage;
import com.speakfreely.speakfreely.chat.ChatRepository;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        // Zapisz wiadomość czatu w repozytorium
        chatRepository.save(chatMessage);

        // Wysyłanie wiadomości do subskrybentów
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
    }

    @GetMapping("/messages/course/{courseId}")
    public List<ChatMessage> getMessagesByCourse(@PathVariable Long courseId) {
        Course course = chatRepository.getOne(courseId).getCourse();
        return chatRepository.findByCourse(course);
    }

    @GetMapping("/messages/tutor/{tutorId}")
    public List<ChatMessage> getMessagesByTutor(@PathVariable Long tutorId) {
        Tutor tutor = chatRepository.getOne(tutorId).getTutor();
        return chatRepository.findByTutor(tutor);
    }

}
