package net.hrkac.tweetnotebook.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hrkac.tweetnotebook.model.Note;

public interface NoteDao extends JpaRepository<Note, Long> {

    List<Note> findByTitle(String title);
}
