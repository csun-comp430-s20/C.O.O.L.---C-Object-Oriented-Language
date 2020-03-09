import java.util.*;

import tokenizer.*;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    public static void testLex(String input, Token... expectedTokens) throws TokenizationException {

        List<Token> expected = new ArrayList<Token>();
        for (final Token token : expectedTokens) {
            expected.add(token);
        }
        List<Token> receivedTokens = Lexer.lex(input);
        assertEquals(receivedTokens.toString(), expected.toString());
    }

    private static void testAllKeywordsIndividually() throws TokenizationException {
        testLex("String", new StringKeywordToken());
        testLex("class", new ClassKeywordToken());
        testLex("constructor", new ConstructorKeywordToken());
        testLex("else", new ElseToken());
        testLex("for", new ForToken());
        testLex("if", new IfToken());
        testLex("Int", new IntKeywordToken());
        testLex("new", new NewToken());
        testLex("println", new PrintlnToken());
        testLex("print", new PrintToken());
        testLex("return", new ReturnToken());
        testLex("this", new ThisToken());
        testLex("Void", new VoidToken());
        testLex("while", new WhileToken());
        testLex("Bool", new BoolKeywordToken());
        testLex("true", new BoolValueToken(true));
        testLex("false", new BoolValueToken(false));
    }

    private static void testIdentifiers() throws TokenizationException {
        testLex("x", new IdentifierToken("x"));
        testLex("y1", new IdentifierToken("y1"));
        testLex("ifx", new IdentifierToken("ifx"));
    }

    private static void testIntegers() throws TokenizationException {
        testLex("0", new IntegerToken(0));
        testLex("10", new IntegerToken(10));
        testLex("500", new IntegerToken(500));
        testLex("123456", new IntegerToken(123456));
    }

    private static void testStringLiterals() throws TokenizationException {
        testLex("\"word\"", new StringLiteralToken("word"));
        testLex("\"12345\"", new StringLiteralToken("12345"));
        testLex("\"This is a sentence.\"", new StringLiteralToken("This is a sentence."));
        testLex("\"return + while bool if false 1234\"", new StringLiteralToken("return + while bool if false 1234"));
    }

    private static void testOperatorTokensIndividually() throws TokenizationException {
        testLex(".", new DotOperatorToken());
        testLex("+", new PlusToken());
        testLex("-", new MinusToken());
        testLex("*", new MultiplicationToken());
        testLex("/", new DivisionToken());
        testLex("%", new ModulusToken());
        testLex("<", new LessThanToken());
        testLex("<=", new LessThanOrEqualToken());
        testLex(">=", new GreaterThanOrEqualToken());
        testLex(">", new GreaterThanToken());
        testLex("=", new EqualToken());
        testLex("==", new EqualEqualToken());
    }

    private static void TestOtherTokensIndividually() throws TokenizationException {
        testLex("(", new LeftParenToken());
        testLex(")", new RightParenToken());
        testLex("{", new LCurlyToken());
        testLex("}", new RCurlyToken());
        testLex(";", new SemicolonToken());
        testLex(",", new CommaToken());
    }

    @Test
    private static void TestMultipleTokens() throws TokenizationException{
        testLex("String x = \"my string\";", new StringKeywordToken(), new IdentifierToken("x"),
                new EqualToken(), new StringLiteralToken("my string"), new SemicolonToken());
    }

    public static void main(String[] args) throws TokenizationException {
        System.out.println("Testing Mode");

        testAllKeywordsIndividually();
        testIdentifiers();
        testStringLiterals();
        testIntegers();
        testOperatorTokensIndividually();
        TestOtherTokensIndividually();
        TestMultipleTokens();

        System.out.println("Tests Passed");
    }

}