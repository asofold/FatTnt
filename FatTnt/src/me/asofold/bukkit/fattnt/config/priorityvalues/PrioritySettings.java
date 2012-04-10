package me.asofold.bukkit.fattnt.config.priorityvalues;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class, to setup with applySettings easily.<br>
 * Usage: PriorityBoolean b = addValue("b", new PriorityBoolean(...));<br>
 * @author mc_dev
 *
 */
public class PrioritySettings{
	protected Map<String, PriorityValue<?>> nameValueMap = new HashMap<String, PriorityValue<?>>();
	
	public void addValue(String name, PriorityValue<?> value){
		nameValueMap.put(name, value);
	}
	
	public void applySettings(PrioritySettings other){
		for (String name: other.nameValueMap.keySet()){
			PriorityValue<?> ov = other.nameValueMap.get(name);
			PriorityValue<?> v = nameValueMap.get(name);
			if (v == null){
				nameValueMap.put(name, ov);
				continue;
			}
			v.applyValue(ov);
		}
	}
}
