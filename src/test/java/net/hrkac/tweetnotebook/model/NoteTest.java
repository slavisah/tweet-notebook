package net.hrkac.tweetnotebook.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoteTest {
    
    private static final String TITLE = "der Titel";
    private static final String TEXT = "der Text";
    
    @Test
    public void buildWithMandatoryFields() {
        // Arrange & Act
        Note note = Note.getBuilder(TITLE).build();
        // Assert
        assertNull(note.getId());
        assertNull(note.getText());
        assertNull(note.getCreatedOn());
        assertNull(note.getModifiedOn());
        assertEquals(TITLE, note.getTitle());
    }
    
    @Test
    public void buildWithAllFields() {
        // Arrange & Act
        Note note = Note.getBuilder(TITLE).text(TEXT).build();
        // Assert
        assertNull(note.getId());
        assertNull(note.getCreatedOn());
        assertNull(note.getModifiedOn());
        assertEquals(TITLE, note.getTitle());
        assertEquals(TEXT, note.getText());
    }

    @Test
    public void prePersist() {
        // Arrange
        Note note = Note.getBuilder(TITLE).build();
        // Act
        note.prePersist();
        // Assert
        assertNull(note.getId());
        assertNull(note.getText());
        assertNotNull(note.getCreatedOn());
        assertNotNull(note.getModifiedOn());
        assertEquals(TITLE, note.getTitle());
        assertEquals(note.getCreatedOn(), note.getModifiedOn());
    }

    @Test
    public void preUpdate() {
        // Arrange
        Note note = Note.getBuilder(TITLE).build();
        // Act
        note.prePersist();
        pause(1000);
        note.preUpdate();
        // Assert
        assertNull(note.getId());
        assertNull(note.getText());
        assertNotNull(note.getCreatedOn());
        assertNotNull(note.getModifiedOn());
        assertEquals(TITLE, note.getTitle());
        assertTrue(note.getModifiedOn().isAfter(note.getCreatedOn()));
    }
    
    private void pause(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}
