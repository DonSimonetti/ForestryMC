/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.storage;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

import forestry.api.core.IItemProvider;

/**
 * Makes a new crate, registers it with the game registry,
 * and creates crating and uncrating recipes for the Carpenter.
 * The icon is rendered automatically from the contained item.
 * If an oreDictName is specified, it will make recipes using that.
 * <p>
 * Can only be called during the Init stage.
 */
public interface ICrateRegistry {

	void registerCrate(Item item);

	void registerCrate(Block block);

	void registerCrate(ItemStack stack);

	void registerCrate(String oreDictName);

	void registerCrate(ITag<Item> tag);

	void registerCrate(IItemProvider provider);

	/**
	 * prevent a crate from being registered
	 *
	 * @param stack the ItemStack to blacklist
	 */
	default void blacklistCrate(ItemStack stack) {

	}

}
