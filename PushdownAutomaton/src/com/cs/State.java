package com.cs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    private boolean acceptState; // if the stack is empty and we end in this state, is the input string accepted?
    private Map<String, Transition> transitions = new HashMap<String, Transition>();

    public State(boolean acceptState) {
        this.acceptState = acceptState;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public Map<String, Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, Transition> transitions) {
        this.transitions = transitions;
    }

}
