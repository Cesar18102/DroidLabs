package nure.kyrychenko_oleh_volodymyrovych_4.Model;

import java.util.Date;

public interface INote {
    String getId();
    void setId(String id);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);

    Importance getImportance();
    void setImportance(Importance importance);

    Date getDestinationDate();
    void setDestinationDate(Date date);

    Date getDestinationTime();
    void setDestinationTime(Date time);

    Date getCreationDateTime();
    void setCreationDateTime(Date dateTime);

    String getImageUri();
    void setImageUri(String imageUri);
}
