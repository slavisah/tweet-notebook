package net.hrkac.tweetnotebook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.service.NoteService;

@Controller
public class NoteController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);
    
    @Autowired
    private NoteService noteService;
    
//    @Autowired
//    public NoteController(NoteService service) {
//        this.noteService = service;
//    }
    
    @RequestMapping(value  = "/api/note", method = RequestMethod.POST)
    @ResponseBody
    public NoteDTO add(@Valid @RequestBody NoteDTO dto) {
        LOGGER.debug("Adding a new note {}", dto);
        Note added = noteService.add(dto);
        LOGGER.debug("Added a new note {}", added);
        return createDTO(added);
    }
    
    @RequestMapping(value = "/api/note", method = RequestMethod.GET)
    @ResponseBody
    public List<NoteDTO> findAll() {
        List<Note> models = noteService.findAll();
        return createDTOs(models);
    }
    
    public NoteDTO update(NoteDTO dto, Long id) {
        Note updated = noteService.update(dto);
        return createDTO(updated);
    }
    
    private List<NoteDTO> createDTOs(List<Note> models) {
        List<NoteDTO> dtos = new ArrayList<>();

        for (Note model : models) {
            dtos.add(createDTO(model));
        }

        return dtos;
    }

    private NoteDTO createDTO(Note model) {
        NoteDTO dto = new NoteDTO();

        dto.setId(model.getId());
        dto.setText(model.getText());
        dto.setTitle(model.getTitle());

        return dto;
    }
    
}
