package net.kunmc.lab.inverseoperation;

import net.kunmc.lab.inverseoperation.packet.ActivateMessage;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(InverseOperation.ModId)
public class InverseOperation {
    public static boolean activated = false;
    public static final String ModId = "inverseoperation";
    public static final String protocolVersion = "1";
    public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ModId, ModId + "_channel"))
            .clientAcceptedVersions(a -> true)
            .serverAcceptedVersions(a -> true)
            .networkProtocolVersion(() -> protocolVersion).simpleChannel();

    public InverseOperation() {
        MinecraftForge.EVENT_BUS.register(this);
        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.register(new Inverter());
        }
        channel.registerMessage(0, ActivateMessage.class, ActivateMessage::encodeMessage, ActivateMessage::decodeMessage, ActivateMessage::receiveMessage);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        channel.send(PacketDistributor.ALL.noArg(), new ActivateMessage(activated));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onServerStart(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("inverseoperation")
                .requires(x -> x.hasPermission(4))
                .then(Commands.literal("start")
                        .executes(x -> {
                            Entity entity = x.getSource().getEntity();

                            if (activated) {
                                StringTextComponent text = new StringTextComponent("InverseOperation has already started.");
                                text.setStyle(text.getStyle().withColor(net.minecraft.util.text.Color.parseColor("red")));
                                entity.sendMessage(text, entity.getUUID());
                                return 1;
                            }

                            activated = true;
                            channel.send(PacketDistributor.ALL.noArg(), new ActivateMessage(true));

                            StringTextComponent text = new StringTextComponent("InverseOperation has been started.");
                            text.setStyle(text.getStyle().withColor(net.minecraft.util.text.Color.parseColor("green")));
                            entity.sendMessage(text, entity.getUUID());
                            return 1;
                        }))
                .then(Commands.literal("stop")
                        .executes(x -> {
                            Entity entity = x.getSource().getEntity();

                            if (!activated) {
                                StringTextComponent text = new StringTextComponent("InverseOperation has already stopped.");
                                text.setStyle(text.getStyle().withColor(net.minecraft.util.text.Color.parseColor("red")));
                                entity.sendMessage(text, entity.getUUID());
                                return 1;
                            }

                            activated = false;
                            channel.send(PacketDistributor.ALL.noArg(), new ActivateMessage(false));

                            StringTextComponent text = new StringTextComponent("InverseOperation has been stopped.");
                            text.setStyle(text.getStyle().withColor(net.minecraft.util.text.Color.parseColor("green")));
                            entity.sendMessage(text, entity.getUUID());
                            return 1;
                        })));
    }
}
