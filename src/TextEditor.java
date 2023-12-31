import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;



// REALLY WHAT I LEARNED HERE IS THERE ARE SO MANY WAYS TO SOLVE THE SAME 'PROJECT' OR APP. PER DARIO, THIS SOLUTION APPEARS
// TO USE THE 'SWING' LIBRARY BASED ON THE IMPORTS. THERE IS ANOTHER TEXT EDITOR SOLUTION FROM GITHUB I'LL PULL TO COMPARE
// AND CONTRAST THAT USES JAVAFX LIBRARY. LOOKS ENTIRELY DIFFERENT. APPEARS THAT SOME LIBRARIES ARE MORE DESIGNED TO BE LESS
// VERBOSE BUT ALSO FORCE CERTAIN PATTERNS TO ACHIEVE THE EASIER WRITING. NOT SURE OF THE DIFFERENCE BETWEEN A LIBARY AND A
// FRAMEWORK, BUT THERE APPEARS TO BE SOME OVERLAP? APPARENTLY A COMPANY OR SPECIFIC TEAM MAY PREFER SPECIFIC LIBRARIES OR
// FRAMEWORKS THAT THEY WILL WANT YOU TO BE FAMILIAR WITH ONCE ON THE JOB. FOR NOW, THE BEST WAY TO LEARN IS TO NOT GET
// SUCKED INTO TUTORIAL HELL (PER DARIO) AND GO START GOOGLING STUFF ABOUT 'HOW DO I DO X USING Y FRAMEWORK/LIBRARY?'
public class TextEditor extends JFrame implements ActionListener{  // what exactly do JFrame and ActionListener do?

    JTextArea textArea; //what exactly is A JTextArea CLASS THAT IT CAN BE MODIFIED IN CONSTRUCTOR?
    JScrollPane scrollPane; // all these are just classes that i'm super unfamiliar with. i suspect java has lots of inbuilt classes like this
    JLabel fontLabel;// bar in pane that labels the font size selector
    JSpinner fontSizeSpinner; // allows for changing of font size via dropdown?
    JButton fontColorButton; // font color
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

        fontLabel = new JLabel("Font: "); // title for the font size

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25)); // IS THIS THE DIMENSIONS OF THE DROP DOWN BAR?
        fontSizeSpinner.setValue(20); // default font size again? what if input size that conflicts with line 34?
        fontSizeSpinner.addChangeListener(new ChangeListener() { // don't actually get what is happening here

            @Override
            public void stateChanged(ChangeEvent e) { // ???????

                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }

        });

        fontColorButton = new JButton("Color"); // label for font color pull down
        fontColorButton.addActionListener(this); // action listen makes it so it does something when clicking on it?

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // [ass in what appears
        // to be an array of fonts from yet another inbuilt something to pass as field to JComboBox

        fontBox = new JComboBox(fonts); // what exactly is a combobox?
        fontBox.addActionListener(this); // again, an action listener is needed
        fontBox.setSelectedItem("Arial");

        // ----- menubar -----

        menuBar = new JMenuBar(); // create menu bar object
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open"); // barious menu items
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this); //without an action listener, these are just buttons that don't actualyu do anything if clicked
        saveItem.addActionListener(this); // the program will still run, but buttons don't work
        exitItem.addActionListener(this);

        fileMenu.add(openItem); // adding menu item objects to the menu object
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
            JFileChooser fileChooser = new JFileChooser(); // some weird stuff about how we save and search for files?
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