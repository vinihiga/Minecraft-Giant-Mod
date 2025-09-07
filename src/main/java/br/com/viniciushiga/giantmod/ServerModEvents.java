package br.com.viniciushiga.giantmod;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static br.com.viniciushiga.giantmod.GiantMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ServerModEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        var searchBox = event.player.getBoundingBox();

        ServerLevel currentLevel = (ServerLevel) event.player.level();

        currentLevel
            .getEntitiesOfClass(LivingEntity.class, searchBox)
            .forEach(entity -> {
                ServerModEvents.onCollisionEntered(currentLevel, event.player, entity);
            });
    }

    private static void onCollisionEntered(
        ServerLevel currentLevel,
        Player player,
        LivingEntity entity
    ) {
        if (!(entity instanceof Mob)) {
            return;
        } else if (!entity.getBoundingBox().intersects(player.getBoundingBox())) {
            return;
        }

        entity.hurtMarked = true;
        entity.hurtServer(currentLevel, player.damageSources().generic(), 9999f);
    }
}