package com.cs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    private static BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {

            PDA.buildAutomaton();

            System.out.println("Provide a string to be evaluated by the provided automaton.");
            System.out.println("Type 'exit' to exit program.");

            // run the program until the user types 'exit'
            while(true) {
                String inputString = bufferRead.readLine();
                if (inputString.equals("exit")) {
                    break;
                }

                // my PDA is non-deterministic, so the 'startState' A simply puts the start symbol onto the stack and does a
                // lambda transition into states B, D, or E.
                if( isAcceptedWhenStartingFromState("B", inputString) ||
                    isAcceptedWhenStartingFromState("D", inputString) ||
                    isAcceptedWhenStartingFromState("E", inputString)) {
                    System.out.println("ACCEPTED");
                } else {
                    System.out.println("REJECTED");
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isAcceptedWhenStartingFromState(String stateName, String inputString) {

        State currentState = PDA.automaton.get(stateName);
        PDA.stack = new Stack();

        // begin iterating over the input string and performing state transitions (and stack operations)
        for (int i = 0; i < inputString.length(); i++){
            currentState = PDA.performTransition(inputString.charAt(i), currentState);
        }

        // once i'm done iterating, I can answer the question of whether or not this input is accepted by evaluating that
        // this path landed me in an accept state AND the stack is empty.
        return (PDA.stack.isEmpty() && currentState != null && currentState.isAcceptState());
    }

}
