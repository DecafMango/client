package gui;

import client.Client;
import collection.CollectionManager;
import command.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.ResourceBundle;


public class CommandWindow {

    //Размеры основного окна
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    //Для размещения компонентов
    private static JFrame jFrame;

    //Компоненты
    private static JLabel chooseLabel;
    private static JComboBox<String> commandBox;
    private static JButton enterButton;


    //Поля, созданные для локализации приложения
    private static String commandTitle;
    private static String chooseCommand;
    private static String enter;
    private static String response;
    private static String error;


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
        jFrame = new JFrame(commandTitle);
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
        commandTitle = Language.getProperty("commandTitle");
        chooseCommand = Language.getProperty("chooseCommand");
        enter = Language.getProperty("enter");
        response = Language.getProperty("response");
        error = Language.getProperty("error");
    }

    public static void changeLanguage() {
        nameComponents();
        jFrame.setTitle(commandTitle);
        chooseLabel.setText(chooseCommand);
        enterButton.setText(enter);
    }

    private static void initComponents() {
        //chooseLabel
        chooseLabel = new JLabel(chooseCommand);

        //commandBox
        Map<String, Command> commands = CommandManager.getCommands();
        commandBox = new JComboBox<>();
        for (String commandName : commands.keySet())
            commandBox.addItem(commandName);

        //enterButton
        enterButton = new JButton(enter);
        enterButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String commandName = (String) commandBox.getSelectedItem();
               Command command = commands.get(commandName);

               if (command instanceof CommandWithCreation) {
                   hide();
                   CreationWindow.show(commandName);
               } else if (command instanceof CommandWithArgument) {
                   hide();
                   ArgumentWindow.show(commandName);
               } else {
                   Response response = Client.workWithServer(new Request(CommandManager.getLogin(), commandName, null, Language.getLanguage()));
                   String definition = response.getDefinition();
                   int code = response.getCode();

                   if (code == 0)
                       JOptionPane.showMessageDialog(jFrame, definition, CommandWindow.response, JOptionPane.INFORMATION_MESSAGE);
                   else
                       JOptionPane.showMessageDialog(jFrame, definition, error, JOptionPane.ERROR_MESSAGE);

                   hide();
                   CollectionManager.updateCollection();
               }
            }
        });
    }

    private static void placeComponents() {
        jFrame.setLayout(null);

        //chooseLabel
        chooseLabel.setBounds(85, 20, 150, 30);
        jFrame.add(chooseLabel);

        //commandBox
        commandBox.setBounds(75, 50, 140, 30);
        jFrame.add(commandBox);

        //enterButton
        enterButton.setBounds(80, 80, 130, 30);
        jFrame.add(enterButton);
    }

}
