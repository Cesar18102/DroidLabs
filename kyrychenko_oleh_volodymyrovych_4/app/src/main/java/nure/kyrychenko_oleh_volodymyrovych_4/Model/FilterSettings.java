package nure.kyrychenko_oleh_volodymyrovych_4.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterSettings implements Parcelable {
    public static final String ARG_FILTER_SETTINGS = "FILTER_SETTINGS";

    private String contentFilter = "";
    private Importance[] importanceFilter = new Importance[] { };

    public String getContentFilter() {
        return contentFilter;
    }

    public void setContentFilter(String contentFilter) {
        this.contentFilter = contentFilter;
    }

    public Importance[] getImportanceFilter() {
        return importanceFilter;
    }

    public void setImportanceFilter(Importance[] importanceFilter) {
        this.importanceFilter = importanceFilter;
    }

    public FilterSettings() {
        
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentFilter);
        dest.writeArray(importanceFilter);
    }

    protected FilterSettings(Parcel in) {
        contentFilter = in.readString();
        importanceFilter = (Importance[])in.readArray(Importance.class.getClassLoader());
    }

    public static final Creator<FilterSettings> CREATOR = new Creator<FilterSettings>() {
        @Override
        public FilterSettings createFromParcel(Parcel in) {
            return new FilterSettings(in);
        }

        @Override
        public FilterSettings[] newArray(int size) {
            return new FilterSettings[size];
        }
    };

    public boolean isNoteMatches(INote note) {
        String title = note.getTitle();
        String description = note.getDescription();

        boolean titleContentBothEmpty = (title == null || title.isEmpty()) && (contentFilter == null || contentFilter.isEmpty());
        boolean descriptionContentBothEmpty = (description == null || description.isEmpty()) && (contentFilter == null || contentFilter.isEmpty());

        boolean titleMatches = titleContentBothEmpty || (title != null && contentFilter != null && title.contains(contentFilter));
        boolean descriptionMatches = descriptionContentBothEmpty || (description != null && contentFilter != null && description.contains(contentFilter));

        boolean contentMatches = titleMatches || descriptionMatches;

        if(note.getImportance() == null) {
            return contentMatches;
        }

        boolean importanceMatches = false;
        for (Importance importance : importanceFilter) {
            importanceMatches |= importance == note.getImportance();
        }

        return contentMatches && importanceMatches;
    }
}
