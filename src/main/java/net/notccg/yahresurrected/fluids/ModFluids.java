package net.notccg.yahresurrected.fluids;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.item.ModItems;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_BLOOD = FLUIDS.register("blood_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.BLOOD_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_BLOOD = FLUIDS.register("flowing_blood_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.BLOOD_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties BLOOD_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.BLOOD_FLUID_TYPE, SOURCE_BLOOD, FLOWING_BLOOD)
            .slopeFindDistance(2).levelDecreasePerBlock(1).block(ModBlocks.BLOOD_LIQUID_BLOCK)
            .bucket(ModItems.BLOOD_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
