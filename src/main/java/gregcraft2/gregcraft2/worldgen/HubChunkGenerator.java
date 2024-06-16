package gregcraft2.gregcraft2.worldgen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Chest;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HubChunkGenerator extends ChunkGenerator {
    @NotNull
    @Override
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }
    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        if (chunkX != 0 || chunkZ !=0) return;
        chunkData.setRegion(0, 100, 0, 16, 101, 16, Material.COAL_BLOCK);
        chunkData.setRegion(2, 100, 2, 14, 101, 14, Material.GRAY_STAINED_GLASS);
        chunkData.setRegion(1, 101, 1, 15, 107, 15, Material.END_STONE_BRICKS);
        chunkData.setRegion(2, 101, 2, 14, 107, 14, Material.AIR);
        chunkData.setRegion(7, 101, 7, 9, 107, 9, Material.DARK_OAK_LOG);
        chunkData.setRegion(1, 107, 1, 15, 108, 15, Material.STRIPPED_CHERRY_WOOD);

        Directional north_torch = (Directional) Material.SOUL_WALL_TORCH.createBlockData();
        north_torch.setFacing(BlockFace.NORTH);
        Directional south_torch = (Directional) Material.SOUL_WALL_TORCH.createBlockData();
        south_torch.setFacing(BlockFace.SOUTH);
        Directional east_torch = (Directional) Material.SOUL_WALL_TORCH.createBlockData();
        east_torch.setFacing(BlockFace.EAST);
        Directional west_torch = (Directional) Material.SOUL_WALL_TORCH.createBlockData();
        west_torch.setFacing(BlockFace.WEST);

        chunkData.setBlock(6, 105, 7, west_torch.clone());
        chunkData.setBlock(6, 105, 8, west_torch.clone());
        chunkData.setBlock(9, 105, 7, east_torch.clone());
        chunkData.setBlock(9, 105, 8, east_torch.clone());
        chunkData.setBlock(7, 105, 6, north_torch.clone());
        chunkData.setBlock(8, 105, 6, north_torch.clone());
        chunkData.setBlock(7, 105, 9, south_torch.clone());
        chunkData.setBlock(8, 105, 9, south_torch.clone());

        chunkData.setBlock(3, 105, 2, south_torch.clone());
        chunkData.setBlock(2, 105, 3, east_torch.clone());
        chunkData.setBlock(12, 105, 2, south_torch.clone());
        chunkData.setBlock(13, 105, 3, west_torch.clone());
        chunkData.setBlock(2, 105, 12, east_torch.clone());
        chunkData.setBlock(3, 105, 13, north_torch.clone());
        chunkData.setBlock(12, 105, 13, north_torch.clone());
        chunkData.setBlock(13, 105, 12, west_torch.clone());

        Chest left_chest = (Chest) Material.CHEST.createBlockData(x -> ((Chest) x).setType(Chest.Type.LEFT));
        Chest right_chest = (Chest) Material.CHEST.createBlockData(x -> ((Chest) x).setType(Chest.Type.RIGHT));

        left_chest.setFacing(BlockFace.SOUTH);
        right_chest.setFacing(BlockFace.SOUTH);
        chunkData.setBlock(8, 101, 2, left_chest.clone());
        chunkData.setBlock(7, 101, 2, right_chest.clone());

        left_chest.setFacing(BlockFace.WEST);
        right_chest.setFacing(BlockFace.WEST);
        chunkData.setBlock(13, 101, 8, left_chest.clone());
        chunkData.setBlock(13, 101, 7, right_chest.clone());

        left_chest.setFacing(BlockFace.NORTH);
        right_chest.setFacing(BlockFace.NORTH);
        chunkData.setBlock(7, 101, 13, left_chest.clone());
        chunkData.setBlock(8, 101, 13, right_chest.clone());
    }
    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
    }
    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
    }
    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
    }

    @Nullable
    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new VoidBiomeProvider();
    }
    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return true;
    }
    @Nullable
    @Override
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world, 4.5, 101, 8.5).setDirection(new Vector(1,0,0));
    }
}
