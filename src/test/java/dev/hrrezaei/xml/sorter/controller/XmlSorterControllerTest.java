package dev.hrrezaei.xml.sorter.controller;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.stream.Stream;

import static dev.hrrezaei.xml.sorter.service.FileUtil.readInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
@ExtendWith(SpringExtension.class)
public class XmlSorterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Provides a stream of input and expected output file paths.
     *
     * @return a Stream of Arguments containing inputFilePath and expectedOutputFilePath
     */
    static Stream<Arguments> xmlFilesProvider() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // Find all input XML files under src/test/resources/xml/*-input.xml
        Resource[] inputResources = resolver.getResources("classpath:xml/*-input.xml");

        return Stream.of(inputResources)
                .map(inputResource -> {
                    try {
                        String inputFilename = inputResource.getFilename();
                        assert inputFilename != null;
                        String expectedOutputFilename = inputFilename.replace("-input.xml", "-output.xml");
                        String inputFilePath = "classpath:xml/" + inputFilename;
                        String expectedOutputFilePath = "classpath:xml/" + expectedOutputFilename;
                        return Arguments.of(inputFilePath, expectedOutputFilePath);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Tests the sortXmlFile endpoint with various input XML files.
     *
     * @param inputFilePath          the path to the input XML file
     * @param expectedOutputFilePath the path to the expected output XML file
     * @throws Exception if an error occurs during the test
     */
    @ParameterizedTest(name = "Test with input file: {0}")
    @MethodSource("xmlFilesProvider")
    void testSortXmlFile(String inputFilePath, String expectedOutputFilePath) throws Exception {
        // Load the input XML file
        Resource inputResource = resourceLoader.getResource(inputFilePath);
        byte[] inputBytes = StreamUtils.copyToByteArray(inputResource.getInputStream());

        // Load the expected output XML file
        Resource expectedOutputResource = resourceLoader.getResource(expectedOutputFilePath);
        String expectedOutput = readInputStream(expectedOutputResource.getInputStream());

        // Create a MockMultipartFile to simulate file upload
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                inputResource.getFilename(),
                MediaType.APPLICATION_XML_VALUE,
                inputBytes
        );

        // Perform the POST request to the controller endpoint
        String responseContent = mockMvc.perform(multipart("/api/v1/sorter/xml")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .trim();
        log.info("Test with input file [{}] has resulted in {}", inputFilePath, responseContent);

        // Compare the response content with the expected output
        assertEquals(expectedOutput, responseContent, "The sorted XML does not match the expected output.");
    }
}