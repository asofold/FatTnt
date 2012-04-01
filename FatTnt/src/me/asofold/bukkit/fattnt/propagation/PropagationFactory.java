package me.asofold.bukkit.fattnt.propagation;

import me.asofold.bukkit.fattnt.config.Settings;

public class PropagationFactory {
	public static Propagation getPropagation(Settings settings){
		return new ArrayPropagation(settings);
	}
}
