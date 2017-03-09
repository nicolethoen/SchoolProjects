package com.cs;

import com.cs.exception.NoTerminalSymbol;

public class ParsingPDA {

    public static int counter = 0;
    public static Object[] currentLexicalCategory;
    public static Object[] nextLexicalCategory;

    private static void setCurrentNextSymbols() {
        currentLexicalCategory = Main.lex.get(counter);
        if (counter + 1 < Main.lex.size()) nextLexicalCategory = Main.lex.get(counter + 1);
        counter ++;
    }

    public static boolean buildParseTree() {
        setCurrentNextSymbols();

        try {
            program();
            return true;
        } catch (NoTerminalSymbol e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void program() throws NoTerminalSymbol {
        try {
            System.out.println("program");
            while (true) {
                exp();
            }
        } catch (NoTerminalSymbol e) {
            semicolon();
        }

    }

    private static void exp() throws NoTerminalSymbol {
        System.out.println("exp");
        try {
            mathExp();
        } catch (NoTerminalSymbol e) {
            try {
                logicalExp();
            } catch (NoTerminalSymbol e2) {
                stringExp();
            }
        }
    }

    private static void lex() {
        System.out.println(currentLexicalCategory[0] + " --> " + currentLexicalCategory[1]);
        setCurrentNextSymbols();
    }

    private static void semicolon() throws NoTerminalSymbol {
        if (currentLexicalCategory[1].equals(LexicalCategory.SEMICOLON)) System.out.println("; -> SEMICOLON");
        else throw new NoTerminalSymbol("expected a semicolon to terminate string. it didn't.");
    }

    ////////////   arithmetic expressions ///////////////////////

    private static void mathExp() throws NoTerminalSymbol{
        System.out.println("mathExp");
        if (nextLexicalCategory[1].equals(LexicalCategory.BINARY_LOGICAL_OPERATOR) ||
                nextLexicalCategory[1].equals(LexicalCategory.BINARY_RELATIONAL_OPERATOR) ||
                nextLexicalCategory[1].equals(LexicalCategory.BITWISE_LOGICAL_OPERATOR)) {
            throw new NoTerminalSymbol("There is a non-mathematical operator in the expression");
        } else if (nextLexicalCategory[0].equals("+") ||
                nextLexicalCategory[0].equals("-")){
            multMathExp();
            lex();
            multMathExp();
        } else {
            multMathExp();
        }
    }

    private static void multMathExp() throws NoTerminalSymbol{
        System.out.println("multMathExp");
        if (nextLexicalCategory[0].equals("*") ||
                nextLexicalCategory[0].equals("/")){
            subMathExp();
            lex();
            subMathExp();
        } else {
            subMathExp();
        }
    }

    private static void subMathExp() throws NoTerminalSymbol{
        System.out.println("subMathExp");
        if (currentLexicalCategory[1].equals(LexicalCategory.OPEN_PAREN)) {
            lex();
            mathExp();
            lex();
        } else if (currentLexicalCategory[1].equals(LexicalCategory.IDENTIFIER) ||
                currentLexicalCategory[1].equals(LexicalCategory.FLOATING_POINT_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.INTEGER_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.CHARACTER_LITERAL)) {
            lex();
        } else {
            throw new NoTerminalSymbol("There is no math terminal symbol");
        }
    }


    ////////////   string expressions  /////////////////////////

    private static void stringExp() throws NoTerminalSymbol{
        System.out.println("stringExp");
        if (nextLexicalCategory[1].equals(LexicalCategory.BINARY_LOGICAL_OPERATOR) ||
                nextLexicalCategory[1].equals(LexicalCategory.BINARY_RELATIONAL_OPERATOR) ||
                nextLexicalCategory[1].equals(LexicalCategory.BITWISE_LOGICAL_OPERATOR) ||
                nextLexicalCategory[1].equals(LexicalCategory.BINARY_ARITHMETIC_OPERATOR)) {
            if (nextLexicalCategory[0].equals("+")) {
                string();
                lex();
                string();
            } else {
                throw new NoTerminalSymbol("There is a non-string operator in the expression");
            }
        } else {
            string();
        }
    }

    private static void string() throws NoTerminalSymbol{
        System.out.println("string");
        if (currentLexicalCategory[1].equals(LexicalCategory.OPEN_PAREN)) {
            lex();
            mathExp();
            lex();
        } else if (currentLexicalCategory[1].equals(LexicalCategory.IDENTIFIER) ||
                currentLexicalCategory[1].equals(LexicalCategory.FLOATING_POINT_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.INTEGER_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.CHARACTER_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.STRING_LITERAL)){
            lex();
        } else {
            throw new NoTerminalSymbol("There is no string terminal symbol");
        }
    }

    ////////////    logical expressions  ///////////////////////

    private static void logicalExp() throws NoTerminalSymbol{
        System.out.println("logicalExp");
        if (nextLexicalCategory[0].equals("||")){
            andExp();
            lex();
            andExp();
        } else {
            andExp();
        }
    }

    private static void andExp() throws NoTerminalSymbol{
        System.out.println("andExp");
        if (nextLexicalCategory[0].equals("&&")){
            bitExp();
            lex();
            bitExp();
        } else {
            bitExp();
        }
    }

    private static void bitExp() throws NoTerminalSymbol{
        System.out.println("bitExp");
        if (nextLexicalCategory[1].equals(LexicalCategory.BITWISE_LOGICAL_OPERATOR)){
            subLogExp();
            lex();
            subLogExp();
        } else {
            subLogExp();
        }
    }

    private static void subLogExp() throws NoTerminalSymbol{
        System.out.println("subLogExp");
        if (currentLexicalCategory[0].equals("!")) {
            lex();
            logicalExp();
        } else if (currentLexicalCategory[1].equals(LexicalCategory.OPEN_PAREN)) {
            lex();
            logicalExp();
            lex();
        } else if (currentLexicalCategory[1].equals(LexicalCategory.IDENTIFIER)) {
            lex();
        } else {
            relExp();
        }
    }

    ////////////////////   relational expressions    //////////////////
    private static void relExp() throws NoTerminalSymbol{
        System.out.println("relExp");
        if (nextLexicalCategory[0].equals("==") ||
                nextLexicalCategory[0].equals("!=")){
            equalsRelExp();
            lex();
            equalsRelExp();
        } else {
            equalsRelExp();
        }
    }

    private static void equalsRelExp() throws NoTerminalSymbol{
        System.out.println("equalsRelExp");
        if (nextLexicalCategory[1].equals(LexicalCategory.BINARY_RELATIONAL_OPERATOR)) {
            subRelExp();
            lex();
            subRelExp();
        } else {
            subRelExp();
        }
    }

    private static void subRelExp() throws NoTerminalSymbol{
        System.out.println("subRelExp");
        if (currentLexicalCategory[1].equals(LexicalCategory.OPEN_PAREN)) {
            lex();
            relExp();
            lex();
        } else if (currentLexicalCategory[1].equals(LexicalCategory.IDENTIFIER) ||
                currentLexicalCategory[1].equals(LexicalCategory.CHARACTER_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.INTEGER_LITERAL) ||
                currentLexicalCategory[1].equals(LexicalCategory.FLOATING_POINT_LITERAL)) {
            lex();
        } else {
            throw new NoTerminalSymbol("no terminal logical or relational symbol");
        }
    }

}
