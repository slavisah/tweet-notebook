package net.hrkac.tweetnotebook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.exception.NoteNotFoundException;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.service.NoteService;

@Controller
@RequestMapping("/note")
public class NoteController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);
    
    @Autowired
    private NoteService noteService;
    
//    @Autowired
//    public NoteController(NoteService service) {
//        this.noteService = service;
//    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public NoteDTO add(@Valid @RequestBody NoteDTO dto) {
        LOGGER.debug("Adding a new note {}", dto);
        
        Note added = noteService.add(dto);
        LOGGER.debug("Added a new note {}", added);
        
        return createDTO(added);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<NoteDTO> findAll() {
        List<Note> models = noteService.findAll();
        return createDTOs(models);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public NoteDTO findById(@PathVariable("id") Long id) throws NoteNotFoundException {
        LOGGER.debug("Finding note with id: {}", id);

        Note found = noteService.findById(id);
        LOGGER.debug("Found note with information: {}", found);

        return createDTO(found);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public NoteDTO update(@Valid @RequestBody NoteDTO dto, @PathVariable("id") Long id) throws NoteNotFoundException {
        Note updated = noteService.update(dto);
        return createDTO(updated);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public NoteDTO deleteById(@PathVariable("id") Long id) throws NoteNotFoundException {
        LOGGER.debug("Deleting a note with id: {}", id);

        Note deleted = noteService.deleteById(id);
        LOGGER.debug("Deleted note with information: {}", deleted);

        return createDTO(deleted);
    }
    
    private List<NoteDTO> createDTOs(List<Note> models) {
        List<NoteDTO> dtos = new ArrayList<>();

        for (Note model : models) {
            dtos.add(createDTO(model));
        }

        return dtos;
    }

    private NoteDTO createDTO(Note model) {
        return NoteDTO.getBuilder().id(model.getId()).title(model.getTitle()).text(model.getText()).build();
    }
    
}
