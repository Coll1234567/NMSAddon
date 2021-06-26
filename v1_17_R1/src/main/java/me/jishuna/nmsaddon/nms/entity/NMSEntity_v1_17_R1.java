package me.jishuna.nmsaddon.nms.entity;

import java.util.UUID;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.world.World;

import me.jishuna.nmsaddon.nms.world.NMSWorld_v1_17_R1;
import net.minecraft.network.chat.ChatComponentText;

public class NMSEntity_v1_17_R1 implements Entity {
	public NMSEntity_v1_17_R1(NMSWorld_v1_17_R1 delegate2, net.minecraft.world.entity.Entity delegate) {
		this.delegate = delegate;
		this.delegate2 = delegate2;
	}

	private final net.minecraft.world.entity.Entity delegate;
	private final NMSWorld_v1_17_R1 delegate2;

	@Override
	public Object getHandle() {
		return delegate;
	}

	@Override
	public void sendMessage(String var1) {
		delegate.sendMessage(new ChatComponentText(var1), UUID.randomUUID());

	}

	@Override
	public Location getLocation() {
		return new Location(getWorld(), new Vector3(delegate.getPositionVector().getX(),
				delegate.getPositionVector().getY(), delegate.getPositionVector().getZ()));
	}

	@Override
	public World getWorld() {
		return delegate2;
	}

	@Override
	public void setLocation(Location var1) {
		delegate.teleportAndSync(var1.getX(), var1.getY(), var1.getZ());
	}

}
