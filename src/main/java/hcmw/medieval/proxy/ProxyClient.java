package hcmw.medieval.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.HCMWMedieval;
import hcmw.core.client.render.TileEntityObjRenderer;
import hcmw.medieval.client.render.TileEntityBarrelRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * A client side proxy for stuff that should only be done client side
 */
@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerRenderers() {
        hcmw.core.proxy.ProxyClient.tileEntityRenderers.put(HCMWMedieval.MOD_ID + ":posterbed", new TileEntityObjRenderer(new ResourceLocation("hcmw:medieval/model/pbed.obj"), new ResourceLocation("hcmw:medieval/textures/pbed.png")));
        hcmw.core.proxy.ProxyClient.tileEntityRenderers.put(HCMWMedieval.MOD_ID + ":barrelInventory", new TileEntityBarrelRenderer(new ResourceLocation("hcmw:medieval/model/barrel.obj"), new ResourceLocation("hcmw:medieval/textures/barrel.png")));
    }
}
