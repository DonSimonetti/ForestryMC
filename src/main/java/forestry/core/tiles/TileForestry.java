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
package forestry.core.tiles;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.IOException;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorLogicSource;
import forestry.api.core.ILocatable;
import forestry.core.errors.ErrorLogic;
import forestry.core.inventory.FakeInventoryAdapter;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.network.IStreamable;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.packets.PacketTileStream;
import forestry.core.utils.NBTUtilForestry;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.TickHelper;

//import net.minecraftforge.fml.common.Optional;

//import buildcraft.api.statements.IStatementContainer;
//import buildcraft.api.statements.ITriggerExternal;
//import buildcraft.api.statements.ITriggerInternal;
//import buildcraft.api.statements.ITriggerInternalSided;
//import buildcraft.api.statements.ITriggerProvider;
//
//@Optional.Interface(iface = "buildcraft.api.statements.ITriggerProvider", modid = Constants.BCLIB_MOD_ID)
public abstract class TileForestry extends TileEntity implements IStreamable, IErrorLogicSource, ISidedInventory, IFilterSlotDelegate, ITitled, ILocatable, ITickableTileEntity, INamedContainerProvider {//, ITriggerProvider {
	private final ErrorLogic errorHandler = new ErrorLogic();
	private final AdjacentTileCache tileCache = new AdjacentTileCache(this);

	private IInventoryAdapter inventory = FakeInventoryAdapter.instance();

	private final TickHelper tickHelper = new TickHelper();
	private boolean needsNetworkUpdate = false;

	public TileForestry(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	protected AdjacentTileCache getTileCache() {
		return tileCache;
	}

	public void onNeighborTileChange(World world, BlockPos pos, BlockPos neighbor) {
		tileCache.onNeighborChange();
	}

	@Override
	public void setRemoved() {
		tileCache.purge();
		super.setRemoved();
	}

	@Override
	public void clearRemoved() {
		tileCache.purge();
		super.clearRemoved();
	}

	// / UPDATING
	@Override
	public final void tick() {
		tickHelper.onTick();

		if (!level.isClientSide) {
			updateServerSide();
		} else {
			updateClientSide();
		}

		if (needsNetworkUpdate) {
			needsNetworkUpdate = false;
			sendNetworkUpdate();
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void updateClientSide() {
	}

	protected void updateServerSide() {
	}

	protected final boolean updateOnInterval(int tickInterval) {
		return tickHelper.updateOnInterval(tickInterval);
	}

	// / SAVING & LOADING
	@Override
	public void load(BlockState state, CompoundNBT data) {
		super.load(state, data);
		inventory.read(data);
	}


	@Override
	public CompoundNBT save(CompoundNBT data) {
		data = super.save(data);
		inventory.write(data);
		return data;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, getUpdateTag());
	}


	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		return NBTUtilForestry.writeStreamableToNbt(this, tag);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		NBTUtilForestry.readStreamableFromNbt(this, tag);
	}

	/* INetworkedEntity */
	protected final void sendNetworkUpdate() {
		PacketTileStream packet = new PacketTileStream(this);
		NetworkUtil.sendNetworkPacket(packet, worldPosition, level);
	}

	/* IStreamable */
	@Override
	public void writeData(PacketBufferForestry data) {

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void readData(PacketBufferForestry data) throws IOException {

	}

	public void onRemoval() {
	}

	@Override
	public World getWorldObj() {
		return level;
	}

	/* ITriggerProvider */
	//	@Optional.Method(modid = Constants.BCLIB_MOD_ID)
	//	@Override
	//	public void addInternalTriggers(Collection<ITriggerInternal> triggers, IStatementContainer container) {
	//	}
	//
	//	@Override
	//	public void addInternalSidedTriggers(Collection<ITriggerInternalSided> triggers, IStatementContainer container, @Nonnull Direction side) {
	//	}
	//
	//	@Optional.Method(modid = Constants.BCLIB_MOD_ID)
	//	@Override
	//	public void addExternalTriggers(Collection<ITriggerExternal> triggers, @Nonnull Direction side, TileEntity tile) {
	//	}

	// / REDSTONE INFO
	protected boolean isRedstoneActivated() {
		return level.getBestNeighborSignal(getBlockPos()) > 0;
	}

	protected final void setNeedsNetworkUpdate() {
		needsNetworkUpdate = true;
	}

	@Override
	public final IErrorLogic getErrorLogic() {
		return errorHandler;
	}

	/* NAME */

	/**
	 * Gets the tile's unlocalized name, based on the block at the location of this entity (client-only).
	 */
	@Override
	public String getUnlocalizedTitle() {
		return getBlockState().getBlock().getDescriptionId();
	}

	/* INVENTORY BASICS */
	public IInventoryAdapter getInternalInventory() {
		return inventory;
	}

	protected final void setInternalInventory(IInventoryAdapter inv) {
		Preconditions.checkNotNull(inv);
		this.inventory = inv;
	}

	/* ISidedInventory */

	@Override
	public boolean isEmpty() {
		return getInternalInventory().isEmpty();
	}

	@Override
	public final int getContainerSize() {
		return getInternalInventory().getContainerSize();
	}

	@Override
	public final ItemStack getItem(int slotIndex) {
		return getInternalInventory().getItem(slotIndex);
	}

	@Override
	public ItemStack removeItem(int slotIndex, int amount) {
		return getInternalInventory().removeItem(slotIndex, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slotIndex) {
		return getInternalInventory().removeItemNoUpdate(slotIndex);
	}

	@Override
	public void setItem(int slotIndex, ItemStack itemstack) {
		getInternalInventory().setItem(slotIndex, itemstack);
	}

	@Override
	public final int getMaxStackSize() {
		return getInternalInventory().getMaxStackSize();
	}

	@Override
	public final void startOpen(PlayerEntity player) {
		getInternalInventory().startOpen(player);
	}

	@Override
	public final void stopOpen(PlayerEntity player) {
		getInternalInventory().stopOpen(player);
	}

	//	@Override
	//	public String getName() {
	//		return getUnlocalizedTitle();
	//	}
	//
	//	@Override
	//	public ITextComponent getDisplayName() {
	//		return new TranslationTextComponent(getUnlocalizedTitle());
	//	}

	@Override
	public final boolean stillValid(PlayerEntity player) {
		return getInternalInventory().stillValid(player);
	}

	//	@Override
	//	public boolean hasCustomName() {
	//		return getInternalInventory().hasCustomName();
	//	}

	@Override
	public final boolean canPlaceItem(int slotIndex, ItemStack itemStack) {
		return getInternalInventory().canPlaceItem(slotIndex, itemStack);
	}

	@Override
	public final boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		return getInternalInventory().canSlotAccept(slotIndex, itemStack);
	}

	@Override
	public boolean isLocked(int slotIndex) {
		return getInternalInventory().isLocked(slotIndex);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return getInternalInventory().getSlotsForFace(side);
	}

	@Override
	public final boolean canPlaceItemThroughFace(int slotIndex, ItemStack itemStack, Direction side) {
		return getInternalInventory().canPlaceItemThroughFace(slotIndex, itemStack, side);
	}

	@Override
	public final boolean canTakeItemThroughFace(int slotIndex, ItemStack itemStack, Direction side) {
		return getInternalInventory().canTakeItemThroughFace(slotIndex, itemStack, side);
	}

	@Override
	public final BlockPos getCoordinates() {
		return getBlockPos();
	}

	@Override
	public void clearContent() {
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing != null) {
				return LazyOptional.of(() -> new SidedInvWrapper(getInternalInventory(), facing)).cast();
			} else {
				return LazyOptional.of(() -> new InvWrapper(getInternalInventory())).cast();
			}

		}
		return super.getCapability(capability, facing);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(this.getUnlocalizedTitle());
	}
}
