package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;

import static java.awt.Color.*;

public class MFrame extends JFrame {
    private String path = null;

    // open file
    private JButton openBtn = new JButton("Open file");
    // save file
    private JButton saveBtn = new JButton("Save file");
    // render file
    private JButton renderBtn = new JButton("Render file");
    // close file
    private JButton closeBtn = new JButton("Close file");
    // clear file area
    private JButton clearBtn = new JButton("Clear");

    // file path label
    private JLabel pathLbl = new JLabel("no file");

    // help area
    private JTextArea helpAr = new JTextArea(8, 10);

    // file area
    private JTextArea fileAr = new JTextArea(8, 10);

    public MFrame() {
        super("Tag Editor");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        Container panel = this.getContentPane();
        panel.setLayout(new BorderLayout());

        Box btnBox = Box.createHorizontalBox();

        btnBox.add(openBtn);
        btnBox.add(saveBtn);
        btnBox.add(closeBtn);
        btnBox.add(renderBtn);
        btnBox.add(clearBtn);

        Border border = BorderFactory.createLineBorder(BLACK);

        // hardcode help text
        helpAr.setText("HELP INFO:\n\n" +
                "Text size:\n" +
                "@font size=...@\t@/size@\n\n" +
                "Text color:\n" +
                "@textcolor=...@\t@/textcolor@\n\n" +
                "Text type:\n" +
                "@font type=...@\t@/type@\n\n" +
                "Image:\n" +
                "@img src='<filename>'@\n\n" +
                "Coord:\n" +
                "@coord:image(x,y)@\n\n" +
                "Background:\n" +
                "@background=...@\n\n" +
                "Title:\n" +
                "@title@\t@/title@\n\n" +
                "New line:\n" +
                "@p@");

        helpAr.setBackground(SystemColor.info);
        helpAr.setEnabled(false);
        helpAr.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        fileAr.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // add buttons click events
        openBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int ret = fileChooser.showDialog(null, "Open file");

            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                path = file.getPath();

                pathLbl.setText(path);

                try {
                    FileReader fileReader = new FileReader(path);
                    fileAr.read(fileReader, "fileAr");
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        closeBtn.addActionListener(e -> {
            if (path != null) {
                path = null;

                pathLbl.setText("no file");
            }
            fileAr.setText("");
        });

        saveBtn.addActionListener(e -> {
            if (!fileAr.getText().isEmpty()) {
                if (path == null) {
                    // set path
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Specify a file to save");

                    int userSelection = fileChooser.showSaveDialog(this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        path = fileToSave.getAbsolutePath();
                    }
                    pathLbl.setText(path);
                }

                try {
                    BufferedWriter fileWrite = new BufferedWriter(new FileWriter(path));
                    fileAr.write(fileWrite);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        clearBtn.addActionListener(e -> {
            fileAr.setText("");
        });

        renderBtn.addActionListener(e -> {
            RenderFrame renderFrame = new RenderFrame(fileAr.getText());
            renderFrame.setVisible(true);
        });

        panel.add(btnBox, BorderLayout.NORTH);
        panel.add(helpAr, BorderLayout.WEST);
        panel.add(fileAr, BorderLayout.CENTER);
        panel.add(pathLbl, BorderLayout.SOUTH);
    }
}
