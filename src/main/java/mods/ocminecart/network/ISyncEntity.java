package mods.ocminecart.network;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncEntity {
	
	void writeSyncData(NBTTagCompound nbt);
	
	void readSyncData(NBTTagCompound nbt);
	
}
