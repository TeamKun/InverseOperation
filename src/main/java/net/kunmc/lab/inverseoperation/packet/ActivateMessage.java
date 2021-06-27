package net.kunmc.lab.inverseoperation.packet;

import net.kunmc.lab.inverseoperation.InverseOperation;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivateMessage {
    public boolean activated;

    public ActivateMessage(boolean b) {
        this.activated = b;
    }

    public static void encodeMessage(ActivateMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.activated);
    }

    public static ActivateMessage decodeMessage(PacketBuffer buffer) {
        return new ActivateMessage(buffer.readBoolean());
    }

    public static void receiveMessage(ActivateMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        InverseOperation.activated = message.activated;
    }
}
