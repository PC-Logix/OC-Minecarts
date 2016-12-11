package mods.ocminecart.interaction.railcraft;

import mods.ocminecart.common.minecart.RailCart;
import mods.railcraft.api.events.CartLockdownEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class RailcraftEventHandler {
	
	HashMap<RailCart,Boolean> carts = new HashMap<RailCart,Boolean>();
	
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new RailcraftEventHandler());
	}
	
	@SubscribeEvent
	public void onCartLockdown(CartLockdownEvent.Lock event){
		if(!(event.cart instanceof RailCart)) return;
		if(!carts.containsKey(event.cart) || !carts.get(event.cart)){
			((RailCart)event.cart).lockdown(true);
			carts.put((RailCart) event.cart, true);
		}
			
	}
	
	@SubscribeEvent
	public void onCartRelease(CartLockdownEvent.Release event){
		if(!(event.cart instanceof RailCart)) return;
		if(!carts.containsKey(event.cart) || carts.get(event.cart)){
			((RailCart)event.cart).lockdown(false);
			carts.put((RailCart) event.cart, false);
		}
	}
}
