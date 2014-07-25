package hcmw.client.render;

import hcmw.common.block.IDirectional;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntityObjRenderer extends TileEntitySpecialRenderer {

    private final ResourceLocation textureLoc;
    public WavefrontObject model;
    protected int displayListID = 0;

    public TileEntityObjRenderer(ResourceLocation modelLoc, ResourceLocation textureLoc) {
        this.textureLoc = textureLoc;
        this.model = (WavefrontObject) AdvancedModelLoader.loadModel(modelLoc);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
        this.checkDisplayList();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        if (tileEntity != null) {
            Block block = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            if (block instanceof IDirectional) {
                switch (ForgeDirection.getOrientation(((IDirectional) block).getDirectionOppositeFacing(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord))) {
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
                    default: {
                        GL11.glTranslated(x, y, z + 1);
                    }
                }
            }
            else {
                GL11.glTranslated(x, y, z + 1);
            }
        }
        GL11.glCallList(this.displayListID);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    protected void checkDisplayList() {
        //ID is 0 if unset or it failed to create to create list
        if (this.displayListID == 0) {
            this.displayListID = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.displayListID, GL11.GL_COMPILE);
            this.bindTexture(this.textureLoc);
            this.model.renderAll();
            GL11.glEndList();
        }
    }
}
