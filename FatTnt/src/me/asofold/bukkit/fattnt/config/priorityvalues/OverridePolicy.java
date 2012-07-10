package me.asofold.bukkit.fattnt.config.priorityvalues;

/**
 * How to override values.
 * @author mc_dev
 *
 */
public enum OverridePolicy {
	/**
	 * Set to maximum for numbers.
	 */
	MAX,
	/**
	 * Set to minimum for numbers.
	 */
	MIN,
	/**
	 * Set to multiplication for numbers.
	 */
	MULT,
	/**
	 * Set to sum of both values.
	 */
	ADD,
	/**
	 * Set to boolean OR.
	 */
	OR,
	/**
	 * Set to boolean AND.
	 */
	AND,
	/**
	 * Override with other value, handled in PriorityValue.applyValue.
	 */
	OVERRIDE,
	/**
	 * Keep value, handled in PriorityValue.applyValue.
	 */
	KEEP,
}
