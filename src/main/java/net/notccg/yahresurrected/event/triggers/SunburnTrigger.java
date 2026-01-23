package net.notccg.yahresurrected.event.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.PlayerAdvancements;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class SunburnTrigger implements CriterionTrigger<SunburnTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "sunburn");

    private final SimpleCriterionTrigger<Instance> internal = new SimpleCriterionTrigger<>() {
        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        protected Instance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pDeserializationContext) {
            return new Instance();
        }
    };

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<SunburnTrigger.Instance> pListener) {
        internal.addPlayerListener(pPlayerAdvancements, pListener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<SunburnTrigger.Instance> pListener) {
        internal.removePlayerListener(pPlayerAdvancements, pListener);
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements pPlayerAdvancements) {
        internal.removePlayerListeners(pPlayerAdvancements);
    }

    @Override
    public SunburnTrigger.Instance createInstance(JsonObject pJson, DeserializationContext pContext) {
        return new Instance();
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player);
    }

    public Instance instance() {
        return new Instance();
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance() {
            super(ID, ContextAwarePredicate.ANY);
        }
    }
}
