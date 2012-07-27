package me.asofold.bpl.fattnt.propagation;

import me.asofold.bpl.fattnt.config.Settings;

public class PropagationFactory {
	public static Propagation getPropagation(Settings settings){
		return new ArrayPropagation(settings);
	}
}
