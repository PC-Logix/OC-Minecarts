package mods.ocminecart.common;

import li.cil.oc.api.network.EnvironmentHost;
import mods.ocminecart.Settings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class Sound {
	private static Map<EnvironmentHost,Map<String,Float>> globalTimeouts = new HashMap<EnvironmentHost,Map<String, Float>>();
	
	public static boolean play(EnvironmentHost host, String sound){
		if(globalTimeouts.containsKey(host)){
			Map<String,Float> timer = globalTimeouts.get(host);
			Float time = (timer.containsKey(sound)) ? timer.get(sound) : 0F;
			if(time <= System.currentTimeMillis()){
				SoundEvent soundEv = new SoundEvent(new ResourceLocation(Settings.OC_ResLoc+":"+sound));
				host.world().playSound(host.xPosition(), host.yPosition(), host.zPosition(), soundEv, SoundCategory.BLOCKS, Settings.OC_SoundVolume, 1);
				timer.put(sound, System.currentTimeMillis()+500F);
				return true;
			}
			return false;
		}
		else{
			Map<String,Float> timer = new HashMap<String, Float>();
			host.world().playSoundEffect(host.xPosition(), host.yPosition(), host.zPosition(), Settings.OC_ResLoc+":"+sound, Settings.OC_SoundVolume, 1);
			timer.put(sound, System.currentTimeMillis()+500F);
			globalTimeouts.put(host, timer);
			return true;
		}
	}
}
