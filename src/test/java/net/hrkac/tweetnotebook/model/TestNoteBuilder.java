package net.hrkac.tweetnotebook.model;

import org.springframework.test.util.ReflectionTestUtils;

public class TestNoteBuilder {

    private Note model;

    public TestNoteBuilder() {
        model = new Note();
    }

    public TestNoteBuilder id(Long id) {
        ReflectionTestUtils.setField(model, "id", id);
        return this;
    }

    public TestNoteBuilder text(String text) {
        model.update(text, model.getTitle());
        return this;
    }

    public TestNoteBuilder title(String title) {
        model.update(model.getText(), title);
        return this;
    }

    public Note build() {
        return model;
    }
}
