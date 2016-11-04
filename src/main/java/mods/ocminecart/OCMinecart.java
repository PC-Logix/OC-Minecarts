package mods.ocminecart;

import mods.ocminecart.common.CommonProxy;
import mods.ocminecart.common.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = OCMinecart.MODID, name=OCMinecart.NAME, version=OCMinecart.VERSION, dependencies = "required-after:OpenComputers@[1.6.0.6-beta.4,);"
		+ "after:Waila;"
		+ "after:Railcraft@[9.7.0.0,)")
public class OCMinecart {
	public static final String MODID = "ocminecart";
	public static final String VERSION = "1.7";
	public static final String NAME = "OC-Minecarts";
	
	@Mod.Instance(OCMinecart.MODID)
	public static OCMinecart instance;
	
	public static Logger logger = LogManager.getLogger(OCMinecart.NAME);
	
	@SidedProxy(serverSide="mods.ocminecart.common.CommonProxy", clientSide="mods.ocminecart.client.ClientProxy")
	public static CommonProxy proxy;
	
	public static Configuration config;
	
	public static CreativeTabs itemGroup = new CreativeTabs(OCMinecart.MODID+".modtab"){
		@Override
		public Item getTabIconItem() {
			return ModItems.item_ComputerCartCase;
		}
	};
	
	//public static CreativeTabs itemGroup = li.cil.oc.api.CreativeTab.instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		config = new Configuration(event.getSuggestedConfigurationFile());
		Settings.init();
		
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void Init(FMLInitializationEvent event){
		logModApis();
		
		proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit();
	}
	
	private void logModApis(){
		//TODO: Add JEI
		// if(Loader.isModLoaded("NotEnoughItems")) OCMinecart.logger.info("Found Mod: NEI");
		if(Loader.isModLoaded("Waila")) OCMinecart.logger.info("Found Mod: WAILA");
		if(Loader.isModLoaded("Railcraft")) OCMinecart.logger.info("Found Mod: Railcraft");
	}
}
