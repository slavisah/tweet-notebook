package net.hrkac.tweetnotebook.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.hrkac.tweetnotebook.dao.NoteDao;
import net.hrkac.tweetnotebook.dto.NoteDTO;
import net.hrkac.tweetnotebook.exception.NoteNotFoundException;
import net.hrkac.tweetnotebook.model.Note;
import net.hrkac.tweetnotebook.model.TestNoteBuilder;

public class NoteServiceTest {
    
    private static final Long ID = 1L;
    private static final String TITLE = "title";
    private static final String TITLE_UPDATED = "updatedTitle";
    private static final String TEXT = "text";
    private static final String TEXT_UPDATED = "updatedText";
    
    private NoteService noteService;
    private NoteDao noteDaoMock;
    
    @Before
    public void setup() {
        noteDaoMock = mock(NoteDao.class);
        noteService = new NoteService(noteDaoMock);
    }

    // TODO 11 add_NewNote_ShouldSaveNote
    
    @Test
    public void findAll_ShouldReturnListOfNotes() {
        // TODO 04 findAll_ShouldReturnListOfNotes
        fail("Not yet implemented");
    }
    
    // TODO 12 findById_NoteFound_ShouldReturnFoundNote
    
    // TODO 13 findById_NoteEntryNotFound_ShouldThrowException

    // TODO 14 update_NoteFound_ShouldUpdateNote
    
    // TODO 15 update_NoteNotFound_ShouldThrowException
    
    // TODO 16 deleteById_NoteFound_ShouldDeleteNoteEntryAndReturnIt

    // TODO 17 deleteById_NoteNotFound_ShouldThrowException

}
