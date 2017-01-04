package net.hrkac.tweetnotebook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.service.NoteService;

@Controller
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
//    @Autowired
//    public NoteController(NoteService service) {
//        this.noteService = service;
//    }
    
    @RequestMapping(value = "/api/note", method = RequestMethod.GET)
    @ResponseBody
    public List<NoteDTO> findAll() {
        List<Note> models = noteService.findAll();
        return createDTOs(models);
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
