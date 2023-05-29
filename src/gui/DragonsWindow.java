package gui;

import dragon.Dragon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DragonsWindow {

    //Размеры основного окна
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;

    //Для размещения компонентов
    private static JFrame jFrame;

    //Компоненты
    private static JButton changeViewButton;

    //Поля, созданные для локализации приложения
    private static String dragonWindow = "Окно с драконами";
    private static String changeView = "Изменить вид";

    static {
        nameComponents();
        jFrame = initJFrame();
        initComponents();
        placeComponents();
    }

    public static void show() {
        jFrame.setVisible(true);
    }
    public static void hide() {
        jFrame.setVisible(false);
    }

    private static JFrame initJFrame() {
        jFrame = new JFrame(dragonWindow);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setBounds(
                (int) (dimension.getWidth() / 2 - WIDTH / 2),
                (int) (dimension.getHeight() / 2 - HEIGHT / 2),
                WIDTH,
                HEIGHT
        );
        return jFrame;
    }

    public static void nameComponents() {
        dragonWindow = Language.getProperty("dragonTitle");
        changeView = Language.getProperty("changeView");
    }
    public static void changeLanguage() {
        nameComponents();
        dragonWindow = Language.getProperty("dragonTitle");
        jFrame.setTitle(dragonWindow);
        changeViewButton.setText(Language.getProperty("changeView"));
    }

    private static void initComponents() {
        changeViewButton = new JButton(changeView);
        changeViewButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
                BasicWindow.show();
            }
        });
    }

    private static void placeComponents() {
        jFrame.setLayout(null);

        //changeViewButton
        changeViewButton.setBounds(220, 440, 150, 30);
        jFrame.add(changeViewButton);
    }

    public static void paintDragon(Dragon dragon) {
        DragonModel model = new DragonModel(jFrame, dragon);
        jFrame.add(model);
        new Thread(model).start();
    }

    public static void removeDragon() {
        jFrame.setVisible(false);
        nameComponents();
        jFrame = initJFrame();
        initComponents();
        placeComponents();
        show();
    }
}

