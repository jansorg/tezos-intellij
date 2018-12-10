package com.tezos.lang.michelson.runConfig;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SortedComboBoxModel;
import com.tezos.intellij.settings.TezosClientConfig;
import com.tezos.intellij.ui.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @author jansorg
 */
public class MichelsonRunConfigurationSettingsForm {
    private final TezosClientConfig defaultClientItem = new TezosClientConfig("Default Tezos client", "-default-", false);
    JPanel mainPanel;
    private SortedComboBoxModel<TezosClientConfig> clientModel;
    private JComboBox clientList;
    private JTextField michelsonFile;
    private JTextField inputParam;
    private JTextField inputStorage;
    private JCheckBox promptForInput;

    public MichelsonRunConfigurationSettingsForm() {
    }

    public String getInputParam() {
        return inputParam.getText();
    }

    public void setInputParam(String value) {
        inputParam.setText(value);
    }

    public String getInputStorage() {
        return inputStorage.getText();
    }

    public void setInputStorage(String value) {
        inputStorage.setText(value);
    }

    public boolean getPromptForInput() {
        return promptForInput.isSelected();
    }

    public void setPromptForInput(boolean enabled) {
        promptForInput.setSelected(enabled);
    }

    @NotNull
    public String getMichelsonFile() {
        return michelsonFile.getText();
    }

    public void setMichelsonFile(@NotNull String file) {
        this.michelsonFile.setText(file);
    }

    public void setTezosClients(List<TezosClientConfig> clients) {
        clientModel.clear();
        clientModel.add(defaultClientItem);
        clientModel.addAll(clients);

        clientModel.setSelectedItem(defaultClientItem);
    }

    public void setSelectedClient(@Nullable TezosClientConfig config) {
        if (config == null) {
            clientModel.setSelectedItem(defaultClientItem);
        } else {
            clientModel.setSelectedItem(config);
        }
    }

    private void createUIComponents() {
        clientModel = new SortedComboBoxModel<>((a, b) -> {
            if (a == defaultClientItem) {
                return -1;
            }
            if (b == defaultClientItem) {
                return 1;
            }
            return a.name.compareTo(b.name);
        });

        clientList = new ComboBox(clientModel);
        //noinspection unchecked
        clientList.setRenderer(new ListCellRendererWrapper<TezosClientConfig>() {
            @Override
            public void customize(JList list, @Nullable TezosClientConfig value, int index, boolean selected, boolean hasFocus) {
                if (value == null) {
                    return;
                }

                setText(value.name);
                if (value == defaultClientItem) {
                    setForeground(JBColor.DARK_GRAY);
                    setIcon(Icons.INSTANCE.getTezos());
                }
            }
        });
    }

    public boolean isUseDefaultTezosClient() {
        return clientList.getSelectedItem() == defaultClientItem;
    }

    @Nullable
    public TezosClientConfig getSelectedTezosClient() {
        if (isUseDefaultTezosClient()) {
            return null;
        }

        return (TezosClientConfig) clientList.getSelectedItem();
    }
}
