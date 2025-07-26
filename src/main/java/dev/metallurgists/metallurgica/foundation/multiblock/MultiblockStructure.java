package dev.metallurgists.metallurgica.foundation.multiblock;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlockRenderer;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.ponder.PonderClient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MultiblockStructure {
    private final BlockEntity master;
    private final ArrayList<Map<BlockPos, BlockState>> structure;
    private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
    
    public MultiblockStructure(BlockEntity master, ArrayList<Map<BlockPos, BlockState>> structure) {
        this.master = master;
        this.structure = structure;
    }
    
    public MultiblockStructure addFluidOutput(BlockPos pos, String identifier) {
        fluidOutputs.add(Map.of(pos, identifier));
        return this;
    }
    public MultiblockStructure addFluidOutputs(List<Map<BlockPos, String>> fluidOutputs) {
        this.fluidOutputs.addAll(fluidOutputs);
        return this;
    }
    
    public static CuboidBuilder cuboidBuilder(BlockEntity master) {
        return new CuboidBuilder(master);
    }
    
    public static class CuboidBuilder {
        private final BlockEntity master;
        private Direction direction = null;
        private boolean isDirectional = false;
        private final ArrayList<Map<BlockPos, BlockState>> structure = new ArrayList<>();
        private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
        private int width = 0;
        private int height = 0;
        private int depth = 0;
        
        public CuboidBuilder(BlockEntity master) {
            this.master = master;
        }
        
        public BlockEntity getMaster() {
            return master;
        }
        
        public BlockPos getMasterPosition() {
            return getMaster().getBlockPos();
        }
        
        public CuboidBuilder cube(int size) {
            return withSize(size, size, size);
        }
        
        public CuboidBuilder withSize(int width, int height) {
            return withSize(width, height, width);
        }
        
        public CuboidBuilder withSize(int width, int height, int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
            return this;
        }
        
        public CuboidBuilder directional(Direction direction) {
            this.direction = direction;
            this.isDirectional = true;
            return this;
        }
        
        public Triplet<Integer, Integer, Integer> getSize() {
            return new Triplet<>(width, height, depth);
        }
        
        public int getWidth() {
            return getSize().getA();
        }
        
        public int getHeight() {
            return getSize().getB();
        }
        
        public int getDepth() {
            return getSize().getC();
        }
        
        public CuboidBuilder withFluidOutputAt(int x, int y, int z, String identifier) {
            BlockPos pos = translateToMaster(x, y, z);
            fluidOutputs.add(Map.of(pos, identifier));
            return withBlockAt(x, y, z, MetallurgicaBlocks.fluidOutput.get().defaultBlockState());
        }
        
        public CuboidBuilder withBlockAt(int x, int y, int z, BlockState block) {
            BlockPos pos = translateToMaster(x, y, z);
            structure.add(Map.of(pos, block));
            return this;
        }
        
        public CuboidBuilder withBlockAt(PositionUtil.PositionRange range, BlockState block) {
            for (Triplet<Integer, Integer, Integer> pos : range.getPositions()) {
                structure.add(Map.of(translateToMaster(pos.getA(), pos.getB(), pos.getC()), block));
            }
            return this;
        }
        
        private BlockPos translateToMaster(int x, int y, int z) {
            BlockPos masterPos = getMasterPosition();
            if (isDirectional) {
                return masterPos.relative(direction, x).above(y).relative(direction.getClockWise(), z);
            }
            return masterPos.offset(x, y, z);
        }
        
        public MultiblockStructure build() {
            return new MultiblockStructure(getMaster(), structure).addFluidOutputs(fluidOutputs);
        }
    }

    public ArrayList<Map<BlockPos, BlockState>> getStructure() {
        return structure;
    }
    
    public BlockEntity getMaster() {
        return master;
    }
    
    public ArrayList<Map<BlockPos, String>> getFluidOutputs() {
        return fluidOutputs;
    }
    
    public ArrayList<FluidOutputBlockEntity> getFluidOutputBlockEntities() {
        ArrayList<FluidOutputBlockEntity> fluidOutputBlockEntities = new ArrayList<>();
        for (Map<BlockPos, String> map : fluidOutputs) {
            for (BlockPos pos : map.keySet()) {
                BlockEntity blockEntity = master.getLevel().getBlockEntity(pos);
                if (blockEntity instanceof FluidOutputBlockEntity) {
                    fluidOutputBlockEntities.add((FluidOutputBlockEntity) blockEntity);
                }
            }
        }
        return fluidOutputBlockEntities;
    }
    
    public BlockPos getFluidOutputPosition(String identifier) {
        for (Map<BlockPos, String> map : fluidOutputs) {
            for (BlockPos pos : map.keySet()) {
                if (map.get(pos).equals(identifier)) {
                    return pos;
                }
            }
        }
        return null;
    }
    
    public BlockPos getMasterPosition() {
        return getMaster().getBlockPos();
    }
    
    public BlockState getMasterBlockState() {
        return getMaster().getLevel().getBlockState(getMasterPosition());
    }
    
    public Direction getMasterDirection() {
        return getMasterBlockState().getValue(BlockStateProperties.FACING);
    }
    
    public boolean isBlockCorrect(BlockPos pos) {
        if (master.getLevel() == null) return false;
        for (Map<BlockPos, BlockState> map : getStructure()) {
            if (map.containsKey(pos)) {
                return master.getLevel().getBlockState(pos).equals(map.get(pos));
            }
        }
        return false;
    }
    
    public void createMissingParticles() {
        ParticleOptions smokeParticle = ParticleTypes.SMOKE;
        if (master.getLevel() == null) {
            Metallurgica.LOGGER.error("Level is null, cannot createIngot particles");
            return;
        }
        
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                //CreateClient.GHOST_BLOCKS.showGhostState(this, map.get(pos)).at(pos).breathingAlpha();
                if (!isBlockCorrect(pos)) {
                    //CreateClient.OUTLINER.showAABB(pos, new AABB(pos)).colored(0xFF0000).lineWidth(0.01f).withFaceTexture(AllSpecialTextures.HIGHLIGHT_CHECKERED);
                    PonderClient.GHOST_BLOCKS.showGhost(pos, GhostBlockRenderer.standard(), GhostBlockParams.of(map.get(pos)).at(pos).breathingAlpha(), 1);
                    //createCustomCubeParticles(pos, master.getLevel(), smokeParticle);
                }
            }
        }
    }
    
    public boolean isStructureCorrect() {
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                if (!isBlockCorrect(pos)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void setFluidOutputCapacity(FluidOutputBlockEntity fluidOutput, int value) {
        if (fluidOutput == null) return;
        if (fluidOutput.tankInventory.getCapacity() == value) return;
        fluidOutput.tankInventory.setCapacity(value);
    }
    
    public void addToGoggleTooltip(List<Component> tooltip) {
        for (FluidOutputBlockEntity fluidOutput : getFluidOutputBlockEntities()) {
            LazyOptional<IFluidHandler> handler = fluidOutput.getCapability(ForgeCapabilities.FLUID_HANDLER);
            Optional<IFluidHandler> resolve = handler.resolve();
            if (!resolve.isPresent()) {
                return;
            } else {
                IFluidHandler tank = resolve.get();
                if (tank.getTanks() == 0) {
                    return;
                } else {
                    LangBuilder tankName = null;
                    for (Map<BlockPos, String> map : getFluidOutputs()) {
                        for (BlockPos pos : map.keySet()) {
                            if (pos.equals(fluidOutput.getBlockPos())) {
                                tankName = MetalLang.translate(map.get(pos));
                            }
                        }
                    }
                    LangBuilder mb = MetalLang.translate("generic.unit.millibuckets");
                    boolean isEmpty = true;
                    if (tankName != null) {
                        tankName.style(ChatFormatting.WHITE).forGoggles(tooltip, 1);
                    }
                    
                    for(int i = 0; i < tank.getTanks(); ++i) {
                        FluidStack fluidStack = tank.getFluidInTank(i);
                        if (!fluidStack.isEmpty()) {
                            MetalLang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                            MetalLang.builder().add(MetalLang.number(fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(MetalLang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                            isEmpty = false;
                        }
                    }
                    
                    if (tank.getTanks() > 1) {
                        if (isEmpty) {
                            tooltip.remove(tooltip.size() - 1);
                        }
                        
                        return;
                    } else if (!isEmpty) {
                        return;
                    } else {
                        MetalLang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(MetalLang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                        return;
                    }
                }
            }
        }
    }
}
