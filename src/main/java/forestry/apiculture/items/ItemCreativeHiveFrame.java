package forestry.apiculture.items;

import forestry.api.apiculture.DefaultBeeModifier;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.hives.IHiveFrame;
import forestry.api.core.ItemGroups;
import forestry.core.items.ItemForestry;
import genetics.api.individual.IGenome;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCreativeHiveFrame extends ItemHiveFrame
{
    private final NoLifeSpanModifier beeModifier;

    public ItemCreativeHiveFrame() {
        super(0,1f);

        beeModifier = new NoLifeSpanModifier();
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_)
    {
        return true;
    }

    @Override
    public IBeeModifier getBeeModifier(ItemStack frame)
    {
        return beeModifier;
    }

    private static class NoLifeSpanModifier extends DefaultBeeModifier
    {

        @Override
        public float getLifespanModifier(IGenome genome, @Nullable IGenome mate, float currentModifier)
        {
            return 0;
        }

        @OnlyIn(Dist.CLIENT)
        public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
            tooltip.add(new TranslationTextComponent("item.forestry.bee.modifier.lifespan", 0));
        }

    }

}
