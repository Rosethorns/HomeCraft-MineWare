package hcmw.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hcmw.core.client.render.TileEntityObjRenderer;
import hcmw.core.common.tileentity.TileEntityBase;

import java.util.HashMap;

/**
 * A client side proxy for stuff that should only be done client side
 */
@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyCommon {

    /**
     * A HashMap of all mappings between TE identifiers and their renderers. This allows us to specify the TEOR to be used
     * per TE instead of per class
     */
    public static final HashMap<String, TileEntityObjRenderer> tileEntityRenderers = new HashMap<String, TileEntityObjRenderer>();

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBase.class, new TileEntityObjRenderer(null));
    }
}
