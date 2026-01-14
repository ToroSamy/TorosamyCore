package net.torosamy.torosamyCore.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagInt;

public class NbtUtil {

    private static NBTTagCompound getOrCreateCustomDataCompound(ItemStack itemStack) {
        CustomData data = CraftItemStack.asNMSCopy(itemStack).a().a(DataComponents.b);
        return data == null ? new NBTTagCompound() : data.d();
    }


    private static void setNbtToItem(ItemStack itemStack, NBTTagCompound compound) {
        var nms = CraftItemStack.asNMSCopy(itemStack);
        nms.b(DataComponents.b, CustomData.a(compound));
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nms));
    }


    public static void setString(ItemStack itemStack, final String keyword, final String value) {
        NBTTagCompound compound = getOrCreateCustomDataCompound(itemStack);

        compound.a(keyword, NBTTagString.a(value));

        setNbtToItem(itemStack, compound);
    }

    public static void setInteger(ItemStack itemStack, final String keyword, final Integer value) {
        NBTTagCompound compound = getOrCreateCustomDataCompound(itemStack);

        compound.a(keyword, NBTTagInt.a(value));

        setNbtToItem(itemStack, compound);
    }


    public static String getString(final ItemStack itemStack, final String keyword) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CustomData data = nmsItem.a().a(DataComponents.b);

        if (data == null) {
            return null;
        }
        NBTTagCompound compound = data.d();

        NBTBase base = compound.a(keyword);
        if (base instanceof NBTTagString) {
            return ((NBTTagString) base).k();
        }
        return null;
    }

    public static int getInteger(final ItemStack itemStack, final String keyword) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CustomData data = nmsItem.a().a(DataComponents.b);

        if (data == null) {
            return 0;
        }
        NBTTagCompound compound = data.d();

        NBTBase base = compound.a(keyword);
        if (base instanceof NBTTagInt) {
            return ((NBTTagInt) base).h();
        }
        return 0;
    }
}