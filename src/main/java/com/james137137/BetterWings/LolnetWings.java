/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.BetterWings;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author James
 */
public class LolnetWings {

    static ItemStack BetterWings;
    protected static final String NAME = ChatColor.AQUA + "Lolnet Wings";

    public LolnetWings() {
        BetterWings = new ItemStack(Material.ELYTRA, 1);
        ItemMeta meta = BetterWings.getItemMeta();
        meta.setDisplayName(NAME);
        BetterWings.setItemMeta(meta);
        try {
            //nz.co.lolnet.lolnetapi.lolcon.LolConSign.addCustomItem("LolnetWings", BetterWings);
        } catch (Exception e) {
            Bukkit.getServer().getLogger().warning("Missing LolCon for BetterWings... but that's ok!");
        }
        
    }

    public static boolean vailidateWings(ItemStack item) {
        try {
            return item.getItemMeta().getDisplayName().equals(NAME);
        } catch (NullPointerException e) {
            return false;
        }

    }

    public static String getOwner(ItemStack item) {
        if (item.getItemMeta() != null) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore != null) {
                for (String string : lore) {
                    return ChatColor.stripColor(string);
                }
            }
        }
        return "";
    }

    public static ItemStack setOwner(ItemStack item, String player) {
        if (item.getItemMeta() != null) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + player);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
