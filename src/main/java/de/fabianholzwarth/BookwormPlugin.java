package de.fabianholzwarth;


import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.block.Lectern;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BookwormPlugin extends JavaPlugin implements Listener {

    public static final int MAX_PREVIEW_DISTANCE = 3;

    protected BookshelfAnalyzer bookshelfAnalyzer = new BookshelfAnalyzer();
    protected LecternAnalyzer lecternAnalyzer = new LecternAnalyzer();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        BlockFace targetBlockFace = event.getPlayer().getTargetBlockFace(MAX_PREVIEW_DISTANCE, FluidCollisionMode.NEVER);
        Block targetBlock = event.getPlayer().getTargetBlockExact(MAX_PREVIEW_DISTANCE, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlockFace == null) {
            // the player is not looking at a valid block
            return;
        }

        BlockData targetBlockData = targetBlock.getBlockData();
        switch (targetBlockData.getMaterial()) {
            case CHISELED_BOOKSHELF -> {
                // the block is a chiseled bookshelf
                bookshelfAnalyzer.onUserTargetingBookshelf(
                        event.getPlayer(),
                        (Directional) targetBlockData,
                        targetBlockFace,
                        (ChiseledBookshelf) targetBlock.getState()
                );
            }
            case LECTERN -> {
                lecternAnalyzer.onUserTargetingLectern(
                        event.getPlayer(),
                        (Lectern) targetBlock.getState()
                );
            }
        }


    }


}