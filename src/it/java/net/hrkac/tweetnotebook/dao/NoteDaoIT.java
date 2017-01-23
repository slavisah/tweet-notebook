package net.hrkac.tweetnotebook.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
 
    // TODO 07 add_NewNote_ShouldAddNoteAndGenerateId

    // TODO 08 findOne_NoteFound_ShouldReturnOneNoteWithId
    
    @Test
    public void findAll_NotesFound_ShouldReturnListOfNotes() {
        // TODO 01 findAll_NotesFound_ShouldReturnListOfNotes
        fail("Not yet implemented");
    }
    
    // TODO 09 update_NoteFound_ShouldUpdateNoteEntry
    
    // TODO 10 deleteById_NoteFound_ShouldDeleteNoteEntry
 
    // TODO 02 findByTitle_NotesFound_ShouldReturnListOfNotesWithTitle

}
