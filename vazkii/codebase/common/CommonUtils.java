package vazkii.codebase.common;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet3Chat;
import net.minecraftforge.common.EnumHelper;
import vazkii.um.common.UpdateManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class CommonUtils {

	static Random rand = new Random();

	public static boolean isOnline() {
		return UpdateManager.online;
	}

	@SideOnly(Side.CLIENT)
	public static Minecraft getMc() {
		return Minecraft.getMinecraft();
	}

	public static MinecraftServer getServer() {
		return MinecraftServer.getServer();
	}

	public static Side getSide() {
		return FMLCommonHandler.instance().getSidedDelegate().getSide();
	}

	public static int parseHexString(String stringToParse) {
		return Integer.parseInt(stringToParse, 16);
	}

	public static boolean flipBoolean(boolean b) {
		return !b;
	}

	public static int nextIntMinMax(int min, int max) {
		return rand.nextInt(max - min) + 1 + min;
	}

	public static String getEntityName(Entity entity) {
		return EntityList.getEntityString(entity);
	}

	public static boolean isEntityJumping(EntityLiving entity) {
		return ReflectionHelper.getPrivateValue(EntityLiving.class, entity, 75);
	}

	public static int getEntityXPValue(EntityLiving entity) {
		return ReflectionHelper.getPrivateValue(EntityLiving.class, entity, 35);
	}

	public static void registerNewToolMaterial(String name, int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) {
		EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiencyOnProperMaterial, damageVsEntity, enchantability);
	}

	public static void registerNewArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability) {
		EnumHelper.addArmorMaterial(name, maxDamageFactor, damageReductionAmounts, enchantability);
	}

	public static <C extends Enum> C getEnumConstant(String name, Class<? extends C> clazz) {
		for (C constant : clazz.getEnumConstants())
			if (constant.name().matches(name)) return constant;

				return null;
	}

	public static void sendChatMessage(EntityPlayer player, String message) {
		sendChatMessage(player, message, false);
	}

	public static void sendChatMessage(EntityPlayer player, String message, boolean op) {
		if (!op || getServer().getConfigurationManager().areCommandsAllowed(player.username)) {
			Packet3Chat chatPacket = new Packet3Chat(message);
			EntityPlayerMP mpPlayer = getServer().getConfigurationManager().getPlayerForUsername(player.username);

			if (player != null) mpPlayer.serverForThisPlayer.sendPacketToPlayer(chatPacket);
		}
	}

	public static void moveEntityTowardsPos(Entity entity, double x, double y, double z, double speed) {
		double xMotion = (x - entity.posX) / speed;
		double yMotion = (y - entity.posY) / speed;
		double zMotion = (z - entity.posZ) / speed;
		double rootMotion = Math.sqrt(xMotion * xMotion + yMotion * yMotion + zMotion * zMotion);
		double negativeRootMotion = 1.0D - rootMotion;

		if (negativeRootMotion > 0.0D) {
			negativeRootMotion *= negativeRootMotion;
			entity.motionX += xMotion / rootMotion * negativeRootMotion * 0.1D;
			entity.motionY += yMotion / rootMotion * negativeRootMotion * 0.1D;
			entity.motionZ += zMotion / rootMotion * negativeRootMotion * 0.1D;
		}
	}

	public static void moveEntityAwayFromPos(Entity entity, double x, double y, double z, double speed) {
		moveEntityTowardsPos(entity, x, y, z, speed);
		entity.motionX = -entity.motionX;
		entity.motionY = 0;
		entity.motionZ = -entity.motionZ;
	}
	
    public static boolean areStacksEqualIgnoreSize(ItemStack stack1, ItemStack stack2) {
        return stack1.itemID == stack2.itemID && (stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == -1 || stack2.getItemDamage() == -1);
    }

}
