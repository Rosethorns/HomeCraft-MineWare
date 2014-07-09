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

package hcmw.common.block;

import hcmw.common.HCMW;
import hcmw.common.tileentity.TileEntityBed;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBedBase extends BlockMultiBlock {

    public BlockBedBase() {
        super(Material.wood);
        setCreativeTab(HCMW.tabHCMW);
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        if (!world.isRemote) {
            //Set this as the parent and create the structure
            TileEntityBed tileEntityBed = (TileEntityBed) world.getTileEntity(x, y, z);

            tileEntityBed.setIsParent();
            this.createStructure(world, x, y, z, meta);
        }
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

/*    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity) {
        //TODO adjust per rotation
        if (this.boundingBox != null && this.boundingBox.length == 6) {
            this.setBlockBounds(this.boundingBox[0], this.boundingBox[1], this.boundingBox[2], this.boundingBox[3], this.boundingBox[4], this.boundingBox[5]);
        }
        else {
            this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        }
        super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.boundingBox = new float[]{0F, 0F, 0F, 2F, 3F, -2F};
        if (this.boundingBox != null && this.boundingBox.length == 6) {
            return AxisAlignedBB.getBoundingBox(x + this.boundingBox[0], y + this.boundingBox[1], z + this.boundingBox[2], x + this.boundingBox[3], y + this.boundingBox[4], z + this.boundingBox[5]);
        }
        else return AxisAlignedBB.getBoundingBox(x + 0F, y + 0F, z + 0F, x + 1F, y + 1F, z + 1F);
    }*/

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBed();
    }
}
