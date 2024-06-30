package nsu.mmf.syspro.sizer.sizerImpl;

import nsu.mmf.syspro.sizer.command.Command;
import nsu.mmf.syspro.sizer.exception.ParserException;

import java.io.IOException;

public class Sizer {

    final Appendable outputStream;

    public Sizer(Appendable outputStream) {
        this.outputStream = outputStream;
    }

    public void run(String line) {
        Parser parser = new Parser();
        Command cmd = null;
        try {
            cmd = parser.parse(line);
        } catch (ParserException e) {
            System.err.println(e.getMessage());
        }
        if (cmd != null) {
            try {
                outputStream.append(cmd.apply().trim());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}


