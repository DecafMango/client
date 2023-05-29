package gui;

import client.Client;
import collection.CollectionManager;
import command.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;

public class ArgumentWindow {

    //Название команды, которое вызвало окно
    private static String commandName;

    //Размеры основного окна
    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;

    //Для размещения компонентов
    private static JFrame jFrame;

    //Компоненты
    private static JLabel enterLabel;
    private static JTextField argumentField;
    private static JButton confirmButton;

    //Поля, созданные для локализации приложения
    private static String argumentTitle;
    private static String enter;
    private static String confirm;
    private static String response;
    private static String error;
    private static String applicationError;
    private static String invalid;

    static {
        nameComponents();
        jFrame = initJFrame();
        initComponents();
        placeComponents();
    }

    public static void show(String commandName) {
        ArgumentWindow.commandName = commandName;

        jFrame.setVisible(true);
    }
    public static void hide() {
        jFrame.setVisible(false);
    }
    private static JFrame initJFrame() {
        jFrame = new JFrame(argumentTitle);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

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
        ResourceBundle bundle = Language.getBundle();
        argumentTitle = Language.getProperty("argumentTitle");
        enter = Language.getProperty("enterArgument");
        confirm = Language.getProperty("confirm");
        response = Language.getProperty("response");
        error = Language.getProperty("error");
        applicationError = Language.getProperty("applicationError");
        invalid = Language.getProperty("invalid");
    }

    public static void changeLanguage() {
        nameComponents();
        jFrame.setTitle(argumentTitle);
        enterLabel.setText(enter);
        confirmButton.setText(confirm);
    }

    private static void initComponents() {
        //enterLabel
        enterLabel = new JLabel(enter);

        //firstArgumentField
        argumentField = new JTextField();

        //confirmButton
        confirmButton = new JButton(confirm);
        confirmButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String argument = argumentField.getText();
                boolean checkInt =
                        ((CommandWithArgument) CommandManager.getCommands().get(commandName)).getArgumentType() == 1;

                try {
                    if (CommandManager.validateArgument(argument, checkInt)) {
                        Response response = Client.workWithServer(new Request(
                                CommandManager.getLogin(),
                                commandName,
                                ObjectSerializer.serializeObject(argument),
                                Language.getLanguage()
                        ));
                        String definition = response.getDefinition();
                        int code = response.getCode();

                        if (code == 0)
                            JOptionPane.showMessageDialog(jFrame, definition, ArgumentWindow.response, JOptionPane.INFORMATION_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(jFrame, definition, error, JOptionPane.ERROR_MESSAGE);

                        argumentField.setText("");
                        hide();
                        CollectionManager.updateCollection();
                    }
                } catch (IOException er) {
                    JOptionPane.showMessageDialog(jFrame, applicationError, error, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void placeComponents() {
        jFrame.setLayout(null);

        //enterLabel
        enterLabel.setBounds(85, 10, 150, 30);
        jFrame.add(enterLabel);

        //firstArgumentField
        argumentField.setBounds(80, 40, 123, 30);
        jFrame.add(argumentField);

        //confirmButton
        confirmButton.setBounds(80, 70, 123, 30);
        jFrame.add(confirmButton);
    }

}
