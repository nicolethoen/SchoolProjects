package com.cs;

import java.util.HashMap;
import java.util.Map;

public class PDA {
    
    static Stack stack;

    // My PDA accepts the language {0^m 1^n 2^p | m=n or n=p}

    public static Map<String, State> automaton = new HashMap<String, State>();

    public static void buildAutomaton(){

        State state = new State(false);
        Map<String, Transition> transitions = new HashMap<String, Transition>();
        transitions.put("0" + Stack.stackStart, new Transition(Stack.StackOperation.PUSH, "B"));
        transitions.put("0" + "0", new Transition(Stack.StackOperation.PUSH, "B"));
        transitions.put("1" + "0", new Transition(Stack.StackOperation.POP, "C"));
        state.setTransitions(transitions);
        automaton.put("B", state);

        state = new State(true);
        transitions = new HashMap<String, Transition>();
        transitions.put("1" + "0", new Transition(Stack.StackOperation.POP, "C"));
        transitions.put("2" + Stack.stackStart, new Transition(Stack.StackOperation.NONE, "D"));
        state.setTransitions(transitions);
        automaton.put("C", state);

        state = new State(true);
        transitions = new HashMap<String, Transition>();
        transitions.put("2" + Stack.stackStart, new Transition(Stack.StackOperation.NONE, "D"));
        state.setTransitions(transitions);
        automaton.put("D", state);

        state = new State(true);
        transitions = new HashMap<String, Transition>();
        transitions.put("0" + Stack.stackStart, new Transition(Stack.StackOperation.NONE, "E"));
        transitions.put("1" + Stack.stackStart, new Transition(Stack.StackOperation.PUSH, "F"));
        state.setTransitions(transitions);
        automaton.put("E", state);

        state = new State(false);
        transitions = new HashMap<String, Transition>();
        transitions.put("1" + Stack.stackStart, new Transition(Stack.StackOperation.PUSH, "F"));
        transitions.put("1" + "1", new Transition(Stack.StackOperation.PUSH, "F"));
        transitions.put("2" + "1", new Transition(Stack.StackOperation.POP, "G"));
        state.setTransitions(transitions);
        automaton.put("F", state);

        state = new State(true);
        transitions = new HashMap<String, Transition>();
        transitions.put("2" + "1", new Transition(Stack.StackOperation.POP, "G"));
        state.setTransitions(transitions);
        automaton.put("G", state);
    }

    public static State performTransition(char input, State currentState) {

        /*
         determining which will be the next transition will require three pieces of information
          1. the next input character
          2. the symbol at the top of the stack
          3. the current state

         each state knows which stack operation to perform and what the next state will be given
         the next input character and the symbol at the top of the stack
         so the 'currentSituation' below is a string representing of that information to be fed to the
         current state to help determine our next steps.
        */

        String topStackSymbol = stack.getTop();
        String currentSituation = input + topStackSymbol;

        // if the currentState is null, then we have somehow landed ourselves off the PDA and can never get into an
        // accept state on this path.
        if (currentState != null) {

            // if the currentState does not know what the transition would be give then currentSituation,
            // then we will never get into an accept state on this path and will return a null currentState
            Transition nextTransition = currentState.getTransitions().get(currentSituation);

            if (nextTransition != null) {
                // this state knows which transition to follow next.

                // perform the appropriate stack operation as defined by the transition
                stack.performOperation(nextTransition.getStackOperation(), input + "");

                // move to the next state so that the next state is now the current state
                currentState = automaton.get(nextTransition.getNextStateName());

            } else {
                return null;
            }
        }

        return currentState;
    }
}
