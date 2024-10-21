# XML Sorter

XML Sorter is a Spring Boot application that provides functionality to sort XML files or content by ordering elements
alphabetically while preserving the structure and content. It offers a RESTful API with endpoints to accept XML content
as a string or as a file upload, and returns the sorted XML in the same format. This tool is useful for developers and
systems that require consistent and deterministic XML outputs for comparison, testing, or processing purposes.

![Java](https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?logo=spring-boot&logoColor=white&style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?logo=apache-maven&logoColor=white&style=for-the-badge)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white&style=for-the-badge)

## Table of Contents

- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Usage Examples](#usage-examples)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Credits](#credits)
- [Contact](#contact)

## Installation

Ensure you have the following installed on your machine:

- **Java**: Version 21
- **Maven**: Version 3.6 or newer
- **Spring Boot**: Version 3.3.4

Instructions:

1. **Clone the Repository**:

   ```
   git clone https://github.com/yourusername/xml-sorter.git
   cd xml-sorter
   ```

2. **Build the Project**:

   Navigate to the project root directory and run:

   ```
   mvn clean install
   ```

   This will compile the project and run all tests to ensure everything is working correctly.

3. **Run the Application**:

   Start the Spring Boot application using:

   ```
   mvn spring-boot:run
   ```

   The application will start and listen on port `8080` by default.

4. **Access the API**:

   You can now send requests to the API endpoints. For example, to sort an XML file, you can use `curl` or any API
   client like Postman.

## API Endpoints

The application exposes the following RESTful API endpoints:

- **POST** `/api/v1/sorter/xml`:

    - **Description**: Sorts the given XML content provided in the request body and returns the sorted XML.
    - **Consumes**: `application/xml`, `text/xml`
    - **Produces**: `application/xml`, `text/xml`
    - **Request Body**: Raw XML content as a string.
    - **Response**: Sorted XML content.

- **POST** `/api/v1/sorter/xml` (Multipart File Upload):

    - **Description**: Accepts an XML file upload and returns the sorted XML as a downloadable file.
    - **Consumes**: `multipart/form-data`
    - **Produces**: `application/xml` (as a file attachment)
    - **Form Data Parameter**:
        - `file`: The XML file to be sorted.
    - **Response**: Sorted XML file with filename appended by `-sorted.xml`.

## Usage Examples

### Sorting XML Content via POST Request

**Request**:

- **Method**: POST
- **URL**: `http://localhost:8080/api/v1/sorter/xml`
- **Headers**:
    - `Content-Type: application/xml`
- **Body**:

  ```
  <root>
      <beta>Second</beta>
      <alpha>First</alpha>
      <gamma>Third</gamma>
  </root>
  ```

**Response**:

- **Status Code**: `200 OK`
- **Headers**:
    - `Content-Type: application/xml`
- **Body**:

  ```
  <root>
      <alpha>First</alpha>
      <beta>Second</beta>
      <gamma>Third</gamma>
  </root>
  ```

### Sorting XML File via File Upload

**Request**:

- **Method**: POST
- **URL**: `http://localhost:8080/api/v1/sorter/xml`
- **Headers**:
    - `Content-Type: multipart/form-data`
- **Form Data**:
    - `file`: Upload the XML file you want to sort.

**Response**:

- **Status Code**: `200 OK`
- **Headers**:
    - `Content-Disposition: attachment; filename="yourfilename-sorted.xml"`
    - `Content-Type: application/xml`
- **Body**:
    - The sorted XML file is returned as a downloadable attachment.

## Testing

The project includes a comprehensive test suite to ensure the correctness and stability of the XML sorting
functionality.

### Running Tests

To run all tests, execute:

```
mvn test
```

### Test Coverage

- **Unit Tests**: Verify the functionality of individual components like the XML sorting service.
- **Integration Tests**: Test the REST API endpoints and the integration between the controller and service layers.
- **Test Scenarios**:

    - Sorting XML files with multi-level nested nodes.
    - Handling XML files with different nodes, attributes, and nested structures.
    - Preserving non-element nodes (comments, processing instructions) at various levels.
    - Managing extra whitespace and new-line characters between nodes.
    - Processing XML files with namespaces and prefixes.
    - Handling CDATA sections and mixed content.
    - ...

## Project Structure

- **src/main/java**: Contains the main application code.
    - **controller**: REST controllers handling API requests.
    - **service**: Services containing the business logic for sorting XML.
    - **exception**: Custom exception classes.
- **src/main/resources**: Contains application configuration files.
- **src/test/java**: Contains unit and integration tests.
    - **controller**: Tests for REST controllers.
    - **service**: Tests for service classes.
- **src/test/resources**: Contains test resources like sample XML files.

## Credits

This project was developed as a personal project to provide a utility for sorting XML files and content. Inspiration and
guidance were taken from various online resources and documentation.

## Contact

- **Email**: [hamidreza74hrr@yahoo.com](mailto:hamidreza74hrr@yahoo.com)
- **LinkedIn**: [Hamid Reza Rezaei](https://www.linkedin.com/in/hamid-reza-rezaei-17896a125/)

---

Feel free to reach out if you have any questions or need assistance with setting up or using the XML Sorter application.