package br.com.viniciushiga.giantmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static br.com.viniciushiga.giantmod.GiantMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents {
    private static double regularHeight = 1.0;
    private static double giantHeight = 10.0;

    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key keyEvent) {
        boolean isValueChanged = false;
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        if (keyEvent.getKey() == GLFW.GLFW_KEY_F9 && keyEvent.getAction() == GLFW.GLFW_PRESS) {
            var scaleAttribute = localPlayer.getAttribute(Attributes.SCALE);
            scaleAttribute.setBaseValue(regularHeight);
            ClientModEvents.sendUIMessage(localPlayer, regularHeight);
        } else if (keyEvent.getKey() == GLFW.GLFW_KEY_F10 && keyEvent.getAction() == GLFW.GLFW_PRESS) {
            var scaleAttribute = localPlayer.getAttribute(Attributes.SCALE);
            scaleAttribute.setBaseValue(giantHeight);
            ClientModEvents.sendUIMessage(localPlayer, giantHeight);
        }
    }

    private static void sendUIMessage(LocalPlayer localPlayer, double newHeight) {
        String message = Locale.giantSize.replace(
            "%x",
            String.valueOf(newHeight)
        );

        localPlayer.displayClientMessage(
            Component.literal(message),
            true
        );
    }
}