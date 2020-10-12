/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableConstant;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableEnum;
import com.resonant.xkm.loader.LoadableExpression;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableSetObject;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestSimpleLife 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestSimpleLife.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestSimpleLife.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???	
		
		// define bounds for variables
		Object[] boundsIArgs100000000 = {new Integer(0), new Integer(100000000)};
		Loadable boundsI100000000 = new LoadableBounds(boundsIArgs100000000);

		Object[] boundsIArgs1000 = {new Integer(0), new Integer(1000)};
		Loadable boundsI1000 = new LoadableBounds(boundsIArgs1000);

		
		String namePrefPlus = "PrefPlus";
		String namePref = "Pref";
		Object[] enumDomainRateClasses = {"Rate Classes", namePrefPlus, namePref};
		Loadable enumRateClasses = new LoadableEnum(enumDomainRateClasses);
		
		Object[] boundsBArgs = {Boolean.class};
		Loadable boundsB = new LoadableBounds(boundsBArgs);
		
		// Common vars ("base class")
		String nameRequired = "Required";
		Object[] varRequiredArgs = {nameRequired, boundsB,};			// Activity & Doc
		Loadable varRequired = new LoadableVariable(varRequiredArgs);
		Object[] refRequiredArgs = {nameRequired};
		Loadable refToRequired = new LoadableLocal(refRequiredArgs);
		
		String nameCompleted = "Completed";
		Object[] varCompletedArgs = {nameCompleted, boundsB,};			// Activity only
		Loadable varCompleted = new LoadableVariable(varCompletedArgs);
		
		String nameReady = "Ready";
		Object[] varReadyArgs = {nameReady, boundsB,};					// Activity only
		Loadable varReady = new LoadableVariable(varReadyArgs);
		
		String nameReceived = "Received";
		Object[] varReceivedArgs = {nameReceived, boundsB,};			// Doc only
		Loadable varReceived = new LoadableVariable(varReceivedArgs);
	
		// Activities
		String nameBloodTest = "BloodTest";
		Object[] memberBloodTestArgs = {nameBloodTest, varRequired, varReady, varCompleted,};
		Loadable memberBloodTest = new LoadableStructure(memberBloodTestArgs);
		
		String nameIssuePolicy = "IssuePolicy";
		Object[] memberIssuePolicyArgs = {nameIssuePolicy, varRequired, varReady, varCompleted,};
		Loadable memberIssuePolicy = new LoadableStructure(memberIssuePolicyArgs);
		
		// Blood Profile Doc Resource 
		String nameCholesterol = "Cholesterol";
		Object[] varCholesterolArgs = {nameCholesterol, boundsI1000,};
		Loadable varCholesterol = new LoadableVariable(varCholesterolArgs);

		String nameHDL = "HDL";
		Object[] varHDLArgs = {nameHDL, boundsI1000,};
		Loadable varHDL = new LoadableVariable(varHDLArgs);

		String nameBloodProfile = "BloodProfile";
		Object[] memberBloodProfileArgs = {nameBloodProfile, varRequired, varReceived,
											varCholesterol,	varHDL,};
		Loadable memberBloodProfile = new LoadableStructure(memberBloodProfileArgs);
		
		// Application Resource 
		String nameAmount = "Amount";
		Object[] varAmountArgs = {nameAmount, boundsI100000000,};
		Loadable varAmount = new LoadableVariable(varAmountArgs);
	
		String nameRequestedClass = "RequestedClass";
		Object[] varRequestedClassArgs = {nameRequestedClass, enumRateClasses,};
		Loadable varRequestedClass = new LoadableSetObject(varRequestedClassArgs);
	
		String nameApplication = "Application";
		Object[] memberApplicationArgs = {nameApplication,
											varAmount, varRequestedClass,};
		Loadable memberApplication = new LoadableStructure(memberApplicationArgs);
		
		// Policy Resource 
		String nameRateClass = "RateClass";
		Object[] varRateClassArgs = {nameRateClass, enumRateClasses,};
		Loadable varRateClass = new LoadableSetObject(varRateClassArgs);
	
		String namePermissibleRateClasses = "PermissibleRateClasses";
		Object[] varPermissibleRateClassesArgs = {namePermissibleRateClasses, enumRateClasses,};
		Loadable varPermissibleRateClasses = new LoadableSetObject(varPermissibleRateClassesArgs);
	
		String nameDocComplete = "DocComplete";
		Object[] varDocCompleteArgs = {nameDocComplete, boundsB,};					// Activity only
		Loadable varDocComplete = new LoadableVariable(varDocCompleteArgs);
	
		String nameIssuable = "Issuable";
		Object[] varIssuableArgs = {nameIssuable, boundsB,};					// Activity only
		Loadable varIssuable = new LoadableVariable(varIssuableArgs);
	
		String nameIssued = "Issued";
		Object[] varIssuedArgs = {nameIssued, boundsB,};					// Activity only
		Loadable varIssued = new LoadableVariable(varIssuedArgs);
	
		String namePolicy = "Policy";
		Object[] memberPolicyArgs = {namePolicy,
										varRateClass, varPermissibleRateClasses,
										varDocComplete, varIssuable, varIssued,};
		Loadable memberPolicy = new LoadableStructure(memberPolicyArgs);
		
		
		// The case & guidelines
		String nameCase = "TheCase";
		
		// paths
		Object[] refPermissibleRateClassesArgs = {namePolicy, namePermissibleRateClasses};
		Loadable refToPermissibleRateClasses = new LoadableLocal(refPermissibleRateClassesArgs);
		
		Object[] refCholesterolArgs = {nameBloodProfile, nameCholesterol};
		Loadable refToCholesterol = new LoadableLocal(refCholesterolArgs);
	
		Object[] refHDLArgs = {nameBloodProfile, nameHDL};
		Loadable refToHDL = new LoadableLocal(refHDLArgs);
	
		Object[] refAmountArgs = {nameApplication, nameAmount};
		Loadable refToAmount = new LoadableLocal(refAmountArgs);
	
		Object[] refBloodProfileRequiredArgs = {nameBloodProfile, nameRequired};
		Loadable refToBloodProfileRequired = new LoadableLocal(refBloodProfileRequiredArgs);
	
		Object[] refBloodProfileReceivedArgs = {nameBloodProfile, nameReceived};
		Loadable refToBloodProfileReceived = new LoadableLocal(refBloodProfileReceivedArgs);
	
		Object[] refDocCompleteArgs = {namePolicy, nameDocComplete};
		Loadable refToDocComplete = new LoadableLocal(refDocCompleteArgs);
	
		Object[] refBloodTestRequiredArgs = {nameBloodTest, nameRequired};
		Loadable refToBloodTestRequired = new LoadableLocal(refBloodTestRequiredArgs);
	
		Object[] refBloodTestReadyArgs = {nameBloodTest, nameReady};
		Loadable refToBloodTestReady = new LoadableLocal(refBloodTestReadyArgs);
	
		Object[] refRequestedClassArgs = {nameApplication, nameRequestedClass};
		Loadable refToRequestedClass = new LoadableLocal(refRequestedClassArgs);
		
		Object[] refIssuePolicyRequiredArgs = {nameIssuePolicy, nameRequired};
		Loadable refToIssuePolicyRequired = new LoadableLocal(refIssuePolicyRequiredArgs);
		
		Object[] refRateClassArgs = {namePolicy, nameRateClass};
		Loadable refToRateClass = new LoadableLocal(refRateClassArgs);
		
		Object[] refIssuePolicyReadyArgs = {nameIssuePolicy, nameReady};
		Loadable refToIssuePolicyReady = new LoadableLocal(refIssuePolicyReadyArgs);
		
		Object[] refIssuableArgs = {namePolicy, nameIssuable};
		Loadable refToIssuable = new LoadableLocal(refIssuableArgs);
		
		Object[] refIssuePolicyCompletedArgs = {nameIssuePolicy, nameCompleted};
		Loadable refToIssuePolicyCompleted = new LoadableLocal(refIssuePolicyCompletedArgs);
		
		Object[] refIssuedArgs = {namePolicy, nameIssued};
		Loadable refToIssued = new LoadableLocal(refIssuedArgs);
		
		// constants
		Object[] enumConstantPrefPlus = {"Just Preferred Plus", namePrefPlus,};
		Loadable enumPrefPlus = new LoadableEnum(enumConstantPrefPlus);

		Object[] constantArgs200 = {new Integer(200)};
		Loadable constant200 = new LoadableConstant(constantArgs200);

		Object[] constantArgs5 = {new Integer(5)};
		Loadable constant5 = new LoadableConstant(constantArgs5);

		Object[] constantArgs200000 = {new Integer(200000)};
		Loadable constant200000 = new LoadableConstant(constantArgs200000);

		// "Product Guideline(s)"
		
		//--------------------------------------------------------------------------------
		Object[] containsPrefPlusExprArgs = {RootOperator.Contains, refToPermissibleRateClasses, enumPrefPlus};
		Loadable containsPrefPlusExpr = new LoadableExpression(containsPrefPlusExprArgs);

		Object[] lowCholesterolExprArgs = {RootOperator.LessEqual, refToCholesterol, constant200,};
		Loadable lowCholesterolExpr = new LoadableExpression(lowCholesterolExprArgs);
		
//		Object[] fiveHDLExprArgs = {RootOperator.Sum, refToHDL, refToHDL, refToHDL, refToHDL, refToHDL, };
		Object[] fiveHDLExprArgs = {RootOperator.Scale, constant5, refToHDL, };
		Loadable fiveHDLExpr = new LoadableExpression(fiveHDLExprArgs);
		
		Object[] lowHDLExprArgs = {RootOperator.LessEqual, refToCholesterol, fiveHDLExpr,};
		Loadable lowHDLExpr = new LoadableExpression(lowHDLExprArgs);
		
		Object[] andCholesterolExprArgs = {RootOperator.And, lowCholesterolExpr, lowHDLExpr,};
		Loadable andCholesterolExpr = new LoadableExpression(andCholesterolExprArgs);
		
		Object[] maxCholesterolExprArgs = {RootOperator.Implies, containsPrefPlusExpr, andCholesterolExpr,};
		Loadable maxCholesterolExpr = new LoadableExpression(maxCholesterolExprArgs);
		
		Object[] maxCholesterolCTArgs = {maxCholesterolExpr, "PerPlus Requires Chol. <= 200 and Chol. <= 5 * HDL"};
		Loadable maxCholesterolCT = new LoadableConstrainment(maxCholesterolCTArgs);
		//--------------------------------------------------------------------------------
		
		// "Process Guidelines"
		
		//--------------------------------------------------------------------------------
		Object[] bigAmountExprArgs = {RootOperator.GreaterThan, refToAmount, constant200000,};
		Loadable bigAmountExpr = new LoadableExpression(bigAmountExprArgs);
		
		Object[] bigAmountImpliesExprArgs = {RootOperator.Implies, bigAmountExpr, refToBloodProfileRequired,};
		Loadable bigAmountImpliesExpr = new LoadableExpression(bigAmountImpliesExprArgs);
		
		Object[] bigAmountImpliesCTArgs = {bigAmountImpliesExpr, "If amount > 200000, blood profiel is required"};
		Loadable bigAmountImpliesCT = new LoadableConstrainment(bigAmountImpliesCTArgs);
		//--------------------------------------------------------------------------------
		Object[] bloodTestImpliesExprArgs = {RootOperator.Implies, refToBloodProfileRequired, refToBloodProfileReceived,};
		Loadable bloodTestImpliesExpr = new LoadableExpression(bloodTestImpliesExprArgs);
		
		Object[] docCompleteExprArgs = {RootOperator.Equal, refToDocComplete, bloodTestImpliesExpr, };
		Loadable docCompleteExpr = new LoadableExpression(docCompleteExprArgs);
		
		Object[] docCompleteCTArgs = {docCompleteExpr, "Doc is complete when all 'LifeDocs' are complete"};
		Loadable docCompleteCT = new LoadableConstrainment(docCompleteCTArgs);		
		//--------------------------------------------------------------------------------
		Object[] bloodProfileNotReceivedExprArgs = {RootOperator.Not, refToBloodProfileReceived,};
		Loadable bloodProfileNotReceivedExpr = new LoadableExpression(bloodProfileNotReceivedExprArgs);
		
		Object[] bloodTestInProcessExprArgs = {RootOperator.And, refToBloodProfileRequired, bloodProfileNotReceivedExpr,};
		Loadable bloodTestInProcessExpr = new LoadableExpression(bloodTestInProcessExprArgs);
		
		Object[] bloodTestInProcessImpliesExprArgs = {RootOperator.Implies, bloodTestInProcessExpr, refToBloodTestRequired,};
		Loadable bloodTestInProcessImpliesExpr = new LoadableExpression(bloodTestInProcessImpliesExprArgs);
		
		Object[] bloodTestInProcessImpliesCTArgs = {bloodTestInProcessImpliesExpr, "If profile is required and not available, require blood test activity"};
		Loadable bloodTestInProcessImpliesCT = new LoadableConstrainment(bloodTestInProcessImpliesCTArgs);
		//--------------------------------------------------------------------------------
		Object[] bloodTestReadyImpliesExprArgs = {RootOperator.Implies, refToBloodTestRequired, refToBloodTestReady,};
		Loadable bloodTestReadyImpliesExpr = new LoadableExpression(bloodTestReadyImpliesExprArgs);
		
		Object[] bloodTestReadyImpliesCTArgs = {bloodTestReadyImpliesExpr, "Blood test is ready when required"};
		Loadable bloodTestReadyImpliesCT = new LoadableConstrainment(bloodTestReadyImpliesCTArgs);
		//--------------------------------------------------------------------------------
		Object[] containsRequestedExprArgs = {RootOperator.Contains, refToPermissibleRateClasses, refToRequestedClass};
		Loadable containsRequestedExpr = new LoadableExpression(containsRequestedExprArgs);
		
		Object[] containsRequestedAndExprArgs = {RootOperator.And, refToDocComplete, containsRequestedExpr};
		Loadable containsRequestedAndExpr = new LoadableExpression(containsRequestedAndExprArgs);
		
		Object[] docImpliesIssuableExprArgs = {RootOperator.Equal, refToIssuable, containsRequestedAndExpr, };
		Loadable docImpliesIssuableExpr = new LoadableExpression(docImpliesIssuableExprArgs);
		
		Object[] docImpliesIssuableCTArgs = {docImpliesIssuableExpr, "Doc is complete when all 'LifeDocs' are complete"};
		Loadable docImpliesIssuableCT = new LoadableConstrainment(docImpliesIssuableCTArgs);		
		//--------------------------------------------------------------------------------
		Object[] issuableImpliesIssuanceExprArgs = {RootOperator.Implies, refToIssuable, refToIssuePolicyRequired,};
		Loadable issuableImpliesIssuanceExpr = new LoadableExpression(issuableImpliesIssuanceExprArgs);
		
		Object[] issuableImpliesIssuanceCTArgs = {issuableImpliesIssuanceExpr, "Issue the policy when issuable"};
		Loadable issuableImpliesIssuanceCT = new LoadableConstrainment(issuableImpliesIssuanceCTArgs);
		//--------------------------------------------------------------------------------
// 		Object[] policyContainsAppExprArgs = {RootOperator.Contains, refToRateClass, refToRequestedClass};
//		Loadable policyContainsAppExpr = new LoadableExpression(policyContainsAppExprArgs);
//		
//		Object[] appContainsPolicyExprArgs = {RootOperator.Contains, refToRateClass, refToRequestedClass};
//		Loadable appContainsPolicyExpr = new LoadableExpression(appContainsPolicyExprArgs);
//		
//		Object[] rateIsRequestedExprArgs = {RootOperator.And, policyContainsAppExpr, appContainsPolicyExpr};
//		Loadable rateIsRequestedExpr = new LoadableExpression(rateIsRequestedExprArgs);
		
		Object[] rateIsRequestedExprArgs = {RootOperator.SetEqual, refToRateClass, refToRequestedClass};
		Loadable rateIsRequestedExpr = new LoadableExpression(rateIsRequestedExprArgs);		
		
		Object[] issuableImpliesRateExprArgs = {RootOperator.Implies, refToIssuable, rateIsRequestedExpr,};
		Loadable issuableImpliesRateExpr = new LoadableExpression(issuableImpliesRateExprArgs);
		
		Object[] issuableImpliesRateCTArgs = {issuableImpliesRateExpr, "Issuable means requested rate"};
		Loadable issuableImpliesRateCT = new LoadableConstrainment(issuableImpliesRateCTArgs);	
		//--------------------------------------------------------------------------------
		Object[] issuePolicyReadyImpliesExprArgs = {RootOperator.Implies, refToIssuePolicyRequired, refToIssuePolicyReady,};
		Loadable issuePolicyReadyImpliesExpr = new LoadableExpression(issuePolicyReadyImpliesExprArgs);
		
		Object[] issuePolicyReadyImpliesCTArgs = {issuePolicyReadyImpliesExpr, "Issue policy is ready when required"};
		Loadable issuePolicyReadyImpliesCT = new LoadableConstrainment(issuePolicyReadyImpliesCTArgs);
		//--------------------------------------------------------------------------------
		Object[] issuePolicyCompletedImpliesExprArgs = {RootOperator.Implies, refToIssuePolicyCompleted, refToIssued,};
		Loadable issuePolicyCompletedImpliesExpr = new LoadableExpression(issuePolicyCompletedImpliesExprArgs);
		
		Object[] issuePolicyCompletedImpliesCTArgs = {issuePolicyCompletedImpliesExpr, "Policy is completed when issued"};
		Loadable issuePolicyCompletedImpliesCT = new LoadableConstrainment(issuePolicyCompletedImpliesCTArgs);
		//--------------------------------------------------------------------------------

		// case structure definition
		Object[] structureArgs = {nameCase,
									// enumRateClasses, enumPrefPlus,
									memberBloodTest, memberIssuePolicy,
									memberApplication, memberBloodProfile, memberPolicy,
									maxCholesterolCT,
									bigAmountImpliesCT, 
									docCompleteCT, 
									bloodTestInProcessImpliesCT,
									bloodTestReadyImpliesCT,
									docImpliesIssuableCT,
									issuableImpliesRateCT,
									issuePolicyReadyImpliesCT,
									issuePolicyCompletedImpliesCT,
									};
		Loadable structure = new LoadableStructure(structureArgs);
		

		// *** session ***

		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		BoundedExpression x = (BoundedExpression)(structure.load(context));
		
		// now make it exist
		kb.reify(x);
		kb.commitAll();
		
		// refiy all the members manually (for now)
		Object[] pathBloodTestArgs = {nameCase, nameBloodTest,};	
 		Assertion varBloodTestAssertion = kb.newAssertion(pathBloodTestArgs, "==", Boolean.TRUE);
		kb.assertAssertion(varBloodTestAssertion);

		Object[] pathIssuePolicyArgs = {nameCase, nameIssuePolicy,};	
 		Assertion varIssuePolicyAssertion = kb.newAssertion(pathIssuePolicyArgs, "==", Boolean.TRUE);
		kb.assertAssertion(varIssuePolicyAssertion);

		Object[] pathApplicationArgs = {nameCase, nameApplication,};	
 		Assertion varApplicationAssertion = kb.newAssertion(pathApplicationArgs, "==", Boolean.TRUE);
		kb.assertAssertion(varApplicationAssertion);

		Object[] pathBloodProfileArgs = {nameCase, nameBloodProfile,};	
 		Assertion varBloodProfileAssertion = kb.newAssertion(pathBloodProfileArgs, "==", Boolean.TRUE);
		kb.assertAssertion(varBloodProfileAssertion);

		Object[] pathPolicyArgs = {nameCase, namePolicy,};	
 		Assertion varPolicyAssertion = kb.newAssertion(pathPolicyArgs, "==", Boolean.TRUE);
		kb.assertAssertion(varPolicyAssertion);

		System.out.println("\nInitial KB state...");	
		System.out.println(context);
		
		System.out.println("\nChanges:");
		System.out.println(toString(kb.getChangedVariables()));
		
		Object[] pathAmountArgs = {nameCase, nameApplication, nameAmount};	
 		Assertion varAmount500000Assertion = kb.newAssertion(pathAmountArgs, "==", new Integer(500000));
		kb.assertAssertion(varAmount500000Assertion);

		Object[] pathRequestedPrefArgs = {nameCase, namePolicy, nameRateClass, namePrefPlus};
		Assertion varRequestedPrefAssertion = kb.newAssertion(pathRequestedPrefArgs, "==", Boolean.TRUE);
		
		System.out.println("\nAsserting");	
		System.out.println(varAmount500000Assertion);		
		System.out.println(varRequestedPrefAssertion);		
		
		kb.assertAssertion(varRequestedPrefAssertion);
		
		System.out.println("\nFinal KB state...");	
		System.out.println(context);		

		System.out.println("\nChanges:");
		System.out.println(toString(kb.getChangedVariables()));
		/*
		{
			Object[] changes = kb.getRawChangedVariables();
			for (int i=0; i<changes.length; i++) {
				BoundedVariable variable = ((BoundedVariable)changes[i]);
				System.out.println("  " + variable);			

				VariableOperator operator = ((VariableOperator)variable.getOperator());
				Object[] names = operator.getPathNames();
				String path = "  @";
				for (int j=0; j<names.length; j++){
					path += "/" + names[j];
				}
				System.out.println(path);			
			}
		}
		*/
		
		kb.commitAll();
		
		System.out.println("\n...Test Done");
	}
}
