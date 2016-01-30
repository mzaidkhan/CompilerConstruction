package ch.unibe.iam.scg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.unibe.iam.scg.minijava.ExpressionLeftRecursionTests;
import ch.unibe.iam.scg.minijava.JavaBytecodeGeneratorTest;
import ch.unibe.iam.scg.minijava.OptimizationTests;
import ch.unibe.iam.scg.minijava.PrettyPrintTest;
import ch.unibe.iam.scg.minijava.SyntaxTest;
import ch.unibe.iam.scg.minijava.TypeCheckerTest;

@RunWith(Suite.class)
@SuiteClasses(
{
	PrettyPrintTest.class, 
	SyntaxTest.class, 
	ExpressionLeftRecursionTests.class,
	TypeCheckerTest.class,
	JavaBytecodeGeneratorTest.class,
	OptimizationTests.class
})
public class AllTests
{

}
