package com.freezedown.metallurgica.infastructure.conductor;

import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.cable_blocks.copycat_cable_block.CopycatCableBlockEntity;
import com.freezedown.metallurgica.experimental.cable_connector.TestCableConnectorBlock;
import com.freezedown.metallurgica.experimental.cable_connector.TestCableConnectorBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CableItem extends Item {
    public ConductorEntry<?> material;

    public CableItem(Properties properties, ConductorEntry<?> material) {
        super(properties);
        this.material = material;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (level.getBlockEntity(pos) instanceof ElectricBlockEntity) {
            return InteractionResult.PASS;
        } else if (level.getBlockEntity(pos) instanceof CopycatCableBlockEntity) {
            return InteractionResult.PASS;
        } else if (level.getBlockEntity(pos) instanceof KineticBlockEntity) {
            return InteractionResult.PASS;
        } else if (!(state.getBlock() instanceof IHaveCables)) {
            return super.useOn(context);
        } else if (stack.getOrCreateTag().getInt("X1") != 0 && stack.getOrCreateTag().getInt("Y1") != 0 && stack.getOrCreateTag().getInt("Z1") != 0) {
            boolean placeWire = true;
            if (level.getBlockEntity(pos) != null) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof TestCableConnectorBlockEntity) {
                    TestCableConnectorBlockEntity be = (TestCableConnectorBlockEntity) blockEntity;
                    if (!(level.getBlockEntity(pos) instanceof ElectricBlockEntity) && this.testConnection(stack, be, pos)) {
                        BlockPos pos2 = new BlockPos(stack.getOrCreateTag().getInt("X1"), stack.getOrCreateTag().getInt("Y1"), stack.getOrCreateTag().getInt("Z1"));
                        BlockEntity var11 = level.getBlockEntity(pos2);
                        if (var11 instanceof TestCableConnectorBlockEntity) {
                            TestCableConnectorBlockEntity be2 = (TestCableConnectorBlockEntity)var11;
                            if (!(level.getBlockEntity(pos2) instanceof ElectricBlockEntity)) {
                                if (be2.getBlockPos() == be.getBlockPos()) {
                                    return InteractionResult.PASS;
                                }

                                if ((Boolean)be.getBlockState().getValue(TestCableConnectorBlock.EXTENSION) || (Boolean)be2.getBlockState().getValue(TestCableConnectorBlock.EXTENSION)) {
                                    return InteractionResult.PASS;
                                }

                                ItemStack toClone = stack.copyWithCount(1);
                                toClone.getOrCreateTag().remove("X1");
                                toClone.getOrCreateTag().remove("Y1");
                                toClone.getOrCreateTag().remove("Z1");

                                be.addConnection(this.material.get(), toClone, pos2, true, false);
                                be.sendData();
                                be.setChanged();
                                be2.addConnection(this.material.get(), toClone, pos, false, false);
                                be2.sendData();
                                be2.setChanged();
                                be.makeControllerAndSpread();
                                be.getOrCreateElectricNetwork().updateNetworkVoltage();
                            }
                        }
                    }
                }
            }

            if ((Boolean)state.getValue(TestCableConnectorBlock.EXTENSION)) {
                return InteractionResult.PASS;
            } else {
                stack.getOrCreateTag().putInt("X1", 0);
                stack.getOrCreateTag().putInt("Y1", 0);
                stack.getOrCreateTag().putInt("Z1", 0);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        } else {
            stack.getOrCreateTag().putInt("X1", pos.getX());
            stack.getOrCreateTag().putInt("Y1", pos.getY());
            stack.getOrCreateTag().putInt("Z1", pos.getZ());
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TestCableConnectorBlockEntity) {
                TestCableConnectorBlockEntity connector = (TestCableConnectorBlockEntity)be;
                if (!(level.getBlockEntity(pos) instanceof ElectricBlockEntity)) {
                    connector.player = player;
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            BlockPos pos = new BlockPos(stack.getOrCreateTag().getInt("X1"), stack.getOrCreateTag().getInt("Y1"), stack.getOrCreateTag().getInt("Z1"));
            BlockEntity var7 = level.getBlockEntity(pos);
            if (var7 instanceof TestCableConnectorBlockEntity) {
                TestCableConnectorBlockEntity be = (TestCableConnectorBlockEntity)var7;
                if (!(level.getBlockEntity(pos) instanceof ElectricBlockEntity)) {
                    be.player = null;
                }
            }

            stack = new ItemStack(stack.getItem(), stack.getCount());
            if (!level.isClientSide) {
                player.displayClientMessage(CreateLang.translateDirect("wires.removed_data", new Object[0]).withStyle(ChatFormatting.YELLOW), true);
            }

            return InteractionResultHolder.success(stack);
        } else {
            return super.use(level, player, hand);
        }
    }

    private boolean testConnection(ItemStack stack, TestCableConnectorBlockEntity be, BlockPos pos) {
        for(CableConnection connection : be.cableConnections) {
            if (connection.point1.getX() == stack.getOrCreateTag().getInt("X1") && connection.point1.getY() == stack.getOrCreateTag().getInt("Y1") && connection.point1.getZ() == stack.getOrCreateTag().getInt("Z1") && connection.point2.getX() == pos.getX() && connection.point2.getY() == pos.getY() && connection.point2.getZ() == pos.getZ()) {
                return false;
            }

            if (connection.point2.getX() == stack.getOrCreateTag().getInt("X1") && connection.point2.getY() == stack.getOrCreateTag().getInt("Y1") && connection.point2.getZ() == stack.getOrCreateTag().getInt("Z1") && connection.point1.getX() == pos.getX() && connection.point1.getY() == pos.getY() && connection.point1.getZ() == pos.getZ()) {
                return false;
            }
        }

        return true;
    }
}
