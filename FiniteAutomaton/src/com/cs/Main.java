package com.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Map<String, State> automaton = new HashMap<String, State>(); // map of state names to states
    private static State startState;
    private static BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        // this is a comma separated file where each line is formatted the following way:
        // stateName,startState(1|0),acceptState(1|0),possibleInput,resultingStateName,possibleInput,resultingStateName...
        // there is no limit on the number or possibleInput-resultingStateName pairs on any given line.
        String fileName = "automaton.txt";
        try {
            buildAutomatonFromFile(fileName);

            System.out.println("Provide a string to be evaluated by the provided automaton.");
            System.out.println("Type 'exit' to exit program.");

            // run the program until the user types 'exit'
            while(true) {
                String inputString = bufferRead.readLine();
                if (inputString.equals("exit")) {
                    break;
                }
                evaluateString(inputString);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void evaluateString(String string) {
        try {

            State currentState = startState;

            for (int i = 0; i < string.length(); i++) {
                Character input = string.charAt(i);
                currentState = automaton.get(currentState.getNextState(input));
            }
            System.out.println(currentState.isAcceptState() ? "ACCEPTED" : "REJECTED");

        } catch (NullPointerException e) {

            // nullPointerException is thrown when the input does not have a defined next state
            // meaning the input string will NOT end up in an accept state
            System.out.println("REJECTED");
        }
    }

    private static void buildAutomatonFromFile(String fileName) throws FileNotFoundException{
        Scanner input = new Scanner(new File(fileName));

        while (input.hasNextLine()) {
            String currentLine = input.nextLine();
            String[] stateInfo = currentLine.split(",");

            String stateName = stateInfo[0];
            boolean isStartState = stateInfo[1].equals("1");
            boolean acceptState = stateInfo[2].equals("1");

            State state = new State(stateName, acceptState);

            for (int i = 3; i < stateInfo.length; i=i+2) {
                char possibleInput = stateInfo[i].charAt(0);
                String resultingStateName = stateInfo[i+1];
                state.addNextState(possibleInput, resultingStateName);
            }

            automaton.put(stateName, state);

            if (isStartState) {
                startState = state;
            }
        }
    }
}
