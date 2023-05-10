package com.speakfreely.speakfreely.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private float grade;
    @Column(nullable = false)
    private int weight;
    private String description;
    private Date date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    public Long getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
