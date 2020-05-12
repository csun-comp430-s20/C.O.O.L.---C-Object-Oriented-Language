package codeGenerator;

import typechecker.types.*;
import parser.*;
import parser.statements.*;
import parser.expressions.*;

import java.util.*;

public class CodeGenerator {
    // private static ArrayList<String> INCLUDE = new ArrayList<String>(); //holds
    private ArrayList<String> StructHeaders = new ArrayList<String>();
    private ArrayList<String> FunctionHeaders = new ArrayList<String>();
    private ArrayList<String> Classes = new ArrayList<String>();
    private ArrayList<String> Main = new ArrayList<String>();
    private Program myProgram;

    public CodeGenerator(Program myProgram) {
        this.myProgram = myProgram;
    }

    public String getCode() throws CodeGeneratorException {
        generateProgramCode();
        String completeCode = "";
        for(int i = 0; i<StructHeaders.size(); i++){
            completeCode += StructHeaders.get(i);
            completeCode += " ";
        }
        for(int i = 0; i<FunctionHeaders.size(); i++){
            completeCode += FunctionHeaders.get(i);
            completeCode += " ";
        }
        for(int i = 0; i<Classes.size(); i++){
            completeCode += Classes.get(i);
            completeCode += " ";
        }
        for(int i = 0; i<Main.size(); i++){
            completeCode += Main.get(i);
            completeCode += " ";
        }
        return completeCode;
    }

    private void generateProgramCode() throws CodeGeneratorException {
        for (int i = 0; i < myProgram.classDefs.size(); i++) {
            generateClassCode(myProgram.classDefs.get(i));
        }
        generateMainCode();
    }

    private void generateClassCode(ClassDef myClass) throws CodeGeneratorException {
        //header
        StructHeaders.add("struct");
        StructHeaders.add(myClass.className);
        StructHeaders.add(";");


        Classes.add("struct");
        Classes.add(myClass.className);
        Classes.add("{");
        for (int i = 0; i < myClass.fields.size(); i++) {
            generateStatementCode(myClass.fields.get(i), Classes);
        }
        //TO-DO: Deal with parent class
        Classes.add("}");

        //Constructor
        //header
        FunctionHeaders.add(myClass.className);
        FunctionHeaders.add(myClass.className + "_Constructor(");
        FunctionHeaders.add("(");
        FunctionHeaders.add(myClass.className);
        FunctionHeaders.add("this");
        for (int i = 0; i < myClass.constructor.parameters.size(); i++) {
            FunctionHeaders.add(",");
            generateStatementCode(myClass.constructor.parameters.get(i), FunctionHeaders);
        }
        generateStatementCode(myClass.constructor.body, FunctionHeaders);


        Classes.add(myClass.className);
        Classes.add(myClass.className + "_Constructor(");
        Classes.add(myClass.className);
        Classes.add("this");
        for (int i = 0; i < myClass.constructor.parameters.size(); i++) {
            Classes.add(",");
            generateStatementCode(myClass.constructor.parameters.get(i), Classes);
        }
        Classes.add(")");
        generateStatementCode(myClass.constructor.body, Classes);



        for (int i = 0; i < myClass.methods.size(); i++) {
            generateMethodDefCode(myClass.methods.get(i), myClass.className);
        }
    }

    private void generateMethodDefCode(MethodDef method, String classname) throws CodeGeneratorException {
        //header
        FunctionHeaders.add(classname + "_" + method.name);
        FunctionHeaders.add("(");
        FunctionHeaders.add(classname);
        FunctionHeaders.add("this");
        for (int i = 0; i < method.parameters.size(); i++) {
            FunctionHeaders.add(",");
            generateStatementCode(method.parameters.get(i), FunctionHeaders);
        }
        FunctionHeaders.add(")");

        Classes.add(classname + "_" + method.name);
        Classes.add("(");
        Classes.add(classname);
        Classes.add("this");
        for (int i = 0; i < method.parameters.size(); i++) {
            Classes.add(",");
            generateStatementCode(method.parameters.get(i), Classes);
        }
        Classes.add(")");
        generateStatementCode(method.body, Classes);
        //handle returns?
    }

    private void generateMainCode() throws CodeGeneratorException {
        MethodDef mymain = myProgram.entryPoint;
        Main.add("int main()");
        generateStatementCode(mymain.body, Main);
        // handle returns?
    }
    public static void generateExpressionCode(Expression exp, ArrayList<String> currentList) throws CodeGeneratorException{
        if (exp instanceof DivisionExp){
            DivisionExp div =(DivisionExp) exp;
            currentList.add("(");
            generateExpressionCode(div.exp1,currentList);
            currentList.add("/");
            generateExpressionCode(div.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof EqualEqualExp){
            EqualEqualExp equalEqual =(EqualEqualExp) exp;
            currentList.add("(");
            generateExpressionCode(equalEqual.exp1,currentList);
            currentList.add("==");
            generateExpressionCode(equalEqual.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof GreaterThanExp){
            GreaterThanExp greaterThan =(GreaterThanExp) exp;
            currentList.add("(");
            generateExpressionCode(greaterThan.exp1,currentList);
            currentList.add(">");
            generateExpressionCode(greaterThan.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof GreaterThanOrEqualExp){
            GreaterThanOrEqualExp greaterThanorEqual =(GreaterThanOrEqualExp) exp;
            currentList.add("(");
            generateExpressionCode(greaterThanorEqual.exp1,currentList);
            currentList.add(">=");
            generateExpressionCode(greaterThanorEqual.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof IntegerExp){
            IntegerExp integer =(IntegerExp) exp;
            currentList.add("(");
            currentList.add(Integer.toString(integer.value));
            currentList.add(")");
        } else if (exp instanceof LessThanExp){
            LessThanExp lessThan =(LessThanExp) exp;
            currentList.add("(");
            generateExpressionCode(lessThan.exp1,currentList);
            currentList.add("<");
            generateExpressionCode(lessThan.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof LessThanOrEqualExp){
            LessThanOrEqualExp lessThanorEqual =(LessThanOrEqualExp) exp;
            currentList.add("(");
            generateExpressionCode(lessThanorEqual.exp1,currentList);
            currentList.add("<=");
            generateExpressionCode(lessThanorEqual.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof MethodCallExp){
            
        } else if (exp instanceof MinusExp){
            MinusExp min =(MinusExp) exp;
            currentList.add("(");
            generateExpressionCode(min.exp1,currentList);
            currentList.add("-");
            generateExpressionCode(min.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof ModulusExp){
            ModulusExp mod =(ModulusExp) exp;
            currentList.add("(");
            generateExpressionCode(mod.exp1,currentList);
            currentList.add("%");
            generateExpressionCode(mod.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof MultiplicationExp){
            MultiplicationExp mult =(MultiplicationExp) exp;
            currentList.add("(");
            generateExpressionCode(mult.exp1,currentList);
            currentList.add("*");
            generateExpressionCode(mult.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof NewExp){

        } else if (exp instanceof ParenthesizedExp){
            ParenthesizedExp paren =(ParenthesizedExp) exp;
            currentList.add("(");
            generateExpressionCode(paren.body,currentList);
            currentList.add(")");
        } else if (exp instanceof PlusExp){
            PlusExp plus =(PlusExp) exp;
            currentList.add("(");
            generateExpressionCode(plus.exp1,currentList);
            currentList.add("+");
            generateExpressionCode(plus.exp2,currentList);
            currentList.add(")");
        } else if (exp instanceof StringExp){
            StringExp string =(StringExp) exp;
            currentList.add("\"");
            currentList.add(string.value);
            currentList.add("\"");
        } else if (exp instanceof ThisExp){

        } else if (exp instanceof VariableExp){
            VariableExp var =(VariableExp) exp;
            currentList.add("(");
            currentList.add(var.name);
            currentList.add(")");
        } else {
            throw new CodeGeneratorException("Unknown expression: " + exp.toString());
        }
    }

    public static void generateStatementCode(Statement s, ArrayList<String> currentList) throws CodeGeneratorException{

        if (s instanceof BlockStmt) {
            BlockStmt stmt = (BlockStmt) s;
            currentList.add("{");
            List<Statement> bodyStmtList = stmt.body;
            for (int i = 0; i < bodyStmtList.size(); i++) {
                generateStatementCode(bodyStmtList.get(i), currentList);
            }
            currentList.add("}");
        } else if (s instanceof ForStmt) {
            ForStmt stmt = (ForStmt) s;
            currentList.add("for(");
            generateStatementCode(stmt.initializer, currentList);
            currentList.add(";");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(";");
            generateStatementCode(stmt.incrementor, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);
        } else if (s instanceof IfElseStmt) {
            IfElseStmt stmt = (IfElseStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
            currentList.add("else");
            generateStatementCode(stmt.falseBranch, currentList);
        } else if (s instanceof IfStmt) {
            IfStmt stmt = (IfStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
        } else if (s instanceof PrintlnStmt) {
            PrintlnStmt stmt = (PrintlnStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
            currentList.add("printf(\"\\n\");");
        } else if (s instanceof PrintStmt) {
            PrintStmt stmt = (PrintStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
        } else if (s instanceof ReturnStmt) {
            ReturnStmt stmt = (ReturnStmt) s;
            currentList.add("return");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } else if (s instanceof ReturnVoidStmt) {
            // Do nothing, no return void in C
        } else if (s instanceof WhileStmt) {
            WhileStmt stmt = (WhileStmt) s;
            currentList.add("while(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);

        } else if (s instanceof VariableDeclarationStmt) {
            VariableDeclarationStmt stmt = (VariableDeclarationStmt) s;
            currentList.add(stmt.type);
            currentList.add(stmt.name);
            if (stmt.value != null) {
                currentList.add("=");
                generateExpressionCode(stmt.value, currentList);
                currentList.add(";");
            }
        } else if (s instanceof VariableAssignmentStmt) {
            VariableAssignmentStmt stmt = (VariableAssignmentStmt) s;
            currentList.add(stmt.name);
            currentList.add("=");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } else {
            throw new CodeGeneratorException("Unknown statement: " + s.toString());
        }
    }

    public static String generatePrintf(Expression e, ArrayList<String> currentList) {
        String output = "printf(\"";
        // TO-DO
        output += "\"):";
        return output;
    }
}