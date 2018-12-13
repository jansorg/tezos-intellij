package com.tezos.lang.michelson.runConfig;

import com.intellij.ui.components.JBLabel;
import com.tezos.client.stack.MichelsonStackType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author jansorg
 */
public class MichelsonInputForm {
    private final MichelsonStackType parameterType;
    private final MichelsonStackType storageType;
    private JBLabel expectedParameterType;
    private JBLabel expectedStorageType;
    private JPanel mainPanel;
    private JPanel storageInput;
    private JPanel parameterInput;

    public MichelsonInputForm(@Nullable MichelsonStackType parameterType, @Nullable MichelsonStackType storageType) {
        this.parameterType = parameterType;
        this.storageType = storageType;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JBLabel getExpectedParameterType() {
        return expectedParameterType;
    }

    public MichelsonValueInput getParamterInput() {
        return (MichelsonValueInput) parameterInput;
    }

    public JBLabel getExpectedStorageType() {
        return expectedStorageType;
    }

    public MichelsonValueInput getStorageInput() {
        return (MichelsonValueInput) storageInput;
    }

    private void createUIComponents() {
        parameterInput = new MichelsonValueInput(parameterType);
        storageInput = new MichelsonValueInput(storageType);
    }
}
