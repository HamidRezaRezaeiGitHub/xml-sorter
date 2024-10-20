package dev.hrrezaei.xml.sorter.service;

import org.w3c.dom.Attr;

import java.util.Comparator;

/**
 * Comparator for sorting attributes of an XML node based on attribute names and values.
 */
public class AttributeComparator implements Comparator<Attr> {

    @Override
    public int compare(Attr attr1, Attr attr2) {
        int nameComparison = attr1.getName().compareTo(attr2.getName());
        if (nameComparison != 0) {
            return nameComparison;
        } else {
            // Names are equal; compare based on attribute values
            return attr1.getValue().compareTo(attr2.getValue());
        }
    }
}