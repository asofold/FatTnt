package me.asofold.bpl.fattnt.config.priorityvalues;

public class PriorityLong extends PriorityValue<Long> {

	public PriorityLong(Long value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}

	@Override
	void onEqPriority(PriorityValue<Long> other) {
		Number n = PriorityNumber.onEqPriority(value, eqPolicy, other.value);
		if (n != null) value = n.longValue();
	}

	@Override
	public PriorityValue<Long> copy() {
		return new PriorityLong(value, priority, eqPolicy);
	}

}
