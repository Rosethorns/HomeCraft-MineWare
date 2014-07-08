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
import org.lwjgl.opengl.GL11;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer {

    public WavefrontObject bedModel;
    private int displayListID;

    public TileEntityBedRenderer() {
        this.bedModel = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation("rorax:PBed.obj"));

        //Should this be generated here?
        //Uses more memory but reduces CPU usage
        this.displayListID = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayListID, GL11.GL_COMPILE);
        this.bedModel.renderAll();
        GL11.glEndList();
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float p_147500_8_) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslated(x, y, z);
        this.bindTexture(new ResourceLocation("rorax:pbed.png"));
        GL11.glCallList(this.displayListID);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
