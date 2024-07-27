package com.freezedown.metallurgica.content.machines.shaking_table;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotItemHandler;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ShakingTableBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<ShakingTableBehaviour> TYPE = new BehaviourType<>();
    TransportedItemStack heldItem;
    List<TransportedItemStack> incoming;
    ShakingTableItemHandler itemHandler;
    LazyOptional<ShakingTableItemHandler> lazyItemHandler;
    TransportedItemStackHandlerBehaviour transportedHandler;
    ItemStackHandler processingOutputBuffer;
    
    Supplier<Integer> maxStackSize;
    Supplier<Boolean> canAcceptItems;
    Predicate<Direction> canFunnelsPullFrom;
    Predicate<ItemStack> acceptedItems;
    Consumer<ItemStack> onHeldInserted;
    boolean allowMerge;
    
    public ShakingTableBehaviour(SmartBlockEntity be) {
        super(be);
        maxStackSize = () -> 64;
        canAcceptItems = () -> true;
        canFunnelsPullFrom = $ -> true;
        acceptedItems = $ -> true;
        onHeldInserted = $ -> {
        };
        incoming = new ArrayList<>();
        itemHandler = new ShakingTableItemHandler(this);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        processingOutputBuffer = new ItemStackHandler(8) {
            protected void onContentsChanged(int slot) {
                be.notifyUpdate();
            };
        };
    }
    
    public void enableMerging() {
        allowMerge = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        Level world = blockEntity.getLevel();
        
        for (Iterator<TransportedItemStack> iterator = incoming.iterator(); iterator.hasNext();) {
            TransportedItemStack ts = iterator.next();
            if (!tick(ts))
                continue;
            if (world.isClientSide && !blockEntity.isVirtual())
                continue;
            if (heldItem == null) {
                heldItem = ts;
            } else {
                if (!ItemHelper.canItemStackAmountsStack(heldItem.stack, ts.stack)) {
                    Vec3 vec = VecHelper.getCenterOf(blockEntity.getBlockPos());
                    Containers.dropItemStack(blockEntity.getLevel(), vec.x, vec.y + .5f, vec.z, ts.stack);
                } else {
                    heldItem.stack.grow(ts.stack.getCount());
                }
            }
            iterator.remove();
            blockEntity.notifyUpdate();
        }
        
        if (heldItem == null)
            return;
        if (!tick(heldItem))
            return;
        
        BlockPos pos = blockEntity.getBlockPos();
        
        if (world.isClientSide)
            return;
        if (handleBeltFunnelOutput())
            return;
        
        
    }
    
    protected boolean tick(TransportedItemStack heldItem) {
        heldItem.prevBeltPosition = heldItem.beltPosition;
        heldItem.prevSideOffset = heldItem.sideOffset;
        float diff = .5f - heldItem.beltPosition;
        if (diff > 1 / 512f) {
            if (diff > 1 / 32f && !BeltHelper.isItemUpright(heldItem.stack))
                heldItem.angle += 1;
            heldItem.beltPosition += diff / 4f;
        }
        return diff < 1 / 16f;
    }
    
    private boolean handleBeltFunnelOutput() {
        BlockState funnel = getWorld().getBlockState(getPos().above());
        Direction funnelFacing = AbstractFunnelBlock.getFunnelFacing(funnel);
        if (funnelFacing == null || !canFunnelsPullFrom.test(funnelFacing.getOpposite()))
            return false;
        
        for (int slot = 0; slot < processingOutputBuffer.getSlots(); slot++) {
            ItemStack previousItem = processingOutputBuffer.getStackInSlot(slot);
            if (previousItem.isEmpty())
                continue;
            ItemStack afterInsert = blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE)
                    .tryExportingToBeltFunnel(previousItem, null, false);
            if (afterInsert == null)
                return false;
            if (previousItem.getCount() != afterInsert.getCount()) {
                processingOutputBuffer.setStackInSlot(slot, afterInsert);
                blockEntity.notifyUpdate();
                return true;
            }
        }
        
        ItemStack previousItem = heldItem.stack;
        ItemStack afterInsert = blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE)
                .tryExportingToBeltFunnel(previousItem, null, false);
        if (afterInsert == null)
            return false;
        if (previousItem.getCount() != afterInsert.getCount()) {
            if (afterInsert.isEmpty())
                heldItem = null;
            else
                heldItem.stack = afterInsert;
            blockEntity.notifyUpdate();
            return true;
        }
        
        return false;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        Level level = getWorld();
        BlockPos pos = getPos();
        ItemHelper.dropContents(level, pos, processingOutputBuffer);
        for (TransportedItemStack transportedItemStack : incoming)
            Block.popResource(level, pos, transportedItemStack.stack);
        if (!getHeldItemStack().isEmpty())
            Block.popResource(level, pos, getHeldItemStack());
    }
    
    @Override
    public void unload() {
        if (lazyItemHandler != null)
            lazyItemHandler.invalidate();
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (heldItem != null)
            compound.put("HeldItem", heldItem.serializeNBT());
        compound.put("OutputBuffer", processingOutputBuffer.serializeNBT());
        if (canMergeItems() && !incoming.isEmpty())
            compound.put("Incoming", NBTHelper.writeCompoundList(incoming, TransportedItemStack::serializeNBT));
    }
    
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        heldItem = null;
        if (compound.contains("HeldItem"))
            heldItem = TransportedItemStack.read(compound.getCompound("HeldItem"));
        processingOutputBuffer.deserializeNBT(compound.getCompound("OutputBuffer"));
        if (canMergeItems()) {
            ListTag list = compound.getList("Incoming", Tag.TAG_COMPOUND);
            incoming = NBTHelper.readCompoundList(list, TransportedItemStack::read);
        }
    }
    
    public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(blockEntity).allowingBeltFunnels()
                .setInsertionHandler(this::tryInsertingFromSide).considerOccupiedWhen(this::isOccupied));
        transportedHandler = new TransportedItemStackHandlerBehaviour(blockEntity, this::applyToAllItems)
                .withStackPlacement(this::getWorldPositionOf);
        behaviours.add(transportedHandler);
    }
    
    public ItemStack getHeldItemStack() {
        return heldItem == null ? ItemStack.EMPTY : heldItem.stack;
    }
    
    public boolean canMergeItems() {
        return allowMerge;
    }
    
    public int getPresentStackSize() {
        int cumulativeStackSize = 0;
        cumulativeStackSize += getHeldItemStack().getCount();
        for (int slot = 0; slot < processingOutputBuffer.getSlots(); slot++)
            cumulativeStackSize += processingOutputBuffer.getStackInSlot(slot)
                    .getCount();
        return cumulativeStackSize;
    }
    
    public int getRemainingSpace() {
        int cumulativeStackSize = getPresentStackSize();
        for (TransportedItemStack transportedItemStack : incoming)
            cumulativeStackSize += transportedItemStack.stack.getCount();
        int fromGetter = maxStackSize.get();
        return (fromGetter == 0 ? 64 : fromGetter) - cumulativeStackSize;
    }
    
    public ItemStack insert(TransportedItemStack heldItem, boolean simulate) {
        if (!canAcceptItems.get())
            return heldItem.stack;
        if (!acceptedItems.test(heldItem.stack))
            return heldItem.stack;
        
        if (canMergeItems()) {
            int remainingSpace = getRemainingSpace();
            ItemStack inserted = heldItem.stack;
            if (remainingSpace <= 0)
                return inserted;
            if (this.heldItem != null && !ItemHelper.canItemStackAmountsStack(this.heldItem.stack, inserted))
                return inserted;
            
            ItemStack returned = ItemStack.EMPTY;
            if (remainingSpace < inserted.getCount()) {
                returned = ItemHandlerHelper.copyStackWithSize(heldItem.stack, inserted.getCount() - remainingSpace);
                if (!simulate) {
                    TransportedItemStack copy = heldItem.copy();
                    copy.stack.setCount(remainingSpace);
                    if (this.heldItem != null)
                        incoming.add(copy);
                    else
                        this.heldItem = copy;
                }
            } else {
                if (!simulate) {
                    if (this.heldItem != null)
                        incoming.add(heldItem);
                    else
                        this.heldItem = heldItem;
                }
            }
            return returned;
        }
        
        if (!simulate) {
            if (this.isEmpty()) {
                if (heldItem.insertedFrom.getAxis()
                        .isHorizontal())
                    AllSoundEvents.DEPOT_SLIDE.playOnServer(getWorld(), getPos());
                else
                    AllSoundEvents.DEPOT_PLOP.playOnServer(getWorld(), getPos());
            }
            this.heldItem = heldItem;
            onHeldInserted.accept(heldItem.stack);
        }
        return ItemStack.EMPTY;
    }
    
    public void setHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
    }
    
    public void removeHeldItem() {
        this.heldItem = null;
    }
    
    public void setCenteredHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
        this.heldItem.beltPosition = 0.5f;
        this.heldItem.prevBeltPosition = 0.5f;
    }
    
    public <T> LazyOptional<T> getItemCapability(Capability<T> cap, Direction side) {
        return lazyItemHandler.cast();
    }
    
    private boolean isOccupied(Direction side) {
        if (!getHeldItemStack().isEmpty() && !canMergeItems())
            return true;
        if (!isOutputEmpty() && !canMergeItems())
            return true;
        if (!canAcceptItems.get())
            return true;
        return false;
    }
    
    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ItemStack inserted = transportedStack.stack;
        
        if (isOccupied(side))
            return inserted;
        
        int size = transportedStack.stack.getCount();
        transportedStack = transportedStack.copy();
        transportedStack.beltPosition = side.getAxis()
                .isVertical() ? .5f : 0;
        transportedStack.insertedFrom = side;
        transportedStack.prevSideOffset = transportedStack.sideOffset;
        transportedStack.prevBeltPosition = transportedStack.beltPosition;
        ItemStack remainder = insert(transportedStack, simulate);
        if (remainder.getCount() != size)
            blockEntity.notifyUpdate();
        
        return remainder;
    }
    
    private void applyToAllItems(float maxDistanceFromCentre,
                                 Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        if (heldItem == null)
            return;
        if (.5f - heldItem.beltPosition > maxDistanceFromCentre)
            return;
        
        boolean dirty = false;
        TransportedItemStack transportedItemStack = heldItem;
        ItemStack stackBefore = transportedItemStack.stack.copy();
        TransportedItemStackHandlerBehaviour.TransportedResult result = processFunction.apply(transportedItemStack);
        if (result == null || result.didntChangeFrom(stackBefore))
            return;
        
        dirty = true;
        heldItem = null;
        if (result.hasHeldOutput())
            setCenteredHeldItem(result.getHeldOutput());
        
        for (TransportedItemStack added : result.getOutputs()) {
            if (getHeldItemStack().isEmpty()) {
                setCenteredHeldItem(added);
                continue;
            }
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(processingOutputBuffer, added.stack, false);
            Vec3 vec = VecHelper.getCenterOf(blockEntity.getBlockPos());
            Containers.dropItemStack(blockEntity.getLevel(), vec.x, vec.y + .5f, vec.z, remainder);
        }
        
        if (dirty)
            blockEntity.notifyUpdate();
    }
    
    public boolean isEmpty() {
        return heldItem == null && isOutputEmpty();
    }
    
    public boolean isOutputEmpty() {
        for (int i = 0; i < processingOutputBuffer.getSlots(); i++)
            if (!processingOutputBuffer.getStackInSlot(i)
                    .isEmpty())
                return false;
        return true;
    }
    
    private Vec3 getWorldPositionOf(TransportedItemStack transported) {
        return VecHelper.getCenterOf(blockEntity.getBlockPos());
    }
    
    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
    
    public boolean isItemValid(ItemStack stack) {
        return acceptedItems.test(stack);
    }
}
