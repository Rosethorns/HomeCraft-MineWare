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
import hcmw.common.tileentity.TileEntityBounding;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBedBase extends BlockContainer {

    //TODO should we store bed data per tile entity or create a new block for each one
    private float[] boundingBoxMax = new float[]{2F, 3F, 2F};

    public BlockBedBase() {
        super(Material.wood);
        setCreativeTab(HCMW.tabHCMW);
    }

    public float[] getBoundingBoxMax() {
        if (this.boundingBoxMax == null || this.boundingBoxMax.length < 3) {
            this.boundingBoxMax = new float[]{1F, 1F, -1F};
        }
        return this.boundingBoxMax;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        if (!world.isRemote) {
            int facing = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            BlockBedBase item = (BlockBedBase) Block.getBlockFromItem(itemStack.getItem());
            float[] boundingBoxMax = item.getBoundingBoxMax();

            //Set the blocks for the bounding box
            switch (facing) {
                //South
                case 0: {
                    for (int checkX = x; checkX > x - boundingBoxMax[0]; checkX--) {
                        for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                            for (int checkZ = z; checkZ < z + boundingBoxMax[2]; checkZ++) {
                                if (!(checkX == x && checkY == y && checkZ == z)) {
                                    this.setBoundingBlock(world, x, y, z, checkX, checkY, checkZ);
                                }
                            }
                        }
                    }
                    break;
                }
                //West
                case 1: {
                    for (int checkX = x; checkX > x - boundingBoxMax[2]; checkX--) {
                        for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                            for (int checkZ = z; checkZ > z - boundingBoxMax[0]; checkZ--) {
                                if (!(checkX == x && checkY == y && checkZ == z)) {
                                    this.setBoundingBlock(world, x, y, z, checkX, checkY, checkZ);
                                }
                            }
                        }
                    }
                    break;
                }
                //North
                case 2: {
                    for (int checkX = x; checkX < x + boundingBoxMax[0]; checkX++) {
                        for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                            for (int checkZ = z; checkZ > z - boundingBoxMax[2]; checkZ--) {
                                if (!(checkX == x && checkY == y && checkZ == z)) {
                                    this.setBoundingBlock(world, x, y, z, checkX, checkY, checkZ);
                                }
                            }
                        }
                    }
                    break;
                }
                //East
                case 3: {
                    for (int checkX = x; checkX < x + boundingBoxMax[2]; checkX++) {
                        for (int checkY = y; checkY < y + boundingBoxMax[1]; checkY++) {
                            for (int checkZ = z; checkZ < z + boundingBoxMax[0]; checkZ++) {
                                if (!(checkX == x && checkY == y && checkZ == z)) {
                                    this.setBoundingBlock(world, x, y, z, checkX, checkY, checkZ);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Sets the target block as an invisible bounding box that passes any calls onto it's parent. TODO Move to a util
     * @param world The world
     * @param parentX The parent x pos
     * @param parentY The parent y pos
     * @param parentZ The parent z pos
     * @param posX The target x pos
     * @param posY The target y pos
     * @param posZ The target z pos
     */
    private void setBoundingBlock(World world, int parentX, int parentY, int parentZ, int posX, int posY, int posZ) {
        //Set the block and verify
        if (world.setBlock(posX, posY, posZ, HCMW.blockBounding)) {
            TileEntityBounding tileEntityBounding = (TileEntityBounding) world.getTileEntity(posX, posY, posZ);
            tileEntityBounding.setParent(parentX, parentY, parentZ);
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
