package de.fabianholzwarth;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.LecternInventory;

public class LecternAnalyzer extends AbstractBookAnalyzer {

    public void onUserTargetingLectern(
            Player player,
            Lectern blockState
    ) {
        LecternInventory inventory = (LecternInventory) blockState.getInventory();
        Component[] description = this.getSlotDescription(inventory, 0);
        if (description.length < 1) {
            return;
        }

        player.sendActionBar(ChatHelper.combineMessageComponents(description));
    }

}
