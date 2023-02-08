package com.reussy.development.setranks.plugin.menu.element;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.reussy.development.setranks.plugin.SetRanksPlugin;
import com.reussy.development.setranks.plugin.config.ConfigManager;
import com.reussy.development.setranks.plugin.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class to build the elements.
 */
public class ElementBuilder {

    private final SetRanksPlugin plugin;

    public ElementBuilder(SetRanksPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createFromSection(@NotNull ConfigurationSection section) {
        String material = section.getString("material");
        String texture = section.getString("texture");
        String displayName = section.getString("display-name");
        List<String> description = section.getStringList("description");

        return this.createItemStack(material, texture, displayName, description, 1, null);
    }

    /**
     * Create a new item stack.
     *
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createFromSection(@NotNull ConfigurationSection section, String[][] placeholders) {
        String material = section.getString("material");
        String texture = section.getString("texture");
        String displayName = section.getString("display-name");
        List<String> description = section.getStringList("description");

        return this.createItemStack(material, texture, displayName, description, 1, placeholders);
    }

    /**
     * Create a new item stack with skull meta support and PlaceholderAPI support.
     *
     * @param name         The name of the player. If the player is online, the player will be used. If the player is offline, the offline player will be used.
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createFromSection(@NotNull String name, @NotNull ConfigurationSection section, String[][] placeholders) {
        String material = section.getString("material");
        String texture = section.getString("texture");
        String displayName = section.getString("display-name");
        List<String> description = section.getStringList("description");

        final Player player = Bukkit.getPlayer(name);
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(name);

        if (player != null) {
            return this.createItemStack(player, material, texture, displayName, description, 1, placeholders);
        } else if (offlinePlayer != null) {
            return this.createItemStack(offlinePlayer, material, texture, displayName, description, 1, placeholders);
        } else {
            return this.createItemStack(material, texture, displayName, description, 1, placeholders);
        }
    }

    /**
     * Create a new item stack with skull meta support and PlaceholderAPI support.
     *
     * @param player       The player.
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createFromSection(@NotNull Player player, @NotNull ConfigurationSection section, String[][] placeholders) {
        String material = section.getString("material");
        String texture = section.getString("texture");
        String displayName = section.getString("display-name");
        List<String> description = section.getStringList("description");

        return this.createItemStack(player, material, texture, displayName, description, 1, placeholders);
    }

    /**
     * Create a new item stack with skull meta support and PlaceholderAPI support.
     *
     * @param player       The offline player.
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createFromSection(@NotNull OfflinePlayer player, @NotNull ConfigurationSection section, String[][] placeholders) {
        String material = section.getString("material");
        String texture = section.getString("texture");
        String displayName = section.getString("display-name");
        List<String> description = section.getStringList("description");

        return this.createItemStack(player, material, texture, displayName, description, 1, placeholders);
    }

    /**
     * Create a new item stack with skull meta support.
     *
     * @param material     The material name.
     * @param texture      The texture.
     * @param displayName  The display name of the item.
     * @param description  The description of the item.
     * @param quantity     The quantity of the item.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createItemStack(String material, String texture, String displayName, List<String> description, int quantity, String[][] placeholders) {

        XMaterial xMaterial = XMaterial.matchXMaterial(material).orElse(XMaterial.STONE);
        ItemStack itemStack = xMaterial.parseItem();

        if (itemStack == null) {
            return new ItemStack(Material.STONE);
        }

        itemStack.setAmount(quantity);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta && (texture != null && !texture.isEmpty())) {

            SkullUtils.applySkin(skullMeta, texture);

            skullMeta.setDisplayName(Utils.colorize(displayName));
            skullMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(skullMeta);
        } else {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null) return itemStack;

            itemMeta.setDisplayName(Utils.colorize(displayName));
            itemMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(itemMeta);
        }
        return process(null, itemStack, placeholders);
    }

    /**
     * Create a new item stack with skull meta support and PlaceholderAPI support.
     *
     * @param player       The player.
     * @param material     The material name.
     * @param texture      The texture.
     * @param displayName  The display name of the item.
     * @param description  The description of the item.
     * @param quantity     The quantity of the item.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createItemStack(@NotNull Player player, String material, String texture, String displayName, List<String> description, int quantity, String[][] placeholders) {

        XMaterial xMaterial = XMaterial.matchXMaterial(material).orElse(XMaterial.STONE);
        ItemStack itemStack = xMaterial.parseItem();

        if (itemStack == null) {
            return new ItemStack(Material.STONE);
        }

        itemStack.setAmount(quantity);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta && (texture != null && !texture.isEmpty())) {

            if (texture.contains("{PLAYER_HEAD}")) {
                skullMeta.setOwningPlayer(player);
            } else {
                SkullUtils.applySkin(skullMeta, texture);
            }

            skullMeta.setDisplayName(Utils.colorize(displayName));
            skullMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(skullMeta);
        } else {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null) return itemStack;

            itemMeta.setDisplayName(Utils.colorize(displayName));
            itemMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(itemMeta);
        }
        return process(player, itemStack, placeholders);
    }

    /**
     * Create a new item stack with skull meta support and PlaceholderAPI support.
     *
     * @param player       The offline player.
     * @param material     The material name.
     * @param texture      The texture.
     * @param displayName  The display name of the item.
     * @param description  The description of the item.
     * @param quantity     The quantity of the item.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    public ItemStack createItemStack(@NotNull OfflinePlayer player, String material, String texture, String displayName, List<String> description, int quantity, String[][] placeholders) {

        XMaterial xMaterial = XMaterial.matchXMaterial(material).orElse(XMaterial.STONE);
        ItemStack itemStack = xMaterial.parseItem();

        if (itemStack == null) {
            return new ItemStack(Material.STONE);
        }

        itemStack.setAmount(quantity);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta && (texture != null && !texture.isEmpty())) {

            if (texture.contains("{PLAYER_HEAD}")) {
                skullMeta.setOwningPlayer(player);
            } else {
                SkullUtils.applySkin(skullMeta, texture);
            }

            skullMeta.setDisplayName(Utils.colorize(displayName));
            skullMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(skullMeta);
        } else {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null) return itemStack;

            itemMeta.setDisplayName(Utils.colorize(displayName));
            itemMeta.setLore(Utils.colorize(description));
            itemStack.setItemMeta(itemMeta);
        }
        return process(player, itemStack, placeholders);
    }

    /**
     * Process an existing item stack with placeholders.
     * {PLACEHOLDER}, {REPLACEMENT}
     *
     * @param player       The offline player.
     * @param itemStack    The item stack.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    @Contract("_, _, _ -> param2")
    private @NotNull ItemStack process(@Nullable Player player, @NotNull ItemStack itemStack, String[][] placeholders) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;
        List<String> lore = new ArrayList<>();

        if (placeholders != null) {
            Arrays.stream(placeholders).forEach(placeholder -> {
                if (itemMeta.getDisplayName().contains(placeholder[0])) {
                    itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName().replace(placeholder[0], placeholder[1])));
                }
            });
        }

        if (plugin.getPlaceholderAPI().isRunning() && player != null) {
            itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName()));
        }

        if (itemMeta.getLore() != null || !itemMeta.getLore().isEmpty()) {
            for (String line : itemMeta.getLore()) {

                if (placeholders != null) {
                    for (String[] placeholder : placeholders) {
                        if (line.contains(placeholder[0])) {
                            line = line.replace(placeholder[0], placeholder[1]);
                        }
                    }
                }

                if (plugin.getPlaceholderAPI().isRunning() && player != null) {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                }

                lore.add(line);
            }
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Process an existing item stack with placeholders.
     * {PLACEHOLDER}, {REPLACEMENT}
     *
     * @param player       The offline player.
     * @param itemStack    The item stack.
     * @param placeholders The placeholders.
     * @return The item stack.
     */
    @Contract("_, _, _ -> param2")
    private @NotNull ItemStack process(@Nullable OfflinePlayer player, @NotNull ItemStack itemStack, String[][] placeholders) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;
        List<String> lore = new ArrayList<>();

        if (placeholders != null) {
            Arrays.stream(placeholders).forEach(placeholder -> {
                if (itemMeta.getDisplayName().contains(placeholder[0])) {
                    itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName().replace(placeholder[0], placeholder[1])));
                }
            });
        }

        if (plugin.getPlaceholderAPI().isRunning()) {
            itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, itemMeta.getDisplayName()));
        }

        if (itemMeta.getLore() != null || !itemMeta.getLore().isEmpty()) {
            for (String line : itemMeta.getLore()) {

                if (placeholders != null) {
                    for (String[] placeholder : placeholders) {
                        if (line.contains(placeholder[0])) {
                            line = line.replace(placeholder[0], placeholder[1]);
                        }
                    }
                }

                if (plugin.getPlaceholderAPI().isRunning()) {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                }

                lore.add(line);
            }
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Set the custom item stack from the configuration section.
     *
     * @param baseGui The base gui. {@link dev.triumphteam.gui.guis.Gui} or {@link dev.triumphteam.gui.guis.PaginatedGui}
     * @param section The configuration section.
     */
    public void populateCustomItems(BaseGui baseGui, @NotNull ConfigManager configManager, @NotNull ConfigurationSection section) {

        if (section.getKeys(false).isEmpty()) return;

        for (String item : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(item);

            if (itemSection == null) continue;

            ItemStack itemStack = plugin.getElementBuilder().createFromSection(itemSection);

            setItem(baseGui, configManager, section, item, itemStack);
        }
    }

    /**
     * Set the custom item stack from the configuration section.
     * This method has player head and PlaceholderAPI support.
     *
     * @param name         The name of the player. If the player is online, the player will be used. Otherwise, the offline player will be used.
     * @param baseGui      The base gui. {@link dev.triumphteam.gui.guis.Gui} or {@link dev.triumphteam.gui.guis.PaginatedGui}
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     */
    public void populateCustomItems(@NotNull String name, BaseGui baseGui, @NotNull ConfigManager configManager, @Nullable ConfigurationSection section, String[][] placeholders) {

        if (section == null || section.getKeys(false).isEmpty()) return;

        for (String item : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(item);

            if (itemSection == null) continue;

            final Player player = Bukkit.getPlayer(name);
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(name);

            if (player != null) {
                ItemStack itemStack = this.createFromSection(player, itemSection, placeholders);
                setItem(baseGui, configManager, section, item, itemStack);
            } else if (offlinePlayer != null) {
                ItemStack itemStack = this.createFromSection(offlinePlayer, itemSection, placeholders);
                setItem(baseGui, configManager, section, item, itemStack);
            }
        }
    }

    /**
     * Set the custom item stack from the configuration section.
     * This method has player head and PlaceholderAPI support.
     *
     * @param player       The player.
     * @param baseGui      The base gui. {@link dev.triumphteam.gui.guis.Gui} or {@link dev.triumphteam.gui.guis.PaginatedGui}
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     */
    public void populateCustomItems(Player player, BaseGui baseGui, @NotNull ConfigManager configManager, @Nullable ConfigurationSection section, String[][] placeholders) {

        if (section == null || section.getKeys(false).isEmpty()) return;

        for (String item : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(item);

            if (itemSection == null) continue;

            ItemStack itemStack = this.createFromSection(player, itemSection, placeholders);

            setItem(baseGui, configManager, section, item, itemStack);
        }
    }

    /**
     * Set the custom item stack from the configuration section.
     * This method has player head and PlaceholderAPI support.
     *
     * @param player       The offline player.
     * @param baseGui      The base gui. {@link dev.triumphteam.gui.guis.Gui} or {@link dev.triumphteam.gui.guis.PaginatedGui}
     * @param section      The configuration section.
     * @param placeholders The placeholders.
     */
    public void populateCustomItems(OfflinePlayer player, BaseGui baseGui, @NotNull ConfigManager configManager, @Nullable ConfigurationSection section, String[][] placeholders) {

        if (section == null || section.getKeys(false).isEmpty()) return;

        for (String item : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(item);

            if (itemSection == null) continue;

            ItemStack itemStack = this.createFromSection(player, itemSection, placeholders);

            setItem(baseGui, configManager, section, item, itemStack);
        }
    }

    private void setItem(BaseGui baseGui, @NotNull ConfigManager configManager, @NotNull ConfigurationSection section, String item, ItemStack itemStack) {
        if (configManager.getList(section.getCurrentPath() + "." + item, "position").size() > 1) {
            for (String position : configManager.getList(section.getCurrentPath() + "." + item, "position")) {
                baseGui.setItem(Integer.parseInt(position), ItemBuilder.from(itemStack).asGuiItem());
            }
        } else if (Objects.equals(configManager.get(section.getCurrentPath() + "." + item, "position"), "-1")) {
            for (int i = 0; i < baseGui.getInventory().getSize(); i++) {

                if (baseGui.getInventory().getItem(i) != null) continue;

                baseGui.setItem(i, ItemBuilder.from(itemStack).asGuiItem());
            }
        } else {
            baseGui.setItem(Integer.parseInt(configManager.get(section.getCurrentPath() + "." + item, "position")), ItemBuilder.from(itemStack).asGuiItem());
        }
    }

    public void setNavigationItems(BaseGui baseGui, int next, int previous) {
        if (baseGui instanceof PaginatedGui paginatedGui) {
            baseGui.setItem(next, ItemBuilder.from(getNextPageItem()).asGuiItem(event -> paginatedGui.next()));
            baseGui.setItem(previous, ItemBuilder.from(getPreviousPageItem()).asGuiItem(event -> paginatedGui.previous()));
        }
    }

    private ItemStack getNextPageItem() {
        return this.createFromSection(plugin.getConfigManager().getSection("menus.navigation-items.next-page-item"));
    }

    private ItemStack getPreviousPageItem() {
        return this.createFromSection(plugin.getConfigManager().getSection("menus.navigation-items.previous-page-item"));
    }

    public ItemStack getBackItem() {
        return this.createFromSection(plugin.getConfigManager().getSection("menus.navigation-items.back-item"));
    }
}

