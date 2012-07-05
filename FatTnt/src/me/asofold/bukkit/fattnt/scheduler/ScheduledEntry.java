package me.asofold.bukkit.fattnt.scheduler;

public class ScheduledEntry {
	public final long ts;
	public final double x;
	public final double z;
	public ScheduledEntry(final double x, final double z){
		ts = System.currentTimeMillis();
		this.x = x;
		this.z = z;
	}
}
