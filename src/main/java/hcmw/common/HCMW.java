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
import hcmw.common.tileentity.TileEntityBase;
import hcmw.proxy.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

@Mod(modid = HCMW.MOD_ID, name = "HomeCraft - MineWare")
public class HCMW {

    public static final String MOD_ID = "hcmw";
    public static final CreativeTabs tabHCMW = new CreativeTabs(HCMW.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Items.bed;
        }
    };
    public static final Block blockBed = new BlockBedBase().setBlockName("blockTestBed").setBlockTextureName(HCMW.MOD_ID + ":pbed.png");

    @SidedProxy(serverSide = "hcmw.proxy.ProxyCommon", clientSide = "hcmw.proxy.ProxyClient")
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(blockBed, "blockBedTest");
        GameRegistry.registerTileEntity(TileEntityBase.class, HCMW.MOD_ID + ":base");
    }
}
