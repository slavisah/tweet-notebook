package net.hrkac.tweetnotebook.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hrkac.tweetnotebook.dao.NoteDao;
import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.model.Note;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    private NoteDao noteDao;
    
    @Autowired
    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    @Transactional
    @Override
    public Note add(NoteDTO added) {
        LOGGER.debug("Adding: {}", added);

        Note model = Note.getBuilder(added.getTitle())
                .description(added.getText())
                .build();

        return noteDao.save(model);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Note> findAll() {
        LOGGER.debug("Finding all");
        return noteDao.findAll();
    }


    @Override
    public Note update(NoteDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }

}
