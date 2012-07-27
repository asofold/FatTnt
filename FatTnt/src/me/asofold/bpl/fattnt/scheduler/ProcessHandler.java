package me.asofold.bpl.fattnt.scheduler;

public interface ProcessHandler<T> {
	public void process(T entry);
}
