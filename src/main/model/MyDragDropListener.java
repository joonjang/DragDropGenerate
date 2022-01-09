package model;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyDragDropListener implements DropTargetListener {
    private List<String> inputPathList = new ArrayList<>();
    private JPanel dropPanel;
    private JLabel label;

    public MyDragDropListener(JPanel dropPanel, JLabel label){
        this.dropPanel = dropPanel;
        this.label = label;
    }

    public List<String> getInputPathList() {
        return inputPathList;
    }

    @Override
    public void drop(DropTargetDropEvent event) {

        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {
                    // Get all the dropped files
                    List<File> files = (List<File>) transferable.getTransferData(flavor);

                    // Loop them through
                    for (File file : files) {
                        // check what the file type is
                        if(file.getPath().contains(".obj")) {
                            inputPathList.add(file.getPath());
                            System.out.println("File path is '" + file.getPath() + "'.");

                            dropPanel.setBackground(new Color(173, 255, 47));
                            label.setText(file.getPath().replaceAll(".+\\\\", ""));
                            dropPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
                        }
                        else {
                            dropPanel.setBackground(Color.pink);
                            label.setText("<html>File type not valid<br>Drag new file here</html>");
                            dropPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
                        }
                    }
                }

            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();

            }
        }

        // Inform that the drop is complete
        event.dropComplete(true);

    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }
}
