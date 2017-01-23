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
    
    @Test
    public void add_NewNote_ShouldSaveNote() {
        NoteDTO dto = NoteDTO.getBuilder().title(TITLE).text(TEXT).build();

        noteService.add(dto);

        ArgumentCaptor<Note> noteArgument = ArgumentCaptor.forClass(Note.class);
        verify(noteDaoMock, times(1)).save(noteArgument.capture());
        verifyNoMoreInteractions(noteDaoMock);

        Note model = noteArgument.getValue();

        assertNull(model.getId());        
        assertThat(model.getTitle(), is(dto.getTitle()));
        assertThat(model.getText(), is(dto.getText()));
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
    
    @Test
    public void findById_NoteFound_ShouldReturnFoundNote() throws NoteNotFoundException {
        Note model = new TestNoteBuilder().id(ID).title(TITLE).text(TEXT).build();

        when(noteDaoMock.findOne(ID)).thenReturn(model);

        Note actual = noteService.findById(ID);

        verify(noteDaoMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(noteDaoMock);

        assertThat(actual, is(model));
    }

    @Test(expected = NoteNotFoundException.class)
    public void findById_NoteEntryNotFound_ShouldThrowException() throws NoteNotFoundException {
        when(noteDaoMock.findOne(ID)).thenReturn(null);

        noteService.findById(ID);

        verify(noteDaoMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(noteDaoMock);
    }
    
    @Test
    public void update_NoteFound_ShouldUpdateNote() throws NoteNotFoundException {
        NoteDTO dto = NoteDTO.getBuilder().id(ID).title(TITLE_UPDATED).text(TEXT_UPDATED).build();

        Note model = new TestNoteBuilder().id(ID).title(TITLE).text(TEXT).build();

        when(noteDaoMock.findOne(dto.getId())).thenReturn(model);

        noteService.update(dto);

        verify(noteDaoMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(noteDaoMock);

        assertThat(model.getId(), is(dto.getId()));
        assertThat(model.getTitle(), is(dto.getTitle()));
        assertThat(model.getText(), is(dto.getText()));
    }

    @Test(expected = NoteNotFoundException.class)
    public void update_NoteNotFound_ShouldThrowException() throws NoteNotFoundException {
        NoteDTO dto = NoteDTO.getBuilder().id(ID).title(TITLE_UPDATED).text(TEXT).build();

        when(noteDaoMock.findOne(dto.getId())).thenReturn(null);

        noteService.update(dto);

        verify(noteDaoMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(noteDaoMock);
    }
    
    @Test
    public void deleteById_NoteFound_ShouldDeleteNoteEntryAndReturnIt() throws NoteNotFoundException {
        Note model = new TestNoteBuilder().id(ID).title(TITLE).text(TEXT).build();

        when(noteDaoMock.findOne(ID)).thenReturn(model);

        Note actual = noteService.deleteById(ID);

        verify(noteDaoMock, times(1)).findOne(ID);
        verify(noteDaoMock, times(1)).delete(model);
        verifyNoMoreInteractions(noteDaoMock);

        assertThat(actual, is(model));
    }

    @Test(expected = NoteNotFoundException.class)
    public void deleteById_NoteEntryNotFound_ShouldThrowException() throws NoteNotFoundException {
        when(noteDaoMock.findOne(ID)).thenReturn(null);

        noteService.deleteById(ID);

        verify(noteDaoMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(noteDaoMock);
    }

}
