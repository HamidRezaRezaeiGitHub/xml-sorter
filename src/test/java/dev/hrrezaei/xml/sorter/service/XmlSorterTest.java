package dev.hrrezaei.xml.sorter.service;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
@SpringBootTest
public class XmlSorterTest {

    @Autowired
    private XmlSorter xmlSorter;

    @Autowired
    private ResourceLoader resourceLoader;

    private void testFile(String inputFileLocation, String expectedFileLocation) throws Exception {
        // Load the input XML file
        Resource inputResource = resourceLoader.getResource(inputFileLocation);
        String inputXml = Files.readString(inputResource.getFile().toPath(), StandardCharsets.UTF_8);

        // Load the expected output XML file
        Resource expectedResource = resourceLoader.getResource(expectedFileLocation);
        String expectedXml = Files.readString(expectedResource.getFile().toPath(), StandardCharsets.UTF_8);

        // Perform the sorting
        String sortedXml = xmlSorter.sort(inputResource.getFile());
        log.info("Sorting the file [{}] has resulted in \n{}", inputFileLocation, sortedXml);

        // Normalize whitespace for comparison
        String normalizedExpectedXml = expectedXml.replaceAll("\\s+", "");
        String normalizedSortedXml = sortedXml.replaceAll("\\s+", "");

        // Assert that the sorted XML matches the expected output
        assertEquals(normalizedExpectedXml, normalizedSortedXml, "The sorted XML does not match the expected output.");

        if (!inputFileLocation.equals(expectedFileLocation)) {
            // The expected file should match itself, too.
            testFile(expectedFileLocation, expectedFileLocation);
        }
    }

    @Test
    public void testSiblingNodeOrdering() throws Exception {
        testFile("classpath:xml/siblingNodeOrdering-input.xml", "classpath:xml/siblingNodeOrdering-output.xml");
    }

    @Test
    public void testAttributeOrdering() throws Exception {
        testFile("classpath:xml/attributeOrdering-input.xml", "classpath:xml/attributeOrdering-output.xml");
    }

    @Test
    public void testNodeComparisonByAttributeNames() throws Exception {
        testFile("classpath:xml/nodeComparisonByAttributeNames-input.xml", "classpath:xml/nodeComparisonByAttributeNames-output.xml");
    }

    @Test
    public void testNodeComparisonByAttributeValues() throws Exception {
        testFile("classpath:xml/nodeComparisonByAttributeValues-input.xml", "classpath:xml/nodeComparisonByAttributeValues-output.xml");
    }

    @Test
    public void testNodeComparisonByTextContent() throws Exception {
        testFile("classpath:xml/nodeComparisonByTextContent-input.xml", "classpath:xml/nodeComparisonByTextContent-output.xml");
    }

    @Test
    public void testCommentPreservation() throws Exception {
        testFile("classpath:xml/commentPreservation-input.xml", "classpath:xml/commentPreservation-output.xml");
    }

    @Test
    public void testNamespaceHandling() throws Exception {
        testFile("classpath:xml/namespaceHandling-input.xml", "classpath:xml/namespaceHandling-output.xml");
    }

    @Test
    public void testEntityAndWhitespacePreservation() throws Exception {
        testFile("classpath:xml/entityAndWhitespacePreservation-input.xml", "classpath:xml/entityAndWhitespacePreservation-output.xml");
    }

    @Test
    public void testInvalidXmlHandling() {
        assertThrows(XmlSortingException.class, () -> {
            Resource inputResource = resourceLoader.getResource("classpath:xml/invalidXmlHandling-input.xml");
            xmlSorter.sort(inputResource.getFile());
        });
    }

    @Test
    public void testOutputFormatting() throws Exception {
        testFile("classpath:xml/outputFormatting-input.xml", "classpath:xml/outputFormatting-output.xml");
    }

    @Test
    public void testCDataPreservation() throws Exception {
        testFile("classpath:xml/cdataPreservation-input.xml", "classpath:xml/cdataPreservation-output.xml");
    }

    @Test
    public void testProcessingInstructionsPreservation() throws Exception {
        testFile("classpath:xml/processingInstructionsPreservation-input.xml", "classpath:xml/processingInstructionsPreservation-output.xml");
    }
}