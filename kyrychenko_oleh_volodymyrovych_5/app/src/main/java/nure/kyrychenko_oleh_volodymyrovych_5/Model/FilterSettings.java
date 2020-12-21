package nure.kyrychenko_oleh_volodymyrovych_5.Model;

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
}
