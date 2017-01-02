package net.hrkac.tweetnotebook.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.repository.NoteRepository;

@Service
public class RepositoryTodoService implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoService.class);

    private NoteRepository repository;

    @Transactional
    @Override
    public Note add(NoteDTO added) {
        LOGGER.debug("Adding: {}", added);

        Note model = Note.getBuilder(added.getTitle())
                .description(added.getText())
                .build();

        return repository.save(model);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Note> findAll() {
        LOGGER.debug("Finding all");
        return repository.findAll();
    }

}
