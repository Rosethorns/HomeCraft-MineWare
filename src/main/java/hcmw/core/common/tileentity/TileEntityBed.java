package hcmw.core.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityBed extends TileEntityMultiBlock implements ICustomizable {

    //TODO save these?
    private List<List<String>> currentParts = new ArrayList<List<String>>();
    //TODO remove default implementation
    private List<float[]> colours = Arrays.asList(new float[] {1F, 1F, 1F, 1F}, new float[] {0.5F, 0.25F, 0F, 1F}, new float[] {0.4F, 0F, 0.85F, 1F});

    @Override
    public List<String> getPartsForPass(int renderPass) {
        return this.currentParts.get(renderPass);
    }

    @Override
    public void setPartsForPass(int renderPass, List<String> parts) {
        this.currentParts.add(renderPass, parts);
    }

    @Override
    public float[] getRGBAForPass(int renderPass) {
        return this.colours.get(renderPass);
    }
}
