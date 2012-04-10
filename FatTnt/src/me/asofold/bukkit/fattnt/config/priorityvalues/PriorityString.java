package me.asofold.bukkit.fattnt.config.priorityvalues;

public class PriorityString extends PriorityValue<String>{
	public PriorityString(String value, int priority, OverridePolicy eqPolicy) {
		super(value, priority, eqPolicy);
	}
	public void onEqPriority(PriorityValue<String> other){
		// same priority
		switch ( eqPolicy){
		case KEEP:
			// do nothing
			break;
		default:
			throw new IllegalArgumentException("Override policy not supported: "+eqPolicy);
		}
	}
	
	@Override
	public PriorityValue<String> copy() {
		return new PriorityString(value, priority, eqPolicy);
	}
}
