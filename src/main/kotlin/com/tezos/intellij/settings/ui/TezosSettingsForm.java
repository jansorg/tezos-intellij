package com.tezos.intellij.settings.ui;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBDimension;
import com.tezos.intellij.settings.TezosClientConfig;
import com.tezos.intellij.settings.TezosSettingService;
import com.tezos.intellij.settings.TezosSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jansorg
 */
public class TezosSettingsForm {
    private JPanel mainPanel;
    private JPanel clientListPanel;
    private CollectionListModel<TezosClientConfig> model;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        // we'd like to use a TableModelEditor instead of a list, but it changed from an abstract class in 145.x to an interface in 163.x (and possibly in earlier builds)
        // there's no way to pass a non-null value which is compatible with all versions we want to support (145.x and later)


        model = new CollectionListModel<>();
        model.add(TezosSettingService.getSettings().clients);

        JBList list = new JBList(model);
        list.setEmptyText("No Tezos clients configured");
        list.setCellRenderer(new ColoredListCellRenderer<TezosClientConfig>() {
            @Override
            protected void customizeCellRenderer(JList list, TezosClientConfig value, int index, boolean selected, boolean hasFocus) {
                append(value.name == null ? "<empty name>" : value.name);
            }
        });

        ToolbarDecorator toolbar = ToolbarDecorator.createDecorator(list).disableUpDownActions();
        toolbar.setPreferredSize(new JBDimension(350, 250));
        toolbar.setAddAction(anActionButton -> {
            model.add(0, new TezosClientConfig());
            list.setSelectedIndex(0);
        });
        toolbar.setRemoveAction(button -> {
            int index = list.getSelectedIndex();
            model.remove(index);
            /*if (model.isEmpty()) {
                list.setSelectedIndex(Math.min(index, model.getSize()));
            }*/
        });
        toolbar.setEditAction(button -> {
            // EditTezosClientDialog dialog = new EditTezosClientDialog(null, model.getElementAt(list.getSelectedIndex()));
            // boolean ok = dialog.showAndGet();
        });

        clientListPanel = new JPanel(new VerticalFlowLayout());
        clientListPanel.add(toolbar.createPanel());
    }

    void applyTo(TezosSettings state) {
        state.setClients(model.getItems());
    }

    public void resetTo(@NotNull TezosSettings settings) {
        model.replaceAll(settings.clients);
    }
}
