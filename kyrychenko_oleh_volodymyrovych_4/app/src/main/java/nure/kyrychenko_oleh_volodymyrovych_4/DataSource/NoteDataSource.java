package nure.kyrychenko_oleh_volodymyrovych_4.DataSource;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;

import nure.kyrychenko_oleh_volodymyrovych_4.Model.INote;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.Note;

public class NoteDataSource extends DataSourceBase implements INoteDataSource {
    private final static Gson gson = new Gson();

    public NoteDataSource(URI dataSourceFilePath) throws IOException {
        super(dataSourceFilePath);
    }

    @Override
    public INote[] getAllNotes() {
        String json = super.Read();

        if(json == null || json.isEmpty()) {
            return new INote[] { };
        }

        return gson.fromJson(json, Note[].class);
    }

    @Override
    public void saveAllNotes(INote[] notes) throws IOException {
        if (notes == null) {
            Save("[]");
            return;
        }

        String json = gson.toJson(notes);
        Save(json);
    }
}
