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

package hcmw.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import hcmw.client.render.TileEntityBedRenderer;
import hcmw.common.tileentity.TileEntityBed;

/**
 * A client side proxy for stuff that should only be done client side
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void registerRenderers() {
        //TODO this only temp for now, need to find a good solution to allow for modules
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBed.class, new TileEntityBedRenderer());
    }
}
