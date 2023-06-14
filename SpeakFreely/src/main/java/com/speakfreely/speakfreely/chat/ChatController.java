package com.speakfreely.speakfreely.chat;

import com.speakfreely.speakfreely.chat.ChatMessage;
import com.speakfreely.speakfreely.chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
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
}
