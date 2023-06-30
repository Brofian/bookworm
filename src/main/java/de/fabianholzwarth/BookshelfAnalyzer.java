package de.fabianholzwarth;

import net.kyori.adventure.text.Component;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BookshelfAnalyzer extends AbstractBookAnalyzer {

    public void onUserTargetingBookshelf(
            Player player,
            Directional directionalBlockData,
            BlockFace targetBlockFace,
            ChiseledBookshelf bookshelfBlockState) {
        Vector blockFacingDirection = directionalBlockData.getFacing().getDirection();
        Vector playerTargetFaceOrientation = targetBlockFace.getDirection();
        if (!blockFacingDirection.equals(playerTargetFaceOrientation)) {
            // player is not looking at the correct face of the block
            return;
        }

        ChiseledBookshelfInventory inventory = bookshelfBlockState.getInventory();
        int targetedSlot = this.getTargetedSlot(player, blockFacingDirection);
        Component[] description = this.getSlotDescription(inventory, targetedSlot, (targetedSlot + 1) + ": ");

        if (description.length < 1) {
            return;
        }

        player.sendActionBar(ChatHelper.combineMessageComponents(description));
    }


    /**
     * use ray tracing to calculate the relative position of the block, the user is looking at
     */
    private int getTargetedSlot(Player player, Vector facing) {
        RayTraceResult rayTraceResult = player.rayTraceBlocks(BookwormPlugin.MAX_PREVIEW_DISTANCE, FluidCollisionMode.NEVER);
        if (rayTraceResult == null) {
            return -1;
        }


        Vector position = rayTraceResult.getHitPosition();

        double relativeY = position.getY() - position.getBlockY();
        boolean isTopRow = relativeY > 0.5;

        // either x or z will always be zero
        boolean isFacingX = facing.getX() != 0;
        double relativeVertical = isFacingX ? (position.getZ() - position.getBlockZ()) : (position.getX() - position.getBlockX());
        boolean isVerticalInverted = ((isFacingX ? facing.getX() : facing.getZ()) < 0) ^ (!isFacingX);
        boolean isRightCol = isVerticalInverted ? relativeVertical > 0.66d : relativeVertical < 0.33d;
        boolean isLeftCol = isVerticalInverted ? relativeVertical < 0.33d : relativeVertical > 0.66d;

        return (isTopRow ? 0 : 3) + (isLeftCol ? 0 : 1) + (isRightCol ? 1 : 0);
    }


}
