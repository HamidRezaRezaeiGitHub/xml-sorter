package dev.hrrezaei.xml.sorter.service;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;

/**
 * Interface for sorting XML content to ensure consistent ordering of elements and attributes.
 * This facilitates effective comparison of XML files.
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
    String sort(String xmlContent) throws XmlSortingException;

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
    String sort(InputStream inputStream) throws XmlSortingException;

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
    String sort(File file) throws XmlSortingException;

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

}