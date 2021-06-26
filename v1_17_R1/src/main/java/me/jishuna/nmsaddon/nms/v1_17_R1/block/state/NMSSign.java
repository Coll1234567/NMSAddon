package me.jishuna.nmsaddon.nms.v1_17_R1.block.state;

import java.lang.reflect.Field;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.platform.block.state.Sign;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.RegionLimitedWorldAccess;
import net.minecraft.world.level.block.entity.TileEntitySign;

public class NMSSign extends NMSBlockState implements Sign {

	private static Field textArray;

	static {
		try {
			textArray = TileEntitySign.class.getDeclaredField("e");
			textArray.setAccessible(true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	private final TileEntitySign delegate;
	private IChatBaseComponent[] text;

	protected NMSSign(RegionLimitedWorldAccess world, BlockPosition position) {
		super(world, position);
		this.delegate = (TileEntitySign) world.getTileEntity(position);

		try {
			this.text = (IChatBaseComponent[]) textArray.get(this.delegate);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public @NotNull String getLine(int line) throws IndexOutOfBoundsException {
		return text[line].getText();
	}

	@Override
	public @NotNull String[] getLines() {
		String[] lines = new String[text.length];
		for (int i = 0; i < text.length; i++) {
			lines[i] = text[i].getText();
		}
		return lines;
	}

	@Override
	public void setLine(int line, @NotNull String text) throws IndexOutOfBoundsException {
		this.delegate.a(line, new ChatComponentText(text));

	}
}
