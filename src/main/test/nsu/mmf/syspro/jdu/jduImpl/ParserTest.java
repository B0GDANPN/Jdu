package nsu.mmf.syspro.jdu.jduImpl;

import nsu.mmf.syspro.jdu.command.Command;
import nsu.mmf.syspro.jdu.exception.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    void test(String input, boolean  transition, int depth, int limit, Path path) {
        Parser parser = new Parser();
        Command cmd = parser.parse(input);
        assertEquals(cmd.transition, transition);
        assertEquals(cmd.depth, depth);
        assertEquals(cmd.limit, limit);
        assertEquals(cmd.path, path);
    }

    void testFailed(String input){
        Parser parser = new Parser();
        assertThrows(ParserException.class, () -> parser.parse(input), "Invalid input: dafaaf");
    }

    @Test
    void testLimitFlag(){
        test("jdu --limit 2 path/to/smth", false, 3, 2, Path.of("path/to/smth"));
    }

    @Test
    void testDepthFlag(){
        test("jdu --depth 1 path/to/another", false, 1, 10, Path.of("path/to/another"));
    }

    @Test
    void testSymbolicLinkFlag(){
        test("jdu -L path/to/smth", true, 3, 10, Path.of("path/to/smth"));
    }

    @Test
    void testCombined(){
        test("jdu --limit 2 --depth 1 -L path/to/smth", true, 1, 2, Path.of("path/to/smth"));
    }

    @Test
    void invalidInput() {
        testFailed("dafaaf");
    }
}
