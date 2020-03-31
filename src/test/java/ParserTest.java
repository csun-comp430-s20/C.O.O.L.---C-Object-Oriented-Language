import java.util.*;

import parser.*;
import parser.statements.*;
import parser.expressions.*;
import tokenizer.*;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    public static void testParse(String programString, Program expected) throws TokenizationException, ParseException {
        List<Token> tokens = Lexer.lex(programString);
        Token[] tokenArray = tokens.toArray(new Token[0]);
        System.out.println(Arrays.toString(tokenArray));
        Parser myparser = new Parser(tokenArray);
        Program actual = myparser.parseProgram();
        // for some reason, comparing the toString()s was working, but comparing the
        // objects themselves was not
        // even though all the .equals() method is doing for those objects is comparing
        // the toString()s
        // assertEquals(expected, actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void ClassDefTest() throws TokenizationException, ParseException {
        // no class defs
        String programString = "Int main(){}";
        Program expected = new Program(new ArrayList<ClassDef>(), new MethodDef("Int", "main",
                new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);

        // one class def
        programString = "class TestClass{constructor(){}}" + "Int main(){}";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        expected = new Program(classDefs, new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);

        // two class defs
        programString = "class TestClassA{constructor(){}} class TestClassB{constructor(){}} " + "Int main(){}";
        classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClassA", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        classDefs.add(new ClassDef("TestClassB", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        expected = new Program(classDefs, new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);
    }

    @Test
    public void InheritanceTest() throws TokenizationException, ParseException {
        String programString = "class TestClass:ParentClass{constructor(){}} " + "Int main(){}";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClass", "ParentClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        Program expected = new Program(classDefs, new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);
    }

    @Test
    public void MethodDefTest() throws TokenizationException, ParseException {
        // one method
        String programString = "class TestClass{constructor(){} Int methodTest(){}} " + "Int main(){}";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        ArrayList<MethodDef> methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(new MethodDef("Int", "methodTest", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                methodDefs));
        Program expected = new Program(classDefs, new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);
        // two methods
        programString = "class TestClass{constructor(){} Int methodA(){} TestClass methodB(){}} " + "Int main(){}";
        classDefs = new ArrayList<ClassDef>();
        methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(new MethodDef("Int", "methodA", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        methodDefs.add(new MethodDef("TestClass", "methodB", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                methodDefs));
        expected = new Program(classDefs, new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>())));
        testParse(programString, expected);
    }

    @Test
    public void HelloWorldTest() throws TokenizationException, ParseException {
        String programString = "Int main(){" + "String mystring= \"Hello World!\";" + "println(mystring);" + "return 0;"
                + ")";
        ArrayList<Statement> stmtList = new ArrayList<Statement>();
        stmtList.add(new VariableInitializerStmt("String", "mystring", new StringExp("Hello World!")));
        stmtList.add(new PrintlnStmt(new VariableExp("mystring")));
        stmtList.add(new ReturnStmt(new IntegerExp(0)));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), mainBody));

        List<Token> tokens = Lexer.lex(programString);

        // I know Token[0] is weird, but, from the documentation,
        // "If the list fits in the specified array, it is returned therein."
        // "Otherwise, a new array is allocated with the runtime type of the specified
        // array and the size of this list."
        // So, as far as I can tell, toArray(new Token[0]) will return an array of the
        // proper size
        Parser myparser = new Parser(tokens.toArray(new Token[0]));
        Program actual = myparser.parseProgram();
        assertEquals(expected, actual);
    }

}