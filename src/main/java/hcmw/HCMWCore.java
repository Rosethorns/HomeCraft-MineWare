package hcmw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hcmw.core.common.block.BlockBounding;
import hcmw.core.common.tileentity.*;
import hcmw.core.proxy.ProxyCommon;
import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = HCMWCore.MOD_ID, name = "HomeCraft - MineWare", useMetadata = true)
public class HCMWCore {

    public static final String MOD_ID = "hcmw|core";
    public static final Logger logger = LogManager.getLogger(HCMWCore.MOD_ID);
    public static final Block blockBounding = new BlockBounding().setBlockName("blockBounding");
    @SidedProxy(serverSide = "hcmw.core.proxy.ProxyCommon", clientSide = "hcmw.core.proxy.ProxyClient")
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(blockBounding, "blockBounding");

        GameRegistry.registerTileEntity(TileEntityBase.class, HCMWCore.MOD_ID + ":base");
        GameRegistry.registerTileEntity(TileEntityBed.class, HCMWCore.MOD_ID + ":bed");
        GameRegistry.registerTileEntity(TileEntityBounding.class, HCMWCore.MOD_ID + ":bounding");
        GameRegistry.registerTileEntity(TileEntityFluid.class, HCMWCore.MOD_ID + ":fluid");
        GameRegistry.registerTileEntity(TileEntityInventory.class, HCMWCore.MOD_ID + ":inventory");

        proxy.registerRenderers();
    }
}
