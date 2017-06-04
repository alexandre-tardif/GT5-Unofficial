package gregtech.loaders.oreprocessing;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public class ProcessingOreSmelting implements gregtech.api.interfaces.IOreRecipeRegistrator {
    private final OrePrefixes[] mSmeltingPrefixes = {OrePrefixes.crushed, OrePrefixes.crushedPurified, OrePrefixes.crushedCentrifuged, OrePrefixes.dustImpure, OrePrefixes.dustPure, OrePrefixes.dustRefined};

    public ProcessingOreSmelting() {
        for (OrePrefixes tPrefix : this.mSmeltingPrefixes) tPrefix.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_ModHandler.removeFurnaceSmelting(aStack);
        if (!aMaterial.contains(SubTag.NO_SMELTING)) {
            if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
                if(aMaterial.mBlastFurnaceTemp<1000 && aMaterial.mDirectSmelting.mBlastFurnaceTemp<1000)
                    if(aMaterial.mBlastFurnaceTemp<20000)
                        GT_Values.RA.addBlastRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial, GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), 1L) : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), null, (int) Math.max(aMaterial.getMass() / 4L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial.mBlastFurnaceTemp);
                if (aMaterial.mBlastFurnaceTemp <= 1000 && aMaterial.mDirectSmelting.mBlastFurnaceTemp <= 1000)
                    GT_ModHandler.addRCBlastFurnaceRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), aMaterial.mBlastFurnaceTemp * 2);
            } else {
                switch (aPrefix) {
                    case crushed:
                    case crushedPurified:
                    case crushedCentrifuged:
                        ItemStack tStack = GT_OreDictUnificator.get(OrePrefixes.nugget, aMaterial.mDirectSmelting, aMaterial.mDirectSmelting == aMaterial ? 10L : 3L);
                        if (tStack == null)
                            tStack = GT_OreDictUnificator.get(aMaterial.contains(SubTag.SMELTING_TO_GEM) ? OrePrefixes.gem : OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
                        if ((tStack == null) && (!aMaterial.contains(SubTag.SMELTING_TO_GEM)))
                            tStack = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
                        GT_ModHandler.addSmeltingRecipe(aStack, tStack);
                        break;
                    default:
                        GT_ModHandler.addSmeltingRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L));
                }
            }
        }
    }
}
