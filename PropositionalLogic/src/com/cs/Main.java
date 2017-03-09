package com.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        System.out.println("This program evaluates the propositional logic sentence");
        System.out.println("(a ∧ b) ∨(c ∧ ¬b) ∨ (¬d ∧ ¬a)\n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {

            System.out.println("Assign a value (0 or 1) for a:");
            int a = Integer.parseInt(bufferRead.readLine());

            System.out.println("Assign a value (0 or 1) for b:");
            int b = Integer.parseInt(bufferRead.readLine());

            System.out.println("Assign a value (0 or 1) for c:");
            int c = Integer.parseInt(bufferRead.readLine());

            System.out.println("Assign a value (0 or 1) for d:");
            int d = Integer.parseInt(bufferRead.readLine());

            System.out.println("The sentence outputs " + evaluateSentence(a, b, c, d) + " for the given inputs.");

        } catch (NumberFormatException nfe) {

            System.out.println("ERROR: All inputs to this program must be a 0 or 1 value.");

        } catch(IOException e) {

            e.printStackTrace();

        }
    }

    private static String evaluateSentence(int a, int b, int c, int d) {
        if (!(a == 1 || a ==0) ||
            !(b == 1 || b ==0) ||
            !(c == 1 || c ==0) ||
            !(d == 1 || d ==0)) {
            throw new NumberFormatException();
        }

        // because of the structure of the propositional logic sentence, I can use short circuit evaluation.
        if (a==1 && b==1) {
            // evaluates a ∧ b
            return "TRUE";
        }

        if (c==1 && b==0) {
            // evaluates c ∧ ¬b
            return "TRUE";
        }

        if (d==0 && a==0) {
            // evaluates ¬d ∧ ¬a
            return "TRUE";
        }

        return "FALSE";
    }
}
