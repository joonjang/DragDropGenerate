package ui;

import model.MyDragDropListener;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// https://docs.oracle.com/javase/tutorial/uiswing/layout/spring.html
public class DragDropFrame extends JFrame {
    // TODO consider making PYTHON_PATH dynamic to where the script is
    private String PYTHON_FILE = "add_ruler.py";
    //    private String PYTHON_PATH = "/Users/joonjang/IdeaProjects/DragDropGenerate/ruler/";
    private String PYTHON_PATH = "Z:/internal/autoscan/ruler/joonRulerTestField/testingGround/";
    // String array to allow scalability to add other control inputs
    private String[] controlArr = {"Ruler Gap: ", "Ruler Width: "};

    private MyDragDropListener myDragDropListener;
    private List<JTextField> textFieldList = new ArrayList<>();

    public DragDropFrame() {
        super("Drag and drop");
        this.setPreferredSize(new Dimension(300,220));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        addComponentsToPane(this);

        this.pack();
        this.setVisible(true);
    }

    public void addComponentsToPane(final Container pane) {
        // DragDrop
        JLabel dropLabel = new JLabel("Drag file here", SwingConstants.CENTER);

        JPanel dropPanel = new JPanel();
        dropPanel.setLayout(new GridBagLayout());
        dropPanel.add(dropLabel);
        dropPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 40, 50));
        dropPanel.setBackground(Color.orange);

        // Create the drag and drop listener
        myDragDropListener = new MyDragDropListener(dropPanel, dropLabel);

        // Connect the label with a drag and drop listener
        new DropTarget(dropPanel, myDragDropListener);


        // Controls
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        JButton genButton = new JButton("Generate");

        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPython(dropPanel, dropLabel);
            }
        });

        // Generate controls label and textfield
        for (int i = 0; i < controlArr.length; i++) {
            textFieldList.add(new JTextField(10));

            JPanel inputLine = new JPanel();
            inputLine.setLayout(new BoxLayout(inputLine, BoxLayout.X_AXIS));
            inputLine.add(new Label(controlArr[i]));
            inputLine.add(textFieldList.get(i));
//            controls.add(new Label(controlArr[i]));
//            controls.add(textFieldList.get(i));
            controls.add(inputLine);
        }
        controls.add(genButton);

        pane.add(controls, BorderLayout.SOUTH);
        pane.add(dropPanel, BorderLayout.NORTH);
    }

    // outputs parameters from textfield to send to python script
    // sends: Ruler gap
    private List<String> getControlCommands() {
        List<String> controlCommands = new ArrayList<>();

        for (JTextField control : textFieldList) {
            controlCommands.add(control.getText());
        }

        return controlCommands;
    }

    // read python script output: https://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
    private void getPython(JPanel dropPanel, JLabel dropLabel) {
        try {
            List<String> inputPathList = myDragDropListener.getInputPathList();
            if(!inputPathList.isEmpty()) {
                List<String> commandList = new ArrayList<>();
                commandList.add("python3");
                commandList.add(PYTHON_PATH + PYTHON_FILE);
                commandList.add(inputPathList.get(inputPathList.size() - 1));
                commandList.addAll(getControlCommands());

                String[] consoleCommand = commandList.toArray(new String[0]);
                //String consoleCommand = String.join(" ", commandList);

                System.out.println(consoleCommand);

                Runtime rt = Runtime.getRuntime();
                Process p = Runtime.getRuntime().exec(consoleCommand);

//                stdInput.close();
//                stdError.close();
//                Process p = Runtime.getRuntime().exec(consoleCommand);

                inputPathList.clear();
                changeDropPanelColorAndLabel(dropPanel, dropLabel, Color.orange, "<html>File generated<br>Drag new file here</html>");
            }
            else {
                changeDropPanelColorAndLabel(dropPanel, dropLabel, Color.PINK, "<html>No file selected<br>Drag file here</html>");
            }
        }
        catch(Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    // helper method to change the drag drop UI color and text
    private void changeDropPanelColorAndLabel(JPanel dropPanel, JLabel dropLabel, Color bgColor, String s) {
        dropPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));
        dropPanel.setBackground(bgColor);
        dropLabel.setText(s);
    }
}
