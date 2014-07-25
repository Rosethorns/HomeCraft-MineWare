package hcmw.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import hcmw.client.render.TileEntityBarrelRenderer;
import hcmw.client.render.TileEntityObjRenderer;
import hcmw.common.tileentity.TileEntityBarrel;
import hcmw.common.tileentity.TileEntityBed;
import net.minecraft.util.ResourceLocation;

/**
 * A client side proxy for stuff that should only be done client side
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerRenderers() {
        //TODO this only temp for now, need to find a good solution to allow for modules
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBed.class, new TileEntityObjRenderer(new ResourceLocation("rorax:pbed.obj"), new ResourceLocation("rorax:pbed.png")));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer(new ResourceLocation("rorax:barrel.obj"), new ResourceLocation("rorax:barrel.png")));
    }
}
