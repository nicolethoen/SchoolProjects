package com.cs;

import com.cs.exception.FailedToParseException;

public class LexicalAnalyzerDFA {

    public static int numberOfDoubleQuotes = 0;
    public static int numberOfSingleQuotes = 0;

    public static InputCategory getInputCategory(char a) {

        if (Character.isDigit(a)){
            return InputCategory.DIGIT;
        }
        if (Character.isLetter(a)){
            return InputCategory.CHARACTER;
        }
        if (a == '_'){
            return InputCategory.UNDERSCORE;
        }
        if (a == '.'){
            return InputCategory.DOT;
        }
        if (a == '\''){
            numberOfSingleQuotes++;
            return InputCategory.SINGLE_QUOTE;
        }
        if (a == '"'){
            numberOfDoubleQuotes++;
            return InputCategory.DOUBLE_QUOTE;
        }
        if (a == '+' ||
            a == '-' ||
            a == '*' ||
            a == '/' ||
            a == '%') {
            return InputCategory.ARITHMETIC_SYMBOL;
        }
        if (a == '<' ||
            a == '>') {
            return InputCategory.RELATIONAL_SYMBOL;
        }
        if (a == '&' ||
            a == '|') {
            return InputCategory.LOGICAL_SYMBOL;
        }
        if (a == '!') {
            return InputCategory.BANG;
        }
        if (a == '(') {
            return InputCategory.OPEN_PAREN;
        }
        if (a == ')') {
            return InputCategory.CLOSE_PAREN;
        }
        if (a == '=') {
            return InputCategory.EQUALS;
        }
        if (a == ';') {
            return InputCategory.SEMICOLON;
        }

        return null;
    }

    public static LexicalCategory getLexicalCategory(LexicalCategory currentLexicalCategory,
                                                     InputCategory inputCategory,
                                                     String currentToken) throws FailedToParseException {


        // take care of STRING_LITERAL and CHARACTER_LITERAL first because
        // any sort of input can be in a string or char.
        if (currentLexicalCategory.equals(LexicalCategory.STRING_LITERAL) &&
                numberOfDoubleQuotes % 2 == 1){
            return LexicalCategory.STRING_LITERAL;
        }
        if (currentLexicalCategory.equals(LexicalCategory.CHARACTER_LITERAL) &&
                numberOfSingleQuotes % 2 == 1){
            return LexicalCategory.CHARACTER_LITERAL;
        }
        // check the char is not empty - not allowed in java
        if (currentLexicalCategory.equals(LexicalCategory.CHARACTER_LITERAL) &&
                numberOfSingleQuotes % 2 == 0 &&
                currentToken.equals("'")) {
            throw new FailedToParseException("empty character literal");
        }

        // since the input is not part of a STRING_LITERAL or CHARACTER_LITERAL:
        switch (inputCategory) {

            case DIGIT:
                switch (currentLexicalCategory) {
                    case IDENTIFIER:
                        return LexicalCategory.IDENTIFIER;
                    case FLOATING_POINT_LITERAL:
                        return LexicalCategory.FLOATING_POINT_LITERAL;
                    default:
                        return LexicalCategory.INTEGER_LITERAL;
                }

            case CHARACTER:
                return LexicalCategory.IDENTIFIER;

            case SINGLE_QUOTE:
                return LexicalCategory.CHARACTER_LITERAL;

            case DOUBLE_QUOTE:
                return LexicalCategory.STRING_LITERAL;

            case DOT:
                // maybe I'll eventually deal with functions?
                return LexicalCategory.FLOATING_POINT_LITERAL;

            case UNDERSCORE:
                return LexicalCategory.IDENTIFIER;

            case OPEN_PAREN:
                return LexicalCategory.OPEN_PAREN;

            case CLOSE_PAREN:
                return LexicalCategory.CLOSE_PAREN;

            case SEMICOLON:
                return LexicalCategory.SEMICOLON;

            case BANG:
                return LexicalCategory.UNARY_LOGICAL_OPERATOR;

            case LOGICAL_SYMBOL:
                return (currentToken.equals("&") || currentToken.equals("|")) ? LexicalCategory.BINARY_LOGICAL_OPERATOR : LexicalCategory.BITWISE_LOGICAL_OPERATOR;

            case RELATIONAL_SYMBOL:
                return LexicalCategory.BINARY_RELATIONAL_OPERATOR;

            case EQUALS:
                if (currentToken.equals("=") || currentToken.equals("!") ||
                        currentToken.equals(">") || currentToken.equals("<")){
                    return LexicalCategory.BINARY_RELATIONAL_OPERATOR;
                }
                return LexicalCategory.ASSIGNMENT;

            case ARITHMETIC_SYMBOL:
                return LexicalCategory.BINARY_ARITHMETIC_OPERATOR;

            default:
                throw new FailedToParseException("I haven't been told how to interpret " + inputCategory);
        }
    }

    public static boolean isFirstCharacterOfNewLexeme(LexicalCategory currentLexicalCategory,
                                                      LexicalCategory futureLexicalCategory,
                                                      char nextCharacter,
                                                      InputCategory currentInputCategory,
                                                      String currentToken) throws FailedToParseException {

        switch (currentLexicalCategory) {
            case NONE:
                return true;

            case STRING_LITERAL:
                return !futureLexicalCategory.equals(LexicalCategory.STRING_LITERAL) ||
                        (numberOfDoubleQuotes % 2 == 1 && currentInputCategory.equals(InputCategory.DOUBLE_QUOTE));

            case CHARACTER_LITERAL:
                if (currentToken.length() > 3) throw new FailedToParseException("unclosed character literal");
                return !futureLexicalCategory.equals(LexicalCategory.CHARACTER_LITERAL) ||
                        (numberOfSingleQuotes % 2 == 1 && currentInputCategory.equals(InputCategory.SINGLE_QUOTE));

            case INTEGER_LITERAL:
                return !futureLexicalCategory.equals(LexicalCategory.FLOATING_POINT_LITERAL) && !currentLexicalCategory.equals(futureLexicalCategory);

            case CLOSE_PAREN:
            case OPEN_PAREN:
            case SEMICOLON:
            case BINARY_ARITHMETIC_OPERATOR:
                return true;

            case BITWISE_LOGICAL_OPERATOR:
                return (!currentToken.equals("" + nextCharacter) || !futureLexicalCategory.equals(LexicalCategory.BINARY_LOGICAL_OPERATOR));

            case UNARY_LOGICAL_OPERATOR:
                return (!futureLexicalCategory.equals(LexicalCategory.BINARY_RELATIONAL_OPERATOR));

            case BINARY_LOGICAL_OPERATOR:
            case BINARY_RELATIONAL_OPERATOR:
                return (currentToken.length() > 2) || !currentLexicalCategory.equals(futureLexicalCategory);

            default:
                return !currentLexicalCategory.equals(futureLexicalCategory);
        }
    }

    public static void buildLex(String javaProgram) throws FailedToParseException {
        // begin at the start state
        LexicalCategory currentLexicalCategory = LexicalCategory.NONE;
        String currentToken = "";

        for (int i = 0; i < javaProgram.length(); i++) {

            // get the next input
            char nextCharacter = javaProgram.charAt(i);
            // figure out what kind of input it is
            InputCategory inputCategory = LexicalAnalyzerDFA.getInputCategory(nextCharacter);

            // given the current state (i.e. lexicalCategory), the type of input, and the inputs that precede this input,
            // tell me what the next state (lexicalCategory) will be.
            LexicalCategory nextLexicalCategory = LexicalAnalyzerDFA.getLexicalCategory(currentLexicalCategory, inputCategory, currentToken);

            // figure out if this new state marks the beginning of a new token/lexeme or if, rather,
            // the current token/lexeme should change its classification
            // for example : and INTEGER_LITERAL should change its classification to
            //               FLOATING_POINT_LITERAL if the next character is a DOT
            boolean firstCharOfNewToken = LexicalAnalyzerDFA.isFirstCharacterOfNewLexeme(currentLexicalCategory, nextLexicalCategory, nextCharacter, inputCategory, currentToken);

            // if the next input marks the beginning of a new token/lexeme
            if (firstCharOfNewToken && !currentLexicalCategory.equals(LexicalCategory.NONE)) {
                // then take the token you've been building and add it to the lex, along with its
                // corresponding lexicalCategory.
                Object[] tokenLexeme = {currentToken, currentLexicalCategory};
                Main.lex.add(tokenLexeme);
                // then use the next input to begin building the new token/lexeme
                currentToken = "" + nextCharacter;
            } else {
                // if the next input does NOT mark the beginning of a new token/lexeme
                // then add it to the token you've been building.
                currentToken += nextCharacter;
            }

            // if the next input is the last symbol in the program
            if (i == javaProgram.length() - 1) {
                // it has already been added to the token you've been building in the previous if/else block
                // so add the token and next lexicalCategory to the lex one last time.
                Object[] tokenLexeme = {currentToken, nextLexicalCategory};
                Main.lex.add(tokenLexeme);
            }

            // update the currentLexicalCategory to reflect which state the most recent input put us in.
            currentLexicalCategory = nextLexicalCategory;
        }
    }
}
