package dev.hrrezaei.xml.sorter.service;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class NodeConvertor {

    public static List<Attr> convertAttributesToList(NamedNodeMap attributes) {
        if (attributes == null || attributes.getLength() == 0) {
            return Collections.emptyList();
        }
        List<Attr> nodes = new ArrayList<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            nodes.add((Attr) node);
        }
        return nodes;
    }

    public static Set<Attr> convertAttributesToSet(NamedNodeMap attributes) {
        if (attributes == null || attributes.getLength() == 0) {
            return Collections.emptySet();
        }
        Set<Attr> nodes = new HashSet<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            nodes.add((Attr) node);
        }
        return nodes;
    }

    public static List<Node> convertNodesToList(NodeList childNodes) {
        if (childNodes == null || childNodes.getLength() == 0) {
            return Collections.emptyList();
        }
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            nodes.add(node);
        }
        return nodes;
    }

    public static String convertNodeToExpandedString(Node node) {
        return "Node = {" +
               "String value: [" + node + "] - " +
               "HashCode: [" + (node == null ? "null" : node.hashCode()) + "] - " +
               "Node name: [" + (node == null ? "null" : node.getNodeName()) + "] - " +
               "Node type: [" + (node == null ? "null" : node.getNodeType()) + "] - " +
               "Node value: [" + (node == null ? "null" : node.getTextContent()) + "]" +
               "}";
    }
}
