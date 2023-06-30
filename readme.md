# Bookworm
A Minecraft Paper plugin for better usability of the lectern and chiseled bookshelves.

## Installation
To install this plugin, download the plugin for your minecraft version and follow the paper setup:
https://docs.papermc.io/paper/adding-plugins
The Plugin is based on Java 17, Paper for minecraft 1.20

## Features

1. Chiseled Bookshelves will show a preview of the targeted book in the users selected language. It is always prefixed by the slot, the item is palced in. This can be one of the following variants:
   - Normal book: `Book` or the displayName, if it was renamed
   - Enchanted book: `Enchanted Book (<first Enchantment>)` or `Enchanted Book (<first Enchantment>, ...)`, if it has multiple enchantments stored
   - Book and quill: `Book and Quill` or the displayName, if it was renamed
   - Written book: `<bookTitle> (by <playerName>)`
   - Any other item: `Suspicious book` (this is only possible by using commands or other plugins)
2. Lecterns will show a preview of book, using the same method as described for the chiseled bookshelves (but only written books will be available in minecraft vanilla. Refer to "suspicious book"!). 

All messages will be displayed to the user as described in the actionbar for a few seconds. The message will only trigger after the player moved or rotated his head. 