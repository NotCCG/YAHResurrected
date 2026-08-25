package net.notccg.yahresurrected.util.init;

import net.notccg.yahresurrected.world.inventory.ShrineGUIMenu;
import net.notccg.yahresurrected.network.MenuStateUpdateMessage;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, YouAreHerobrineResurrected.MOD_ID);
    public static final RegistryObject<MenuType<ShrineGUIMenu>> SHRINE_GUI = REGISTRY.register("shrine_gui", () -> IForgeMenuType.create(ShrineGUIMenu::new));

    public interface MenuAccessor {
        Map<String, Object> getMenuState();

        Map<Integer, Slot> getSlots();

        default void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
            getMenuState().put(elementType + ":" + name, elementState);
            if (player instanceof ServerPlayer serverPlayer) {
                YouAreHerobrineResurrected.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MenuStateUpdateMessage(elementType, name, elementState));
            } else if (player.level().isClientSide) {
                if (Minecraft.getInstance().screen instanceof ModScreens.ScreenAccessor accessor && needClientUpdate)
                    accessor.updateMenuState(elementType, name, elementState);
                YouAreHerobrineResurrected.PACKET_HANDLER.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
            }
        }

        default <T> T getMenuState(int elementType, String name, T defaultValue) {
            try {
                return (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
            } catch (ClassCastException e) {
                return defaultValue;
            }
        }
    }

}
