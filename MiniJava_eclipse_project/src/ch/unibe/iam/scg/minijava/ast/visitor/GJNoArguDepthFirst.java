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
public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

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
   public R visit(Goal n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
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
   public R visit(MainClass n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
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
   public R visit(MainFunction n) {
      R _ret=null;
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
      return _ret;
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
   public R visit(ClassDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    * </PRE>
    */
   public R visit(VarDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
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
   public R visit(MethodDeclaration n) {
      R _ret=null;
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
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> FormalParameter()
    * f1 -> ( MoreFormalParameter() )*
    * </PRE>
    */
   public R visit(FormalParameterList n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> Identifier()
    * </PRE>
    */
   public R visit(FormalParameter n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> ","
    * f1 -> FormalParameter()
    * </PRE>
    */
   public R visit(MoreFormalParameter n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
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
   public R visit(Type n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "String"
    * f1 -> "["
    * f2 -> "]"
    * </PRE>
    */
   public R visit(StringArrayType n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "void"
    * </PRE>
    */
   public R visit(VoidType n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    * </PRE>
    */
   public R visit(ArrayType n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "boolean"
    * </PRE>
    */
   public R visit(BooleanType n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "int"
    * </PRE>
    */
   public R visit(IntegerType n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
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
   public R visit(Statement n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    * </PRE>
    */
   public R visit(Block n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    * </PRE>
    */
   public R visit(AssignmentStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
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
   public R visit(ArrayAssignmentStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
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
   public R visit(IfStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
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
   public R visit(WhileStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
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
   public R visit(PrintStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Sum()
    * f1 -> ( ( "&&" | "&lt;" | "&gt;" ) Expression() )?
    * </PRE>
    */
   public R visit(Expression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Term()
    * f1 -> ( ( "+" | "-" ) Expression() )?
    * </PRE>
    */
   public R visit(Sum n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Factor()
    * f1 -> ( "*" Expression() )?
    * </PRE>
    */
   public R visit(Term n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> PrimaryExpression()
    * f1 -> ( F() )*
    * </PRE>
    */
   public R visit(Factor n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> ArrayLookUp()
    *       | LengthLookUp()
    *       | MethodCall()
    * </PRE>
    */
   public R visit(F n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "["
    * f1 -> Expression()
    * f2 -> "]"
    * </PRE>
    */
   public R visit(ArrayLookUp n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "."
    * f1 -> "length"
    * </PRE>
    */
   public R visit(LengthLookUp n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
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
   public R visit(MethodCall n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> ( Expression() ( "," MoreParameterExpression() )* )?
    * </PRE>
    */
   public R visit(ParamList n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> Expression()
    * </PRE>
    */
   public R visit(MoreParameterExpression n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
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
   public R visit(PrimaryExpression n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> &lt;INTEGER_LITERAL&gt;
    * </PRE>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "true"
    * </PRE>
    */
   public R visit(TrueLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "false"
    * </PRE>
    */
   public R visit(FalseLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> &lt;IDENTIFIER&gt;
    * </PRE>
    */
   public R visit(Identifier n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "this"
    * </PRE>
    */
   public R visit(ThisExpression n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
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
   public R visit(ArrayAllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    * </PRE>
    */
   public R visit(AllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "!"
    * f1 -> Expression()
    * </PRE>
    */
   public R visit(NotExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * <PRE>
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    * </PRE>
    */
   public R visit(BracketExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

}
