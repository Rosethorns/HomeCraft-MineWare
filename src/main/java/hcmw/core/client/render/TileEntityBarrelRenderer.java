package hcmw.core.client.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntityBarrelRenderer extends TileEntityObjRenderer {

    public TileEntityBarrelRenderer(ResourceLocation modelLoc, ResourceLocation textureLoc) {
        super(modelLoc, textureLoc);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
        this.checkDisplayList();

        if (tileEntity != null) {
            int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            //If standard directions, render as normal
            if (meta < 8) super.renderTileEntityAt(tileEntity, x, y, z, partialTick);
            else {
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_CULL_FACE);
                //Model is off by -one in z
                switch (ForgeDirection.getOrientation(meta - 8)) {
                    //South
                    case SOUTH: {
                        GL11.glTranslated(x, y + 1D, z + 1D);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        GL11.glRotatef(180F, 0F, 0F, 1F);
                        break;
                    }
                    //West
                    case WEST: {
                        GL11.glTranslated(x, y + 1D, z);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        GL11.glRotatef(90F, 0F, 0F, 1F);
                        break;
                    }
                    //North
                    case NORTH: {
                        GL11.glTranslated(x + 1D, y + 1D, z);
                        GL11.glRotatef(90F, 1F, 0F, 0F);
                        GL11.glRotatef(180F, 0F, 1F, 0F);
                        break;
                    }
                    //East
                    case EAST: {
                        GL11.glTranslated(x + 1D, y + 1D, z + 1D);
                        GL11.glRotatef(90F, -1F, 0F, 0F);
                        GL11.glRotatef(90F, 0F, 0F, 1F);
                        break;
                    }
                    default: {
                        GL11.glTranslated(x, y, z + 1);
                    }
                }
                GL11.glCallList(this.displayListID);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
            }
        }
    }
}
