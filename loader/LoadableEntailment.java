/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import java.util.ArrayList;
import java.util.List;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.expressions.Variable;
import com.resonant.xkm.operators.EntailmentOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableEntailment 
	extends LoadableObject 
{
	protected LoadableEntailment() {super();}
	public LoadableEntailment(Object[] parameters) {super(parameters);}
	
	protected Context getParameterContext(Context context, Object name) {
		return context;
	}
	
	protected EntailmentOperator newEntailmentOperator(Object name) {
		EntailmentOperator operator = new EntailmentOperator(name);
		return operator;
	}
	
	public Object load(Context context) {
		Object result = load(context, getParameter(0));
		return result;
	}
	
	public Object load(Context context, Object nameParameter) {
		nameParameter = annotatedName(nameParameter);
		int size = getSize();
		BoundedExpression story = context.getStory();
		Context parameterContext = getParameterContext(context, nameParameter);
		EntailmentOperator operator = newEntailmentOperator(nameParameter);		
		List operands = new ArrayList(size);
		if (story != null) {
			operands.add(story);
		}
		Entailment expression = (Entailment)(operator.newExpression(operands, parameterContext));
		expression.setAnnotation(annotatedAnnotation(nameParameter));
		context.addMember(expression);
		if ((nameParameter != "") && (context.getVariable(nameParameter) == null)) {
			context.addVariable(expression);
		}
		if (parameterContext.getStory() == null) {
			parameterContext.initStory(expression);
		}
		for (int i=1; i < size; i++) {
			Object parameter = getParameter(i);
			if (parameter instanceof Loadable) {
					parameter = ((Loadable)parameter).load(parameterContext);
					registerLoadedObject(operands, parameter);
					// ToDo MLB: check this out
					if ((parameter instanceof Expression) && 
							!(parameter instanceof Variable)){
						Expression member = ((Expression)parameter);
						member.addDomainListener(expression);
					}
			}
		}
//		for (int phase=1; phase<4; phase++){
//			for (int i=1; i < size; i++) {
//				Object parameter = getParameter(i);
//				if (parameter instanceof Loadable) {
//					int type = ((parameter instanceof LoadableEntailment) ?
//									2 :	((parameter instanceof LoadableVariable) ? 2 : 2));
//					if (type == phase){
//						parameter = ((Loadable)parameter).load(parameterContext);
//						registerLoadedObject(operands, parameter);
//					}
//				}
//			}
//		}
		expression.activate();
		return expression;
	}

	public void registerLoadedObject(List operands, Object object) {
		if (object instanceof Entailment) {
			Entailment entailment = ((Entailment)object);
			operands.add(entailment);
		}
//		if (object instanceof Expression) {
//			Expression expression = ((Expression)object);
//			// operands.add(expression);
//		}
		return;
	}

}
