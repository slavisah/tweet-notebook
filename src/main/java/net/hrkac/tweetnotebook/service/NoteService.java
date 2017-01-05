package net.hrkac.tweetnotebook.service;

import java.util.List;

import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.model.Note;

public interface NoteService {
    
    public Note add(NoteDTO added);

    public List<Note> findAll();

    public Note update(NoteDTO dto);

}
