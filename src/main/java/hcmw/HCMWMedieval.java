package hcmw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hcmw.core.common.block.BlockBedBase;
import hcmw.core.common.item.ItemBlockMulti;
import hcmw.core.common.tileentity.TileEntityBed;
import hcmw.medieval.common.block.BlockBarrel;
import hcmw.medieval.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

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

    //TODO do we wanna keep anon classes or add in helper methods to the block?
    public static final Block blockPosterBed = new BlockBedBase(){
        @Override
        public TileEntity createNewTileEntity(World world, int meta) {
            TileEntityBed tileEntityBed = new TileEntityBed();
            tileEntityBed.setTileEntityRendererId(HCMWMedieval.MOD_ID + ":posterbed");
            tileEntityBed.setPartsForPass(0, new ArrayList<String>() {{
                add("pbed_backboard");
                add("pbed_base");
                add("pbed_cloth_left");
                add("pbed_cloth_front");
                add("pbed_cloth_right");
                add("pbed_cloth_top");
                add("pbed_mattress");
                add("pbed_pillow");
                add("pbed_back_bl");
                add("pbed_post_br");
                add("pbed_post_fl");
                add("pbed_post_fr");
            }});
            return tileEntityBed;
        }
    }.setBoundingBoxMax(2F, 3F, 2F).setCollisionBoxes(new ArrayList<float[]>() {{
        add(new float[]{0, 0, 0, 0.1875F, 3, 0.1875F}); //Left front post
        add(new float[]{1.8125F, 0, 0, 2, 3, 0.1875F}); //Right front post
        add(new float[]{1.8125F, 0, 1.8125F, 2, 3, 2}); //Right back post
        add(new float[]{0, 0, 1.8125F, 0.1875F, 3, 2}); //Left back post
        add(new float[]{0, 0, 0, 2, 0.8125F, 2}); //Bed
        add(new float[]{0, 0, 1.8125F, 2F, 1.5F, 2F}); //Backboard
    }}).setBlockName("blockPosterBed").setCreativeTab(tabHCMWMedieval);

    public static final Block blockBed = new BlockBedBase() {
        @Override
        public TileEntity createNewTileEntity(World world, int meta) {
            TileEntityBed tileEntityBed = new TileEntityBed();
            tileEntityBed.setTileEntityRendererId(HCMWMedieval.MOD_ID + ":bed");
            //No tint
            tileEntityBed.setPartsForPass(0, new ArrayList<String>() {{
                add("pbed_pillow");
            }});
            //Wood
            tileEntityBed.setPartsForPass(1, new ArrayList<String>() {{
                add("pbed_backboard");
                add("pbed_base");
                add("pbed_post_bl_sm");
                add("pbed_post_br_sm");
                add("pbed_post_fl_sm");
                add("pbed_post_fr_sm");
            }});
            //Cloth
            tileEntityBed.setPartsForPass(2, new ArrayList<String>() {{
                add("pbed_mattress");
            }});
            return tileEntityBed;
        }
    }.setBoundingBoxMax(2F, 2F, 2F).setCollisionBoxes(new ArrayList<float[]>() {{
        add(new float[]{0, 0, 0, 2, 0.8125F, 2}); //Bed
        add(new float[]{0, 0, 1.8125F, 2F, 1.5F, 2F}); //Backboard
    }}).setBlockName("blockBed").setCreativeTab(tabHCMWMedieval);
    public static final Block blockBarrelInventory = new BlockBarrel.BlockBarrelInventory().setBlockName("blockBarrelInventory").setCreativeTab(tabHCMWMedieval);
    //public static final Block blockBarrelFluid = new BlockBarrel.BlockBarrelFluid().setBlockName("blockBarrelFluid").setCreativeTab(tabHCMWMedieval);

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(blockPosterBed, ItemBlockMulti.class, blockPosterBed.getUnlocalizedName());
        GameRegistry.registerBlock(blockBed, ItemBlockMulti.class, blockBed.getUnlocalizedName());
        GameRegistry.registerBlock(blockBarrelInventory, blockBarrelInventory.getUnlocalizedName());

        proxy.registerRenderers();
    }
}
