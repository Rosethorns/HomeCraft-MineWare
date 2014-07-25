package hcmw.medieval.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import hcmw.core.client.render.TileEntityBarrelRenderer;
import hcmw.core.client.render.TileEntityObjRenderer;
import hcmw.core.common.tileentity.TileEntityBarrel;
import hcmw.core.common.tileentity.TileEntityBed;
import net.minecraft.util.ResourceLocation;

/**
 * A client side proxy for stuff that should only be done client side
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerRenderers() {
        //TODO this only temp for now, need to find a good solution to allow for modules
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBed.class, new TileEntityObjRenderer(new ResourceLocation("hcmw:medieval/model/pbed.obj"), new ResourceLocation("hcmw:medieval/textures/pbed.png")));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer(new ResourceLocation("hcmw:medieval/model/barrel.obj"), new ResourceLocation("hcmw:medieval/textures/barrel.png")));
    }
}
