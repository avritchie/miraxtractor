package MRXS;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Victoria
 */
public class MRXS extends javax.swing.JFrame {

    public static MRXS frame;
    public static MRXSSlide miraxslide;
    public static MRXSLevel miraxlevel;
    public static String[] info;
    public static boolean folderMode;
    public static int[] syncCounter;

    public String getOutputField() {
        String oft = outputField.getText();
        oft = oft.replace("\\", "/");
        if (!oft.endsWith("/")) {
            return oft;
        } else {
            return oft + "/";
        }
    }

    private void load() {
        try {
            if (folderMode) {
                try {
                    String[] files = getFolderModeFileList();
                    System.out.println(Arrays.toString(files));
                    miraxslide = new MRXSSlide(inputField.getText() + "/" + files[0] + "/Slidedat.ini");
                } catch (Exception ex) {
                    folderMode = false;
                    infoLabel.setText("One MRXS file will be handled");
                    miraxslide = new MRXSSlide(inputField.getText() + "/Slidedat.ini");
                }
            } else {
                String path = inputField.getText();
                miraxslide = new MRXSSlide(path);
            }
            loadAndSetInfo();
        } catch (Exception ex) {
            error(ex);
        }
    }

    public MRXS() {
        initComponents();
        setLocationRelativeTo(null);
        setEnabledDisabled(extractPanel, false);
        setEnabledDisabled(outputPanel, false);
        inputField.setDropTarget(new DropTarget() {

            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_LINK);
                    List<File> files = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    handleInputOutput(files.get(0));
                    evt.dropComplete(true);
                } catch (UnsupportedFlavorException | IOException ex) {
                    error(ex);
                }
            }

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                dtde.acceptDrag(DnDConstants.ACTION_LINK);
            }
        });
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        inputPanel = new javax.swing.JPanel();
        inputField = new javax.swing.JTextField();
        buttonOpenInput = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        infoLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        extractPanel = new javax.swing.JPanel();
        zoomCombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        channelCombo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cropBox = new javax.swing.JCheckBox();
        outputPanel = new javax.swing.JPanel();
        buttExctractAll = new javax.swing.JButton();
        buttExtractMerge = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        outputField = new javax.swing.JTextField();
        buttonOpenOutput = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mirax extractor");
        setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("mirax slide format extractor");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("MiraXtractor");

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        inputField.setText("D:/Files/Mirax/Slide.mrxs");

        buttonOpenInput.setText("Browse...");
        buttonOpenInput.setPreferredSize(new java.awt.Dimension(80, 20));
        buttonOpenInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenInputActionPerformed(evt);
            }
        });

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setVisibleRowCount(7);
        jScrollPane1.setViewportView(jList1);

        infoLabel.setText("Please select either a single file or a folder with multiple files");

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inputPanelLayout.createSequentialGroup()
                        .addComponent(inputField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOpenInput, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonOpenInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jLabel3.setText("v1.0.4");

        extractPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Extraction settings"));

        zoomCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select..." }));
        zoomCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomComboActionPerformed(evt);
            }
        });

        jLabel4.setText("Zoom level");

        channelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select..." }));
        channelCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelComboActionPerformed(evt);
            }
        });

        jLabel5.setText("Channel");

        cropBox.setFont(cropBox.getFont());
        cropBox.setSelected(true);
        cropBox.setText(" do not crop");

        javax.swing.GroupLayout extractPanelLayout = new javax.swing.GroupLayout(extractPanel);
        extractPanel.setLayout(extractPanelLayout);
        extractPanelLayout.setHorizontalGroup(
            extractPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extractPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(zoomCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(channelCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cropBox)
                .addContainerGap())
        );
        extractPanelLayout.setVerticalGroup(
            extractPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extractPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(extractPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zoomCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(channelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cropBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        buttExctractAll.setFont(buttExctractAll.getFont().deriveFont(buttExctractAll.getFont().getSize()+1f));
        buttExctractAll.setText("Extract all tiles");
        buttExctractAll.setMaximumSize(new java.awt.Dimension(250, 23));
        buttExctractAll.setMinimumSize(new java.awt.Dimension(150, 23));
        buttExctractAll.setPreferredSize(new java.awt.Dimension(150, 23));
        buttExctractAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttExctractAllActionPerformed(evt);
            }
        });

        buttExtractMerge.setFont(buttExtractMerge.getFont().deriveFont(buttExtractMerge.getFont().getSize()+1f));
        buttExtractMerge.setText("Extract a merged image");
        buttExtractMerge.setMaximumSize(new java.awt.Dimension(250, 23));
        buttExtractMerge.setMinimumSize(new java.awt.Dimension(150, 23));
        buttExtractMerge.setPreferredSize(new java.awt.Dimension(150, 23));
        buttExtractMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttExtractMergeActionPerformed(evt);
            }
        });

        outputField.setText("D:/Files/Mirax/Output/");

        buttonOpenOutput.setText("Browse...");
        buttonOpenOutput.setPreferredSize(new java.awt.Dimension(80, 20));
        buttonOpenOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenOutputActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(buttExctractAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttExtractMerge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(outputField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOpenOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outputPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonOpenOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttExctractAll, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttExtractMerge, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(extractPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(extractPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOpenInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenInputActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(inputField.getText()));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("Mirax files", "mrxs", "ini");
        chooser.addChoosableFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handleInputOutput(chooser.getSelectedFile());
        }
    }//GEN-LAST:event_buttonOpenInputActionPerformed

    private void buttonOpenOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenOutputActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(inputField.getText()));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputField.setText(chooser.getSelectedFile().getPath().replace("\\", "/"));
        }
    }//GEN-LAST:event_buttonOpenOutputActionPerformed

    private void zoomComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomComboActionPerformed
        loadLevelData();
    }//GEN-LAST:event_zoomComboActionPerformed

    private void channelComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channelComboActionPerformed
        loadLevelData();
    }//GEN-LAST:event_channelComboActionPerformed

    private void buttExctractAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttExctractAllActionPerformed
        int zoom = getSelectedZoom();
        int channel = getSelectedChannel();
        String out = getOutputField();
        if (folderMode) {
            String[] files = getFolderModeFileList();
            syncCounter = new int[files.length];
            for (int i = 0; i < files.length; i++) {
                try {
                    MRXSSlide sl = new MRXSSlide(inputField.getText() + "/" + files[0] + "/Slidedat.ini");
                    MRXSLevel lv = new MRXSLevel(sl, zoom, channel);
                    lv.extractSingleTiles(i, out);
                } catch (Exception ex) {
                    error(ex);
                }
            }
        } else {
            try {
                syncCounter = new int[1];
                miraxlevel.extractSingleTiles(0, out);
            } catch (Exception ex) {
                error(ex);
            }
        }
    }//GEN-LAST:event_buttExctractAllActionPerformed

    private void buttExtractMergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttExtractMergeActionPerformed
        int zoom = getSelectedZoom();
        int channel = getSelectedChannel();
        String out = getOutputField();
        if (folderMode) {
            String[] files = getFolderModeFileList();
            syncCounter = new int[files.length];
            for (int i = 0; i < files.length; i++) {
                try {
                    MRXSSlide sl = new MRXSSlide(inputField.getText() + "/" + files[i] + "/Slidedat.ini");
                    MRXSLevel lv = new MRXSLevel(sl, zoom, channel);
                    lv.setCropping(cropMerged());
                    lv.extractMergedTiles(i, out);
                } catch (Exception ex) {
                    error(ex);
                }
            }
        } else {
            try {
                syncCounter = new int[1];
                miraxlevel.setCropping(cropMerged());
                miraxlevel.extractMergedTiles(0, out);
            } catch (Exception ex) {
                error(ex);
            }
        }
    }//GEN-LAST:event_buttExtractMergeActionPerformed

    private void setEnabledDisabled(JPanel panel, boolean enabled) {
        Component[] com = panel.getComponents();
        for (Component com1 : com) {
            com1.setEnabled(enabled);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            error(ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new MRXS();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttExctractAll;
    private javax.swing.JButton buttExtractMerge;
    private javax.swing.JButton buttonOpenInput;
    private javax.swing.JButton buttonOpenOutput;
    private javax.swing.JComboBox<String> channelCombo;
    private javax.swing.JCheckBox cropBox;
    private javax.swing.JPanel extractPanel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JTextField inputField;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField outputField;
    private javax.swing.JPanel outputPanel;
    protected javax.swing.JProgressBar progressBar;
    private javax.swing.JComboBox<String> zoomCombo;
    // End of variables declaration//GEN-END:variables

    private String[] numberList(int count) {
        String[] strs = new String[count];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = Integer.toString(i);
        }
        return strs;
    }

    private void loadLevelData() {
        int zoom = getSelectedZoom();
        int channel = getSelectedChannel();
        // MRXSLevel(mirax file,level,channel) | level 0 is with the highest resolution
        try {
            miraxlevel = new MRXSLevel(miraxslide, zoom, channel);
            String[] strings = miraxlevel.getInfo();
            progressBar.setValue(0);
            updateInfo(strings, false);
            updateList();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void updateList() {
        jList1.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                super.setSelectionInterval(-1, -1);
            }
        });
        jList1.setModel(new AbstractListModel() {
            public int getSize() {
                return info.length;
            }

            public String getElementAt(int i) {
                return info[i];
            }
        });
    }

    private void updateInfo(String[] strings, boolean slideinfo) { //slideinfo or levelinfo
        if (slideinfo) {
            info[0] = strings[0];
            info[1] = strings[1];
            info[2] = strings[2];
        } else {
            info[3] = strings[0];
            info[4] = strings[1];
            info[5] = strings[2];
        }
        info[6] = "";
    }

    public static void error(Exception ex, String msg) {
        JOptionPane.showMessageDialog(frame, (msg == null ? "" : msg + "\n")
                + ex.getMessage() + "\n" + ex.getClass().toString(), "Dialog", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(System.err);
    }

    public static void error(Exception ex) {
        error(ex, null);
    }

    private void loadAndSetInfo() {
        try {
            info = new String[7];
            String[] strings = miraxslide.getInfo();
            updateInfo(strings, true);
            String[] zoomLevels = numberList(miraxslide.zoomLevelCount), channels = numberList(miraxslide.channelCount);
            zoomCombo.setModel(new DefaultComboBoxModel(zoomLevels));
            channelCombo.setModel(new DefaultComboBoxModel(channels));
            loadLevelData();
            setEnabledDisabled(extractPanel, true);
            setEnabledDisabled(outputPanel, true);
        } catch (Exception ex) {
            error(ex);
        }
    }

    public void buttonsEnabled(boolean b) {
        buttExctractAll.setEnabled(b);
        buttExtractMerge.setEnabled(b);
    }

    void readyMessage() {
        info[6] = "Extraction was succesfully finished";
        updateList();
    }

    private String[] getFolderModeFileList() {
        File path = new File(inputField.getText());
        String[] files = path.list((new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                File f = new File(current, name);
                return f.isDirectory() && !f.getName().equals("Output");
            }
        }));
        return files;
    }

    private int getSelectedZoom() {
        return Integer.parseInt((String) zoomCombo.getSelectedItem());
    }

    private int getSelectedChannel() {
        return Integer.parseInt((String) channelCombo.getSelectedItem());
    }

    public boolean cropMerged() {
        return cropBox.isSelected();
    }

    private void handleInputOutput(File file) {
        String path = file.getPath().replace("\\", "/");
        if (file.isDirectory()) {
            System.out.println("A directory was chosen");
            inputField.setText(path + "/");
            outputField.setText(path + "/Output/");
            infoLabel.setText("Multiple MRXS files in a folder will be handled");
            folderMode = true;
        } else {
            System.out.println("A file was chosen");
            inputField.setText(path);
            outputField.setText(path + "_Output/");
            infoLabel.setText("One MRXS file will be handled");
            folderMode = false;
        }
        load();
    }
}
