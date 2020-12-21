package nure.kyrychenko_oleh_volodymyrovych_5.DataSource;

import nure.kyrychenko_oleh_volodymyrovych_5.Model.FilterSettings;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.INote;

public interface ISyncNoteDataSource {
    INote[] getAllNotes(FilterSettings filters);
    INote getById(String id);
    void addNote(INote note);
    void updateNote(INote note);
    void deleteNote(INote note);
}
