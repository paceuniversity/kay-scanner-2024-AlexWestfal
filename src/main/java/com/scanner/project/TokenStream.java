package com.scanner.project;

// TokenStream.java
// Implementation of the Scanner for KAY

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TokenStream {

    private boolean eof = false;
    private char currentChar = ' ';
    private BufferedReader input;

    public boolean isEoFile() {
        return eof;
    }

    public TokenStream(String fileName) {
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            eof = true;
        }
    }

    public Token nextToken() {
        Token token = new Token();
        token.setType("Other");
        token.setValue("");

        skipWhiteSpace();

        while (currentChar == '/') {
            currentChar = readChar();
            if (currentChar == '/') {
                while (!eof && !isEndOfLine(currentChar)) {
                    currentChar = readChar();
                }
                skipWhiteSpace();
            } else {
                token.setType("Operator");
                token.setValue("/");
                return token;
            }
        }

        if (isOperator(currentChar)) {
            token.setType("Operator");
            token.setValue(token.getValue() + currentChar);

            switch (currentChar) {
                case '<':
                case '>':
                    char op = currentChar;
                    currentChar = readChar();
                    if (currentChar == '=') {
                        token.setValue(op + "=");
                        currentChar = readChar();
                    }
                    return token;
                case '=':
                case ':':
                    char prev = currentChar;
                    currentChar = readChar();
                    if (currentChar == '=') {
                        token.setValue(prev + "=");
                        currentChar = readChar();
                    } else {
                        token.setType("Other");
                    }
                    return token;
                case '!':
                    currentChar = readChar();
                    if (currentChar == '=') {
                        token.setValue("!=");
                        currentChar = readChar();
                    }
                    return token;
                case '|':
                    currentChar = readChar();
                    if (currentChar == '|') {
                        token.setValue("||");
                        currentChar = readChar();
                    } else {
                        token.setType("Other");
                    }
                    return token;
                case '&':
                    currentChar = readChar();
                    if (currentChar == '&') {
                        token.setValue("&&");
                        currentChar = readChar();
                    } else {
                        token.setType("Other");
                    }
                    return token;
                default:
                    currentChar = readChar();
                    return token;
            }
        }

        if (isSeparator(currentChar)) {
            token.setType("Separator");
            token.setValue("" + currentChar);
            currentChar = readChar();
            return token;
        }

        if (isLetter(currentChar)) {
            token.setType("Identifier");
            while (isLetter(currentChar) || isDigit(currentChar)) {
                token.setValue(token.getValue() + currentChar);
                currentChar = readChar();
            }
            if (isKeyword(token.getValue())) {
                token.setType("Keyword");
            } else if (token.getValue().equals("True") || token.getValue().equals("False")) {
                token.setType("Literal");
            }
            if (isEndOfToken(currentChar)) return token;
        }

        if (isDigit(currentChar)) {
            token.setType("Literal");
            while (isDigit(currentChar)) {
                token.setValue(token.getValue() + currentChar);
                currentChar = readChar();
            }
            if (isEndOfToken(currentChar)) return token;
        }

        token.setType("Other");

        if (eof) return token;

        while (!isEndOfToken(currentChar)) {
            token.setValue(token.getValue() + currentChar);
            currentChar = readChar();
        }

        skipWhiteSpace();
        return token;
    }

    private char readChar() {
        if (eof) return (char) 0;
        try {
            int c = input.read();
            if (c == -1) {
                eof = true;
                return (char) 0;
            }
            return (char) c;
        } catch (IOException e) {
            System.exit(-1);
            return 0;
        }
    }

    private boolean isKeyword(String s) {
        return s.equals("bool") || s.equals("else") || s.equals("if") ||
               s.equals("integer") || s.equals("while") || s.equals("main");
    }

    private boolean isOperator(char c) {
        return "+-*/<>=!&|:".indexOf(c) >= 0;
    }

    private boolean isSeparator(char c) {
        return "();{},{}".indexOf(c) >= 0;
    }

    private boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isEndOfToken(char c) {
        return isWhiteSpace(c) || isOperator(c) || isSeparator(c) || eof;
    }

    private boolean isEndOfLine(char c) {
        return c == '\r' || c == '\n' || c == '\f';
    }

    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private void skipWhiteSpace() {
        while (!eof && isWhiteSpace(currentChar)) {
            currentChar = readChar();
        }
    }

    public boolean isEndofFile() {
        return eof;
    }
}
