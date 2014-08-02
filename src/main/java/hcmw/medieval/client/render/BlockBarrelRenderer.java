package hcmw.medieval.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import hcmw.HCMWMedieval;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class BlockBarrelRenderer implements ISimpleBlockRenderingHandler {

    WavefrontObject model = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation("hcmw:medieval/model/barrel.obj"));

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_TRIANGLES);
        GL11.glTranslatef(x, y, z);
        //Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("hcmw", "medieval/textures/barrel.png"));
        model.tessellateAll(tessellator);
        tessellator.draw();
        GL11.glTranslatef(-x, -y, -z);
        tessellator.startDrawingQuads();
        //Tessellator.instance.setTranslation((double)(-Minecraft.getMinecraft().renderGlobal.loadRenderers().posX), (double)(-this.posY), (double)(-this.posZ));
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return HCMWMedieval.blockBarrelInventory.getRenderType();
    }
}
