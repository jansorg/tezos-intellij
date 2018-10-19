package com.tezos.intellij.settings.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBDimension;
import com.tezos.intellij.settings.StackVisualizationPosition;
import com.tezos.intellij.settings.TezosClientConfig;
import com.tezos.intellij.settings.TezosSettingService;
import com.tezos.intellij.settings.TezosSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.io.File;
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
    private JBList clientList;

    private boolean listenerSuspended = false;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        model = new CollectionListModel<>();
        model.add(TezosSettingService.getSettings().clients);

        clientList = new JBList(model);
        selectionModel = clientList.getSelectionModel();

        clientList.setEmptyText("No Tezos clients configured");
        clientList.setCellRenderer(new ColoredListCellRenderer<TezosClientConfig>() {
            @Override
            protected void customizeCellRenderer(JList list, TezosClientConfig value, int index, boolean selected, boolean hasFocus) {
                String name = value.name.isEmpty() ? "<not available>" : value.name;
                if (value.isDefault) {
                    append(name, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                } else {
                    append(name);
                }
            }
        });

        ToolbarDecorator toolbar = ToolbarDecorator.createDecorator(clientList).disableUpDownActions();
        toolbar.setPreferredSize(new JBDimension(350, 250));
        toolbar.setAddAction(anActionButton -> {
            model.add(new TezosClientConfig());
            selectionModel.setSelectionInterval(model.getSize(), model.getSize());
        });
        toolbar.setRemoveAction(button -> {
            model.remove(selectionModel.getMinSelectionIndex());
        });

        clientListPanel = new JPanel(new VerticalFlowLayout());
        clientListPanel.add(toolbar.createPanel());
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
            if (clientList.isSelectionEmpty()) {
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
}
