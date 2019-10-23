package com.projecturanus.uranustech.mixin;

import com.projecturanus.uranustech.client.network.packet.UTCustomPayloadS2CPacket;
import net.fabricmc.fabric.impl.network.ServerSidePacketRegistryImpl;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ServerSidePacketRegistryImpl.class, remap = false)
public class MixinCustomPayloadS2CPacket {
    @Overwrite
    public Packet<?> toPacket(Identifier id, PacketByteBuf buf) {
        return new UTCustomPayloadS2CPacket(id, buf);
    }
}