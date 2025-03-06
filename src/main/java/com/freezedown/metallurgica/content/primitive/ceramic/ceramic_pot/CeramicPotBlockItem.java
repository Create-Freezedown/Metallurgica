package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_pot;


import com.freezedown.metallurgica.foundation.util.MetalLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT;

public class CeramicPotBlockItem extends BlockItem {
    public CeramicPotBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    
    public FluidStack getFluid(ItemStack pStack) {
        return loadFluidStackFromNBT(pStack.getOrCreateTagElement("BlockEntityTag").getCompound("TankContent"));
    }
    
    public boolean hasFluid(ItemStack pStack) {
        return getFluid(pStack) != null;
    }
    
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        LangBuilder mb = MetalLang.translate("generic.unit.millibuckets");
        if (hasFluid(pStack)) {
            pTooltipComponents.add(getFluid(pStack).getDisplayName());
            pTooltipComponents.add(MetalLang.builder().add(MetalLang.number(getFluid(pStack).getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(MetalLang.number(1000).add(mb).style(ChatFormatting.DARK_GRAY)).component());
        }
    }
    
    public void insertFluid(ItemStack pStack, FluidStack pFluid) {
        pFluid.writeToNBT(pStack.getOrCreateTagElement("BlockEntityTag").getCompound("TankContent"));
    }
    
    public InteractionResult place(BlockPlaceContext pContext) {
        if (pContext.getLevel().getBlockState(pContext.getClickedPos()).getFluidState().isSource()) {
            FluidStack fluid = new FluidStack(pContext.getLevel().getBlockState(pContext.getClickedPos()).getFluidState().getType(), 1000);
            insertFluid(pContext.getItemInHand(), fluid);
            pContext.getLevel().setBlock(pContext.getClickedPos(), Blocks.AIR.defaultBlockState(), 3);
            return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
        }
        return super.place(pContext);
    }
}
