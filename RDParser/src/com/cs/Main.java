package com.cs;

import com.cs.exception.FailedToParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    // This parser will parse numeric, logical, and relational expressions

    private static String javaProgram = "";
    public static List<Object[]> lex = new ArrayList<>();

    public static void main(String[] args) {
        readJavaProgramForParsing();
        try {
            LexicalAnalyzerDFA.buildLex(javaProgram);
            printLex();
            boolean successful = ParsingPDA.buildParseTree();
            System.out.println(successful ? "Parsed Successfully." : "Unable to parse.");

        } catch (FailedToParseException e) {
            System.out.println("There was an error parsing. " + e.getMessage());
        }
    }

    public static void readJavaProgramForParsing() {
        try {
            Scanner input = new Scanner(new File("testFile.java"));
            while (input.hasNextLine()) {
                javaProgram += input.nextLine();
            }
            javaProgram = javaProgram.replaceAll("\\s","");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printLex() {
        for(Object[] tuple : lex) {
            System.out.println(tuple[0] + " -> " + tuple[1]);
        }
    }
}
