package net.hrkac.tweetnotebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hrkac.tweetnotebook.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
