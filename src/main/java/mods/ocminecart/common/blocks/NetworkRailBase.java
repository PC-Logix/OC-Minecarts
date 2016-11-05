package mods.ocminecart.common.blocks;

import mods.ocminecart.OCMinecart;
import mods.ocminecart.common.tileentity.NetworkRailBaseTile;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NetworkRailBase extends BlockContainer {
	
	protected NetworkRailBase() {
		super(Material.IRON);
		this.setUnlocalizedName(OCMinecart.MODID+".networkrailbase");
		this.setHardness(2F);
		this.setResistance(5f);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			player.openGui(OCMinecart.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	public TileEntity createNewTileEntity(World world, int meta) {
		return new NetworkRailBaseTile();
	}
}
