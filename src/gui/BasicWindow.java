package gui;

import collection.CollectionManager;
import command.CommandManager;
import dragon.Color;
import dragon.Dragon;
import dragon.DragonCharacter;
import dragon.DragonType;
import dragon.comparators.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Comparator;
import java.util.ResourceBundle;

public class BasicWindow {

    //Размеры основного окна
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;

    //Для размещения компонентов
    private static JFrame jFrame;


    //Компоненты
    private static JLabel languageLabel;
    private static JComboBox<String> languagesBox;
    private static JLabel loginLabel;
    private static JLabel sortLabel;
    private static JComboBox<Comparator<Dragon>> sortBox;
    private static JTable dragonsTable;
    private static JScrollPane scrollPane;
    private static JButton commandsButton;
    private static JButton changeViewButton;
    private static JButton exitButton;

    //Поля, созданные для локализации приложения
    private static String basicTitle;
    private static String language;
    private static String login = CommandManager.getLogin();
    private static String sort;
    private static String[] columnNames;
    private static String commands;
    private static String changeView;
    private static String exit;
    private static String confirm;
    private static String sure;
    private static String[] exitOptions;

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
        jFrame = new JFrame(basicTitle);
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
        ResourceBundle bundle = Language.getBundle();
        basicTitle = Language.getProperty("basicTitle");
        language = Language.getProperty("language");
        sort = Language.getProperty("sort");
        columnNames = new String[] {
                Language.getProperty("id"),
                Language.getProperty("name"),
                Language.getProperty("x"),
                Language.getProperty("y"),
                Language.getProperty("date"),
                Language.getProperty("age"),
                Language.getProperty("color"),
                Language.getProperty("type"),
                Language.getProperty("character"),
                Language.getProperty("depth"),
        };
        commands = Language.getProperty("commands");
        changeView = Language.getProperty("changeView");
        exit = Language.getProperty("exit");
        confirm = Language.getProperty("confirm");
        sure = Language.getProperty("sure");
        exitOptions = new String[] {Language.getProperty("yes"), Language.getProperty("no")};
    }

    public static void changeLanguage() {
        nameComponents();
        jFrame.setTitle(basicTitle);
        languageLabel.setText(language);
        sortLabel.setText(sort);
        commandsButton.setText(commands);
        changeViewButton.setText(changeView);
        exitButton.setText(exit);
        jFrame.remove(sortBox);
        sortBox = new JComboBox<>(new Comparator[]{new IdComparator(), new XComparator(), new YComparator(), new NameComparator(),
                new LocalDateComparator(), new AgeComparator(), new ColorComparator(), new DragonTypeComparator(),
                new DragonCharacterComparator(), new DepthComparator()});
        sortBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Comparator<Dragon> comparator = (Comparator<Dragon>) sortBox.getSelectedItem();
                CollectionManager.setComparator(comparator);
                CollectionManager.updateCollection();
            }
        });
        sortBox.setBounds(300, 35, 110, 30);
        jFrame.add(sortBox);
    }

    private static void initComponents() {
        //languageLabel
        languageLabel = new JLabel(language);

        //languagesBox
        String[] languagesNames = {"русский", "íslenskur", "shqiptare", "english"};
        languagesBox = new JComboBox<>(languagesNames);
        languagesBox.setSelectedItem(Language.getLanguage());
        languagesBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Language.setLanguage((String) languagesBox.getSelectedItem());
            }
        });

        //userLabel
        loginLabel = new JLabel(login, SwingConstants.CENTER);

        //sortLabel
        sortLabel = new JLabel(sort);

        //sortBox
        sortBox = new JComboBox<>(new Comparator[]{new IdComparator(), new XComparator(), new YComparator(), new NameComparator(),
                new LocalDateComparator(), new AgeComparator(), new ColorComparator(), new DragonTypeComparator(),
                new DragonCharacterComparator(), new DepthComparator()});
        sortBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Comparator<Dragon> comparator = (Comparator<Dragon>) sortBox.getSelectedItem();
                CollectionManager.setComparator(comparator);
                CollectionManager.updateCollection();
            }
        });

        //commandsButton
        commandsButton = new JButton(commands);
        commandsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandWindow.show();
            }
        });

        //changeViewButton
        changeViewButton = new JButton(changeView);
        changeViewButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DragonsWindow.show();
            }
        });

        //exitButton
        exitButton = new JButton(exit);
        exitButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showOptionDialog(jFrame, confirm, confirm, JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, exitOptions, exitOptions[0]);
                if (answer == 0) {
                    hide();
                    StartWindow.show();
                }
            }
        });
    }

    private static void placeComponents() {
        jFrame.setLayout(null);

        //languageLabel
        languageLabel.setBounds(10, 10, 60, 30);
        jFrame.add(languageLabel);

        //languagesBox
        languagesBox.setBounds(70, 10, 110, 30);
        jFrame.add(languagesBox);

        //loginLabel
        loginLabel.setBounds(430, 10, 200, 30);
        jFrame.add(loginLabel);

        //sortLabel
        sortLabel.setBounds(190, 35, 130, 30);
        jFrame.add(sortLabel);

        //sortBox
        sortBox.setBounds(300, 35, 110, 30);
        jFrame.add(sortBox);

        //commandsButton
        commandsButton.setBounds(5, 440, 100, 30);
        jFrame.add(commandsButton);

        //changeViewButton
        changeViewButton.setBounds(220, 440, 150, 30);
        jFrame.add(changeViewButton);

        //exitButton
        exitButton.setBounds(495, 440, 100, 30);
        jFrame.add(exitButton);

    }

    public static void updateData(String[][] data) {
        if (dragonsTable != null) {
            jFrame.remove(dragonsTable);
            jFrame.remove(scrollPane);
        }
        dragonsTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dragonsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int rowIndex = dragonsTable.getSelectedRow();
                TableModel model = dragonsTable.getModel();

                int id = Integer.parseInt((String) model.getValueAt(rowIndex, 0));
                String name = (String) model.getValueAt(rowIndex, 1);
                float x = Float.parseFloat((String) model.getValueAt(rowIndex, 2));
                int y = Integer.parseInt((String) model.getValueAt(rowIndex, 3));
                long age = Long.parseLong((String) model.getValueAt(rowIndex, 5));
                dragon.Color color = Color.valueOf((String) model.getValueAt(rowIndex, 6));
                DragonType type = DragonType.valueOf((String) model.getValueAt(rowIndex, 7));
                DragonCharacter character = DragonCharacter.valueOf((String) model.getValueAt(rowIndex, 8));
                Integer depth = null;
                if (!((String) model.getValueAt(rowIndex, 9)).equals("null"))
                    depth = Integer.parseInt((String) model.getValueAt(rowIndex, 9));
                CreationWindow.setId(id);
                CreationWindow.fillFields(name, x, y, age, color, type, character, depth);
                CreationWindow.show("update_by_id");
            }
        });

        scrollPane = new JScrollPane(dragonsTable);

        scrollPane.setBounds(0, 70, 600, 350);
        jFrame.add(scrollPane);
        jFrame.revalidate();
    }
}

