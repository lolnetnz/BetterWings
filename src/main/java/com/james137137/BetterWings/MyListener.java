/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james137137.BetterWings;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import static com.james137137.BetterWings.BetterWings.effectOff;
import org.bukkit.block.BlockFace;

/**
 *
 * @author James
 */
class MyListener implements Listener {

    BetterWings plugin;
    double defaultTargetSpeed = 1.2;
    double MaxSpeed = 50;
    public static HashMap<String, Double> targetSpeedMap = new HashMap<>();
    HashMap<String, Long> lastGliding = new HashMap<>();
    HashMap<String, Long> lastBoost = new HashMap<>();
    HashMap<String, Long> superBooster = new HashMap<>();
    HashMap<String, Location> lastLocation = new HashMap<>();
    HashMap<String, Long> lastLocationChangePY = new HashMap<>();

    public MyListener(BetterWings aThis) {
        plugin = aThis;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player2 = (Player) entity;
            if (BetterWings.isGliding(player2)) {
                double speed = getSpeed(player2);
                if (speed > 0.4) {
                    setSpeed(player2, speed, 0.1 * speed);
                }
            }

        }
    }

    @EventHandler
    public void onMoveWithEffect(PlayerMoveEvent event) {
        Long lastGidingTime = lastGliding.get(event.getPlayer().getName());
        if (lastGidingTime != null && System.currentTimeMillis() - lastGidingTime <= 1000L) {

            Player player = event.getPlayer();
            Double targetSpeed = targetSpeedMap.get(player.getName());
            if (targetSpeed == null) {
                targetSpeed = defaultTargetSpeed;
                targetSpeedMap.put(player.getName(), targetSpeed);
            }
            if (!effectOff.contains(event.getPlayer().getName())) {
                Location location = player.getLocation();
                if (getSpeed(player) >= 0.85 * targetSpeed) {
                    Random r = new Random();
                    for (int i = 0; i < 3; i++) {
                        Location to = new Location(location.getWorld(),
                                location.getX() + (r.nextDouble() * 1 - 0.5),
                                location.getY() + (r.nextDouble() * 1 - 2.5),
                                location.getZ() + (r.nextDouble() * 1 - 0.5));
                        player.getWorld().playEffect(to, Effect.MOBSPAWNER_FLAMES, 0);
                    }

                } else {
                    Random r = new Random();
                    for (int i = 0; i < 2; i++) {
                        Location to = new Location(location.getWorld(),
                                location.getX() + (r.nextDouble() * 1 - 0.5),
                                location.getY() + (r.nextDouble() * 1 - 0.5),
                                location.getZ() + (r.nextDouble() * 1 - 0.5));
                        player.getWorld().playEffect(to, Effect.SMOKE, 0);
                    }

                }
            }

            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && player.isSneaking()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, (int) (targetSpeed * 5.0), 50, false, false));
            }

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        effectOff.remove(event.getPlayer().getName());
        lastLocation.remove(event.getPlayer().getName());
        targetSpeedMap.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getX() != from.getX() || to.getZ() != from.getZ()) {
            if (BetterWings.isGliding(player)) {
                lastGliding.put(player.getName(), System.currentTimeMillis());
                double speed = getSpeed(player);
                Double targetSpeed = targetSpeedMap.get(player.getName());
                if (targetSpeed == null) {
                    targetSpeed = defaultTargetSpeed;
                    targetSpeedMap.put(player.getName(), targetSpeed);
                }
                Location location = player.getLocation();

                if (player.getLocation().getY() < 250) {
                    if (speed > 0.3 && AFKCheck(player)) {
                        if (System.currentTimeMillis() - lastLocationChangePY.get(player.getName()) >= 10 * 1000) {
                            float pitch = player.getLocation().getPitch();
                            if (pitch < 45) {
                                setSpeed(player, getSpeed(player), 0.9 * getSpeed(player));
                            }

                        }
                        return;
                    }

                    if (speed > 0.5 || speed > targetSpeed * 0.5) {

                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.SLOW)) {
                                setSpeed(player, speed, 0.1 * speed);
                                return;
                            }
                        }
                        if (speed <= 0.8 * targetSpeed) {
                            setSpeed(player, speed, 1.05 * speed);
                        } else if (speed <= targetSpeed && location.getPitch() > -45F) {
                            setSpeed(player, speed, 1.025 * speed);
                        }

                    }

                    //event.getPlayer().getWorld().spawnParticle(Particle.DRIP_LAVA, from, 20);
                    //event.getPlayer().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, from, 20);
                } else {
                    setSpeed(player, speed, 0.5 * speed);
                }

                if (player.getLocation().getY() <= 100) {
                    player.getInventory().getChestplate().setDurability((short) 0);

                }

            }
        }

    }

    private double getSpeed(Player player) {
        Vector vel = player.getVelocity();
        return Math.sqrt(Math.pow(vel.getX(), 2) + Math.pow(vel.getY(), 2) + Math.pow(vel.getZ(), 2));
    }

    private void setSpeed(Player player, double speed, double targetSpeed) {
        Vector vel = player.getVelocity();
        /*double pitch = player.getLocation().getPitch();
        if (pitch < 0) {
        vel.setY((1 + (pitch / 360)) * targetSpeed * vel.getY() / speed);
        }*/
        vel.setX(targetSpeed * vel.getX() / speed);

        vel.setZ(targetSpeed * vel.getZ() / speed);
        player.setVelocity(vel);
    }

    public boolean AFKCheck(Player player) {
        Location location = player.getLocation().clone();
        Location location2 = lastLocation.get(player.getName());
        if (location2 == null) {
            lastLocation.put(player.getName(), location);
            lastLocationChangePY.put(player.getName(), System.currentTimeMillis());
            return false;
        }
        //System.out.println(System.currentTimeMillis() - lastLocationChangePY.get(player.getName()));

        if (location.getYaw() == location2.getYaw() && location2.getPitch() == location.getPitch()) {
            if (System.currentTimeMillis() - lastLocationChangePY.get(player.getName()) >= 5 * 1000) {
                return true;
            } else {
                return false;
            }
        }
        lastLocation.put(player.getName(), location);
        lastLocationChangePY.put(player.getName(), System.currentTimeMillis());

        return false;
    }

}
