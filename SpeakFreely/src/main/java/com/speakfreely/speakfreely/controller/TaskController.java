package com.speakfreely.speakfreely.controller;

import com.speakfreely.speakfreely.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final FlashCardRepository flashCardRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, FlashCardRepository flashCardRepository) {
        this.taskRepository = taskRepository;
        this.flashCardRepository = flashCardRepository;
    }


}
