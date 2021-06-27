package net.kunmc.lab.inverseoperation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Inverter {
    private final Minecraft minecraft = Minecraft.getInstance();
    private double originalSensitivity = minecraft.options.sensitivity;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (InverseOperation.activated) {
            minecraft.options.sensitivity = -(originalSensitivity + 0.67);
            minecraft.options.keyUp.setKey(InputMappings.getKey(83, 0));
            minecraft.options.keyDown.setKey(InputMappings.getKey(87, 0));
            minecraft.options.keyLeft.setKey(InputMappings.getKey(68, 0));
            minecraft.options.keyRight.setKey(InputMappings.getKey(65, 0));
        } else {
            minecraft.options.sensitivity = originalSensitivity;
            minecraft.options.keyUp.setKey(InputMappings.getKey(87, 0));
            minecraft.options.keyDown.setKey(InputMappings.getKey(83, 0));
            minecraft.options.keyLeft.setKey(InputMappings.getKey(65, 0));
            minecraft.options.keyRight.setKey(InputMappings.getKey(68, 0));
        }
        KeyBinding.resetMapping();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        originalSensitivity = minecraft.options.sensitivity;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e) {
        InverseOperation.activated = false;
        minecraft.options.sensitivity = originalSensitivity;
        minecraft.options.keyUp.setKey(InputMappings.getKey(87, 0));
        minecraft.options.keyDown.setKey(InputMappings.getKey(83, 0));
        minecraft.options.keyLeft.setKey(InputMappings.getKey(65, 0));
        minecraft.options.keyRight.setKey(InputMappings.getKey(68, 0));
        KeyBinding.resetMapping();
    }
}
