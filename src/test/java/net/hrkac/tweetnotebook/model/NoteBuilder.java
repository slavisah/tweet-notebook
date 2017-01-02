package net.hrkac.tweetnotebook.model;

import org.springframework.test.util.ReflectionTestUtils;

public class NoteBuilder {

    private Note model;

    public NoteBuilder() {
        model = new Note();
    }

    public NoteBuilder id(Long id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }

    public NoteBuilder text(String text) {
        model.update(text, model.getTitle());
        return this;
    }

    public NoteBuilder title(String title) {
        model.update(model.getText(), title);
        return this;
    }

    public Note build() {
        return model;
    }
}
