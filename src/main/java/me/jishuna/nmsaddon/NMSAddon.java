package me.jishuna.nmsaddon;

import org.bukkit.Bukkit;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.injection.annotations.Inject;

import me.jishuna.nmsaddon.nms.NMSAdapter;

@Addon("NMS-Addon")
@Author("jishuna")
@Version("0.1.0")
public class NMSAddon extends TerraAddon implements EventListener {

	private static NMSAdapter adapter;

	@Inject
	private TerraPlugin plugin;

	private static NMSAddon instance;

	@Override
	public void initialize() {
		instance = this;
		initializeNMSAdapter();
		adapter.registerListeners(this, plugin);
	}

	private void initializeNMSAdapter() {
		String version = getServerVersion();

		try {
			adapter = (NMSAdapter) Class.forName("me.jishuna.nmsaddon.nms." + version + ".NMSAdapter_" + version)
					.getDeclaredConstructor().newInstance();
			plugin.logger().info("Version detected: " + version);
		} catch (ReflectiveOperationException e) {
			plugin.logger().severe("Server version \"" + version + "\" is unsupported! Please check for updates.");
		}
	}

	private String getServerVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public static NMSAddon getInstance() {
		return instance;
	}

	public TerraPlugin getPlugin() {
		return plugin;
	}
}
