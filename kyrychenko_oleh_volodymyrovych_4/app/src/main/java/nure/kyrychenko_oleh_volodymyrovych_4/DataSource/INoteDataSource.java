package nure.kyrychenko_oleh_volodymyrovych_4.DataSource;

import java.io.IOException;

import nure.kyrychenko_oleh_volodymyrovych_4.Model.INote;

public interface INoteDataSource extends IDataSource {
    INote[] getAllNotes();
    void saveAllNotes(INote[] notes) throws IOException;
}
