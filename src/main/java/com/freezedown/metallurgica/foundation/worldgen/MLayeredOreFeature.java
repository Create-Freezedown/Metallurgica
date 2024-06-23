package com.freezedown.metallurgica.foundation.worldgen;

import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MLayeredOreFeature extends MBaseConfigDrivenOreFeature<MConfigDrivenLayeredOreFeatureConfiguration> {
    public MLayeredOreFeature() {
        super(MConfigDrivenLayeredOreFeatureConfiguration.CODEC);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<MConfigDrivenLayeredOreFeatureConfiguration> pContext) {
        RandomSource random = pContext.random();
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        MConfigDrivenLayeredOreFeatureConfiguration config = pContext.config();
        List<LayerPattern> patternPool = config.getLayerPatterns();
        
        if (patternPool.isEmpty())
            return false;
        
        LayerPattern layerPattern = patternPool.get(random.nextInt(patternPool.size()));
        
        int placedAmount = 0;
        int size = config.getClusterSize();
        int radius = Mth.ceil(config.getClusterSize() / 2f);
        int x0 = blockpos.getX() - radius;
        int y0 = blockpos.getY() - radius;
        int z0 = blockpos.getZ() - radius;
        int width = size + 1;
        int length = size + 1;
        int height = size + 1;
        
        if (blockpos.getY() >= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockpos.getX(),
                blockpos.getZ()))
            return false;
        
        List<LayerPattern.Layer> resolvedLayers = new ArrayList<>();
        List<Float> layerDiameterOffsets = new ArrayList<>();
        
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BulkSectionAccess bulksectionaccess = new BulkSectionAccess(worldgenlevel);
        int layerCoordinate = random.nextInt(4);
        int slantyCoordinate = random.nextInt(3);
        float slope = random.nextFloat() * .75f;
        
        try {
            
            for (int x = 0; x < width; x++) {
                float dx = x * 2f / width - 1;
                if (dx * dx > 1)
                    continue;
                
                for (int y = 0; y < height; y++) {
                    float dy = y * 2f / height - 1;
                    if (dx * dx + dy * dy > 1)
                        continue;
                    if (worldgenlevel.isOutsideBuildHeight(y0 + y))
                        continue;
                    
                    for (int z = 0; z < length; z++) {
                        float dz = z * 2f / height - 1;
                        
                        int layerIndex = layerCoordinate == 0 ? z : layerCoordinate == 1 ? x : y;
                        if (slantyCoordinate != layerCoordinate)
                            layerIndex += (int) (Mth.floor(slantyCoordinate == 0 ? z : slantyCoordinate == 1 ? x : y) * slope);
                        
                        while (layerIndex >= resolvedLayers.size()) {
                            LayerPattern.Layer next = layerPattern.rollNext(
                                    resolvedLayers.isEmpty() ? null : resolvedLayers.get(resolvedLayers.size() - 1),
                                    random);
                            float offset = random.nextFloat() * .5f + .5f;
                            for (int i = 0; i < next.minSize + random.nextInt(1 + next.maxSize - next.minSize); i++) {
                                resolvedLayers.add(next);
                                layerDiameterOffsets.add(offset);
                            }
                        }
                        
                        if (dx * dx + dy * dy + dz * dz > 1 * layerDiameterOffsets.get(layerIndex))
                            continue;
                        
                        LayerPattern.Layer layer = resolvedLayers.get(layerIndex);
                        List<OreConfiguration.TargetBlockState> state = layer.rollBlock(random);
                        
                        int currentX = x0 + x;
                        int currentY = y0 + y;
                        int currentZ = z0 + z;
                        
                        mutablePos.set(currentX, currentY, currentZ);
                        if (!worldgenlevel.ensureCanWrite(mutablePos))
                            continue;
                        LevelChunkSection levelchunksection = bulksectionaccess.getSection(mutablePos);
                        if (levelchunksection == null)
                            continue;
                        
                        int i3 = SectionPos.sectionRelative(currentX);
                        int j3 = SectionPos.sectionRelative(currentY);
                        int k3 = SectionPos.sectionRelative(currentZ);
                        BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);
                        
                        for (OreConfiguration.TargetBlockState oreconfiguration$targetblockstate : state) {
                            if (!canPlaceOre(blockstate, bulksectionaccess::getBlockState, random, config,
                                    oreconfiguration$targetblockstate, mutablePos))
                                continue;
                            if (oreconfiguration$targetblockstate.state.isAir())
                                continue;
                            levelchunksection.setBlockState(i3, j3, k3, oreconfiguration$targetblockstate.state, false);
                            ++placedAmount;
                            break;
                        }
                        
                    }
                }
            }
            
        } catch (Throwable throwable1) {
            try {
                bulksectionaccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }
            
            throw throwable1;
        }
        
        bulksectionaccess.close();
        return placedAmount > 0;
    }
}
