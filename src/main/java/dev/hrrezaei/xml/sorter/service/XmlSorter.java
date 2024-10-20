package dev.hrrezaei.xml.sorter.service;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * Interface for sorting XML content to ensure consistent ordering of elements and attributes.
 * This facilitates effective comparison of XML files by standard text editors.
 */
public interface XmlSorter {

    /**
     * Sorts the given XML content represented as a {@code String}.
     * <p>
     * This method parses the XML content, sorts the elements and attributes,
     * and returns the sorted XML as a {@code String}.
     *
     * @param xmlContent the XML content to be sorted
     * @return the sorted XML content as a {@code String}
     * @throws XmlSortingException if an error occurs during parsing or sorting
     */
    default String sort(String xmlContent) throws XmlSortingException {
        try {
            Document document = parseXmlContent(xmlContent);
            return sort(document);
        } catch (Exception e) {
            throw new XmlSortingException("Error sorting XML content from String", e);
        }
    }

    /**
     * Sorts the XML content read from the given {@code InputStream}.
     * <p>
     * The method reads the XML content from the input stream, sorts the elements and attributes,
     * and returns the sorted XML as a {@code String}.
     *
     * @param inputStream the {@code InputStream} containing the XML content to be sorted
     * @return the sorted XML content as a {@code String}
     * @throws XmlSortingException if an error occurs during reading, parsing, or sorting
     */
    default String sort(InputStream inputStream) throws XmlSortingException {
        try {
            String xmlContent = readInputStream(inputStream);
            return sort(xmlContent);
        } catch (Exception e) {
            throw new XmlSortingException("Error sorting XML content from InputStream", e);
        }
    }

    /**
     * Sorts the XML content from the specified {@code File}.
     * <p>
     * This method reads the XML content from the file, sorts the elements and attributes,
     * and returns the sorted XML as a {@code String}.
     *
     * @param file the {@code File} containing the XML content to be sorted
     * @return the sorted XML content as a {@code String}
     * @throws XmlSortingException if an error occurs during file access, parsing, or sorting
     */
    default String sort(File file) throws XmlSortingException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return sort(inputStream);
        } catch (Exception e) {
            throw new XmlSortingException("Error sorting XML content from File", e);
        }
    }

    /**
     * Sorts the given XML {@code Document} object.
     * <p>
     * This method sorts the elements and attributes within the provided {@code Document}
     * and returns the sorted XML as a {@code String}.
     *
     * @param document the XML {@code Document} to be sorted
     * @return the sorted XML content as a {@code String}
     * @throws XmlSortingException if an error occurs during sorting or transformation
     */
    String sort(Document document) throws XmlSortingException;

    /**
     * Parses the XML content from a {@code String} into a {@code Document} object.
     *
     * @param xmlContent the XML content as a {@code String}
     * @return the parsed XML as a {@code Document}
     * @throws Exception if an error occurs during parsing
     */
    private Document parseXmlContent(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        // Disable external entities for security
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        try (InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes())) {
            return builder.parse(inputStream);
        }
    }

    /**
     * Reads the content from an {@code InputStream} into a {@code String}.
     *
     * @param inputStream the {@code InputStream} to read from
     * @return the content as a {@code String}
     * @throws IOException if an error occurs during reading
     */
    private String readInputStream(InputStream inputStream) throws IOException {
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