package mods.ocminecart.common.blocks;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.items.ModItems;
import mods.ocminecart.common.tileentity.NetworkRailBaseTile;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
	public static Block block_NetworkRail;
	public static Block block_NetworkRailBase;
	
	public static void init(){
		block_NetworkRail = new NetworkRail().setCreativeTab(OCMinecart.itemGroup);
		block_NetworkRailBase = new NetworkRailBase().setCreativeTab(OCMinecart.itemGroup); 
		
		GameRegistry.registerTileEntity(NetworkRailBaseTile.class, OCMinecart.MODID+"networkrailbasetile");
		
		registerBlock(block_NetworkRail, "networkrail", true);
		registerBlock(block_NetworkRailBase, "networkrailbase", true);
	}

	private static void registerBlock(Block block, String id, boolean withItem){
		block.setRegistryName(OCMinecart.MODID, id);
		GameRegistry.register(block);

		if(!withItem)
			return;

		ItemBlock item = new ItemBlock(block);
		GameRegistry.register(item);

		if(FMLCommonHandler.instance().getEffectiveSide().isClient() && block instanceof IBlockItemModelRegister){
			Map<Integer, ModelResourceLocation> models = new HashMap<>();
			((IBlockItemModelRegister) block).modelVariants(models);
			for(Map.Entry<Integer, ModelResourceLocation> entry : models.entrySet()){
				ModelLoader.setCustomModelResourceLocation(item , entry.getKey(), entry.getValue());
			}
		}
	}

	public interface IBlockItemModelRegister{

		void modelVariants(Map<Integer, ModelResourceLocation> models);
	}
}
