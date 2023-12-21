import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame implements ActionListener{  // what exactly do JFrame and ActionListener do?

    JTextArea textArea; //what exactly is A JTextArea CLASS THAT IT CAN BE MODIFIED IN CONSTRUCTOR?
    JScrollPane scrollPane; // all these are just classes that i'm super unfamiliar with. i suspect java has lots of inbuilt classes like this
    JLabel fontLabel;
    JSpinner fontSizeSpinner; // allows for changing of font size via dropdown?
    JButton fontColorButton;
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    TextEditor(){ // this whole thing is the constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // is just the 'X' to close out of the pane
        this.setTitle("Bro text Editor"); // will set title on pane
        this.setSize(500, 500); // sets dimensions of the pane
        this.setLayout(new FlowLayout()); // WHAT IS FLOW LAYOUT????? just different than generic layout?
        this.setLocationRelativeTo(null);  // Centers the pane in the screen

        textArea = new JTextArea(); //instantiates field, WHICH DOES WHAT?
        textArea.setLineWrap(true); // continues text onto next line when reaches end of pane
        textArea.setWrapStyleWord(true); // wraps word at boundary such that if word can't fit on a line, it will bring whole word to next line
        textArea.setFont(new Font("Arial",Font.PLAIN,20)); // just setting default font and size

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450)); // sets text box size slightly smaller than pane
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // scroll bar will always be present even if not needed

        fontLabel = new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25)); // IS THIS THE DIMENSIONS OF THE DROP DOWN BAR?
        fontSizeSpinner.setValue(20); // default font size again? what if input size that conflicts with line 34?
        fontSizeSpinner.addChangeListener(new ChangeListener() { // don't actually get what is happening here

            @Override
            public void stateChanged(ChangeEvent e) { // ???????

                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }

        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        // ----- menubar -----

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // ----- /menubar -----

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true); //WHAT DOES THIS ACTUALLY DO?
    }

    @Override
    public void actionPerformed(ActionEvent e) { // this is from the ActionListener interface

        if(e.getSource()==fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();

            Color color = colorChooser.showDialog(null, "Choose a color", Color.black);

            textArea.setForeground(color);
        }

        if(e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }

        if(e.getSource()==openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally {
                    fileIn.close();
                }
            }
        }
        if(e.getSource()==saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                }
                catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally {
                    fileOut.close();
                }
            }
        }
        if(e.getSource()==exitItem) {
            System.exit(0);
        }
    }
}