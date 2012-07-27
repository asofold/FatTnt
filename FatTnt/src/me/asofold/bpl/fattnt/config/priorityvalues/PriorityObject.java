package me.asofold.bpl.fattnt.config.priorityvalues;

/**
 * Clone returns a new PriorityObject referencing the same value !
 * @author mc_dev
 *
 * @param <T>
 */
public class PriorityObject<T> extends PriorityValue<T> {

	public PriorityObject(T value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}

	@Override
	void onEqPriority(PriorityValue<T> other) {
		throw new IllegalArgumentException("Unsupported override policy: "+eqPolicy);
	}

	@Override
	public PriorityValue<T> copy() {
		return new PriorityObject<T>(value, priority, eqPolicy);
	}

}
