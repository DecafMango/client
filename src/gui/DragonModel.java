package gui;

import dragon.Dragon;
import dragon.DragonCharacter;
import dragon.DragonType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class DragonModel extends JButton implements Runnable {

    private static Map<String, Color> colors;

    private JFrame jFrame;
    private Dragon dragon;
    private Color color;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public DragonModel(JFrame jFrame, Dragon dragon) {
        colors = new HashMap<>();
        String owner = dragon.getOwner();
        if (!colors.containsKey(owner))
            colors.put(owner, Color.getHSBColor(owner.hashCode() + 1000,
                    owner.hashCode() + 2000,
                    owner.hashCode() + 3000));
        color = colors.get(owner);

        this.jFrame = jFrame;
        jFrame.add(this);
        this.dragon = dragon;

        x1 = (int) (Math.random() * 600);
        y1 = (int) (Math.random() * 500);

        x2 = (int) dragon.getCoordinates().getX();
        y2 = dragon.getCoordinates().getY();
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = dragon.getId();
                String name = dragon.getName();
                float x = dragon.getCoordinates().getX();
                int y = dragon.getCoordinates().getY();
                Long age = dragon.getAge();
                dragon.Color color = dragon.getColor();
                DragonType type = dragon.getType();
                DragonCharacter character = dragon.getCharacter();
                Integer depth = dragon.getCave() == null ? null : dragon.getCave().getDepth();

                CreationWindow.fillFields(name, x, y, age, color, type, character, depth);
                CreationWindow.setId(id);
                CreationWindow.show("update_by_id");
            }
        });
    }

    @Override
    public void run() {
        while (x1 != x2 || y1 != y2) {
            if (x1 < x2)
                x1++;
            if (x1 > x2)
                x1--;
            if (y1 < y2)
                y1++;
            if (y1 > y2)
                y1--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setBounds(x1, y1, 300, 300);
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.drawLine(x1, y1 + 10, x1, y1 - 10);
        g2d.drawLine(x1, y1 - 10, x1 - 5, y1 - 8);
        g2d.drawLine(x1 - 5, y1 - 7, x1, y1 - 6);
        g2d.drawLine(x1, y1 + 10, x1 + 5, y1 + 15);
        g2d.drawLine(x1, y1, x1 - 15, y1 - 5);
        g2d.drawLine(x1 - 15, y1 - 5, x1 - 30, y1);
        g2d.drawLine(x1 - 30, y1, x1 - 20, y1 + 7);
        g2d.drawLine(x1 - 20, y1 + 7, x1 - 9, y1 + 5);
        g2d.drawLine(x1 - 9, y1 + 5, x1, y1);
        g2d.drawLine(x1, y1, x1 + 15, y1 - 5);
        g2d.drawLine(x1 + 15, y1 - 5, x1 + 30, y1);
        g2d.drawLine(x1 + 30, y1, x1 + 20, y1 + 7);
        g2d.drawLine(x1 + 20, y1 + 7, x1 + 9, y1 + 5);
        g2d.drawLine(x1 + 9, y1 + 5, x1, y1);
    }
}
