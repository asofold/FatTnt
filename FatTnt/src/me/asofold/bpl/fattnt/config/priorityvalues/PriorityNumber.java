package me.asofold.bpl.fattnt.config.priorityvalues;

public class PriorityNumber extends PriorityValue<Number>{
	public PriorityNumber(Number value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}
	
	public void onEqPriority( PriorityValue<Number> other){
		final Number ref = onEqPriority(value, eqPolicy, other.value);
		if (ref != null) value = ref;
	}
	
	/**
	 * Return a Number if value has to be changed, null otherwise
	 * @param n1
	 * @param p1
	 * @param n2
	 * @param p2
	 * @return
	 */
	static Number onEqPriority(final Number n1, final OverridePolicy eqPolicy, final Number n2){
		// same priority
		switch(eqPolicy){
		case MAX:
			if (n1.doubleValue() < n2.doubleValue()) return n2;
			break;
		case MIN:
			if (n1.doubleValue() > n2.doubleValue()) return n2;
			break;
		case MULT:
			return new Double(n1.doubleValue() * n2.doubleValue());
		case ADD:
			return new Double(n1.doubleValue() + n2.doubleValue());
		default:
			throw new IllegalArgumentException("Unsupported override policy: "+eqPolicy);
		}
		return null;
	}
	
	@Override
	public PriorityValue<Number> copy() {
		return new PriorityNumber(value, priority, eqPolicy);
	}
}
