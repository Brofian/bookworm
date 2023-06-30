package de.fabianholzwarth;

import com.destroystokyo.paper.Title;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;


import java.util.Set;

public class BookwormPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // todo

        BlockFace targetBlockFace = event.getPlayer().getTargetBlockFace(3, FluidCollisionMode.NEVER);
        Block targetBlock =  event.getPlayer().getTargetBlockExact(3, FluidCollisionMode.NEVER);
        if(targetBlock == null || targetBlockFace == null) {
            // the player is not looking at a valid block
            return;
        }

        BlockData targetBlockData = targetBlock.getBlockData();
        Material targetBlockMaterial = targetBlockData.getMaterial();
        BlockState targetBlockState = targetBlock.getState();
        if(targetBlockMaterial != Material.CHISELED_BOOKSHELF
                || !(targetBlockState instanceof ChiseledBookshelf bookshelfBlockState)
                || !(targetBlockData instanceof Directional directionalBlockData)) {
            // the block is not a chiseled bookshelf
            return;
        }


        Vector blockFacingDirection = directionalBlockData.getFacing().getDirection();
        Vector playerTargetFaceOrientation = targetBlockFace.getDirection();
        if(!blockFacingDirection.equals(playerTargetFaceOrientation)) {
            // player is not looking at the correct face of the block
            return;
        }

        ChiseledBookshelfInventory inventory = bookshelfBlockState.getInventory();
        ItemStack[] itemStack = inventory.getContents();


        int targetedSlot = this.getTargetedSlot(event.getPlayer(), blockFacingDirection);
        if(targetedSlot < 0 || targetedSlot > 5) {
            return;
        }
        if(itemStack.length > targetedSlot && itemStack[targetedSlot] != null) {
            event.getPlayer().sendActionBar(Component.text("Book: "+ targetedSlot + ": " + this.getTitleFromItemStack(itemStack[targetedSlot])));
        }
        else {
            // reset book title
            event.getPlayer().sendActionBar(Component.text(""));
        }
    }

    private int getTargetedSlot(Player player, Vector facing) {
        RayTraceResult rayTraceResult = player.rayTraceBlocks(3, FluidCollisionMode.NEVER);
        if(rayTraceResult == null) {
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
        boolean isLeftCol  = isVerticalInverted ? relativeVertical < 0.33d : relativeVertical > 0.66d;

        return (isTopRow ? 0 : 3) + (isLeftCol ? 0 : 1) + (isRightCol ? 1 : 0);
    }

    private String getTitleFromItemStack(ItemStack stack) {

        switch(stack.getType()) {
            case WRITTEN_BOOK:
                BookMeta bookMeta = (BookMeta)stack.getItemMeta();
                return "Written book: " + bookMeta.getTitle();
            case WRITABLE_BOOK:
                return "Unfinished book";
            case BOOK:
                return "Empty book";
            case ENCHANTED_BOOK:
                // todo: fix this
                ItemMeta enchantedBookMeta = stack.getItemMeta();

                Set<Enchantment> enchants = enchantedBookMeta.getEnchants().keySet();
                System.out.println(enchantedBookMeta);
                if(enchants.size() == 0) {
                    return "Enchanted book";
                }

                Enchantment firstEnchantment = enchants.stream().findFirst().get();
                String enchantmentList = firstEnchantment + (enchants.size() > 1 ? ",..." : "");
                return "Enchanted book ("+enchantmentList+")";
        }


        return "todo! No case for material " + stack.getType().name();
    }



}