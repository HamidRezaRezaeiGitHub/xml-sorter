package dev.hrrezaei.xml.sorter.controller;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import dev.hrrezaei.xml.sorter.service.XmlSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/sorter")
public class XmlSorterController {

    private final XmlSorter xmlSorter;

    @Autowired
    public XmlSorterController(XmlSorter xmlSorter) {
        this.xmlSorter = xmlSorter;
    }

    /**
     * Sorts the given XML content provided in the request body and returns the sorted XML.
     *
     * @param xmlContent the XML content to sort
     * @return a ResponseEntity containing the sorted XML string
     */
    @PostMapping(value = "/xml",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE})
    public ResponseEntity<String> sortXmlString(@RequestBody String xmlContent) {
        try {
            String sortedXml = xmlSorter.sort(xmlContent);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(sortedXml);
        } catch (XmlSortingException e) {
            return ResponseEntity.badRequest().body("Error sorting XML content: " + e.getMessage());
        }
    }

    /**
     * Sorts the XML content from the uploaded file and returns the sorted XML as a downloadable file.
     *
     * @param file the XML file to sort
     * @return a ResponseEntity containing the sorted XML file
     */
    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sortXmlFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try {
            String sortedXml = xmlSorter.sort(file.getInputStream());

            // Create a resource from the sorted XML
            ByteArrayResource resource = new ByteArrayResource(sortedXml.getBytes());

            // Prepare the response headers
            HttpHeaders headers = new HttpHeaders();
            String inputFileName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename().split("\\.")[0];
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(inputFileName + "-sorted.xml").build());
            headers.setContentType(MediaType.APPLICATION_XML);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (XmlSortingException | IOException e) {
            return ResponseEntity.badRequest().body("Error sorting XML file: " + e.getMessage());
        }
    }
}