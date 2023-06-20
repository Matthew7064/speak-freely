package com.speakfreely.speakfreely.modelTests;

import com.speakfreely.speakfreely.model.FlashCard;
import com.speakfreely.speakfreely.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlashCardTests {

    @Test
    public void testFlashCardGettersAndSetters() {
        // Tworzenie obiektu FlashCard
        FlashCard flashCard = new FlashCard();

        // Ustawianie wartości
        flashCard.setKey("Key");
        flashCard.setValue("Value");

        // Sprawdzanie getterów
        Assertions.assertEquals("Key", flashCard.getKey());
        Assertions.assertEquals("Value", flashCard.getValue());
    }

    @Test
    public void testFlashCardTaskAssociation() {
        // Tworzenie obiektu Task
        Task task = new Task();

        // Tworzenie obiektu FlashCard
        FlashCard flashCard = new FlashCard();
        flashCard.setKey("Key");
        flashCard.setValue("Value");

        // Dodawanie FlashCard do Task
        task.addFlashCard(flashCard);

        // Sprawdzanie asocjacji
        Assertions.assertTrue(task.getFlashCards().contains(flashCard));
        Assertions.assertEquals(task, flashCard.getTask());
    }
}
