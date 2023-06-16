package com.speakfreely.speakfreely.chat;


import com.speakfreely.speakfreely.model.Course;
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

    // Getters and setters

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

}
