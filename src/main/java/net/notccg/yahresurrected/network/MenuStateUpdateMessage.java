package net.notccg.yahresurrected.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.init.ModMenus;
import net.notccg.yahresurrected.init.ModScreens;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MenuStateUpdateMessage {
    private final int elementType;
    private final String name;
    private final Object elementState;

    public MenuStateUpdateMessage(int elementType, String name, Object elementState) {
        this.elementType = elementType;
        this.name = name;
        this.elementState = elementState;
    }

    public MenuStateUpdateMessage(FriendlyByteBuf buffer) {
        this.elementType = buffer.readInt();
        this.name = buffer.readUtf();
        Object elementState = null;
        if (elementType == 0) {
            elementState = buffer.readUtf();
        } else if (elementType == 1) {
            elementState = buffer.readBoolean();
        } else if (elementType == 2) {
            elementState = buffer.readDouble();
        }
        this.elementState = elementState;
    }

    public static void buffer(MenuStateUpdateMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.elementType);
        buffer.writeUtf(message.name);
        if (message.elementType == 0) {
            buffer.writeUtf((String) message.elementState);
        } else if (message.elementType == 1) {
            buffer.writeBoolean((boolean) message.elementState);
        } else if (message.elementType == 2 && message.elementState instanceof Number n) {
            buffer.writeDouble(n.doubleValue());
        }
    }

    public static void handler(MenuStateUpdateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (message.name.length() > 256 || message.elementState instanceof String string && string.length() > 8192)
            return;
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender().containerMenu instanceof ModMenus.MenuAccessor menu) {
                menu.getMenuState().put(message.elementType + ":" + message.name, message.elementState);
                if (!context.getDirection().getReceptionSide().isServer() && Minecraft.getInstance().screen instanceof ModScreens.ScreenAccessor accessor) {
                    accessor.updateMenuState(message.elementType, message.name, message.elementState);
                }
            }
        });
        context.setPacketHandled(true);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        YouAreHerobrineResurrected.addNetworkMessage(MenuStateUpdateMessage.class, MenuStateUpdateMessage::buffer, MenuStateUpdateMessage::new, MenuStateUpdateMessage::handler);
    }
}
