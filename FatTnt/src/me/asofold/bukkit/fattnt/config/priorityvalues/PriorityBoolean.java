package me.asofold.bukkit.fattnt.config.priorityvalues;

public class PriorityBoolean extends PriorityValue<Boolean>{
	public PriorityBoolean(Boolean value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}
	public void onEqPriority(PriorityValue<Boolean> other){
		// same priority
		switch ( eqPolicy){
		case OR:
			value = value || other.value;
			break;
		case AND:
			value = value && other.value;
			break;
		case KEEP:
			break;
		case OVERRIDE:
			value = other.value;
			break;
		default:
			throw new IllegalArgumentException("Override policy not supported: "+eqPolicy);
		}
	}
	
	@Override
	public PriorityValue<Boolean> copy() {
		return new PriorityBoolean(value, priority, eqPolicy);
	}
}
