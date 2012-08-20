package vazkii.codebase.client;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import vazkii.codebase.common.CommonUtils;

public final class CornerText {

	private static List<ICornerTextHandler> cornerTextHandlers = new LinkedList();

	public static boolean registerCornerTextHandler(ICornerTextHandler handler) {
		return cornerTextHandlers.add(handler);
	}

	private static List<CornerTextEntry> updateCornerText() {
		List<CornerTextEntry> foundEntries = new LinkedList();

		for (ICornerTextHandler handler : cornerTextHandlers) {
			List<CornerTextEntry> handlerEntries = handler.updateCornerText();

			if (handlerEntries != null && !handlerEntries.isEmpty()) foundEntries.addAll(handlerEntries);
		}

		return foundEntries;
	}

	public static void onTick() {
		Minecraft mc = CommonUtils.getMc();

		int y = 2;
		int x = 2;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (CornerTextEntry entry : updateCornerText()) {
			mc.fontRenderer.drawStringWithShadow(entry.text, x, y, entry.color);
			y += 14;
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
