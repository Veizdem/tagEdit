package intrpr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Interpr {
    private String text;
    private String title;
    private Color bgColor;
    private ArrayList<Component> items;

    public Interpr(String document) {
        items = new ArrayList<Component>();
        String tmp = document.replace("\t", " ");
        this.text = tmp.replace("\n", " ");

        parse();
    }

    private void parse() {
        boolean flag = true;
        String tmp = null;
        String tmp1 = null;

        // parse @b@ tag
        text = text.replace("@b@", "\n");

        // parse @title@ ... @/title@ tag
        if (text.contains("@title@")) {
            title = text.substring(
                    text.indexOf("@title@"),
                    text.indexOf("@/title@")
            )
                    .replace("@title@", "");

            text = text.replace("@title@", "");
            text = text.replace("@/title@", "");
            text = text.replaceAll(title, "");
        }

        // parse @background=...@ tag
        if (text.contains("@background=")) {
            tmp = text.substring(text.indexOf("@background=") + 12, text.indexOf("@background=#") + 19);
            bgColor = new Color(
                    Integer.valueOf(tmp.substring(1, 3), 16),
                    Integer.valueOf(tmp.substring(3, 5), 16),
                    Integer.valueOf(tmp.substring(5, 7), 16)
            );
            tmp = "@background=" + tmp + "@";
            text = text.replaceAll(tmp, "");
        }

        while (flag) {
            tmp = null;
            tmp1 = null;

            System.out.println(text);
            if (text.contains("@")) {
                tmp = text.substring(0, text.indexOf("@"));
                if (tmp.length() == 0) {
                    // parse @img src='<filename>'@ tag
                    if (text.contains("@img src='")) {
                        tmp = text.substring(text.indexOf("@img src='") + 10);
                        tmp = tmp.substring(0, tmp.indexOf("'@"));
                        try {
                            BufferedImage bufferedImage = ImageIO.read(new File(tmp));
                            JLabel pic = new JLabel(new ImageIcon(bufferedImage));
                            // parse @coord:image(x,y)@ tag
                            if (text.contains("@coord:image(")) {
                                tmp1 = text.substring(text.indexOf("@coord:image(") + 13, text.indexOf(")@"));
                                int x = Integer.parseInt(tmp1.substring(0, 3));
                                int y = Integer.parseInt(tmp1.substring(4, 7));

                                pic.setLocation(x, y);

                                tmp1 = String.format("@coord:image\\(%s\\)@", tmp1);
                                text = text.replaceFirst(tmp1, "");
                            }
                            items.add(pic);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        tmp = String.format("@img src='%s'@", tmp.replace("\\", "\\\\"));
                        text = text.replaceFirst(tmp, "");

                        // parse @font size=...@	@/size@ tag
                    } else {
                        if (text.contains("@font size=")) {
                            tmp = text.substring(text.indexOf("@font size=") + 13, text.indexOf("@/size@"));
                            int size = Integer.parseInt(text.substring(text.indexOf("@font size=")+11, text.indexOf("@font size=") + 12));

                            JLabel lbl = new JLabel(tmp);
                            lbl.setFont(lbl.getFont().deriveFont(3f * size));

                            tmp1 = String.format("@font size=%s@%s@/size@", size, tmp);
                            text = text.replaceFirst(tmp1, "");
                            items.add(lbl);
                        } else {
                            // pasre @font type=...@    @/type@ tag
                            if (text.contains("@font type=")) {
                                tmp = text.substring(text.indexOf("@font type=") + 13, text.indexOf("@/type@"));
                                int type = Integer.parseInt(text.substring(text.indexOf("@font type=")+11, text.indexOf("@font type=") + 12));

                                JLabel lbl = new JLabel(tmp);
                                switch (type) {
                                    case 1: {
                                        lbl.setFont(new Font(Font.SERIF, Font.PLAIN, lbl.getFont().getSize()));
                                        break;
                                    }
                                    case 2: {
                                        lbl.setFont(new Font(Font.MONOSPACED, Font.PLAIN, lbl.getFont().getSize()));
                                        break;
                                    }
                                    case 3: {
                                        lbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, lbl.getFont().getSize()));
                                        break;
                                    }
                                }

                                tmp1 = String.format("@font type=%s@%s@/type@", type, tmp);
                                text = text.replaceFirst(tmp1, "");
                                items.add(lbl);
                            } else {
                                // parse @textcolor=...@    @/textcolor@ tag
                                if (text.contains("@textcolor=#")) {
                                    tmp = text.substring(text.indexOf("@textcolor=#") + 19, text.indexOf("@/textcolor@"));
                                    tmp1 = text.substring(text.indexOf("@textcolor=#") + 12, text.indexOf("@textcolor=#") + 18);

                                    Color color = new Color(
                                            Integer.valueOf(tmp1.substring(0, 2), 16),
                                            Integer.valueOf(tmp1.substring(2, 4), 16),
                                            Integer.valueOf(tmp1.substring(4, 6), 16)
                                    );

                                    JLabel lbl = new JLabel(tmp);
                                    lbl.setForeground(color);

                                    tmp = String.format("@textcolor=#%s@%s@/textcolor@", tmp1, tmp);
                                    text = text.replaceAll(tmp, "");

                                    items.add(lbl);
                                } else {
                                    flag = false;
                                }
                            }
                        }
                    }
                } else {
                    String[] list = tmp.split("\n");
                    for (String s : list) {
                        items.add(new JLabel(s));
                    }
                    text = text.replaceFirst(tmp, "");
                }
            } else {
                String[] list = text.split("\n");
                for (String s : list) {
                    items.add(new JLabel(s));
                }
                text = text.replaceFirst(text, "");
                flag = false;
            }
        }
    }

    public ArrayList<Component> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public Color getBackgroundColor() {
        return bgColor;
    }
}
