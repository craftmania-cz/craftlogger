package cz.craftmania.logger.listeners.internal

import cz.craftmania.logger.Main
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.projectiles.BlockProjectileSource
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class PlayerDeathLogListener: Listener {

    val inventoryCache = arrayListOf<Pair<Player, PlayerInventory>>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDeath(event: EntityDamageEvent) {
        if (event.entity !is Player) return
        if (event.isCancelled) return

        val player = event.entity as Player
        
        if (player.health - event.finalDamage <= 0) {

            when (event.cause) {
                DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_SWEEP_ATTACK -> {
                    if (player.killer is Player) { // Zabit hráčem
                        val killerPlayer: Player? = (event.entity as Player).killer
                        if (killerPlayer !== null) {
                            this.createLog(player, "PLAYER_KILL", player.location, killerPlayer.name)
                        }
                    } else { // Jinak smrt entitou
                        val damageEventByEntity = event as EntityDamageByEntityEvent
                        if (damageEventByEntity.damager is Player) {
                            val killerPlayer: Player? = (event.damager as Player).killer
                            if (killerPlayer !== null) {
                                this.createLog(player, "PLAYER_KILL", player.location, killerPlayer.name)
                            }
                        } else {
                            this.createLog(player, "ENTITY_KILL", player.location, damageEventByEntity.damager.type.name)
                        }
                    }
                }
                else -> {
                    // Zabití kaktus, lava atd
                    this.createLog(player, event.cause.name, player.location, null)
                }
            }

            // Když je hráč zabit jinou entitou?
            if (event is EntityDamageByEntityEvent) {
                val damager = event.damager
                if (damager is Projectile) {
                    val shooter = damager.shooter
                    if (shooter is LivingEntity) {
                        if (shooter is Skeleton) {
                            this.createLog(player, "ENTITY_KILL", player.location, "SKELETON")
                        }
                        if (shooter is Pillager) {
                            this.createLog(player, "ENTITY_KILL", player.location, "PILLAGER")
                        }
                        if (shooter is Player) {
                            this.createLog(player, "PLAYER_KILL_BOW", player.location, shooter.player?.name)
                        }
                    } else if (shooter is BlockProjectileSource) {
                        val shooterBlock: BlockProjectileSource = shooter
                        this.createLog(player, "PROJECTILE_SOURCE", player.location, shooterBlock.block.type.name)
                    }
                }
            }
        }
    }

    private fun createLog(player: Player, deathType: String, location: Location, killerName: String?) {
        var entityKillerName = "Unknown"
        if (killerName != null) {
            entityKillerName = killerName
        }

        val playerInventoryJson: String = this.preparePlayerInventoryIsJSON(player.inventory)
        val hasTotem: Boolean = player.inventory.containsAtLeast(ItemStack(Material.TOTEM_OF_UNDYING), 1)
        val locationAsJson: String = prepareJsonLocation(location)

        // Save log
        Main.instance!!.sQL!!.createDataLog(player, "deaths", deathType, locationAsJson, entityKillerName, playerInventoryJson, hasTotem, System.currentTimeMillis())
    }

    private fun prepareJsonLocation(location: Location): String {
        val locationObject = JSONObject()
        locationObject["world"] = location.world?.name
        locationObject["x"] = location.x
        locationObject["y"] = location.y
        locationObject["z"] = location.z
        locationObject["yaw"] = location.yaw
        locationObject["pitch"] = location.pitch
        return locationObject.toString()
    }

    /**
     * Nice, pohezkaný json itemstack
     */
    private fun preparePlayerInventoryIsJSON(inventory: PlayerInventory): String {
        val inventoryObject = JSONObject()
        // Obsah itemů v inventáři
        val playerInventoryContent = JSONObject()
        inventory.contents.forEachIndexed { index, itemStack -> //TODO: Změnit content na něco jinýho?
            run {
                if (itemStack != null && itemStack.type != Material.AIR) {
                    playerInventoryContent["$index"] = prepareItemStackToJson(itemStack)
                }
            }
        }
        inventoryObject["content"] = playerInventoryContent

        // Obsah itemů v armor slotech
        val playerArmorContent = JSONArray()
        inventory.armorContents.forEachIndexed { index, itemStack ->
            run {
                if (itemStack != null && itemStack.type != Material.AIR) {
                    playerArmorContent.add(prepareItemStackToJson(itemStack))
                }
            }
        }
        inventoryObject["armor"] = playerArmorContent
        return inventoryObject.toString()
    }

    private fun prepareItemStackToJson(itemStack: ItemStack): JSONObject {
        val slotObject = JSONObject()
        slotObject["material"] = itemStack.type
        slotObject["amount"] = itemStack.amount
        slotObject["durability"] = itemStack.durability.toInt() //TODO: Vrací pořád 0?
        if (itemStack.hasItemMeta()) {
            slotObject["item_name"] = itemStack.itemMeta!!.displayName
            if (itemStack.itemMeta!!.hasCustomModelData()) {
                slotObject["custom_meta_data"] = itemStack.itemMeta!!.customModelData
            }
            val itemStackLore = JSONObject()
            if (itemStack.itemMeta!!.hasLore()) {
                itemStack.itemMeta!!.lore!!.forEachIndexed { index, string ->
                    run {
                        itemStackLore[index] = string
                    }
                }
            }
            slotObject["lore"] = itemStackLore
            val itemFlags = JSONArray()
            if (itemStack.itemMeta!!.itemFlags.size >= 1) {
                itemStack.itemMeta!!.itemFlags.forEachIndexed { index, itemFlag ->
                    run {
                        itemFlags.add(index, itemFlag.name)
                    }
                }
            }
            slotObject["flags"] = itemFlags
            val itemEnchantments = JSONArray()
            if (itemStack.itemMeta!!.hasEnchants()) {
                itemStack.itemMeta!!.enchants.forEach { (enchant, value) ->
                    run {
                        val enchantObject = JSONObject()
                        enchantObject[enchant.key] = value
                        itemEnchantments.add(enchantObject)
                    }
                }
            }
            slotObject["enchantments"] = itemEnchantments
        }
        return slotObject
    }
}