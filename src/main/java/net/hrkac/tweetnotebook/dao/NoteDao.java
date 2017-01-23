package net.hrkac.tweetnotebook.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hrkac.tweetnotebook.model.Note;

public interface NoteDao extends JpaRepository<Note, Long> {
    
    // TODO 03 findByTitle
    
}
