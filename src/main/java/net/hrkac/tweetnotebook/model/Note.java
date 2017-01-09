package net.hrkac.tweetnotebook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name="note")
public class Note {

    public static final int MAX_LENGTH_TEXT = 140;
    public static final int MAX_LENGTH_TITLE = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_on", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name = "text", nullable = true, length = MAX_LENGTH_TEXT)
    private String text;

    @Column(name = "modified_on", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedOn;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @Version
    private long version;
    
    protected Note() {
        
    }

    private Note(Builder builder) {
        this.title = builder.title;
        this.text = builder.text;
    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public String getText() {
        return text;
    }

    public DateTime getModifiedOn() {
        return modifiedOn;
    }

    public String getTitle() {
        return title;
    }

    public long getVersion() {
        return version;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        createdOn = now;
        modifiedOn = now;
    }

    @PreUpdate
    public void preUpdate() {
        modifiedOn = DateTime.now();
    }

    public void update(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public static class Builder {

        private String title;
        private String text;

        public Builder(String title) {
            this.title = title;
        }

        public Note build() {
            return new Note(this);
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
