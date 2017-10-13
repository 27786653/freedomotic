/**
 *
 * Copyright (c) 2009-2016 Freedomotic team http://freedomotic.com
 *
 * This file is part of Freedomotic
 *
 * This Program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Freedomotic; see the file COPYING. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.freedomotic.jfrontend;

import com.freedomotic.api.API;
import com.freedomotic.api.Plugin;
import com.freedomotic.plugins.ClientStorage;
import com.freedomotic.plugins.PluginsManager;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the gui to edit the plugins.
 * @author Enrico Nicoletti
 */
@SuppressWarnings("squid:S1948") //We are not planning to serialize UI components
public class PluginConfigure extends javax.swing.JFrame {

    private ClientStorage clients;
    private final API api;

    private PluginsManager pluginsManager;
    private static HashMap<Plugin, String> predefined = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(PluginConfigure.class.getName());

    /**
     * Creates new form PluginConfigure
     *
     * @param api the api that is selected for the plugin
     */
    public PluginConfigure(API api) {
        this.api = api;
        this.clients = api.getClientStorage();
        this.pluginsManager = api.getPluginManager();
        initComponents();
        populatePluginsList();
        //add listener to category selection changes
        cmbPlugin.addActionListener(e -> {
            btnDefault.setEnabled(false);
            getConfiguration((Plugin) cmbPlugin.getSelectedItem());
        });

        setPreferredSize(new Dimension(650, 550));
        pack();
        setVisible(true);
    }

    /**
     * Populates the list of all plugins from the API.
     */
    private void populatePluginsList() {
        cmbPlugin.removeAllItems();

        api.getClients("plugin").stream()
                .filter(it -> it instanceof Plugin)
                .forEach(cmbPlugin::addItem);

        if (cmbPlugin.getItemCount() > 0) {
            cmbPlugin.setSelectedIndex(0);
        }
    }

    /**
     * Loads the configuration for selected plugin for modification. Also caches the current
     * configuration in a `predefined` map to restore later if needed.
     * @param item - plugin for which configuration needs to be modified.
     */
    private void getConfiguration(Plugin item) {
        txtArea.setContentType("text/xml");

        String config = readConfiguration(item);
        //add old config to predefined to be restored in a later step
        predefined.put(item, config);
        btnDefault.setEnabled(true);
        txtArea.setText(config);
    }

    /**
     * Convenient method for reading the configuration of given plugin.
     * @param plugin - plugin for which to load the configuration
     * @return String representation of full configuration
     */
    private String readConfiguration(Plugin plugin) {
        File file = plugin.getFile();
        try {
            return FileUtils.readFileToString(file, (Charset) null).trim();
        } catch (IOException e) {
            LOG.error("Failed to read configuration from file '"+file.getAbsolutePath()+"'", e);
            return "";
        }
    }

    /**
     * Persists the given configuration for the given plugin.
     *
     * @param plugin - plugin for which to store the new configuration
     * @param newConfig - string representation of new configuration to store
     * @throws IOException thrown in case access to file system fails
     */
    private void saveConfiguration(Plugin plugin, String newConfig)
            throws IOException {
        FileUtils.write(plugin.getFile(), newConfig, (Charset) null);
    }

    /** Loads the original configuration of given plugin.
     *
     * @param plugin - plugin for which to restore configuration
     */
    private void rollbackConfiguration(Plugin plugin) {
        txtArea.setText(predefined.get(plugin));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "Convert2Lambda"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbPlugin = new javax.swing.JComboBox();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JEditorPane();
        btnDefault = new javax.swing.JButton();
        uninstallButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(api.getI18n().msg("plugins_configuration_editor"));

        btnSave.setText(api.getI18n().msg("save_restart"));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText(api.getI18n().msg("cancel"));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(txtArea);

        btnDefault.setText(api.getI18n().msg("undo_edit"));
        btnDefault.setEnabled(false);
        btnDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaultActionPerformed(evt);
            }
        });

        uninstallButton.setForeground(new java.awt.Color(255, 0, 0));
        uninstallButton.setText(api.getI18n().msg("uninstall")
        );
        uninstallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uninstallButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPlugin, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDefault)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uninstallButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbPlugin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel)
                    .addComponent(btnDefault)
                    .addComponent(uninstallButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)    {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)    {//GEN-FIRST:event_btnSaveActionPerformed

        Plugin plugin = (Plugin) cmbPlugin.getSelectedItem();
        String name = plugin.getName();

        try {
            saveConfiguration(plugin, txtArea.getText());
            //stopping and unloading the plugin
            clients.remove(plugin);
            //reload it with the new configuration
            File configDir = plugin.getFile().getParentFile();
            LOG.trace("Reloading plugin '{}' with configuration '{}'", name, configDir);
            pluginsManager.loadSingleBoundle(configDir);

            //if not loaded sucessfully reset to old configuration
            if (clients.get(name) == null) {
                //reset to old working config and reload plugin
                rollbackConfiguration(plugin);
                saveConfiguration(plugin, txtArea.getText());
                pluginsManager.loadSingleBoundle(configDir);
                clients.get(name).start();
                JOptionPane.showMessageDialog(this,
                        api.getI18n().msg("warn_reset_old_config"));
            } else {
                clients.get(name).start();
                this.dispose();
            }
        } catch (Exception ex) {
            LOG.error("Failed to save configuration for plugin '"+name+"'", ex);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDefaultActionPerformed(java.awt.event.ActionEvent evt)    {//GEN-FIRST:event_btnDefaultActionPerformed
        Plugin plugin = (Plugin) cmbPlugin.getSelectedItem();
        rollbackConfiguration(plugin);
    }//GEN-LAST:event_btnDefaultActionPerformed

    private void uninstallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uninstallButtonActionPerformed
        Plugin item = (Plugin) cmbPlugin.getSelectedItem();

        File boundleRootFolder = item.getFile().getParentFile();
        String uninstallCandidates =
                clients.getClients().stream()
                    .filter(client -> client instanceof Plugin)
                    .map(plugin -> (Plugin) plugin)
                    .filter(plugin -> plugin.getFile().getParentFile().equals(boundleRootFolder))
                    .map(plugin -> "'" + plugin.getName() + "'")
                    .collect(Collectors.joining(" "));

        String localizedMessage
                = api.getI18n().msg("confirm_plugin_delete", new Object[]{uninstallCandidates});

        int result = JOptionPane.showConfirmDialog(null,
                new JLabel(localizedMessage),
                api.getI18n().msg("confirm_deletion_title"),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            pluginsManager.uninstallBundle(item);
            dispose();
        }
    }//GEN-LAST:event_uninstallButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDefault;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbPlugin;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JEditorPane txtArea;
    private javax.swing.JButton uninstallButton;
    // End of variables declaration//GEN-END:variables
}
