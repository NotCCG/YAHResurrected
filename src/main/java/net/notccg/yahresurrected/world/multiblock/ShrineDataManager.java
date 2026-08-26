package net.notccg.yahresurrected.world.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class ShrineDataManager extends SavedData {
    private static final String DATA_NAME = "shrine_data";
    public static final int MAX_FLUID = 1000;

    private final Map<Long, ShrineData> shrines = new HashMap<>();

    public static ShrineDataManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                ShrineDataManager::load,
                ShrineDataManager::new,
                DATA_NAME
        );
    }

    private ShrineData getOrCreate(BlockPos pos) {
        long key = pos.asLong();

        ShrineData data = shrines.get(key);

        if (data == null) {
            data = new ShrineData();
            shrines.put(key, data);
            setDirty();
        }

        return data;
    }

    public boolean hasShrine(BlockPos pos) {
        return shrines.containsKey(pos.asLong());
    }

    public int getFluidLevel(BlockPos pos) {
        ShrineData data = shrines.get(pos.asLong());

        if (data == null) {
            return 0;
        }

        return data.fluidLevel;
    }

    public void setFluidLevel(BlockPos pos, int amount) {
        ShrineData data = getOrCreate(pos);

        int newAmount = Mth.clamp(
                amount, 0, MAX_FLUID
        );
        if (data.fluidLevel != newAmount) {
            data.fluidLevel = newAmount;
            setDirty();
        }
    }

    public void addFluid(BlockPos pos, int amount) {
        setFluidLevel(pos, getFluidLevel(pos) + amount);
    }

    public void removeFluid(BlockPos pos, int amount) {
        setFluidLevel(pos, getFluidLevel(pos) - amount);
    }

    public int getProgress(BlockPos pos) {
        ShrineData data = shrines.get(pos.asLong());

        if (data == null) {
            return 0;
        }

        return data.craftProgress;
    }

    public void setProgress(BlockPos pos, int progress) {
        ShrineData data = getOrCreate(pos);

        if (data.craftProgress != progress) {
            data.craftProgress = progress;
            setDirty();
        }
    }

    public void removeShrine(BlockPos pos) {
        if (shrines.remove(pos.asLong()) != null) {
            setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag shrineList = new ListTag();

        for (Map.Entry<Long, ShrineData> entry : shrines.entrySet()) {
            CompoundTag shrineTag = new CompoundTag();

            shrineTag.putLong("Pos", entry.getKey());

            shrineTag.putInt("FluidLevel", entry.getValue().fluidLevel);

            shrineTag.putInt("Progress", entry.getValue().craftProgress);

            shrineList.add(shrineTag);
        }

        tag.put("Shrines", shrineList);
        return tag;
    }

    public static ShrineDataManager load(CompoundTag tag) {
        ShrineDataManager manager = new ShrineDataManager();

        ListTag shrineList = tag.getList("Shrines", Tag.TAG_COMPOUND);

        for (int i = 0; i < shrineList.size(); i++) {
            CompoundTag shrineTag = shrineList.getCompound(i);
            long pos = shrineTag.getLong("Pos");
            ShrineData data = new ShrineData();

            data.fluidLevel = shrineTag.getInt("FluidLevel");
            data.craftProgress = shrineTag.getInt("Progress");

            manager.shrines.put(pos, data);
        }
        return manager;
    }

    private static class ShrineData {
        private int fluidLevel = 0;
        private int craftProgress = 0;
    }
}
