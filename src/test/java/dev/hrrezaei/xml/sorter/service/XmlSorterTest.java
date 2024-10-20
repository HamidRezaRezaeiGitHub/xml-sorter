package dev.hrrezaei.xml.sorter.service;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for XmlSorter implementations.
 */
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

        // Normalize whitespace for comparison
        String normalizedExpectedXml = expectedXml.replaceAll("\\s+", "");
        String normalizedSortedXml = sortedXml.replaceAll("\\s+", "");

        // Assert that the sorted XML matches the expected output
        assertEquals(normalizedExpectedXml, normalizedSortedXml, "The sorted XML does not match the expected output.");
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

    /**
     * Repeating the other test scenarios should lead to the same results.
     * The output file should be resulted in themselves.
     */
    @Test
    public void testDeterministicOutput() throws Exception {
        testSiblingNodeOrdering();
        testFile("classpath:xml/siblingNodeOrdering-output.xml", "classpath:xml/siblingNodeOrdering-output.xml");

        testAttributeOrdering();
        testFile("classpath:xml/attributeOrdering-output.xml", "classpath:xml/attributeOrdering-output.xml");

        testNodeComparisonByAttributeNames();
        testFile("classpath:xml/nodeComparisonByAttributeNames-output.xml", "classpath:xml/nodeComparisonByAttributeNames-output.xml");

        testNodeComparisonByAttributeValues();
        testFile("classpath:xml/nodeComparisonByAttributeValues-output.xml", "classpath:xml/nodeComparisonByAttributeValues-output.xml");

        testNodeComparisonByTextContent();
        testFile("classpath:xml/nodeComparisonByTextContent-output.xml", "classpath:xml/nodeComparisonByTextContent-output.xml");

        testCommentPreservation();
        testFile("classpath:xml/commentPreservation-output.xml", "classpath:xml/commentPreservation-output.xml");

        testNamespaceHandling();
        testFile("classpath:xml/namespaceHandling-output.xml", "classpath:xml/namespaceHandling-output.xml");

        testEntityAndWhitespacePreservation();
        testFile("classpath:xml/entityAndWhitespacePreservation-output.xml", "classpath:xml/entityAndWhitespacePreservation-output.xml");

        testInvalidXmlHandling();

        testOutputFormatting();
        testFile("classpath:xml/outputFormatting-output.xml", "classpath:xml/outputFormatting-output.xml");

        testCDataPreservation();
        testFile("classpath:xml/cdataPreservation-output.xml", "classpath:xml/cdataPreservation-output.xml");

        testProcessingInstructionsPreservation();
        testFile("classpath:xml/processingInstructionsPreservation-output.xml", "classpath:xml/processingInstructionsPreservation-output.xml");

    }
}