package com.speakfreely.speakfreely.repository;

import com.speakfreely.speakfreely.model.ChatMessage;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByCourse(Course course);

    List<ChatMessage> findByTutor(Tutor tutor);

}

