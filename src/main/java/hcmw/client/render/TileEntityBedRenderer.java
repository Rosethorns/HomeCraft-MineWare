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

package hcmw.client.render;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer {

    public WavefrontObject bedModel;
    private int displayListID = 0;

    public TileEntityBedRenderer() {
        this.bedModel = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation("rorax:PBed.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float p_147500_8_) {
        //ID is 0 if unset or it failed to create to create list
        if (this.displayListID == 0) {
            this.displayListID = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.displayListID, GL11.GL_COMPILE);
            this.bindTexture(new ResourceLocation("rorax:pbed.png"));
            this.bedModel.renderAll();
            GL11.glEndList();
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        if (tileEntity != null) {
            switch (ForgeDirection.getOrientation(tileEntity.getBlockMetadata())) {
                //South
                case SOUTH: {
                    GL11.glTranslated(x + 1D, y, z);
                    GL11.glRotatef(180F, 0F, 1F, 0F);
                    break;
                }
                //West
                case WEST: {
                    GL11.glTranslated(x + 1D, y, z + 1D);
                    GL11.glRotatef(90F, 0F, 1F, 0F);
                    break;
                }
                //North
                case NORTH: {
                    GL11.glTranslated(x, y, z + 1D);
                    break;
                }
                //East
                case EAST: {
                    GL11.glTranslated(x, y, z);
                    GL11.glRotatef(90F, 0F, -1F, 0F);
                    break;
                }
            }
        }
        GL11.glCallList(this.displayListID);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
