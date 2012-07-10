package me.asofold.bukkit.fattnt.config.priorityvalues;

public class PriorityFloat extends PriorityValue<Float> {

	public PriorityFloat(Float value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}

	@Override
	void onEqPriority(PriorityValue<Float> other) {
		Number n = PriorityNumber.onEqPriority(value, eqPolicy, other.value);
		if (n != null) value = n.floatValue();
	}

	@Override
	public PriorityValue<Float> copy() {
		return new PriorityFloat(value, priority, eqPolicy);
	}

}
