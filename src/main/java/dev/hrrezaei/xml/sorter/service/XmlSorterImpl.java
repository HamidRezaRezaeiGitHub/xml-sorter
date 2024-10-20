package dev.hrrezaei.xml.sorter.service;

import dev.hrrezaei.xml.sorter.exception.XmlSortingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

import static dev.hrrezaei.xml.sorter.service.NodeConvertor.convertAttributesToList;
import static dev.hrrezaei.xml.sorter.service.NodeConvertor.convertNodeToExpandedString;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Log4j2
@Service
public class XmlSorterImpl implements XmlSorter {

    // ThreadLocal to store the xmlDeclaration per thread
    private static final ThreadLocal<String> xmlDeclaration = new ThreadLocal<>();

    /**
     * Sorts the given XML content according to the defined sorting rules.
     * <p>
     * This method extracts the XML declaration (if present) from the input XML string and stores it.
     * The XML content is then parsed into a DOM Document for processing.
     * After sorting, the original XML declaration is reattached to the output if it was present in the input.
     * </p>
     *
     * @param xmlContent the XML content to sort as a String
     * @return the sorted XML content as a String
     * @throws XmlSortingException if an error occurs during parsing or sorting
     */
    @Override
    public String sort(String xmlContent) throws XmlSortingException {
        try {
            String trimmedXmlContent = xmlContent.trim();

            // Check if the XML declaration is present
            if (trimmedXmlContent.startsWith("<?xml")) {
                // Find the end of the XML declaration
                int endIndex = trimmedXmlContent.indexOf("?>");
                if (endIndex > -1) {
                    // Extract the XML declaration
                    xmlDeclaration.set(trimmedXmlContent.substring(0, endIndex + 2));
                    log.info("XML declaration is preserved: [{}]", xmlDeclaration.get());
                    // Remove the declaration from the content
                    xmlContent = trimmedXmlContent.substring(endIndex + 2).trim();
                }
            }

            Document document = parseXmlContent(xmlContent);
            return sort(document);
        } catch (Exception e) {
            throw new XmlSortingException("Error sorting XML content from String", e);
        } finally {
            xmlDeclaration.remove();
        }
    }

    /**
     * Sorts the given XML Document according to the defined sorting rules.
     * <p>
     * This method sorts the elements and attributes within the provided Document.
     * After sorting, the Document is transformed back into an XML String.
     * </p>
     * <p>
     * <strong>Note:</strong> There may be differences between the input and output XMLs in terms of the XML declaration tag.
     * Since the XML declaration is not part of the Document object, its presence in the output depends on whether it was
     * preserved during parsing. If the XML declaration was extracted and stored when parsing the XML content as a String,
     * it will be reattached to the output. Otherwise, if the Document was provided directly without an associated XML declaration,
     * and no ENCODING is detected by the Document, the output will not include an XML declaration, even if the Document
     * object has default values for VERSION or STANDALONE.
     * </p>
     *
     * @param document the XML Document to sort
     * @return the sorted XML content as a String
     * @throws XmlSortingException if an error occurs during sorting or transformation
     */
    @Override
    public String sort(Document document) throws XmlSortingException {
        try {
            // Start sorting from the root element
            Node root = document.getDocumentElement();
            sortNode(root);

            // Transform the Document back to a String
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Indentation settings
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-number", "4");

            // By default, ignore the declaration tag, unless strong document(s) exist
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if (isBlank(xmlDeclaration.get())) {
                // It's not guaranteed that this version is extracted from the input document. It could be the default value.
                String xmlVersion = document.getXmlVersion();
                String encoding = document.getXmlEncoding();
                if (encoding != null) {
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                    if (xmlVersion != null) {
                        transformer.setOutputProperty(OutputKeys.VERSION, xmlVersion);
                    }
                    transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
                    transformer.setOutputProperty(OutputKeys.STANDALONE, document.getXmlStandalone() ? "yes" : "no");
                }
            }

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            String sortedXml = writer.toString();
            if (isBlank(xmlDeclaration.get())) {
                return sortedXml;
            } else {
                return xmlDeclaration.get() + "\n" + sortedXml;
            }

        } catch (TransformerException e) {
            throw new XmlSortingException("Error transforming sorted XML document to String", e);
        }
    }

    private void sortNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Sort attributes
            sortAttributes(element);

            // Recursively sort child nodes
            NodeList childNodes = element.getChildNodes();
            List<Node> childElements = new ArrayList<>();

            // Keep Non-Element nodes (like comments) right before the next Element node
            List<Node> nonElementChildren = new ArrayList<>();
            Map<Node, List<Node>> elementsAfterNonElements = new HashMap<>();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);

                // Ignore text nodes that contain only whitespace
                if (child.getNodeType() == Node.TEXT_NODE) {
                    if (child.getTextContent() == null || child.getTextContent().trim().isEmpty()) {
                        continue;
                    }
                }

                if (child.getNodeType() != Node.ELEMENT_NODE) {
                    nonElementChildren.add(child);
                } else {
                    childElements.add(child);
                    if (!nonElementChildren.isEmpty()) {
                        elementsAfterNonElements.put(child, nonElementChildren);
                        nonElementChildren = new ArrayList<>();
                    }
                }
            }
            List<Node> bottomNonElementChildren = nonElementChildren.isEmpty() ? Collections.emptyList() : new ArrayList<>(nonElementChildren);

            // Sort child elements of this node
            childElements.sort(new NodeComparator());

            // Sort attributes of child elements and recursively sort their children
            for (Node childElement : childElements) {
                sortNode(childElement);
            }

            // Remove all child nodes
            while (element.hasChildNodes()) {
                element.removeChild(element.getFirstChild());
            }

            // Append sorted element nodes
            for (Node childElement : childElements) {
                nonElementChildren = getProceedingsOfNode(childElement, elementsAfterNonElements);
                for (Node childComment : nonElementChildren) {
                    element.appendChild(childComment);
                }
                element.appendChild(childElement);
            }
            for (Node childElement : bottomNonElementChildren) {
                element.appendChild(childElement);
            }
        }
    }

    private void sortAttributes(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        if (attributes != null && attributes.getLength() > 1) {
            List<Attr> attrList = convertAttributesToList(attributes);

            // Remove all attributes
            for (Attr attr : attrList) {
                element.removeAttributeNode(attr);
            }

            // Sort attributes
            attrList.sort(new AttributeComparator());

            // Add attributes back in sorted order
            for (Attr attr : attrList) {
                element.setAttributeNode(attr);
            }
        }
    }

    /**
     * Retrieves the list of non-element nodes (e.g., comments, processing instructions)
     * that immediately precede the specified element node within its parent element's child nodes.
     * <p>
     * The method uses a map where each key is an element node and the corresponding value
     * is a list of non-element nodes that were originally located directly before that element.
     * Since the {@code Node} class does not override {@code equals()} and {@code hashCode()},
     * node identity is based on object reference (memory address).
     * Therefore, it's crucial that the same {@code Node} instances are used both when
     * populating the map and when calling this method.
     * </p>
     *
     * @param node                     the element node whose preceding non-element siblings are to be retrieved
     * @param elementsAfterNonElements a map associating element nodes with their preceding non-element siblings
     * @return a list of non-element nodes that precede the specified element node,
     * or an empty list if none are found or if the map is null or empty
     */
    private List<Node> getProceedingsOfNode(Node node, Map<Node, List<Node>> elementsAfterNonElements) {
        if (elementsAfterNonElements == null || elementsAfterNonElements.isEmpty()) {
            return Collections.emptyList();
        }

        log.debug("Element keys: {}", elementsAfterNonElements.keySet().stream().map(NodeConvertor::convertNodeToExpandedString).toList());
        log.debug("Target element: {}", convertNodeToExpandedString(node));
        log.debug("Element keys contain the target element: {}", elementsAfterNonElements.containsKey(node));

        return elementsAfterNonElements.getOrDefault(node, Collections.emptyList());
    }
}