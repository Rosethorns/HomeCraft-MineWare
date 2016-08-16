/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package hcmw.core.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The base, non ticking {@link TileEntity} for decorative blocks that require one
 */
public abstract class TileEntityBase extends TileEntity {

    protected String tileEntityRendererId = "";

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        tileEntityRendererId = tagCompound.getString("tileEntityRendererId");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setString("tileEntityRendererId", tileEntityRendererId);
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    public TileEntityBase setTileEntityRendererId(String tileEntityRendererId) {
        this.tileEntityRendererId = tileEntityRendererId;
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return this;
    }

    @SideOnly(Side.CLIENT)
    public String getTileEntityRendererId() {
        return tileEntityRendererId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    //TODO based on collision box
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
