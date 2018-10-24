package com.tezos.intellij.settings.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.*;
import com.intellij.ui.ToolbarDecorator.ElementActionButton;
import com.intellij.ui.components.JBList;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.StatusText;
import com.tezos.client.TezosClientDetector;
import com.tezos.intellij.settings.StackVisualizationPosition;
import com.tezos.intellij.settings.TezosClientConfig;
import com.tezos.intellij.settings.TezosSettingService;
import com.tezos.intellij.settings.TezosSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * We'd like to use a TableModelEditor instead of a list, but it changed from an abstract class in 145.x to an interface in 163.x (and possibly in earlier builds)
 * there's no way to pass a non-null value which is compatible with all versions we want to support (145.x and later)
 *
 * @author jansorg
 */
public class TezosSettingsForm {
    private JPanel mainPanel;
    private JPanel clientListPanel;

    private JPanel editorPanel;
    private JTextField nameTextField;
    private JCheckBox defaultClientCheckbox;
    private TextFieldWithBrowseButton executablePathBrowser;
    private JLabel nameLabel;
    private JLabel dockerLabel;
    private JLabel executableLabel;
    private JComboBox<StackVisualizationPosition> stackVisualization;

    private CollectionListModel<TezosClientConfig> model;
    private ListSelectionModel selectionModel;
    private JBList<TezosClientConfig> clientList;

    private boolean listenerSuspended = false;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        model = new CollectionListModel<>();
        model.add(TezosSettingService.getSettings().clients);

        clientList = new JBList<>(model);
        selectionModel = clientList.getSelectionModel();

        StatusText emptyText = clientList.getEmptyText();
        emptyText.appendText("No Tezos clients configured.");
        emptyText.appendSecondaryText("Autodetect clients ...", SimpleTextAttributes.LINK_ATTRIBUTES, e -> {
            detectClient();
        });

        clientList.setCellRenderer(new ColoredListCellRenderer<TezosClientConfig>() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, TezosClientConfig value, int index, boolean selected, boolean hasFocus) {
                String name = value.name.isEmpty() ? "<not available>" : value.name;
                if (value.isDefault) {
                    append(name, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                    append(" (default client)", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                } else {
                    append(name);
                }
            }
        });

        ToolbarDecorator toolbar = ToolbarDecorator.createDecorator(clientList).disableUpDownActions();
        toolbar.setPreferredSize(new JBDimension(250, 150));
        toolbar.setAddAction(anActionButton -> {
            model.add(new TezosClientConfig());
            selectionModel.setSelectionInterval(model.getSize(), model.getSize());
        });
        toolbar.setRemoveAction(button -> model.remove(selectionModel.getMinSelectionIndex()));

        CopyClientAction copyClientAction = new CopyClientAction(clientList, model);
        toolbar.addExtraAction(copyClientAction);

        clientListPanel = new JPanel(new BorderLayout());
        clientListPanel.add(toolbar.createPanel());
    }

    private void detectClient() {
        //fixme replace with progress dialog and background job?
        TezosClientDetector detector = new TezosClientDetector();
        List<TezosClientConfig> detected = detector.detectClients();
        if (detected.isEmpty()) {
            Messages.showInfoMessage(clientList, "No clients could be detected in your configured PATH environment. Please configure a client manually.", "No Clients Found");
        } else {
            model.replaceAll(detected);
        }
    }

    public void init() {
        executablePathBrowser.addBrowseFolderListener(null, null, null, new FileChooserDescriptor(true, false, false, false, false, false), new TextComponentAccessor<JTextField>() {
            public String getText(JTextField component) {
                return component.getText();
            }

            public void setText(JTextField component, @NotNull String text) {
                final int len = text.length();
                if (len > 0 && text.charAt(len - 1) == File.separatorChar) {
                    text = text.substring(0, len - 1);
                }
                component.setText(text);
            }
        });

        clientList.addListSelectionListener(e -> {
            boolean hasSelection = clientList.isSelectionEmpty();
            if (hasSelection) {
                load(null);
            } else {
                load(model.getElementAt(clientList.getSelectedIndex()));
            }
        });

        DocumentAdapter listener = new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                applyClientEditorValues();
            }
        };

        nameTextField.getDocument().addDocumentListener(listener);
        defaultClientCheckbox.addActionListener(e -> {
            //reset existing default clients
            applyClientEditorValues();

            if (defaultClientCheckbox.isSelected()) {
                TezosClientConfig current = model.getElementAt(clientList.getSelectedIndex());
                for (TezosClientConfig c : model.getItems()) {
                    if (c != current) {
                        c.isDefault = false;
                    }
                }
                clientList.updateUI();
            }
        });
        executablePathBrowser.getTextField().getDocument().addDocumentListener(listener);

        for (StackVisualizationPosition position : StackVisualizationPosition.values()) {
            stackVisualization.addItem(position);
        }

        load(null);
    }

    private void applyClientEditorValues() {
        if (listenerSuspended) {
            return;
        }

        TezosClientConfig current = model.getElementAt(selectionModel.getLeadSelectionIndex());
        if (current != null) {
            current.name = nameTextField.getText();
            current.isDefault = defaultClientCheckbox.isSelected();
            current.executablePath = executablePathBrowser.getText();

            clientList.updateUI();
        }
    }

    private void load(@Nullable TezosClientConfig config) {
        listenerSuspended = true;
        try {
            boolean enabled = config != null;

            editorPanel.setEnabled(enabled);

            nameLabel.setEnabled(enabled);
            nameTextField.setEnabled(enabled);

            dockerLabel.setEnabled(enabled);
            defaultClientCheckbox.setEnabled(enabled);

            executableLabel.setEnabled(enabled);
            executablePathBrowser.setEnabled(enabled);
            executablePathBrowser.setButtonEnabled(enabled);

            if (enabled) {
                nameTextField.setText(config.name);
                defaultClientCheckbox.setSelected(config.isDefault);
                executablePathBrowser.setText(config.executablePath);
            } else {
                nameTextField.setText("");
                defaultClientCheckbox.setSelected(false);
                executablePathBrowser.setText("");
            }
        } finally {
            listenerSuspended = false;
        }
    }

    void applyTo(TezosSettings state) {
        state.setClients(model.getItems().stream().map(c -> new TezosClientConfig().applyFrom(c)).collect(Collectors.toList()));
        state.stackPanelPosition = stackVisualization.getItemAt(stackVisualization.getSelectedIndex());
    }

    public void resetTo(@NotNull TezosSettings settings) {
        selectionModel.clearSelection();

        model.replaceAll(settings.clients);
        stackVisualization.setSelectedItem(settings.stackPanelPosition);
    }

    private static class CopyClientAction extends ElementActionButton {
        private final JBList<TezosClientConfig> list;
        private final CollectionListModel<TezosClientConfig> model;

        CopyClientAction(JBList<TezosClientConfig> list, CollectionListModel<TezosClientConfig> model) {
            super("Copy selected client configuration", "Adds a copy of the currently selected item to the list.", PlatformIcons.COPY_ICON);
            this.list = list;
            this.model = model;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                TezosClientConfig current = model.getElementAt(index);

                TezosClientConfig copy = new TezosClientConfig().applyFrom(current);
                copy.isDefault = false;
                model.add(copy);
            }
        }

        @Override
        public boolean isDumbAware() {
            return true;
        }
    }
}
