package hcmw.medieval.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.HCMWMedieval;
import hcmw.core.client.render.RenderInfo;
import hcmw.core.client.render.TileEntityObjRenderer;
import hcmw.medieval.client.render.BlockBarrelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * A client side proxy for stuff that should only be done client side
 */
@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerRenderers() {
        hcmw.core.proxy.ProxyClient.tileEntityRenderers.put(HCMWMedieval.MOD_ID + ":posterbed", new TileEntityObjRenderer(new RenderInfo(new ResourceLocation("hcmw:medieval/model/pbed.obj")).setResourceLocForPass(0, new ResourceLocation("hcmw", "medieval/textures/pbed.png"))));
        hcmw.core.proxy.ProxyClient.tileEntityRenderers.put(HCMWMedieval.MOD_ID + ":bed", new TileEntityObjRenderer(new RenderInfo(new ResourceLocation("hcmw:medieval/model/pbed.obj"))
                .setRenderPasses(3).setResourceLocs(new ArrayList<ResourceLocation>() {{
                    add(0, new ResourceLocation("hcmw", "medieval/textures/pbednotint.png"));
                    add(1, new ResourceLocation("hcmw", "medieval/textures/pbedwood.png"));
                    add(2, new ResourceLocation("hcmw", "medieval/textures/pbedcloth.png"));
                }})));
        //hcmw.core.proxy.ProxyClient.tileEntityRenderers.put(HCMWMedieval.MOD_ID + ":barrelInventory", new TileEntityBarrelRenderer(new RenderInfo(new ResourceLocation("hcmw:medieval/model/barrel.obj")).setResourceLocForPass(0, new ResourceLocation("hcmw", "medieval/textures/barrel.png"))));
        RenderingRegistry.registerBlockHandler(new BlockBarrelRenderer());
    }
}
