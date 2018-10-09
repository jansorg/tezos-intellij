package com.tezos.intellij.settings;

import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;

import javax.swing.*;

/**
 * @author jansorg
 */
public class TezosConfigurableForm {
    private JList clientList;
    private JPanel mainPanel;
    private ListModel clientListModel;

    private void createUIComponents() {
        clientListModel = new CollectionListModel<String>();
        clientList = new JBList(clientListModel);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
