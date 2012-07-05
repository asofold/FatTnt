package me.asofold.bukkit.fattnt.scheduler;

public interface ScheduledEntry {
	public long getExpirationTime();
	public int getBlockX();
	public int getBlockZ();
}
