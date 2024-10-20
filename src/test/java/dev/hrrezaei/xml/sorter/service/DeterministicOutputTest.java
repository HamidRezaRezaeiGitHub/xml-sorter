package dev.hrrezaei.xml.sorter.service;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class DeterministicOutputTest {

    private static final int N = 10; // Number of repetitions

    @TestFactory
    public List<DynamicTest> runTestsMultipleTimes() throws Exception {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        // Get the test class you want to run multiple times
        Class<?> testClass = XmlSorterTest.class;

        // Create a TestContextManager for the test class
        TestContextManager testContextManager = new TestContextManager(testClass);

        // Iterate over all methods in the test class
        for (Method method : testClass.getDeclaredMethods()) {
            // Check if the method is annotated with @Test
            if (method.isAnnotationPresent(Test.class)) {
                // Run each test method N times
                for (int i = 1; i <= N; i++) {
                    String displayName = method.getName() + " - run " + i;

                    // Create a dynamic test
                    DynamicTest dynamicTest = DynamicTest.dynamicTest(displayName, () -> {
                        // Create a new instance of the test class
                        Object testInstance = testClass.getDeclaredConstructor().newInstance();

                        // Prepare the test instance (inject dependencies)
                        testContextManager.prepareTestInstance(testInstance);

                        try {
                            // Invoke the test method
                            method.invoke(testInstance);
                        } catch (Throwable e) {
                            // If the test method throws an exception, rethrow it to fail the test
                            throw e.getCause() != null ? e.getCause() : e;
                        }
                    });

                    dynamicTests.add(dynamicTest);
                }
            }
        }

        return dynamicTests;
    }
}