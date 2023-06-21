package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.FlashCard;
import com.speakfreely.speakfreely.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTests {

    private Task task;
    private FlashCard flashCard;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setName("Task 1");

        flashCard = new FlashCard();
        flashCard.setKey("Key");
        flashCard.setValue("Value");
    }

    @Test
    public void testAddFlashCardToTask() {
        task.addFlashCard(flashCard);

        Assertions.assertEquals(1, task.getFlashCards().size());
        Assertions.assertTrue(task.getFlashCards().contains(flashCard));
        Assertions.assertEquals(task, flashCard.getTask());
    }

    @Test
    public void testRemoveFlashCardFromTask() {
        task.addFlashCard(flashCard);
        task.removeFlashCard(flashCard);

        Assertions.assertEquals(0, task.getFlashCards().size());
        Assertions.assertFalse(task.getFlashCards().contains(flashCard));
        Assertions.assertNull(flashCard.getTask());
    }
}
