package me.asofold.bukkit.fattnt.scheduler;

public interface ProcessHandler<T> {
	public void process(T entry);
}
