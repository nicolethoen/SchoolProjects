package com.cs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ncooprider on 2/17/2016.
 */
public class State {

    private String stateName;
    private boolean acceptState;
    // a map from next input to the next state name.
    private Map<Character, String> nextStates = new HashMap<Character, String>();

    public State(String stateName, boolean acceptState) {
        this.stateName = stateName;
        this.acceptState = acceptState;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public void setAcceptState(boolean acceptState) {
        this.acceptState = acceptState;
    }

    public Map<Character, String> getNextStates() {
        return nextStates;
    }

    public void setNextStates(Map<Character, String> nextStates) {
        this.nextStates = nextStates;
    }

    public void addNextState(Character input, String name) {
        nextStates.put(input, name);
    }

    public String getNextState(Character input) {
        return nextStates.get(input);
    }
}
