package dev.hrrezaei.xml.sorter.service;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        // Step 1: Compare node names lexicographically
        int nameComparison = node1.getNodeName().compareTo(node2.getNodeName());
        if (nameComparison != 0) {
            return nameComparison;
        }

        // Step 2: Compare sorted lists of attribute names lexicographically
        NamedNodeMap attrs1 = node1.getAttributes();
        NamedNodeMap attrs2 = node2.getAttributes();

        List<String> attrNames1 = getSortedAttributeNames(attrs1);
        List<String> attrNames2 = getSortedAttributeNames(attrs2);

        int attrNamesComparison = compareLists(attrNames1, attrNames2);
        if (attrNamesComparison != 0) {
            return attrNamesComparison;
        }

        // Step 3: Compare attribute values in the order of sorted attribute names
        int attrValuesComparison = compareAttributeValues(attrs1, attrs2, attrNames1);
        if (attrValuesComparison != 0) {
            return attrValuesComparison;
        }

        // Step 4: Compare text content lexicographically
        String textContent1 = getTextContent(node1);
        String textContent2 = getTextContent(node2);

        int textComparison = textContent1.compareTo(textContent2);
        if (textComparison != 0) {
            return textComparison;
        }

        // Nodes are considered equal
        return 0;
    }

    private List<String> getSortedAttributeNames(NamedNodeMap attrs) {
        List<String> names = new ArrayList<>();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                names.add(attrs.item(i).getNodeName());
            }
            Collections.sort(names);
        }
        return names;
    }

    private int compareLists(List<String> list1, List<String> list2) {
        int minSize = Math.min(list1.size(), list2.size());
        for (int i = 0; i < minSize; i++) {
            int comparison = list1.get(i).compareTo(list2.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        if (list1.size() > list2.size()) {
            return 1;
        } else if (list1.size() < list2.size()) {
            return -1;
        }
        return 0;
    }

    private int compareAttributeValues(NamedNodeMap attrs1, NamedNodeMap attrs2, List<String> sortedAttrNames) {
        for (String attrName : sortedAttrNames) {
            String value1 = attrs1.getNamedItem(attrName).getNodeValue();
            String value2 = attrs2.getNamedItem(attrName).getNodeValue();
            int valueComparison = value1.compareTo(value2);
            if (valueComparison != 0) {
                return valueComparison;
            }
        }
        return 0;
    }

    private String getTextContent(Node node) {
        StringBuilder textContent = new StringBuilder();
        textContent.append(node.getNodeValue() == null ? "" : node.getNodeValue());

        // Collect child nodes
        List<Node> childNodes = new ArrayList<>();
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                childNodes.add(child);
            }
            child = child.getNextSibling();
        }

        // Sort child nodes
        childNodes.sort(new NodeComparator());

        // Process sorted child nodes
        for (Node sortedChild : childNodes) {
            if (sortedChild.getNodeType() == Node.TEXT_NODE || sortedChild.getNodeType() == Node.CDATA_SECTION_NODE) {
                textContent.append(sortedChild.getNodeValue());
            }
        }

        return textContent.toString();
    }
}