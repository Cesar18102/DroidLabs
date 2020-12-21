package nure.kyrychenko_oleh_volodymyrovych_4.Model;

import java.util.Date;

public class Note implements INote {
    private String id;
    private String imageUri;
    private String title;
    private String description;
    private Date destinationDate;
    private Date destinationTime;
    private Date creationDateTime;
    private Importance importance;

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Importance getImportance() {
        return this.importance;
    }

    @Override
    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    @Override
    public Date getDestinationDate() {
        return this.destinationDate;
    }

    @Override
    public void setDestinationDate(Date date) {
        this.destinationDate = date;
    }

    @Override
    public Date getDestinationTime() {
        return this.destinationTime;
    }

    @Override
    public void setDestinationTime(Date time) {
        this.destinationTime = time;
    }

    @Override
    public Date getCreationDateTime() { return this.creationDateTime; }

    @Override
    public void setCreationDateTime(Date dateTime) { this.creationDateTime = dateTime; }

    @Override
    public String getImageUri() {
        return this.imageUri;
    }

    @Override
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }
}
