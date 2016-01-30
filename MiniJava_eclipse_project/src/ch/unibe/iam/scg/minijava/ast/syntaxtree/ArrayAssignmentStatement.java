//
// Generated by JTB 1.3.2
//

package ch.unibe.iam.scg.minijava.ast.syntaxtree;

/**
 * Grammar production:
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
public class ArrayAssignmentStatement extends Node {
   public Identifier f0;
   public NodeToken f1;
   public Expression f2;
   public NodeToken f3;
   public NodeToken f4;
   public Expression f5;
   public NodeToken f6;

   public ArrayAssignmentStatement(Identifier n0, NodeToken n1, Expression n2, NodeToken n3, NodeToken n4, Expression n5, NodeToken n6) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
      f5 = n5;
      f6 = n6;
   }

   public ArrayAssignmentStatement(Identifier n0, Expression n1, Expression n2) {
      f0 = n0;
      f1 = new NodeToken("[");
      f2 = n1;
      f3 = new NodeToken("]");
      f4 = new NodeToken("=");
      f5 = n2;
      f6 = new NodeToken(";");
   }

   public void accept(ch.unibe.iam.scg.minijava.ast.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

