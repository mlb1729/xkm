/*
 * Created on Aug 9, 2005
 *
 
 */
package operators;

import entailments.Quantification;


/**
 * @author MLB
 *
 *
 */
public class QuantificationOperator 
	extends EntailmentOperator
{
	public QuantificationOperator() {this("");}
	
	public QuantificationOperator(Object name) {
		this(name, Quantification.class);	
	}

	public QuantificationOperator(Object name, Class objectClass) {
		super(name, objectClass);
	}

}
