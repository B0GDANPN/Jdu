package nsu.mmf.syspro.jdu;

import nsu.mmf.syspro.jdu.jduImpl.JDU;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JDU jdu = new JDU(System.out);
        while (true) {
            String line = scanner.nextLine();
            jdu.run(line);
        }
    }
}
