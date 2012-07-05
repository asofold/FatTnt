package me.asofold.bukkit.fattnt.config.priorityvalues;

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
		if (!(other instanceof PriorityObject<?>)) return;
		T otherVal;
		try{
			otherVal = other.getValue(null);
		}
		catch (Throwable t){
			// bad object 
			return;
		}
		switch (eqPolicy) {
		case KEEP:
			break;
		case OVERRIDE:
			value = otherVal;
		default:
			throw new IllegalArgumentException("Unsupported override policy: "+eqPolicy);
		}
	}

	@Override
	public PriorityValue<T> copy() {
		return new PriorityObject<T>(value, priority, eqPolicy);
	}

}
