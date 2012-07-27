package me.asofold.bpl.fattnt.config.priorityvalues;

public class PriorityInteger extends PriorityValue<Integer> {

	public PriorityInteger(Integer value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}

	@Override
	void onEqPriority(PriorityValue<Integer> other) {
		Number n = PriorityNumber.onEqPriority(value, eqPolicy, other.value);
		if (n != null) value = n.intValue();
	}

	@Override
	public PriorityValue<Integer> copy() {
		return new PriorityInteger(value, priority, eqPolicy);
	}

}
