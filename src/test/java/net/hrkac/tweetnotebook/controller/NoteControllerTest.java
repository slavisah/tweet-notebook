package net.hrkac.tweetnotebook.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    
    @Test
    public void add_EmptyNote_ShouldReturnValidationErrorForTitle() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().build();

        mockMvc.perform(post("/note")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].path", is("title")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("The title cannot be empty.")));
    }
    
    @Test
    public void add_TitleAndTextAreTooLong_ShouldReturnValidationErrorsForTitleAndText() throws Exception {
        String title = TestUtil.createStringWithLength(Note.MAX_LENGTH_TITLE + 1);
        String text = TestUtil.createStringWithLength(Note.MAX_LENGTH_TEXT + 1);
        NoteDTO dto = NoteDTO.getBuilder().title(title).text(text).build();

        mockMvc.perform(post("/note").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
            .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("title", "text")))
            .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder("The maximum length of the text is 140 characters.", "The maximum length of the title is 50 characters.")));
        
        verifyZeroInteractions(noteServiceMock);
    }
    
    @Test
    public void add_NewNote_ShouldAddNoteAndReturnAddedEntry() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().title("title").text("text").build();
        Note added = new TestNoteBuilder().id(1L).title("title").text("text").build();
        when(noteServiceMock.add(any(NoteDTO.class))).thenReturn(added);

        mockMvc.perform(post("/note")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.text", is("text")));

        ArgumentCaptor<NoteDTO> dtoCaptor = ArgumentCaptor.forClass(NoteDTO.class);
        verify(noteServiceMock, times(1)).add(dtoCaptor.capture());
        verifyNoMoreInteractions(noteServiceMock);

        NoteDTO dtoArgument = dtoCaptor.getValue();
        assertNull(dtoArgument.getId());
        assertThat(dtoArgument.getTitle(), is("title"));
        assertThat(dtoArgument.getText(), is("text"));
    }
    
    @Test
    public void findAll_NotesFound_ShouldReturnFoundNoteEntries() throws Exception {
        Note first = new TestNoteBuilder().id(1L).title("Example 1").text("Lorem ipsum").build();
        Note second = new TestNoteBuilder().id(2L).title("Example 2").text("Lorem ipsum").build();
        when(noteServiceMock.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/note"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Example 1")))
                .andExpect(jsonPath("$[0].text", is("Lorem ipsum")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Example 2")))
                .andExpect(jsonPath("$[1].text", is("Lorem ipsum")));

        verify(noteServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(noteServiceMock);
    }
}
