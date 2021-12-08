package forestry.mycology.features;

import forestry.modules.features.FeatureItem;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import forestry.mycology.ModuleMycology;
import forestry.mycology.items.ItemFungusGE;

public class MycologyItems
{
    //Fungi
    public static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleMycology.class);

    public static final FeatureItem<ItemFungusGE> FUNGI_SPORES = REGISTRY.item(ItemFungusGE::new,"fungus_spore_ge");
}
