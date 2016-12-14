package mods.ocminecart.common.items;

import mods.ocminecart.OCMinecart;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
	
	public static Item item_ComputerCart;
	public static Item item_ComputerCartCase;
	public static Item item_CartRemoteModule;
	public static Item item_CartRemoteAnalyzer;
	public static Item item_LinkingUpgrade;
	
	public static void init(){
		item_ComputerCart=new ItemComputerCart().setCreativeTab(OCMinecart.itemGroup);
		item_ComputerCartCase=new ComputerCartCase().setCreativeTab(OCMinecart.itemGroup);
		item_CartRemoteModule = new ItemCartRemoteModule().setCreativeTab(OCMinecart.itemGroup);
		item_CartRemoteAnalyzer = new ItemRemoteAnalyzer().setCreativeTab(OCMinecart.itemGroup);
		item_LinkingUpgrade = new ItemLinkingUpgrade().setCreativeTab(OCMinecart.itemGroup);
		
		registerItem(item_ComputerCart,"itemcomputercart");
		registerItem(item_ComputerCartCase,"itemcomputercartcase");
		registerItem(item_CartRemoteModule,"itemcartremotemodule");
		registerItem(item_CartRemoteAnalyzer,"itemcartremoteanalyzer");
		registerItem(item_LinkingUpgrade,"linkingupgrade");
	}

	private static void registerItem(Item item, String id){
		item.setRegistryName(OCMinecart.MODID, id);
		GameRegistry.register(item);

		if(FMLCommonHandler.instance().getEffectiveSide().isClient() && item instanceof IItemModelRegister){
			Map<Integer, ModelResourceLocation> models = new HashMap<>();
			((IItemModelRegister) item).modelVariants(models);
			for(Map.Entry<Integer, ModelResourceLocation> entry : models.entrySet()){
				ModelLoader.setCustomModelResourceLocation(item , entry.getKey(), entry.getValue());
			}
		}
	}

	public interface IItemModelRegister{

		void modelVariants(Map<Integer, ModelResourceLocation> models);
	}
}
