package de.fabianholzwarth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public class ChatHelper {

    public static Component translated(String key) {
        return translated(key, new Component[0]);
    }

    public static Component translated(String key, Component[] args) {
        TranslatableComponent.Builder translation = Component.translatable().key(key);
        if (args.length > 0) {
            translation.args(args);
        }
        return translation.build();
    }

    public static Component text(String text) {
        return Component.text(text);
    }

    /**
     * Merge an array of components into one single component
     */
    public static Component combineMessageComponents(Component[] components) {
        Component result = Component.text("");

        for (Component component : components) {
            if (component != null) {
                result = result.append(component);
            }
        }

        return result;
    }

}
