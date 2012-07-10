package me.asofold.bukkit.fattnt.config.priorityvalues;

/**
 * Value holding an object and a priority, for applying other values of the same type and adapting concerning to an OverridePolicy and priorities. 
 * @author mc_dev
 *
 * @param <T>
 */
public abstract class PriorityValue <T>{
	/**
	 * Stored value (content).
	 */
	public T value;
	/**
	 * Priority for the value.
	 */
	public int priority;
	/**
	 * Policy for what to happen if priorities are equal on applyValue.
	 */
	public OverridePolicy eqPolicy;
	/**
	 * Standard constructor for fast sub-classing.
	 * @param value
	 * @param priority
	 * @param eqPolicy
	 */
	public PriorityValue(T value, int priority, OverridePolicy eqPolicy){
		this.eqPolicy = eqPolicy;
		setValue(value, priority);
	}
	/**
	 * Set priority and value.
	 * @param value
	 * @param priority
	 */
	public void setValue(T value, int priority){
		this.value = value;
		this.priority = priority;
	}
	
	/**
	 * Priority and override-policy dependent setting mechanism.<br>
	 * NOTE: Unchecked cast to PriorityValue<T> - make sure this is intended or failures prevented by contract.
	 * @param other
	 * @throws Exceptions related to casting other to PriorityValue<T> (unchecked).
	 */
	@SuppressWarnings("unchecked")
	public void applyValue(PriorityValue<?> other){
		PriorityValue<T> ref = (PriorityValue<T>) other;
		if (ref.value == null) return;
		else if (value == null){
			value = ref.value;
			priority = ref.priority; // TODO or MAX or MIN?
			return;
		} 
		else if ( ref.priority < priority) return;
		else if ( ref.priority > priority){
			value = ref.value;
			priority = ref.priority;
			return;
		}
		else{
			switch(eqPolicy){
			case KEEP:
				return;
			case OVERRIDE:
				value = ref.value;
				return;
			}
			onEqPriority(ref);
		}
	}
	/**
	 * Get the value, return preset if value is null.
	 * @param preset
	 * @return
	 */
	public T getValue( T preset){
		if (value == null) return preset;
		else return value;
	} 
	
	/**
	 * Check if the value is set at all.
	 * @return
	 */
	public boolean isSet(){
		return value != null;
	}
	
	/**
	 * Override this, called on applyValue if values are both not null and priorities are equal.
	 * @param other
	 */
	abstract void onEqPriority(PriorityValue<T> other);
	
	/**
	 * Return a copy to set into new settings on applySettings - this might really clone the object or just keep references, in case a clone is not necessary by design.
	 * @return
	 */
	public abstract PriorityValue<T> copy();
}
