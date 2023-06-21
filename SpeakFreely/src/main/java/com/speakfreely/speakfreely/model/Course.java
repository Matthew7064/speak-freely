package com.speakfreely.speakfreely.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> enrolledParticipants;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Grade> grades;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    private Tutor tutor;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Task> tasks;

    public void enrollParticipant(Participant participant) {
        boolean canAdd = true;
        for(Participant iParticipant: enrolledParticipants) {
            if (participant.getId().equals(iParticipant.getId())) canAdd = false;
        }
        if (canAdd) enrolledParticipants.add(participant);
    }

    public void addGrade(Grade grade) {
        this.grades.add(grade);
    }

    public void deleteGrade(Grade grade) {
        this.grades.remove(grade);
    }

    public void deleteGrades() {
        this.grades.clear();
    }

    public void assignTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public void deleteTutor() {
        this.tutor = null;
    }

    public void deleteParticipants() {
        if (enrolledParticipants != null) {
            this.enrolledParticipants.clear();
        }
    }

    public void deleteParticipant(Participant participant) {
        this.enrolledParticipants.remove(participant);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Participant> getEnrolledParticipants() {
        return enrolledParticipants;
    }

    public void setEnrolledParticipants(List<Participant> enrolledParticipants) {
        this.enrolledParticipants = enrolledParticipants;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void removeTask(Task task){
        tasks.remove(task);
    }
}
