package com.tezos.lang.michelson.runConfig;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;

/**
 * @author jansorg
 */
public class MichelsonInputForm {
    private JBLabel expectedParameterType;
    private JBLabel expectedStorageType;
    private JBTextField paramterInput;
    private JBTextField storageInput;
    private JPanel mainPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JBLabel getExpectedParameterType() {
        return expectedParameterType;
    }

    public JBTextField getParamterInput() {
        return paramterInput;
    }

    public JBLabel getExpectedStorageType() {
        return expectedStorageType;
    }

    public JBTextField getStorageInput() {
        return storageInput;
    }
}
