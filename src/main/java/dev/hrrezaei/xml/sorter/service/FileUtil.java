package dev.hrrezaei.xml.sorter.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {

    /**
     * Reads the content from an {@code InputStream} into a {@code String}.
     *
     * @param inputStream the {@code InputStream} to read from
     * @return the content as a {@code String}
     * @throws IOException if an error occurs during reading
     */
    public static String readInputStream(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8192];
            int length;
            while ((length = reader.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, length);
            }
            return sb.toString();
        }
    }
}
