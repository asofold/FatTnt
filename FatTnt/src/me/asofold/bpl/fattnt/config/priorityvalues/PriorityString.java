package me.asofold.bpl.fattnt.config.priorityvalues;

public class PriorityString extends PriorityValue<String>{
	public PriorityString(String value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}
	
	public void onEqPriority(PriorityValue<String> other){
		// same priority
		switch ( eqPolicy){
		case MIN:
			if (value.compareTo(other.value) == 1) value = other.value;
			return;
		case MAX:
			if (value.compareTo(other.value) == -1) value = other.value;
			return;
		case OR:
			if (value.isEmpty()) value = other.value;
			return;
		case ADD:
			value = value + other.value;
			return;
		default:
			throw new IllegalArgumentException("Override policy not supported: "+eqPolicy);
		}
	}
	
	@Override
	public PriorityValue<String> copy() {
		return new PriorityString(value, priority, eqPolicy);
	}
}
