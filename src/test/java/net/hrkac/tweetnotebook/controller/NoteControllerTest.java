package net.hrkac.tweetnotebook.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.hrkac.tweetnotebook.config.TestContext;
import net.hrkac.tweetnotebook.config.TestUtil;
import net.hrkac.tweetnotebook.config.WebAppContext;
import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.exception.NoteNotFoundException;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.model.TestNoteBuilder;
import net.hrkac.tweetnotebook.service.NoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class NoteControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private NoteService noteServiceMock;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.reset(noteServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    // TODO 18 add_EmptyNote_ShouldReturnValidationErrorForTitle
    
    // TODO 19 add_TitleAndTextAreTooLong_ShouldReturnValidationErrorsForTitleAndText
    
    // TODO 20 add_NewNote_ShouldAddNoteAndReturnAddedEntry
    
    @Test
    public void findAll_NotesFound_ShouldReturnFoundNoteEntries() throws Exception {
        // TODO 05 findAll_NotesFound_ShouldReturnFoundNoteEntries
        fail("Not yet implemented");
    }
    
    // TODO 21 findById_NoteFound_ShouldReturnFoundEntry

    // TODO 22 findById_NoteNotFound_ShouldReturnHttpStatusCode404
    
    // TODO 23 update_EmptyNote_ShouldReturnValidationErrorForTitle

    // TODO 24 update_TitleAndTextAreTooLong_ShouldReturnValidationErrorsForTitleAndText

    // TODO 25 update_NoteNotFound_ShouldReturnHttpStatusCode404

    // TODO 26 update_NoteFound_ShouldUpdateNoteAndReturnIt
    
    // TODO 27 deleteById_NoteFound_ShouldDeleteNoteAndReturnIt
    
}
