package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.resonant.xkm.containers.Namespace;
import com.resonant.xkm.containers.NamespaceObject;
import com.resonant.xkm.functions.Quantity;
import com.resonant.xkm.operations.And;
import com.resonant.xkm.operations.AttrFloatMax;
import com.resonant.xkm.operations.AttrFloatMin;
import com.resonant.xkm.operations.AttrIntMax;
import com.resonant.xkm.operations.AttrIntMin;
import com.resonant.xkm.operations.Contains;
import com.resonant.xkm.operations.Entails;
import com.resonant.xkm.operations.Equal;
import com.resonant.xkm.operations.FloatCond;
import com.resonant.xkm.operations.FloatInt;
import com.resonant.xkm.operations.FloatMax;
import com.resonant.xkm.operations.FloatMin;
import com.resonant.xkm.operations.FloatNeg;
import com.resonant.xkm.operations.FloatScale;
import com.resonant.xkm.operations.FloatSum;
import com.resonant.xkm.operations.GreaterEqual;
import com.resonant.xkm.operations.GreaterThan;
import com.resonant.xkm.operations.IntCond;
import com.resonant.xkm.operations.IntFloat;
import com.resonant.xkm.operations.IntMax;
import com.resonant.xkm.operations.IntMin;
import com.resonant.xkm.operations.IntNeg;
import com.resonant.xkm.operations.IntScale;
import com.resonant.xkm.operations.IntSum;
import com.resonant.xkm.operations.LessEqual;
import com.resonant.xkm.operations.LessThan;
import com.resonant.xkm.operations.Nand;
import com.resonant.xkm.operations.Nor;
import com.resonant.xkm.operations.Not;
import com.resonant.xkm.operations.Or;
import com.resonant.xkm.operations.SetEqual;
import com.resonant.xkm.operations.StrCat;
import com.resonant.xkm.operations.Unequal;

/**
 * @author MLB
 *
 *
 */
public class RootOperator 
	extends OperatorObject 
{
	private static final Namespace _namespace = new NamespaceObject(Collections.synchronizedMap(new HashMap()));
	
	private static final List _rootOperators = new ArrayList();
	
	private static List get_rootOperators() {return _rootOperators;}
	
	public static Operator[] rootOperators() {
		List rootOperators = get_rootOperators();
		Operator[] operators = new Operator[rootOperators.size()];
		rootOperators.toArray(operators);
		return operators;
	}
		
	public static Namespace namespace() {
		return _namespace;
	}
	
	public static Operator getOperator(Object name) {
		Operator operator = (Operator)(namespace().get(name));
		return operator;
	}
	
	public RootOperator() {super();}
	
	public RootOperator(String name, Class expressionClass) {
		super(name, expressionClass);
		namespace().add(this);
		get_rootOperators().add(this);
	}

	public String toString() {
		String string = ((String)getName());
		return string;
	}

	public static final RootOperator LessThan = new RootOperator("<", LessThan.class);
	public static final RootOperator LessEqual = new RootOperator("<=", LessEqual.class);
	public static final RootOperator GreaterThan = new RootOperator(">", GreaterThan.class);
	public static final RootOperator GreaterEqual = new RootOperator(">=", GreaterEqual.class);
	
//	public static final RootOperator FooLT = new RootOperator("?<", LessThan.class);
	public static final RootOperator Implies = new RootOperator("imp", LessEqual.class);
//	public static final RootOperator FooGT = new RootOperator("?>", GreaterThan.class);
//	public static final RootOperator FooLE = new RootOperator("?>=", GreaterEqual.class);

	public static final RootOperator ImpliesAll = new RootOperator("=|", Entails.class);

	public static final RootOperator Equal = new RootOperator("==", Equal.class);
	public static final RootOperator Unequal = new RootOperator("!=", Unequal.class);

	public static final RootOperator Eqv = new RootOperator("eqv", Equal.class);
	public static final RootOperator Xor = new RootOperator("xor", Unequal.class);

	public static final RootOperator And = new RootOperator("and", And.class);
	public static final RootOperator Or = new RootOperator("or", Or.class);
	public static final RootOperator Nand = new RootOperator("nand", Nand.class);
	public static final RootOperator Nor = new RootOperator("nor", Nor.class);

	
	public static final RootOperator All = new RootOperator("all", And.class);
	public static final RootOperator Any = new RootOperator("any", Or.class);
	public static final RootOperator NotAll = new RootOperator("notAll", Nand.class);
	public static final RootOperator None = new RootOperator("none", Nor.class);
	
	public static final RootOperator Not = new RootOperator("not", Not.class);
	
	public static final RootOperator Int = new RootOperator("int", IntFloat.class);
	public static final RootOperator Flo = new RootOperator("flo", FloatInt.class);
	
	public static final RootOperator IntCond = new RootOperator("?", IntCond.class);		
	public static final RootOperator FloatCond = new RootOperator("f?", FloatCond.class);	
	
	public static final RootOperator Min = new RootOperator("min", IntMin.class);
	public static final RootOperator Max = new RootOperator("max", IntMax.class);
	public static final RootOperator FloatMin = new RootOperator("fmin", FloatMin.class);
	public static final RootOperator FloatMax = new RootOperator("fmax", FloatMax.class);
	
	public static final RootOperator Neg = new RootOperator("neg", IntNeg.class);
	public static final RootOperator FloatNeg = new RootOperator("fneg", FloatNeg.class);

	public static final RootOperator Sum = new RootOperator("+", IntSum.class);	
	public static final RootOperator FloatSum = new RootOperator("f+", FloatSum.class);
	
	public static final RootOperator Scale = new RootOperator("scale", IntScale.class);	
	public static final RootOperator FloatScale = new RootOperator("fscale", FloatScale.class);
	
	public static final RootOperator StrCat = new RootOperator("s+", StrCat.class);

	public static final RootOperator Contains = new RootOperator("set>=", Contains.class);
	public static final RootOperator SetEqual = new RootOperator("set==", SetEqual.class);

	public static final RootOperator Quantity = new RootOperator("qty", Quantity.class);

	public static final RootOperator IntMinAttr = new RootOperator("minAttr", AttrIntMin.class);
	public static final RootOperator IntMaxAttr = new RootOperator("maxAttr", AttrIntMax.class);

	public static final RootOperator FloatMinAttr = new RootOperator("fminAttr", AttrFloatMin.class);
	public static final RootOperator FloatMaxAttr = new RootOperator("fmaxAttr", AttrFloatMax.class);

}
