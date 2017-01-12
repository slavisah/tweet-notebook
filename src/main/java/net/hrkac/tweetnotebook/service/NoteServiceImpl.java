package net.hrkac.tweetnotebook.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hrkac.tweetnotebook.dao.NoteDao;
import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.exception.NoteNotFoundException;
import net.hrkac.tweetnotebook.model.Note;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    private NoteDao noteDao;
    
    @Autowired
    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    @Transactional
    public Note add(NoteDTO added) {
        LOGGER.debug("Adding a note: {}", added);

        Note model = Note.getBuilder(added.getTitle())
                .text(added.getText())
                .build();

        return noteDao.save(model);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        LOGGER.debug("Finding all notes");
        return noteDao.findAll();
    }
    
    @Override
    @Transactional(readOnly = true, rollbackFor = {NoteNotFoundException.class})
    public Note findById(Long id) throws NoteNotFoundException {
        LOGGER.debug("Finding a note with id: {}", id);

        Note found = noteDao.findOne(id);
        LOGGER.debug("Found note: {}", found);

        if (found == null) {
            throw new NoteNotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Override
    @Transactional(rollbackFor = {NoteNotFoundException.class})
    public Note update(NoteDTO updated) throws NoteNotFoundException {
        LOGGER.debug("Updating note with information: {}", updated);

        Note model = findById(updated.getId());
        model.update(updated.getText(), updated.getTitle());

        return model;
    }

    @Override
    @Transactional(rollbackFor = {NoteNotFoundException.class})
    public Note deleteById(Long id) throws NoteNotFoundException {
        
        Note deleted = findById(id);
        LOGGER.debug("Deleting note: {}", deleted);
        noteDao.delete(deleted);
        
        return deleted;
    }
}
