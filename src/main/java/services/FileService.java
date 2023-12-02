package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileService {
    public static final String FILE_PATH = "src/main/resources/%s.txt";

    static String readStringFromDocument(String fileName) {
        try {
            java.io.File file = new java.io.File(FILE_PATH.formatted(fileName));
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line=br.readLine()) != null){
                sb.append(line);
                sb.append('\n');
            }
            fr.close();

            return sb.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
