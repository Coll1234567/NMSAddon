package me.jishuna.nmsaddon.nms.v1_17_R1.entity;

import java.util.UUID;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.world.World;

import me.jishuna.nmsaddon.nms.v1_17_R1.world.NMSWorld;
import net.minecraft.network.chat.ChatComponentText;

public class NMSEntity implements Entity {
	public NMSEntity(NMSWorld world, net.minecraft.world.entity.Entity delegate) {
		this.delegate = delegate;
		this.world = world;
	}

	private final net.minecraft.world.entity.Entity delegate;
	private final NMSWorld world;

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
		return world;
	}

	@Override
	public void setLocation(Location var1) {
		delegate.teleportAndSync(var1.getX(), var1.getY(), var1.getZ());
	}

}
