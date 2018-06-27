/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.BetterWings;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author James
 */
public class BetterWings extends JavaPlugin {

    static boolean minigame = false;
    static HashSet<String> effectOff = new HashSet<>();

    public static boolean isGliding(Player player) {
        if (player == null) {
            return false;
        }
        if (player.getInventory().getChestplate() == null || !LolnetWings.vailidateWings(player.getInventory().getChestplate())) {
            return false;
        }
        return !player.isFlying() && !player.isOnGround()
                && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
                && player.getInventory().getChestplate().getType() == (Material.ELYTRA)
                && (Math.abs(player.getVelocity().getX()) > Math.abs(player.getVelocity().getY())
                || Math.abs(player.getVelocity().getZ()) > Math.abs(player.getVelocity().getY()));
    }

    @Override
    public void onEnable() {
        LolnetWings lolnetWings = new LolnetWings();
        getServer().getPluginManager().registerEvents(new MyListener(this), this);
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    public void reload() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("BetterWings")) {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                player.getInventory().addItem(LolnetWings.BetterWings);
                effectOff.add(sender.getName());
                return true;
            }
            
        } else if (command.getName().equalsIgnoreCase("BetterWings_old")) {

            /*if (args.length > 0) {
            if (args[0].equalsIgnoreCase("tnt")) {
            if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            location.setY(location.getY() - 2);
            final Bat myBat = (Bat) location.getWorld().spawnEntity(location, EntityType.BAT);
            final TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
            tnt.setFuseTicks(5 * 20);
            tnt.setYield(20);
            myBat.setPassenger(tnt);
            myBat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20000000, 20));
            new ThreadedFloatingEntity(myBat, tnt);
            }
            } else if (args[0].equalsIgnoreCase("pot")) {
            Player player = (Player) sender;
            Location location = player.getLocation().clone();
            Random r = new Random();
            location.setY(location.getY() - 2);
            Location to = new Location(location.getWorld(), location.getX() + (r.nextDouble() * 2 - 1),
            location.getY() + (r.nextDouble() * 2 - 1),
            location.getZ() + (r.nextDouble() * 2 - 1));
            final Bat myBat = (Bat) location.getWorld().spawnEntity(to, EntityType.BAT);
            ItemStack pot = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta = (PotionMeta) pot.getItemMeta();
            meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 20*10, 137), true);
            final Item item = location.getWorld().dropItem(to,pot );
            
            item.setPickupDelay(5*20);
            myBat.setPassenger(item);
            myBat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20000000, 20));
            new ThreadedFloatingEntity(myBat, item);
            }
            } else if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation().clone();
            Random r = new Random();
            location.setY(location.getY() - 2);
            for (int i = 0; i < 30; i++) {
            Location to = new Location(location.getWorld(), location.getX() + (r.nextDouble() * 2 - 1),
            location.getY() + (r.nextDouble() * 2 - 1),
            location.getZ() + (r.nextDouble() * 2 - 1));
            final Bat myBat = (Bat) location.getWorld().spawnEntity(to, EntityType.BAT);
            final Item item = location.getWorld().dropItem(to, new ItemStack(Material.DIAMOND_BLOCK));
            myBat.setPassenger(item);
            myBat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20000000, 20));
            new ThreadedFloatingEntity(myBat, item);
            }
            
            }*/
        } else if (command.getName().equalsIgnoreCase("wingeffect")) {
            if (sender instanceof Player) {
                if (effectOff.contains(sender.getName()))
                {
                    effectOff.remove(sender.getName());
                    sender.sendMessage("Effect is on");
                }
                else
                {
                    effectOff.add(sender.getName());
                    sender.sendMessage("Effect is off");
                }
            } else {

            }
        } else if (command.getName().equalsIgnoreCase("WingSpeed"))
        {
             if (sender instanceof Player) {
                 if (args.length > 0)
                 {
                     double speed;
                     try {
                         speed = Double.parseDouble(args[0]);
                     } catch (Exception e) {
                         sender.sendMessage("Must be a number. e.g. /wingspeed 1.2");
                         return true;
                     }
                     if (speed < 0)
                     {
                         sender.sendMessage("Too low");
                         return true;
                     }
                     if (speed <= 1.5 || sender.isOp() && speed <= 30)
                     {
                         if (args.length >= 2 && sender.isOp())
                         {
                             String playerName = args[1];
                         }
                         MyListener.targetSpeedMap.put(sender.getName(), speed);
                         sender.sendMessage("Speed is now: " + speed);
                     }
                     else
                     {
                         sender.sendMessage("Too high!");
                         return true;
                     }
                     
                 }
             }
        }
        return true;
    }

    public static class ThreadedFloatingEntity implements Runnable {

        final LivingEntity bat;
        final Entity entity;
        final Location location;
        long maxAge = 5 * 1000;
        long startAge;

        public ThreadedFloatingEntity(LivingEntity bat, Entity entity) {
            startAge = System.currentTimeMillis();
            this.entity = entity;
            this.bat = bat;
            location = bat.getLocation().clone();
            start();
        }

        private void start() {
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {

            while (!bat.isDead() && !entity.isDead() && System.currentTimeMillis() - startAge <= maxAge) {
                Vector velocity = bat.getVelocity();
                velocity.setX(-velocity.getX());
                velocity.setY(-velocity.getY());
                velocity.setZ(-velocity.getZ());
                bat.setVelocity(velocity);
                bat.teleport(location);
                try {
                    Thread.sleep(40);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BetterWings.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    if (item.getItemStack().getAmount() > 1) {
                        ItemStack itemStack = item.getItemStack();
                        itemStack.setAmount(1);
                        item.setItemStack(itemStack);
                    }
                }
            }
            entity.remove();
            bat.remove();
        }

    }

}
