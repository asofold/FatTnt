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
	
	public <T1 extends PriorityValue<?>> T1 addValue(String name, T1 value){
		nameValueMap.put(name, value);
		return value;
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
	
	public void setPriority(int priority){
		for (PriorityValue<?> v : nameValueMap.values()){
			v.priority = priority;
		}
	}
	
	public boolean hasValues(){
		if (nameValueMap.isEmpty()) return false;
		for (PriorityValue<?> v : nameValueMap.values()){
			if (v.value != null) return true;
		}
		return false;
	}
	
	public void resetAllValues(int priority){
		for (PriorityValue<?> v : nameValueMap.values()){
			v.setValue(null, priority);
		}
	}
	
}
