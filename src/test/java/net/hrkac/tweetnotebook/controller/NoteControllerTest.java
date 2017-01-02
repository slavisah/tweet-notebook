package net.hrkac.tweetnotebook.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.hrkac.tweetnotebook.config.WebAppContext;
import net.hrkac.tweetnotebook.TestUtil;
import net.hrkac.tweetnotebook.config.TestContext;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.model.NoteBuilder;
import net.hrkac.tweetnotebook.service.NoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class NoteControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private NoteService noteServiceMock;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(noteServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void findAll_NotesFound_ShouldReturnFoundNoteEntries() throws Exception {
        Note first = new NoteBuilder().id(1L).title("Example 1").text("Lorem ipsum").build();
        Note second = new NoteBuilder().id(2L).title("Example 2").text("Lorem ipsum").build();

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
}
