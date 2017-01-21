package net.hrkac.tweetnotebook.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import net.hrkac.tweetnotebook.config.ExampleApplicationContext;
import net.hrkac.tweetnotebook.model.Note;

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
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ExampleApplicationContext.class})
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
    @ExpectedDatabase(value = "noteData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add_NewNote_ShouldAddNoteAndReturnAddedEntry() {
        Note model = Note.getBuilder("Example 3").text("Lorem ipsum").build();
        noteDao.save(model);
    }

    @Test
    @ExpectedDatabase("noteData.xml")
    public void findOne_NoteFound_ShouldReturnOneNoteWithId() {
        Note found = noteDao.findOne(1L);
        assertNotNull(found);
        assertThat(found.getId(), equalTo(1L));
        assertThat(found.getText(), equalTo("Lorem ipsum"));
    }
    
    @Test
    @ExpectedDatabase(value = "noteData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_NoteFound_ShouldUpdateNoteEntry() {
        Note model = noteDao.findOne(1L);
        model.update("Text", "Title");
        noteDao.saveAndFlush(model);
    }
    
    @Test
    @ExpectedDatabase("noteData-delete-expected.xml")
    public void deleteById_NoteFound_ShouldDeleteNoteEntry() {
        Note deleted = noteDao.findOne(1L);
        noteDao.delete(deleted);
    }

}
