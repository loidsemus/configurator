package me.loidsemus.configurator.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.messages.Messages;
import me.loidsemus.configurator.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Use when displaying a list of items in a GUI
 */
public class ListMenu extends Gui implements Returnable {

    private final PaginatedPane paginatedPane;
    private final StaticPane navigatorPane;

    public ListMenu(Plugin plugin, int rows, String title) {
        super(plugin, rows, title);

        paginatedPane = new PaginatedPane(0, 0, 9, rows - 1);
        navigatorPane = new StaticPane(0, rows - 1, 9, 1);

        updateNavigator();

        addPane(paginatedPane);
        addPane(navigatorPane);
    }

    @Override
    public void setParent(Gui parent) {
        if (parent != null) {
            GuiItem backItem = new GuiItem(ItemBuilder.simple(Material.BARRIER, Messages.get(LangKey.NAV_BACK, false)), event -> {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
                parent.show(event.getWhoClicked());
            });
            navigatorPane.addItem(backItem, 0, 0);
        }
        else {
            navigatorPane.addItem(new GuiItem(ItemBuilder.simple(Material.AIR)), 0, 0);
        }
    }

    protected void updateNavigator() {
        navigatorPane.addItem(new GuiItem(ItemBuilder.simple(Material.AIR)), 3, 0);
        navigatorPane.addItem(new GuiItem(ItemBuilder.simple(Material.AIR)), 5, 0);

        if (hasPreviousPage()) {
            ItemStack previousPageItem = new ItemBuilder(Material.ARROW)
                    .displayname(Messages.get(LangKey.NAV_PREVIOUS_PAGE, false))
                    .lore(Messages.get(LangKey.NAV_PAGE_INDICATOR, false, paginatedPane.getPage() + 1 + "", paginatedPane.getPages() + ""))
                    .build();
            GuiItem previousPageGuiItem = new GuiItem(previousPageItem, event -> {
                event.setCancelled(true);
                //if (paginatedPane.getPage() > 0 && paginatedPane.getPages() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                updateNavigator();
                update();
                //}
            });

            navigatorPane.addItem(previousPageGuiItem, 3, 0);
        }

        if (hasNextPage()) {
            ItemStack nextPageItem = new ItemBuilder(Material.ARROW)
                    .displayname(Messages.get(LangKey.NAV_NEXT_PAGE, false))
                    .lore(Messages.get(LangKey.NAV_PAGE_INDICATOR, false, paginatedPane.getPage() + 1 + "", paginatedPane.getPages() + ""))
                    .build();
            GuiItem nextPageGuiItem = new GuiItem(nextPageItem, event -> {
                event.setCancelled(true);
                if (paginatedPane.getPages() > paginatedPane.getPage() + 1 && paginatedPane.getPages() > 0) {
                    paginatedPane.setPage(paginatedPane.getPage() + 1);
                    updateNavigator();
                    update();
                }
            });
            navigatorPane.addItem(nextPageGuiItem, 5, 0);
        }
    }

    private boolean hasNextPage() {
        return paginatedPane.getPages() > (paginatedPane.getPage() + 1);
    }

    private boolean hasPreviousPage() {
        return paginatedPane.getPage() > 0 && paginatedPane.getPages() > 0;
    }

    protected void addItems(List<GuiItem> items) {
        paginatedPane.populateWithGuiItems(items);
        updateNavigator();
    }

    protected void clear() {
        paginatedPane.clear();
        updateNavigator();
    }

    public PaginatedPane getPaginatedPane() {
        return paginatedPane;
    }

    public StaticPane getNavigatorPane() {
        return navigatorPane;
    }
}
