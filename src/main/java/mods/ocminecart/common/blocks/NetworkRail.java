package mods.ocminecart.common.blocks;

import li.cil.oc.api.network.Environment;
import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.tileentity.NetworkRailBaseTile;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkRail extends BlockRailBase implements INetRail{

	protected NetworkRail() {
		super(false);
		this.setUnlocalizedName(OCMinecart.MODID+".networkrail");
		this.setHardness(0.7F);
	}

	@Override
	public Environment getResponseEnvironment(World world, BlockPos pos) {
		if(world.getTileEntity(pos.add(0,1,0)) instanceof NetworkRailBaseTile) return ((NetworkRailBaseTile) world.getTileEntity(pos.add(0,1,0))).getRailPlug();
		return null;
	}

	@Override
	public boolean isValid(World world, BlockPos pos, EntityMinecart cart) {
		return (cart instanceof Environment) && cart.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 0.5 && cart.worldObj == world;
	}

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {
		return null; //TODO: Find out how this works
	}
}
