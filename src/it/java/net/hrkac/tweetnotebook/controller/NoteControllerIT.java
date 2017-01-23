package net.hrkac.tweetnotebook.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import net.hrkac.tweetnotebook.config.TweetNotebookApplicationContext;
import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.config.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TweetNotebookApplicationContext.class})
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("noteData.xml")
@ActiveProfiles("it")
public class NoteControllerIT {
    
    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    @ExpectedDatabase(value="noteData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add_NewNote_ShouldAddNoteAndReturnAddedEntry() throws Exception {
        NoteDTO added = NoteDTO.getBuilder().title("Example 3").text("Lorem ipsum").build();

        mockMvc.perform(post("/api/note")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(added))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("Example 3")))
                .andExpect(jsonPath("$.text", is("Lorem ipsum")));
    }

    @Test
    @ExpectedDatabase("noteData.xml")
    public void findAll_NotesFound_ShouldReturnFoundNoteEntries() throws Exception {
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
    }
    
    @Test
    @ExpectedDatabase(value="noteData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_NoteFound_ShouldUpdateNoteAndReturnIt() throws Exception {
        NoteDTO updated = NoteDTO.getBuilder().id(1L).title("Title").text("Text").build();

        mockMvc.perform(put("/api/note/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updated))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.text", is("Text")));
    }
    
    @Test
    @ExpectedDatabase("noteData-delete-expected.xml")
    public void deleteById_NoteFound_ShouldDeleteNoteAndReturnIt() throws Exception {
        mockMvc.perform(delete("/api/note/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Example 1")))
                .andExpect(jsonPath("$.text", is("Lorem ipsum")));
    }
}
