package hcmw.core.common.tileentity;

import java.util.List;

public interface ICustomizable {

    /**
     * This method provides the parts that should be rendered on this render pass. The first render pass is 0 then increases
     * to maxRenderPass - 1
     * @param renderPass The render pass
     * @return The parts
     */
    public List<String> getPartsForPass(int renderPass);

    /**
     * Sets the parents for the specified render pass
     * @param renderPass The render pass
     * @param parts The parts
     */
    public void setPartsForPass(int renderPass, List<String> parts);

    /**
     * Returns the colour to be applied before rendering
     * @param renderPass The render pass
     * @return The colour
     */
    public float[] getRGBAForPass(int renderPass);
}
