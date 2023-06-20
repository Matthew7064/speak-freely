package com.speakfreely.speakfreely.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<FlashCard> flashCards;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grade_id", referencedColumnName = "id")
    private Grade grade;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    public Task() {
        this.flashCards = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }

    public void removeFlashCard(FlashCard flashCard){
        this.flashCards.remove(flashCard);
    }

    public void addFlashCard(FlashCard flashCard) {
        this.flashCards.add(flashCard);
        flashCard.setTask(this);
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
