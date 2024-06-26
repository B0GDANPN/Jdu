package nsu.mmf.syspro.jdu.jduImpl;

import nsu.mmf.syspro.jdu.command.Command;
import nsu.mmf.syspro.jdu.exception.ParserException;

import java.io.IOException;

public class JDU {

    final Appendable outputStream;

    public JDU(Appendable outputStream) {
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


