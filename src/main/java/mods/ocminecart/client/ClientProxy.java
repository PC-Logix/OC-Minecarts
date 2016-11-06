package mods.ocminecart.client;

import mods.ocminecart.client.manual.ManualRegister;
import mods.ocminecart.client.renderer.entity.ComputerCartRenderer;
import mods.ocminecart.common.CommonProxy;
import mods.ocminecart.common.minecart.ComputerCart;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	public void init(){
		super.init();

		RenderingRegistry.registerEntityRenderingHandler(ComputerCart.class, (entity) -> new ComputerCartRenderer(Minecraft.getMinecraft().getRenderManager()));
		//MinecraftForgeClient.registerItemRenderer(ModItems.item_ComputerCart, new ComputerCartItemRenderer());
		
		ManualRegister.registermanual();
	}
}
