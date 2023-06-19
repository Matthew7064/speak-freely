package com.speakfreely.speakfreely.chat;

import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant sender;

    public ChatMessage() {
        // Konstruktor bezargumentowy
    }

    public ChatMessage(String content, LocalDateTime timestamp, Course course, Tutor tutor, Participant sender) {
        this.content = content;
        this.timestamp = timestamp;
        this.course = course;
        this.tutor = tutor;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }
}
