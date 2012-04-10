package me.asofold.bukkit.fattnt.config.priorityvalues;

public class PriorityNumber extends PriorityValue<Number>{
	public PriorityNumber(Number value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}
	public void onEqPriority( PriorityValue<Number> other){
		// same priority
		int cmp = Double.compare(value.doubleValue(), other.value.doubleValue());
		switch(eqPolicy){
		case MAX:
			if (cmp == -1) value = other.value;
			break;
		case MIN:
			if (cmp == 1) value = other.value;
			break;
		case MULT:
			// TODO: preserve type ?
			value = new Double( value.doubleValue() * other.value.doubleValue());
			break;
		case OVERRIDE:
			value = other.value;
			break;
		case KEEP:
			break;
		default:
			throw new IllegalArgumentException("Unsupported override policy: "+eqPolicy);
		}
	}
	
	@Override
	public PriorityValue<Number> copy() {
		return new PriorityNumber(value, priority, eqPolicy);
	}
}
