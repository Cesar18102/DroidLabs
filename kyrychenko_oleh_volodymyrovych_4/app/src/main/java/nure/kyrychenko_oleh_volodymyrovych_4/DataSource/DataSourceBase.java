package nure.kyrychenko_oleh_volodymyrovych_4.DataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URI;

public class DataSourceBase implements IDataSource {
    private File file;

    public DataSourceBase(URI dataSourceFilePath) throws IOException {
        this.file = new File(dataSourceFilePath);

        if(!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public String Read() {
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            reader.close();

            return builder.toString();
        } catch (IOException ex) {
            return "";
        }
    }

    @Override
    public void Save(String data) throws IOException {
        FileWriter writer = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        bufferedWriter.write(data);

        bufferedWriter.close();
        writer.close();
    }
}
