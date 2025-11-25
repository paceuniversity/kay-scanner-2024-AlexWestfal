package com.scanner.project;

// TokenStream.java
// Implementation of the Scanner for KAY

// This code DOES NOT implement a scanner for KAY yet. You have to complete
// the code and also make sure it implements a scanner for KAY - not something else.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    // READ THE COMPLETE FILE FIRST
    // You will need to adapt it to KAY, NOT JAY

    // Instance variables 
    private boolean isEof = false; // is end of file
    private char nextChar = ' '; // next character in input stream
    private BufferedReader input;

    // This function was added to make the demo file work
    public boolean isEoFile() {
        return isEof;
    }

    // Constructor
    // Pass a filename for the program text as a source for the TokenStream.
    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            isEof = true;
        }
    }

    public Token nextToken() { // Main function of the scanner
        Token t = new Token(); // Return next token type and value.
        t.setType("Other");
        t.setValue("");

        // First check for whitespaces and bypass them
        skipWhiteSpace();

        // Then check for a comment, and bypass it
        // but remember that / may also be a division operator.
        while (nextChar == '/') {
            nextChar = readChar();
            if (nextChar == '/') {
                while (!isEof && !isEndOfLine(nextChar)) {
                    nextChar = readChar();
                }
                skipWhiteSpace();
            } else {
                t.setValue("/");
                t.setType("Operator");
                return t;
            }
        }

        // Then check for an operator
        if (isOperator(nextChar)) {
            t.setType("Operator");
            t.setValue("" + nextChar);
            switch (nextChar) {
            case '<':
            case '>':
                nextChar = readChar();
                if (nextChar == '=') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                }
                return t;
            case '=':
                nextChar = readChar();
                if (nextChar == '=') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                } else t.setType("Other");
                return t;
            case ':':
                nextChar = readChar();
                if (nextChar == '=') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                } else t.setType("Other");
                return t;
            case '!':
                nextChar = readChar();
                if (nextChar == '=') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                }
                return t;
            case '|':
                nextChar = readChar();
                if (nextChar == '|') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                } else t.setType("Other");
                return t;
            case '&':
                nextChar = readChar();
                if (nextChar == '&') {
                    t.setValue(t.getValue() + nextChar);
                    nextChar = readChar();
                } else t.setType("Other");
                return t;
            default:
                nextChar = readChar();
                return t;
            }
        }

        // Then check for a separator
        if (isSeparator(nextChar)) {
            t.setType("Separator");
            t.setValue("" + nextChar);
            nextChar = readChar();
            return t;
        }

        // Then check for identifier, keyword, or literal
        if (isLetter(nextChar)) {
            t.setType("Identifier");
            while (isLetter(nextChar) || isDigit(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }
            if (isKeyword(t.getValue())) {
                t.setType("Keyword");
            } else if (t.getValue().equals("True") || t.getValue().equals("False")) {
                t.setType("Literal");
            }
            if (isEndOfToken(nextChar)) {
                return t;
            }
        }

        // Then check integer literals
        if (isDigit(nextChar)) {
            t.setType("Literal");
            while (isDigit(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }
            if (isEndOfToken(nextChar)) return t;
        }

        // Unknown token
        t.setType("Other");
        if (!isEof) {
            while (!isEndOfToken(nextChar)) {
                t.setValue(t.getValue() + nextChar);
                nextChar = readChar();
            }
        }
        skipWhiteSpace();
        return t;
    }

    private char readChar() {
        if (isEof) return 0;
        int i;
        try { i = input.read(); } catch (IOException e) { System.exit(-1); return 0; }
        if (i == -1) { isEof = true; return 0; }
        return (char) i;
    }

    private boolean isKeyword(String s) {
        return s.equals("bool") || s.equals("else") || s.equals("if") ||
               s.equals("integer") || s.equals("while") || s.equals("main");
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfLine(char c) {
        return c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfToken(char c) {
        return isWhiteSpace(c) || isOperator(c) || isSeparator(c) || isEof;
    }

    private void skipWhiteSpace() {
        while (!isEof && isWhiteSpace(nextChar)) nextChar = readChar();
    }

    private boolean isSeparator(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == ',';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' ||
               c == '<' || c == '>' || c == '=' || c == '!' ||
               c == '&' || c == '|' || c == ':';
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public boolean isEndofFile() {
        return isEof;
    }
}
