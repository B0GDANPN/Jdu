package nsu.mmf.syspro.sizer.sizerImpl;

import nsu.mmf.syspro.sizer.command.Command;
import nsu.mmf.syspro.sizer.exception.ParserException;

import java.nio.file.Path;

public class Parser {

    public Command parse(String line) {
        String[] options = line.split(" ");
        if (!options[0].equals("sizer")) {
            throw new ParserException("Invalid input: " + options[0]);
        }
        int index = 1;
        boolean transition = false;
        Integer limit = null;
        Integer depth = null;
        Path path = null;
        while (index < options.length) {
            switch (options[index]) {
                case "-L" -> transition = true;
                case "--limit" -> limit = Integer.valueOf(options[++index]);
                case "--depth" -> depth = Integer.valueOf(options[++index]);
                default -> {try {
                   path = Path.of(options[index]);
                } catch (IllegalArgumentException e) {
                    throw new ParserException("Invalid input: " + options[index]);
                }
                }
            }
            index++;
        }
        return new Command(transition, depth, limit, path);
    }
}
