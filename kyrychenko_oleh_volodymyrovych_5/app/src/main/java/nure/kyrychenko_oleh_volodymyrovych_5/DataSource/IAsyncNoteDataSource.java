package nure.kyrychenko_oleh_volodymyrovych_5.DataSource;

import nure.kyrychenko_oleh_volodymyrovych_5.Listeners.OnAsyncNoteQueryDoneListener;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.FilterSettings;

public interface IAsyncNoteDataSource {
    void getAllNotesAsync(FilterSettings filters, OnAsyncNoteQueryDoneListener listener);
    void getByIdAsync(String id, OnAsyncNoteQueryDoneListener listener);
}
