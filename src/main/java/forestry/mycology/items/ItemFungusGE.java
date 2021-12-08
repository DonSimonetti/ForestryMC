package forestry.mycology.items;

import forestry.api.core.ItemGroups;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.core.genetics.ItemGE;
import forestry.core.items.definitions.IColoredItem;
import genetics.api.organism.IOrganismType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemFungusGE extends ItemGE implements IColoredItem
{

    public ItemFungusGE()
    {
        super(new Properties().tab(ItemGroups.tabMycology));
    }

    @Override
    protected IAlleleForestrySpecies getSpecies(ItemStack itemStack) {
        return null;
    }

    @Override
    protected IOrganismType getType() {
        return null;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int tintIndex) {
        return 0;
    }
}
