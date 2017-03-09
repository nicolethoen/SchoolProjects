package com.cs;

public class Transition {

    private Stack.StackOperation stackOperation;
    private String nextStateName;

    public Transition(Stack.StackOperation stackOperation, String nextStateName) {
        this.stackOperation = stackOperation;
        this.nextStateName = nextStateName;
    }

    public Stack.StackOperation getStackOperation() {
        return stackOperation;
    }

    public String getNextStateName() {
        return nextStateName;
    }

}
