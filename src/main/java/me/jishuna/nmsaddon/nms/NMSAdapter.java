package me.jishuna.nmsaddon.nms;

import java.util.Map;
import java.util.UUID;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.world.TerraWorld;

public interface NMSAdapter {
	public void registerListeners(TerraAddon addon, TerraPlugin plugin);

	public Map<UUID, TerraWorld> getWorldMap();

	public TerraWorld getWorld(UUID type);
}
