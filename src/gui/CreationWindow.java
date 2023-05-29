package gui;

import client.Client;
import collection.CollectionManager;
import command.CommandManager;
import command.ObjectSerializer;
import command.Request;
import command.Response;
import dragon.*;
import dragon.Color;
import dragon.gui_validators.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CreationWindow {

    //Название команды, которое вызвало окно
    private static String commandName;
    //id дракона, которого нужно изменить командой update_by_id
    private static int id;
    //Размеры основного окна
    private static final int WIDTH = 250;
    private static final int HEIGHT = 320;

    //Для размещения компонентов
    private static JFrame jFrame;

    //Компоненты
    private static JLabel nameLabel;
    private static JTextField nameField;
    private static JLabel xLabel;
    private static JTextField xField;
    private static JLabel yLabel;
    private static JTextField yField;
    private static JLabel ageLabel;
    private static JTextField ageField;
    private static JLabel colorLabel;
    private static JComboBox<Color> colorBox;
    private static JLabel typeLabel;
    private static JComboBox<DragonType> typeBox;
    private static JLabel characterLabel;
    private static JComboBox<DragonCharacter> characterBox;
    private static JLabel depthLabel;
    private static JTextField depthField;
    private static JButton createButton;
    private static JButton removeButton;


    //Поля, созданные для локализации приложения
    private static String creationTitle;
    private static String updateTitle;
    private static String name;
    private static String x;
    private static String y;
    private static String age;
    private static String color;
    private static String type;
    private static String character;
    private static String depth;
    private static String create;
    private static String update;
    private static String remove;
    private static String response;
    private static String error;
    private static String invalid;
    private static String applicationError;


    static {
        nameComponents();
        jFrame = initJFrame();
        initComponents();
        placeComponents();
    }

    public static void show(String commandName) {
        jFrame.setVisible(true);
        if (commandName.equals("update_by_id")) {
            jFrame.setTitle(updateTitle);
            createButton.setText(update);
        } else {
            jFrame.setTitle(creationTitle);
            createButton.setText(create);
        }
        CreationWindow.commandName = commandName;
    }

    public static void hide() {
        jFrame.setVisible(false);
    }

    private static JFrame initJFrame() {
        jFrame = new JFrame(creationTitle);
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
        creationTitle = Language.getProperty("creationTitle");
        updateTitle = Language.getProperty("updateTitle");
        name = Language.getProperty("name");
        x = Language.getProperty("x");
        y = Language.getProperty("y");
        age = Language.getProperty("age");
        color = Language.getProperty("color");
        type = Language.getProperty("type");
        character = Language.getProperty("character");
        depth = Language.getProperty("depth");
        create = Language.getProperty("create");
        update = Language.getProperty("update");
        response = Language.getProperty("response");
        error = Language.getProperty("error");
        invalid = Language.getProperty("invalid");
        applicationError = Language.getProperty("applicationError");
        remove = Language.getProperty("remove");
    }

    public static void changeLanguage() {
        nameComponents();
        jFrame.setTitle(creationTitle);
        nameLabel.setText(name);
        xLabel.setText(x);
        yLabel.setText(y);
        ageLabel.setText(age);
        colorLabel.setText(color);
        typeLabel.setText(type);
        characterLabel.setText(character);
        depthLabel.setText(depth);
        createButton.setText(create);
        removeButton.setText(remove);
    }

    private static void initComponents() {
        //nameLabel
        nameLabel = new JLabel(name);

        //nameField
        nameField = new JTextField();

        //xLabel
        xLabel = new JLabel(x);

        //xField
        xField = new JTextField();

        //yLabel
        yLabel = new JLabel(y);

        //yField
        yField = new JTextField();

        //ageLabel
        ageLabel = new JLabel(age);

        //ageField
        ageField = new JTextField();

        //colorLabel
        colorLabel = new JLabel(color);

        //colorBox
        colorBox = new JComboBox<>(new Color[]{Color.BLACK, Color.WHITE, Color.BROWN});

        //typeLabel
        typeLabel = new JLabel(type);

        //typeBox
        typeBox = new JComboBox<>(new DragonType[]{DragonType.WATER, DragonType.UNDERGROUND, DragonType.AIR, DragonType.FIRE});

        //characterLabel
        characterLabel = new JLabel(character);
        //characterBox
        characterBox = new JComboBox<>(new DragonCharacter[]{DragonCharacter.WISE, DragonCharacter.GOOD, DragonCharacter.CHAOTIC_EVIL,
                DragonCharacter.CHAOTIC_EVIL, DragonCharacter.NULL});

        //depthLabel
        depthLabel = new JLabel(depth);

        //depthField
        depthField = new JTextField();

        //createButton
        createButton = new JButton(create);
        createButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String x = xField.getText();
                String y = yField.getText();
                String age = ageField.getText();
                dragon.Color color = (Color) colorBox.getSelectedItem();
                DragonType type = (DragonType) typeBox.getSelectedItem();
                DragonCharacter character = (DragonCharacter) characterBox.getSelectedItem();
                String depth = depthField.getText();

                if (!JNameValidator.isValid(name)) {
                    JOptionPane.showMessageDialog(jFrame, invalid + " \"" + CreationWindow.name + "\"", error, JOptionPane.ERROR_MESSAGE);
                    hide();
                    return;
                }
                if (!JXValidator.isValid(x)) {
                    JOptionPane.showMessageDialog(jFrame, invalid + " \"" + CreationWindow.x + "\"", error, JOptionPane.ERROR_MESSAGE);
                    hide();
                    return;
                }
                if (!JYValidator.isValid(y)) {
                    JOptionPane.showMessageDialog(jFrame, invalid + " \"" + CreationWindow.y + "\"", error, JOptionPane.ERROR_MESSAGE);
                    hide();
                    return;
                }
                if (!JAgeValidator.isValid(age)) {
                    JOptionPane.showMessageDialog(jFrame, invalid + " \"" + CreationWindow.age + "\"", error, JOptionPane.ERROR_MESSAGE);
                    hide();
                    return;
                }
                if (!JDepthValidator.isValid(depth)) {
                    JOptionPane.showMessageDialog(jFrame, invalid + " \"" + CreationWindow.depth + "\"", error, JOptionPane.ERROR_MESSAGE);
                    hide();
                    return;
                }
                Coordinates coordinates = new Coordinates(Float.parseFloat(x), Integer.parseInt(y));

                Map<String, Object> dragonCharacteristics = new HashMap<>();
                dragonCharacteristics.put("character", character);
                dragonCharacteristics.put("depth", depth.isBlank() ? null : Integer.parseInt(depth));
                dragonCharacteristics.put("color", color);
                dragonCharacteristics.put("name", name);
                dragonCharacteristics.put("coordinates", coordinates);
                dragonCharacteristics.put("creationDate", LocalDate.now());
                dragonCharacteristics.put("type", type);
                dragonCharacteristics.put("age", Long.parseLong(age));
                dragonCharacteristics.put("owner", CommandManager.getLogin());
                if (commandName.equals("update_by_id"))
                    dragonCharacteristics.put("id", id);

                try {
                    Response response = Client.workWithServer(new Request(CommandManager.getLogin(), commandName,
                            ObjectSerializer.serializeObject(dragonCharacteristics), Language.getLanguage()));
                    String definition = response.getDefinition();
                    int code = response.getCode();

                    if (code == 0)
                        JOptionPane.showMessageDialog(jFrame, definition, CreationWindow.response, JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(jFrame, definition, error, JOptionPane.ERROR_MESSAGE);

                } catch (IOException er) {
                    JOptionPane.showMessageDialog(jFrame, applicationError, error, JOptionPane.ERROR_MESSAGE);
                }

                nameField.setText("");
                xField.setText("");
                yField.setText("");
                ageField.setText("");
                depthField.setText("");

                hide();
                CollectionManager.updateCollection();
            }
        });
        //removeButton
        removeButton = new JButton(remove);
        removeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Response response1 = Client.workWithServer(new Request(
                            CommandManager.getLogin(),
                            "remove_by_id",
                            ObjectSerializer.serializeObject(id),
                            Language.getLanguage()
                    ));
                    int code = response1.getCode();
                    String definition = response1.getDefinition();
                    if (code == 0) {
                        JOptionPane.showMessageDialog(jFrame, definition, CreationWindow.response, JOptionPane.INFORMATION_MESSAGE);
                        DragonsWindow.removeDragon();
                    } else
                        JOptionPane.showMessageDialog(jFrame, definition, error, JOptionPane.ERROR_MESSAGE);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(jFrame, applicationError, error, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void placeComponents() {
        jFrame.setLayout(null);

        //nameLabel
        nameLabel.setBounds(10, 10, 50, 30);
        jFrame.add(nameLabel);

        //nameField
        nameField.setBounds(120, 10, 100, 30);
        jFrame.add(nameField);

        //xLabel
        xLabel.setBounds(10, 40, 50, 30);
        jFrame.add(xLabel);

        //xField
        xField.setBounds(120, 40, 100, 30);
        jFrame.add(xField);

        //yLabel
        yLabel.setBounds(10, 70, 50, 30);
        jFrame.add(yLabel);

        //yField
        yField.setBounds(120, 70, 100, 30);
        jFrame.add(yField);

        //ageLabel
        ageLabel.setBounds(10, 100, 50, 30);
        jFrame.add(ageLabel);

        //ageField
        ageField.setBounds(120, 100, 100, 30);
        jFrame.add(ageField);

        //colorLabel
        colorLabel.setBounds(10, 130, 50, 30);
        jFrame.add(colorLabel);

        //colorBox
        colorBox.setBounds(120, 130, 100, 30);
        jFrame.add(colorBox);

        //typeLabel
        typeLabel.setBounds(10, 160, 50, 30);
        jFrame.add(typeLabel);

        //typeBox
        typeBox.setBounds(120, 160, 100, 30);
        jFrame.add(typeBox);

        //characterLabel
        characterLabel.setBounds(10, 190, 70, 30);
        jFrame.add(characterLabel);

        //characterBox
        characterBox.setBounds(120, 190, 100, 30);
        jFrame.add(characterBox);

        //depthLabel
        depthLabel.setBounds(10, 220, 70, 30);
        jFrame.add(depthLabel);

        //depthField
        depthField.setBounds(120, 220, 100, 30);
        jFrame.add(depthField);

        //createButton
        createButton.setBounds(10, 255, 100, 30);
        jFrame.add(createButton);

        //removeButton
        removeButton.setBounds(120, 255, 100, 30);
        jFrame.add(removeButton);
    }

    public static void fillFields(String name, float x, int y, Long age, Color color,
                                  DragonType type, DragonCharacter character, Integer depth) {
        nameField.setText(name);
        xField.setText(Float.toString(x));
        yField.setText(Integer.toString(y));
        ageField.setText(Long.toString(age));
        colorBox.setSelectedItem(color);
        typeBox.setSelectedItem(type);
        characterBox.setSelectedItem(character);
        depthField.setText(depth == null ? null : Integer.toString(depth));
    }

    public static void setId(int id) {
        CreationWindow.id = id;
    }
}
