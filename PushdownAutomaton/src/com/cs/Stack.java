package com.cs;

public class Stack {

    static String stackStart = "\u03B3";
    private Node top;

    public Stack() {
        push(stackStart);
    }

    private class Node {
        private String item;
        private Node next;
    }

    public enum StackOperation {
        POP,
        PUSH,
        NONE
    }

    public String performOperation(StackOperation stackOperation, String input) {
        switch (stackOperation) {
            case POP:
                return pop();
            case PUSH:
                push(input);
            case NONE:
            default:
                return "";
        }
    }

    public String getTop() {
        return top.item;
    }

    public boolean isEmpty() {
        return stackStart.equals(top.item);
    }

    public void push(String item) {
        Node oldfirst = top;
        top = new Node();
        top.item = item;
        top.next = oldfirst;
    }

    public String pop() {
        if (isEmpty()) throw new RuntimeException("Stack underflow");
        String item = top.item;
        top = top.next;
        return item;
    }

}
