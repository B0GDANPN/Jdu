package nsu.mmf.syspro.sizer;

import nsu.mmf.syspro.sizer.sizerImpl.Sizer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Sizer sizer = new Sizer(System.out);
        while (true) {
            String line = scanner.nextLine();
            sizer.run(line);
        }
    }
}
