package com.speakfreely.speakfreely.chat;


import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.model.Participant;
import com.speakfreely.speakfreely.model.Tutor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// reprezentuje wiadomość czatu
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

    @ManyToOne // Określ relację z polem course
    @JoinColumn(name = "course_id") // Określ nazwę kolumny, która reprezentuje relację
    private Course course;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    private Participant sender;


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setSender(Participant sender) {this.sender = sender;}

    public Participant getSender() { return sender;}

    public String getMessage() {
        return content;
    }
    public void setMessage(String message) {this.content = message;}
}
