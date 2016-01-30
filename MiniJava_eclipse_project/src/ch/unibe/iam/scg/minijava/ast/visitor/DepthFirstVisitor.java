//
// Generated by JTB 1.3.2
//

package ch.unibe.iam.scg.minijava.ast.visitor;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class DepthFirstVisitor implements Visitor {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public void visit(NodeList n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeListOptional n) {
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this);
   }

   public void visit(NodeOptional n) {
      if ( n.present() )
         n.node.accept(this);
   }

   public void visit(NodeSequence n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeToken n) { }

   //
   // User-generated visitor methods below
   //

   /**
    * <PRE>
    * f0 -> MainClass()
    * f1 -> ( ClassDeclaration() )*
    * f2 -> &lt;EOF&gt;
    * </PRE>
    */
   public void visit(Goal n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> MainFunction()
    * f4 -> "}"
    * </PRE>
    */
   public void visit(MainClass n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "public"
    * f1 -> "static"
    * f2 -> VoidType()
    * f3 -> "main"
    * f4 -> "("
    * f5 -> StringArrayType()
    * f6 -> Identifier()
    * f7 -> ")"
    * f8 -> "{"
    * f9 -> ( Statement() )*
    * f10 -> "}"
    * </PRE>
    */
   public void visit(MainFunction n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> ( "extends" Identifier() )?
    * f3 -> "{"
    * f4 -> ( VarDeclaration() )*
    * f5 -> ( MethodDeclaration() )*
    * f6 -> "}"
    * </PRE>
    */
   public void visit(ClassDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    * </PRE>
    */
   public void visit(VarDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    * </PRE>
    */
   public void visit(MethodDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
   }

   /**
    * <PRE>
    * f0 -> FormalParameter()
    * f1 -> ( MoreFormalParameter() )*
    * </PRE>
    */
   public void visit(FormalParameterList n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> Identifier()
    * </PRE>
    */
   public void visit(FormalParameter n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ","
    * f1 -> FormalParameter()
    * </PRE>
    */
   public void visit(MoreFormalParameter n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ArrayType()
    *       | StringArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | VoidType()
    *       | Identifier()
    * </PRE>
    */
   public void visit(Type n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "String"
    * f1 -> "["
    * f2 -> "]"
    * </PRE>
    */
   public void visit(StringArrayType n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "void"
    * </PRE>
    */
   public void visit(VoidType n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    * </PRE>
    */
   public void visit(ArrayType n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "boolean"
    * </PRE>
    */
   public void visit(BooleanType n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "int"
    * </PRE>
    */
   public void visit(IntegerType n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    * </PRE>
    */
   public void visit(Statement n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    * </PRE>
    */
   public void visit(Block n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    * </PRE>
    */
   public void visit(AssignmentStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    * </PRE>
    */
   public void visit(ArrayAssignmentStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    * </PRE>
    */
   public void visit(IfStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * </PRE>
    */
   public void visit(WhileStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    * </PRE>
    */
   public void visit(PrintStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Sum()
    * f1 -> ( ( "&&" | "&lt;" | "&gt;" ) Expression() )?
    * </PRE>
    */
   public void visit(Expression n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Term()
    * f1 -> ( ( "+" | "-" ) Expression() )?
    * </PRE>
    */
   public void visit(Sum n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Factor()
    * f1 -> ( "*" Expression() )?
    * </PRE>
    */
   public void visit(Term n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> PrimaryExpression()
    * f1 -> ( F() )*
    * </PRE>
    */
   public void visit(Factor n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ArrayLookUp()
    *       | LengthLookUp()
    *       | MethodCall()
    * </PRE>
    */
   public void visit(F n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "["
    * f1 -> Expression()
    * f2 -> "]"
    * </PRE>
    */
   public void visit(ArrayLookUp n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "."
    * f1 -> "length"
    * </PRE>
    */
   public void visit(LengthLookUp n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "."
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ParamList()
    * f4 -> ")"
    * </PRE>
    */
   public void visit(MethodCall n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ( Expression() ( "," MoreParameterExpression() )* )?
    * </PRE>
    */
   public void visit(ParamList n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Expression()
    * </PRE>
    */
   public void visit(MoreParameterExpression n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    * </PRE>
    */
   public void visit(PrimaryExpression n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> &lt;INTEGER_LITERAL&gt;
    * </PRE>
    */
   public void visit(IntegerLiteral n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "true"
    * </PRE>
    */
   public void visit(TrueLiteral n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "false"
    * </PRE>
    */
   public void visit(FalseLiteral n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> &lt;IDENTIFIER&gt;
    * </PRE>
    */
   public void visit(Identifier n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "this"
    * </PRE>
    */
   public void visit(ThisExpression n) {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    * </PRE>
    */
   public void visit(ArrayAllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    * </PRE>
    */
   public void visit(AllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "!"
    * f1 -> Expression()
    * </PRE>
    */
   public void visit(NotExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    * </PRE>
    */
   public void visit(BracketExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

}