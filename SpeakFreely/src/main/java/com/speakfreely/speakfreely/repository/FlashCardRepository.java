package com.speakfreely.speakfreely.repository;

import com.speakfreely.speakfreely.model.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {
}
