/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package hcmw.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hcmw.common.block.BlockBedBase;
import hcmw.common.block.BlockBounding;
import hcmw.common.item.ItemBedBase;
import hcmw.common.tileentity.TileEntityBase;
import hcmw.common.tileentity.TileEntityBounding;
import hcmw.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = HCMW.MOD_ID, name = "HomeCraft - MineWare")
public class HCMW {

    public static final String MOD_ID = "hcmw";
    public static final Logger logger = LogManager.getLogger(HCMW.MOD_ID);
    public static final CreativeTabs tabHCMW = new CreativeTabs(HCMW.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Items.bed;
        }
    };
    public static final Block blockBed = new BlockBedBase().setBlockName("blockTestBed");
    public static final Block blockBounding = new BlockBounding().setBlockName("boundingBlock");

    @SidedProxy(serverSide = "hcmw.proxy.ProxyCommon", clientSide = "hcmw.proxy.ProxyClient")
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(blockBed, ItemBedBase.class, "blockBedTest");
        GameRegistry.registerBlock(blockBounding, "blockBounding");

        GameRegistry.registerTileEntity(TileEntityBase.class, HCMW.MOD_ID + ":base");
        GameRegistry.registerTileEntity(TileEntityBounding.class, HCMW.MOD_ID + ":bounding");

        proxy.registerRenderers();
    }
}
