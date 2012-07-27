package me.asofold.bpl.fattnt.config.priorityvalues;

public class PriorityDouble extends PriorityValue<Double> {
	
	public PriorityDouble(Double value, int priority, OverridePolicy eqPolicy){
		super(value, priority, eqPolicy);
	}

	@Override
	void onEqPriority(PriorityValue<Double> other) {
		Number n = PriorityNumber.onEqPriority(value, eqPolicy, other.value);
		if (n != null) value = n.doubleValue();
	}

	@Override
	public PriorityValue<Double> copy() {
		return new PriorityDouble(value, priority, eqPolicy);
	}

}
