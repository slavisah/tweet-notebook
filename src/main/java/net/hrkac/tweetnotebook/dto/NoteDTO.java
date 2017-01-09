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
    
    protected NoteDTO() {}

    public NoteDTO(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.text = builder.text;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public static class Builder {

        private Long id;
        private String title;
        private String text;

        public Builder() {
        }

        public NoteDTO build() {
            return new NoteDTO(this);
        }
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
