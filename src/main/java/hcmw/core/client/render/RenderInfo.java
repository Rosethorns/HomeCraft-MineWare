package hcmw.core.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

import java.util.ArrayList;
import java.util.List;

//TODO add support for different model for each render pass?
//This can be serialised (mostly). Maybe save to the TE to allow for customising resource location per render pass? Or
//move the getResourceLoc to ICustomisable
/*
We could create a display list for each render pass of the model + texture (but not colour) and save the list ID to help
performance. The downside to this though is the lack of being able to disable individual parts, it would have to be a set
configuration for each one then we remember. Could have something like RenderState or something? Heck, we could even cache
colour in a display list and check if it has changed, if so, free the display list and rebuild. Could further improve
performance greatly as glcolor can be expensive at times. I like this idea, building caches and freeing them if changed.
TODO look into this
 */
@SideOnly(Side.CLIENT)
public class RenderInfo {

    private final WavefrontObject model;
    private int renderPasses = 1;
    private List<ResourceLocation> resourceLocations = new ArrayList<ResourceLocation>();
    private List<List<String>> parts = new ArrayList<List<String>>();

    public RenderInfo(WavefrontObject model) {
        this.model = model;
    }

    public RenderInfo(ResourceLocation modelLoc) {
        this.model = (WavefrontObject) AdvancedModelLoader.loadModel(modelLoc);
    }

    public int getRenderPasses() {
        return this.renderPasses;
    }

    public ResourceLocation getResourceLocForPass(int renderPass) {
        return this.resourceLocations.size() > renderPass ? this.resourceLocations.get(renderPass) : null;
    }

    public List<String> getPartsForPass(int renderPass) {
        return this.parts.size() > renderPass ? this.parts.get(renderPass) : new ArrayList<String>();
    }

    public WavefrontObject getModel() {
        return this.model;
    }

    public RenderInfo setResourceLocForPass(int pass, ResourceLocation resourceLocation) {
        this.resourceLocations.add(pass, resourceLocation);
        return this;
    }

    public RenderInfo setResourceLocs(List<ResourceLocation> resourceLocs) {
        this.resourceLocations = resourceLocs;
        return this;
    }

    public RenderInfo setRenderPasses(int renderPasses) {
        this.renderPasses = renderPasses;
        return this;
    }

    /**
     * Sets the parents for the specified render pass
     * @param renderPass The render pass
     * @param parts The parts
     */
    public void setPartsForPass(int renderPass, List<String> parts) {
        this.parts.add(renderPass, parts);
    }
}
