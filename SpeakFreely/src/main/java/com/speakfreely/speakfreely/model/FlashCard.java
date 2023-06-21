package com.speakfreely.speakfreely.model;

import jakarta.persistence.*;

@Entity
public class FlashCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String key;
    @Column(nullable = false)
    private String value;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
