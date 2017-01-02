package net.hrkac.tweetnotebook.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import net.hrkac.tweetnotebook.model.Note;

public class NoteDTO {

    private Long id;

    @Length(max = Note.MAX_LENGTH_TEXT)
    private String text;

    @NotEmpty
    @Length(max = Note.MAX_LENGTH_TITLE)
    private String title;

    public NoteDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
