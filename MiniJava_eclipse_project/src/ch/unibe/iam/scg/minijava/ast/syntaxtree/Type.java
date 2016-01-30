//
// Generated by JTB 1.3.2
//

package ch.unibe.iam.scg.minijava.ast.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ArrayType()
 *       | BooleanType()
 *       | IntegerType()
 *       | VoidType()
 *       | Identifier()
 * </PRE>
 */
public class Type extends Node {
   public NodeChoice f0;

   public Type(NodeChoice n0) {
      f0 = n0;
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

