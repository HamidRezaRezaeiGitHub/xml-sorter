#!/bin/bash

# test-api.sh
# This script demonstrates how to use the XML Sorter application via curl commands.
# It includes examples of sending XML content as a string and as a file upload.

# Base URL of the XML Sorter application
BASE_URL="http://localhost:8080/api/v1/sorter/xml"

# Directory containing test XML files
XML_DIR="xml"

# Function to send XML content as a string
function sort_xml_string() {
    echo "Sorting XML content as a string..."

    # Read XML content from a file
    INPUT_FILE="$XML_DIR/attributesAndNestedNodes-input.xml"
    if [ ! -f "$INPUT_FILE" ]; then
        echo "Input file not found: $INPUT_FILE"
        exit 1
    fi

    XML_CONTENT=$(cat "$INPUT_FILE")

    # Send POST request with XML content
    RESPONSE=$(curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/xml" \
        -d "$XML_CONTENT")

    echo "Response:"
    echo "$RESPONSE"
    echo
}

# Function to send XML content as a file upload
function sort_xml_file() {
    echo "Sorting XML content as a file upload..."

    # Path to the input XML file
    INPUT_FILE="$XML_DIR/attributesAndNestedNodes-input.xml"
    if [ ! -f "$INPUT_FILE" ]; then
        echo "Input file not found: $INPUT_FILE"
        exit 1
    fi

    # Send POST request with the XML file
    RESPONSE=$(curl -s -X POST "$BASE_URL" \
        -F "file=@$INPUT_FILE;type=application/xml")

    # Save the response to a file
    OUTPUT_FILE="$XML_DIR/attributesAndNestedNodes-sorted.xml"
    echo "$RESPONSE" > "$OUTPUT_FILE"

    echo "Sorted XML saved to: $OUTPUT_FILE"
    echo
}

# Change to the script's directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR" || exit

# Call the functions
sort_xml_string
sort_xml_file