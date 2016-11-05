package mods.ocminecart.common.blocks;

import li.cil.oc.api.network.Environment;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INetRail {
	
	/*Called with the position of the Rail (the world object, x, y and z)
	  Returns the Environment the Cart should connect to */
	Environment getResponseEnvironment(World world, BlockPos pos);
	
	/*Called with the position of the Rail (the world object, x, y and z) and the Minecart
	  Return true if the Cart is allowed to connect to the Environment  */
	boolean isValid(World world, BlockPos pos, EntityMinecart cart);
	
}
