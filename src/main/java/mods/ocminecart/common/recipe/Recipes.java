package mods.ocminecart.common.recipe;

import li.cil.oc.api.API;
import li.cil.oc.api.detail.ItemAPI;
import li.cil.oc.common.block.Redstone;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.blocks.ModBlocks;
import mods.ocminecart.common.items.ItemComputerCart;
import mods.ocminecart.common.items.ModItems;
import mods.railcraft.common.items.ItemCrowbar;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {
	
	public static void init(){
		ItemAPI items  = API.items;
		
		RecipeSorter.register(OCMinecart.MODID + ":romcrafting", RomCrafting.class , Category.SHAPELESS, "after:minecraft:shaped");
		RecipeSorter.register(OCMinecart.MODID + ":emblemcrafting", EmblemCrafting.class , Category.SHAPELESS, "after:minecraft:shaped");
		
		GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.block_NetworkRailBase),1),
				"CSC",
				"MBM",
				"IKI", 'S',items.get("lanCard").createItemStack(1), 'C',items.get("chamelium").createItemStack(1), 'I', new ItemStack(Items.IRON_INGOT),
					   'M',items.get("chip2").createItemStack(1), 'K',Item.getItemFromBlock(items.get("cable").block()), 'B',items.get("printedCircuitBoard").createItemStack(1));


		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.item_CartRemoteModule,1,0),
				"IC0",
				"IBR",
				"IGW", 'I',"nuggetIron", 'C',items.get("chip1").createItemStack(1), 'B', items.get("printedCircuitBoard").createItemStack(1), 'R', Items.REDSTONE,
					   'G',items.get("generatorUpgrade").createItemStack(1),'W',items.get("wlanCard").createItemStack(1)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.item_CartRemoteModule,1,1),
				"IC0",
				"IBR",
				"IGW", 'I',"nuggetIron", 'C',items.get("chip2").createItemStack(1), 'B', items.get("printedCircuitBoard").createItemStack(1), 'R', Items.REDSTONE,
					   'G',items.get("generatorUpgrade").createItemStack(1),'W',items.get("wlanCard").createItemStack(1)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.item_CartRemoteModule,1,2),
				"ICS",
				"IBR",
				"IGW", 'I',"nuggetIron", 'C',items.get("chip3").createItemStack(1), 'B', items.get("printedCircuitBoard").createItemStack(1), 'R', Items.REDSTONE,
					   'G',items.get("generatorUpgrade").createItemStack(1),'W',items.get("wlanCard").createItemStack(1) , 'S', items.get("solarGeneratorUpgrade").createItemStack(1)));
		
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_CartRemoteAnalyzer,1,0),
				"W",
				"C",
				"A",   'A',items.get("analyzer").createItemStack(1), 'C', items.get("chip2").createItemStack(1), 'W',items.get("wlanCard").createItemStack(1));
		

			GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_LinkingUpgrade,1,0),
					"XCX",
					"MAM",
					"XPX",'C',ItemCrowbar.getItem,'M',items.get("chip1").createItemStack(1),'A',Item.getItemFromBlock(Blocks.STICKY_PISTON),'P', items.get("printedCircuitBoard").createItemStack(1));
			
			GameRegistry.addRecipe(new EmblemCrafting());

		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.block_NetworkRail ), Item.getItemFromBlock(items.get("cable").block()), new ItemStack(Items.REDSTONE), Item.getItemFromBlock(Blocks.DETECTOR_RAIL));
	
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_ComputerCartCase,1,0),"X","Y","Z",'X',Item.getItemFromBlock(items.get("case1").block()), 'Y', Items.MINECART, 'Z', Item.getItemFromBlock(items.get("cable").block()));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_ComputerCartCase,1,1),"X","Y","Z",'X',Item.getItemFromBlock(items.get("case2").block()), 'Y', Items.MINECART, 'Z', Item.getItemFromBlock(items.get("cable").block()));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_ComputerCartCase,1,2),"X","Y","Z",'X',Item.getItemFromBlock(items.get("case3").block()), 'Y', Items.MINECART, 'Z', Item.getItemFromBlock(items.get("cable").block()));
		
		
		GameRegistry.addRecipe(new RomCrafting(new ItemStack(ModItems.item_ComputerCart) , ItemComputerCart.SLOT_ROM));

	}
}
