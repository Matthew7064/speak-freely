package com.speakfreely.speakfreely.chat;

import com.speakfreely.speakfreely.chat.ChatMessage;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;
import com.speakfreely.speakfreely.chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository ChatRepository;

    @Autowired
    public ChatService(ChatRepository ChatRepository) {
        this.ChatRepository = ChatRepository;
    }

    public void sendMessageToCourse(Participant sender, Course course, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setCourse(course);
        chatMessage.setContent(message);
        chatMessage.setTimestamp(LocalDateTime.now());

        ChatRepository.save(chatMessage);
    }

    public void sendMessageToTutor(Participant sender, Tutor tutor, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setTutor(tutor);
        chatMessage.setContent(message);
        chatMessage.setTimestamp(LocalDateTime.now());

        ChatRepository.save(chatMessage);
    }

    public List<ChatMessage> getCourseChatMessages(Course course) {
        return ChatRepository.findByCourse(course);
    }

    public List<ChatMessage> getTutorChatMessages(Tutor tutor) {
        return ChatRepository.findByTutor(tutor);
    }
}
