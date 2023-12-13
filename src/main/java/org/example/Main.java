package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DuplicateChecker duplicateChecker = new DuplicateChecker();
        System.out.println("Enter the desired path:");
        String directoryPath = new Scanner(System.in).nextLine();
        try {
            duplicateChecker.findDuplicateTxtFiles(directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
