package br.com.viniciushiga.giantmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.w3c.dom.Attr;
import net.minecraft.world.damagesource.DamageSource;

import static br.com.viniciushiga.giantmod.GiantMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents {
    private static boolean isGiantMode = false;

    private class Constants {
        private static final double regularHeight = 1.0;
        private static final double giantHeight = 10.0;

        private static final double regularSpeed = 0.1;
        private static final double giantSpeed = 1.0;
    }

    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key keyEvent) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        if (localPlayer == null) {
            return;
        }

        var scaleAttribute = localPlayer.getAttribute(Attributes.SCALE);
        var speedAttribute = localPlayer.getAttribute(Attributes.MOVEMENT_SPEED);

        if (keyEvent.getKey() == GLFW.GLFW_KEY_F9 && keyEvent.getAction() == GLFW.GLFW_PRESS) {
            isGiantMode = false;
            scaleAttribute.setBaseValue(Constants.regularHeight);
            speedAttribute.setBaseValue(Constants.regularSpeed);
            ClientModEvents.sendUIMessage(localPlayer, Constants.regularHeight);
        } else if (keyEvent.getKey() == GLFW.GLFW_KEY_F10 && keyEvent.getAction() == GLFW.GLFW_PRESS) {
            isGiantMode = true;
            scaleAttribute.setBaseValue(Constants.giantHeight);
            speedAttribute.setBaseValue(Constants.giantSpeed);
            ClientModEvents.sendUIMessage(localPlayer, Constants.giantHeight);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isGiantMode) {
            return;
        }

        var searchBox = event.player.getBoundingBox();

        event
            .player
            .level()
            .getEntitiesOfClass(LivingEntity.class, searchBox)
            .forEach(entity -> {
                if (entity == event.player || !(entity instanceof Mob)) {
                    return;
                } else if (!entity.getBoundingBox().intersects(event.player.getBoundingBox())) {
                    return;
                }

                entity.hurtMarked = true;
                entity.hurt(event.player.damageSources().generic(), 9999f);
            });
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