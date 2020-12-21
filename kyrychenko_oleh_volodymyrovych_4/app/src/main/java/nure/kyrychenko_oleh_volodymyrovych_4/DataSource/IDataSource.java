package nure.kyrychenko_oleh_volodymyrovych_4.DataSource;

import java.io.IOException;

public interface IDataSource {
    String Read();
    void Save(String data) throws IOException;
}
