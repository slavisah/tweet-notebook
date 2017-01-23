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
    
    @Test
    public void add_EmptyNote_ShouldReturnValidationErrorForTitle() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().build();

        mockMvc.perform(post("/api/note")
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

        mockMvc.perform(post("/api/note").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(dto)))
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

        mockMvc.perform(post("/api/note")
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

        mockMvc.perform(get("/api/note"))
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
    
    @Test
    public void findById_NoteFound_ShouldReturnFoundEntry() throws Exception {
        Note found = new TestNoteBuilder().id(1L).title("Example 1").text("Lorem ipsum").build();

        when(noteServiceMock.findById(1L)).thenReturn(found);

        mockMvc.perform(get("/api/note/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Example 1")))
                .andExpect(jsonPath("$.text", is("Lorem ipsum")));

        verify(noteServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(noteServiceMock);
    }

    @Test
    public void findById_NoteNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(noteServiceMock.findById(1L)).thenThrow(new NoteNotFoundException(""));

        mockMvc.perform(get("/api/note/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(noteServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(noteServiceMock);
    }
    
    @Test
    public void update_EmptyNote_ShouldReturnValidationErrorForTitle() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().id(1L).build();

        mockMvc.perform(put("/api/note/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].path", is("title")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("The title cannot be empty.")));
        // because of the validation errors it never gets to point of interaction with service method update() 
        verifyZeroInteractions(noteServiceMock);
    }

    @Test
    public void update_TitleAndTextAreTooLong_ShouldReturnValidationErrorsForTitleAndText() throws Exception {
        String title = TestUtil.createStringWithLength(Note.MAX_LENGTH_TITLE + 1);
        String text = TestUtil.createStringWithLength(Note.MAX_LENGTH_TEXT + 1);

        NoteDTO dto = NoteDTO.getBuilder().id(1L).title(title).text(text).build();

        mockMvc.perform(put("/api/note/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("title", "text")))
                .andExpect(jsonPath("$.fieldErrors[*].message", containsInAnyOrder(
                        "The maximum length of the text is 140 characters.",
                        "The maximum length of the title is 50 characters."
                )));
        // because of the validation errors it never gets to point of interaction with service method update()
        verifyZeroInteractions(noteServiceMock);
    }

    @Test
    public void update_NoteNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().id(10L).title("title").text("text").build();

        when(noteServiceMock.update(any(NoteDTO.class))).thenThrow(new NoteNotFoundException(""));

        mockMvc.perform(put("/api/note/{id}", 10L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isNotFound());

        ArgumentCaptor<NoteDTO> dtoCaptor = ArgumentCaptor.forClass(NoteDTO.class);
        verify(noteServiceMock, times(1)).update(dtoCaptor.capture());
        verifyNoMoreInteractions(noteServiceMock);

        NoteDTO dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(10L));
        assertThat(dtoArgument.getTitle(), is("title"));
        assertThat(dtoArgument.getText(), is("text"));
    }

    @Test
    public void update_NoteFound_ShouldUpdateNoteAndReturnIt() throws Exception {
        NoteDTO dto = NoteDTO.getBuilder().id(1L).title("title").text("text").build();

        Note updated = new TestNoteBuilder().id(1L).title("title").text("text").build();

        when(noteServiceMock.update(any(NoteDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/note/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.text", is("text")));

        ArgumentCaptor<NoteDTO> dtoCaptor = ArgumentCaptor.forClass(NoteDTO.class);
        verify(noteServiceMock, times(1)).update(dtoCaptor.capture());
        verifyNoMoreInteractions(noteServiceMock);

        NoteDTO dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(1L));
        assertThat(dtoArgument.getTitle(), is("title"));
        assertThat(dtoArgument.getText(), is("text"));
    }
    
    @Test
    public void deleteById_NoteFound_ShouldDeleteNoteAndReturnIt() throws Exception {
        Note deleted = new TestNoteBuilder().id(1L).title("Example 1").text("Lorem ipsum").build();

        when(noteServiceMock.deleteById(1L)).thenReturn(deleted);

        mockMvc.perform(delete("/api/note/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Example 1")))
                .andExpect(jsonPath("$.text", is("Lorem ipsum")));

        verify(noteServiceMock, times(1)).deleteById(1L);
        verifyNoMoreInteractions(noteServiceMock);
    }
}
