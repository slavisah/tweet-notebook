package net.hrkac.tweetnotebook.service;

import static org.hamcrest.Matchers.is;
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

import net.hrkac.tweetnotebook.dao.NoteDao;
import net.hrkac.tweetnotebook.model.Note;

public class NoteServiceTest {
    
    private NoteService noteService;
    private NoteDao noteDaoMock;
    
    @Before
    public void setup() {
        noteDaoMock = mock(NoteDao.class);
        noteService = new NoteService(noteDaoMock);
    }

    @Test
    public void findAll_ShouldReturnListOfNotes() {
        List<Note> models = new ArrayList<>();
        when(noteDaoMock.findAll()).thenReturn(models);

        List<Note> actual = noteService.findAll();

        verify(noteDaoMock, times(1)).findAll();
        verifyNoMoreInteractions(noteDaoMock);

        assertThat(actual, is(models));
    }

}
