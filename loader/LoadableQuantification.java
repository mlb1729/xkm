/*
 * Created on Aug 9, 2005
 *
 
 */
package loader;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.entailments.Quantification;
import com.resonant.xkm.operators.EntailmentOperator;
import com.resonant.xkm.operators.QuantificationOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableQuantification 
	extends LoadableMember
{
	private	static int	_quantificationUID	= 0;
	
	private Object				_freeVariableName				= null;
	private LoadableLocal		_basisEntailmentLoadablePath	= null;
	private LoadableEntailment	_closureBodyLoadableEntailment	= null;
	
	public LoadableQuantification() {super();}
	
	public static String operatorName(Object localName, Object pathLocal) {
		Object path = pathLocal;
		if (pathLocal instanceof LoadableLocal){
			LoadableLocal local = (LoadableLocal) pathLocal;
			path = toPathString(local.getParameters());
		}
		String name = "(for all " + localName + " in " + path +")_" + ++_quantificationUID;
		return name;
	}

	// parameter[0] = free variable's name
	// parameter[1] = LoadableLocal which will reference the basis entailment
	// parameter[2] = LoadableEntailment which will be the free body loaded inside each closure
	public LoadableQuantification(Object[] parameters) {
		super(new Object[]{operatorName(getName(parameters[0]), parameters[1])});
		_freeVariableName = getName(parameters[0]);
		_basisEntailmentLoadablePath = (LoadableLocal)parameters[1];
		_closureBodyLoadableEntailment = (LoadableEntailment)parameters[2];
	}
	
	public EntailmentOperator newEntailmentOperator(Object name) {
		EntailmentOperator operator = new QuantificationOperator(name);
		return operator;
	}

	public Object load(Context context, Object name) {
        Quantification quantification = (Quantification)(super.load(context, name));
        Entailment basisEntailment = null;
        Object testObj = _basisEntailmentLoadablePath.load(context);
        try
        {
            basisEntailment = (Entailment) testObj;
        }
        catch (ClassCastException e)
        {
            System.out.println(e);
        }
		quantification.initializeLocalNameAndEntailment(_freeVariableName, 
														basisEntailment,
														_closureBodyLoadableEntailment);
		context.addMember(quantification);
		return quantification;
	}

}
