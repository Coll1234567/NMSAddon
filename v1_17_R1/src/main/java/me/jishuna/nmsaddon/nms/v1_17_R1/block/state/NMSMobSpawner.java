package me.jishuna.nmsaddon.nms.v1_17_R1.block.state;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.platform.block.state.MobSpawner;
import com.dfsek.terra.api.platform.block.state.SerialState;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;

public class NMSMobSpawner extends NMSBlockState implements MobSpawner {

	private TileEntityMobSpawner spawner;

	protected NMSMobSpawner(RegionLimitedWorldAccess world, BlockPosition position) {
		super(world, position);
		this.spawner = (TileEntityMobSpawner) world.getTileEntity(position);
	}

	@Override
	public EntityType getSpawnedType() {
		NamespacedKey key = CraftNamespacedKey
				.fromMinecraft(spawner.getSpawner().getMobName(spawner.getWorld(), spawner.getPosition()));
		return new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(key.getKey().toUpperCase()));
	}

	@Override
	public void setSpawnedType(@NotNull EntityType creatureType) {
		org.bukkit.entity.EntityType type = ((BukkitEntityType) creatureType).getHandle();
		EntityTypes<?> NMSType = IRegistry.Y
				.a(ResourceKey.a(IRegistry.l, CraftNamespacedKey.toMinecraft(type.getKey())));
		spawner.getSpawner().setMobName(NMSType);
	}

	@Override
	public int getDelay() {
		return 0;
	}

	@Override
	public void setDelay(int delay) {

	}

	@Override
	public int getMinSpawnDelay() {
		return 0;
	}

	@Override
	public void setMinSpawnDelay(int delay) {

	}

	@Override
	public int getMaxSpawnDelay() {
		return 0;
	}

	@Override
	public void setMaxSpawnDelay(int delay) {

	}

	@Override
	public int getSpawnCount() {
		return 0;
	}

	@Override
	public void setSpawnCount(int spawnCount) {

	}

	@Override
	public int getMaxNearbyEntities() {
		return 0;
	}

	@Override
	public void setMaxNearbyEntities(int maxNearbyEntities) {

	}

	@Override
	public int getRequiredPlayerRange() {
		return 0;
	}

	@Override
	public void setRequiredPlayerRange(int requiredPlayerRange) {

	}

	@Override
	public int getSpawnRange() {
		return 0;
	}

	@Override
	public void setSpawnRange(int spawnRange) {

	}

	@Override
	public void applyState(String state) {
		SerialState.parse(state).forEach((k, v) -> {
			switch (k) {
			case "type":
				setSpawnedType(new BukkitEntityType(org.bukkit.entity.EntityType.valueOf(v.toUpperCase())));
				return;
			case "delay":
				setDelay(Integer.parseInt(v));
				return;
			case "min_delay":
				setMinSpawnDelay(Integer.parseInt(v));
				return;
			case "max_delay":
				setMaxSpawnDelay(Integer.parseInt(v));
				return;
			case "spawn_count":
				setSpawnCount(Integer.parseInt(v));
				return;
			case "spawn_range":
				setSpawnRange(Integer.parseInt(v));
				return;
			case "max_nearby":
				setMaxNearbyEntities(Integer.parseInt(v));
				return;
			case "required_player_range":
				setRequiredPlayerRange(Integer.parseInt(v));
				return;
			default:
				throw new IllegalArgumentException("Invalid property: " + k);
			}
		});
	}
}
