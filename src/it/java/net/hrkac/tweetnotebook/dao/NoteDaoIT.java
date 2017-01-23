package net.hrkac.tweetnotebook.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import net.hrkac.tweetnotebook.config.TweetNotebookApplicationContext;
import net.hrkac.tweetnotebook.model.Note;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TweetNotebookApplicationContext.class})
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("noteData.xml")
@ActiveProfiles("it")
public class NoteDaoIT {
    
    @Autowired
    private NoteDao noteDao;
    
    @Test
    public void add_NewNote_ShouldAddNoteAndGenerateId() {
        Note model = Note.getBuilder("Example 3").text("Lorem ipsum").build();
        noteDao.save(model);
        assertThat(model.getId(), is(notNullValue()));
    }

    @Test
    public void findOne_NoteFound_ShouldReturnOneNoteWithId() {
        Note found = noteDao.findOne(1L);
        assertNotNull(found);
        assertThat(found.getId(), equalTo(1L));
        assertThat(found.getText(), equalTo("Lorem ipsum"));
    }
    
    @Test
    public void findAll_NotesFound_ShouldReturnListOfNotes() {
        List<Note> notes = noteDao.findAll();
        assertThat(notes, hasSize(greaterThan(0)));
    }
    
    @Test
    public void update_NoteFound_ShouldUpdateNoteEntry() {
        Note model = noteDao.findOne(1L);
        model.update("Text", "Title");
        noteDao.saveAndFlush(model);
        assertThat(model.getTitle(), is("Title"));
        assertThat(model.getText(), is("Text"));
    }
    
    @Test
    @ExpectedDatabase("noteData-delete-expected.xml")
    public void deleteById_NoteFound_ShouldDeleteNoteEntry() {
        Note deleted = noteDao.findOne(1L);
        noteDao.delete(deleted);
    }
    
    @Test
    public void findByTitle_NotesFound_ShouldReturnListOfNotesWithTitle() {
        List<Note> list = noteDao.findByTitle("Example 1");
        assertNotNull(list);
        assertThat(list, hasSize(greaterThan(0)));
    }

}
