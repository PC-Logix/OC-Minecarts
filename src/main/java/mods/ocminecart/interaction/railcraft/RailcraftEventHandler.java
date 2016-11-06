package mods.ocminecart.interaction.railcraft;

import mods.ocminecart.common.minecart.AdvCart;
import mods.railcraft.api.events.CartLockdownEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class RailcraftEventHandler {
	
	HashMap<AdvCart,Boolean> carts = new HashMap<AdvCart,Boolean>();
	
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new RailcraftEventHandler());
	}
	
	@SubscribeEvent
	public void onCartLockdown(CartLockdownEvent.Lock event){
		if(!(event.cart instanceof AdvCart)) return;
		if(!carts.containsKey(event.cart) || !carts.get(event.cart)){
			((AdvCart)event.cart).lockdown(true);
			carts.put((AdvCart) event.cart, true);
		}
			
	}
	
	@SubscribeEvent
	public void onCartRelease(CartLockdownEvent.Release event){
		if(!(event.cart instanceof AdvCart)) return;
		if(!carts.containsKey(event.cart) || carts.get(event.cart)){
			((AdvCart)event.cart).lockdown(false);
			carts.put((AdvCart) event.cart, false);
		}
	}
}
