package nsu.mmf.syspro.jdu.jduImpl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JDUTest {

    void test(String flags, String testDirectory, String expected) {
        String input = "jdu " + flags + " src/main/testResources/" + testDirectory;
        JDU jdu = new JDU(new StringBuilder());
        jdu.run(input);
        assertEquals(expected, String.valueOf(jdu.outputStream));
    }

    void test(String testDirectory, String expected) {
        test("", testDirectory, expected);
    }

    @Test
    void testWithoutFlags() {
        String expected = """
                /firstDirectory[1.0 MiB]
                \t/secondDirectory[1.0 MiB]
                \t\t/sample.txt[1.0 MiB]""";
        test("test1", expected);
    }

    @Test
    void testWithLimit() {
        String expected = """
                /firstDir[1.1 MiB]
                \t/sample1[228.0 KiB]
                \t/sample0[866.0 KiB]
                /secondDir[1.2 MiB]
                \t/sample2[550.0 KiB]
                \t/sample3[647.0 KiB]""";
        test("--limit 2", "test2", expected);
    }

    @Test
    void testWithDepth() {
        String expected = """
                /sample5[59.0 KiB]
                /sample4[749.0 KiB]
                /firstDir[1.1 MiB]
                /secondDir[1.2 MiB]""";
        test("--depth 1", "test2", expected);
    }

    @Test
    void testSymbolicLink() {
        String expected = """
                /sample8[239.0 KiB]
                /secondDir[1.2 MiB]
                \t/sample2[550.0 KiB]
                \t/sample3[647.0 KiB]
                \t/firstDirectory[1.0 MiB]
                \t\t/secondDirectory[1.0 MiB]""";
        test("-L", "test3", expected);
    }

    @Test
    void testFilesWithSameSize() {
        String expected = "/sample.txt[1.0 MiB]\n/sample1txt[1.0 MiB]";
        test("test4", expected);
    }
}
