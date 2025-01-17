/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.items;

import java.util.Locale;

import net.minecraft.item.Item;

import forestry.api.core.IItemSubtype;
import forestry.core.utils.OreDictUtil;

public class ItemFruit extends ItemForestryFood {

	public enum EnumFruit implements IItemSubtype {
		CHERRY(OreDictUtil.CROP_CHERRY),
		WALNUT(OreDictUtil.CROP_WALNUT),
		CHESTNUT(OreDictUtil.CROP_CHESTNUT),
		LEMON(OreDictUtil.CROP_LEMON),
		PLUM(OreDictUtil.CROP_PLUM),
		DATES(OreDictUtil.CROP_DATE),
		PAPAYA(OreDictUtil.CROP_PAPAYA);
		//, COCONUT("cropCoconut");
		public static final EnumFruit[] VALUES = values();

		private final String oreDict;
		private final String name;

		//TODO tags
		EnumFruit(String oreDict) {
			this.oreDict = oreDict;
			this.name = this.toString().toLowerCase(Locale.ENGLISH);
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		//		public String getOreDict() {
		//			return oreDict;
		//		}
	}

	private EnumFruit type;

	public ItemFruit(EnumFruit type) {
		super(1, 0.2f, (new Item.Properties()));
		this.type = type;
	}

	public EnumFruit getType() {
		return type;
	}

	@Override
	public boolean canBeDepleted() {
		return false;
	}

}
