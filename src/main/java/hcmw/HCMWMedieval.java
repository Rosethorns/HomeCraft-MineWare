package hcmw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hcmw.core.common.block.BlockBedBase;
import hcmw.core.common.item.ItemBlockMulti;
import hcmw.medieval.common.block.BlockBarrel;
import hcmw.medieval.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = HCMWMedieval.MOD_ID, name = "HomeCraft - MineWare | Medieval", dependencies = "required-after:hcmw|core", useMetadata = true)
public class HCMWMedieval {

    public static final String MOD_ID = "hcmw|medieval";
    public static final Logger logger = LogManager.getLogger(HCMWMedieval.MOD_ID);
    public static final CreativeTabs tabHCMWMedieval = new CreativeTabs(HCMWMedieval.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Items.bed;
        }
    };
    @SidedProxy(serverSide = "hcmw.medieval.proxy.ProxyCommon", clientSide = "hcmw.medieval.proxy.ProxyClient")
    public static ProxyCommon proxy;

    public static final Block blockBed = new BlockBedBase().setBlockName("blockPosterBed").setCreativeTab(tabHCMWMedieval);
    public static final Block blockBarrelInventory = new BlockBarrel.BlockBarrelInventory().setBlockName("blockBarrelInventory").setCreativeTab(tabHCMWMedieval);
    //public static final Block blockBarrelFluid = new BlockBarrel.BlockBarrelFluid().setBlockName("blockBarrelFluid").setCreativeTab(tabHCMWMedieval);

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(blockBed, ItemBlockMulti.class, blockBed.getUnlocalizedName());
        GameRegistry.registerBlock(blockBarrelInventory, blockBarrelInventory.getUnlocalizedName());

        proxy.registerRenderers();
    }
}
