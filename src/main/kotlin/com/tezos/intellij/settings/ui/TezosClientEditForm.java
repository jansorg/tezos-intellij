package com.tezos.intellij.settings.ui;

import com.tezos.intellij.settings.TezosClientConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jansorg
 */
public class TezosClientEditForm {
    private JTextField name;
    private JCheckBox isDockerScript;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void applyFrom(@NotNull TezosClientConfig config) {
        name.setText(config.name == null ? "" : config.name);
        isDockerScript.setSelected(config.isDockerScript);
    }

    public void applyTo(@NotNull TezosClientConfig config) {
        config.name = name.getText();
        config.isDockerScript = isDockerScript.isSelected();
    }
}
