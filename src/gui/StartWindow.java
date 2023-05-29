package gui;

import client.Client;
import collection.CollectionManager;
import command.CommandManager;
import command.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

public class StartWindow {

    //Размеры стартового окна
    private static final int WIDTH = 250;
    private static final int HEIGHT = 200;

    //Для размещения компонентов
    private static JFrame jFrame;
    private static JPanel jPanel;

    //Компоненты стартового окна
    private static JLabel languageLabel;
    private static JComboBox<String> languagesBox;
    private static JLabel loginLabel;
    private static JLabel passwordLabel;
    private static JTextField loginField;
    private static JPasswordField passwordField;
    private static JRadioButton loginRadioButton;
    private static JRadioButton registerRadioButton;
    private static JButton enterButton;

    //Поля, созданные для локализации приложения
    private static String language;
    private static String startTitle;
    private static String name;
    private static String password;
    private static String login;
    private static String register;
    private static String enter;
    private static String error;


    static {
        nameComponents();

        jFrame = initJFrame();
        jPanel = new JPanel();
        jFrame.add(jPanel);

        initComponents();
        placeComponents();
    }

    public static void show() {
        jFrame.setVisible(true);
    }

    public static void hide() {
        jFrame.dispose();
    }

    private static JFrame initJFrame() {
        jFrame = new JFrame(startTitle);
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
        language = Language.getProperty("language");
        startTitle = Language.getProperty("startTitle");
        name = Language.getProperty("name");
        password = Language.getProperty("password");
        login = Language.getProperty("login");
        register = Language.getProperty("register");
        enter = Language.getProperty("enter");
        error = Language.getProperty("error");
    }

    public static void changeLanguage() {
        nameComponents();
        languageLabel.setText(language);
        jFrame.setTitle(startTitle);
        loginLabel.setText(name);
        passwordLabel.setText(password);
        loginRadioButton.setText(login);
        registerRadioButton.setText(register);
        enterButton.setText(enter);
    }

    private static void initComponents() {
        languageLabel = new JLabel(language);

        String[] languagesNames = {"русский", "íslenskur", "shqiptare", "english"};
        languagesBox = new JComboBox<>(languagesNames);
        languagesBox.setSelectedItem(Language.getLanguage());
        languagesBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Language.setStartLanguage((String) languagesBox.getSelectedItem());
            }
        });

        loginLabel = new JLabel(name);
        passwordLabel = new JLabel(password);

        loginField = new JTextField(7);
        passwordField = new JPasswordField(7);

        ButtonGroup buttonGroup = new ButtonGroup();

        loginRadioButton = new JRadioButton(login);
        loginRadioButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterButton.setText(login);
            }
        });
        buttonGroup.add(loginRadioButton);

        registerRadioButton = new JRadioButton(register);
        registerRadioButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterButton.setText(register);
            }
        });
        buttonGroup.add(registerRadioButton);
        loginRadioButton.setSelected(true);

        enterButton = new JButton(enter);
        enterButton.addActionListener(new enterButtonAction());
    }

    private static void placeComponents() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel.setLayout(gridBagLayout);

        //Размещаем languageLabel
        GridBagConstraints languageLabelConstraints = new GridBagConstraints();
        languageLabelConstraints.weightx = 0;
        languageLabelConstraints.weighty = 0;
        languageLabelConstraints.gridx = 0;
        languageLabelConstraints.gridy = 0;
        languageLabelConstraints.gridwidth = 1;
        languageLabelConstraints.gridheight = 1;
        jPanel.add(languageLabel, languageLabelConstraints);

        //Размещаем languagesBox
        GridBagConstraints languagesBoxConstraints = new GridBagConstraints();
        languagesBoxConstraints.weightx = 0;
        languagesBoxConstraints.weighty = 0;
        languagesBoxConstraints.gridx = 1;
        languagesBoxConstraints.gridy = 0;
        languagesBoxConstraints.gridwidth = 1;
        languagesBoxConstraints.gridheight = 1;
        jPanel.add(languagesBox, languagesBoxConstraints);

        //Размещаем loginLabel
        GridBagConstraints loginLabelConstraints = new GridBagConstraints();
        loginLabelConstraints.weightx = 0;
        loginLabelConstraints.weighty = 0;
        loginLabelConstraints.gridx = 0;
        loginLabelConstraints.gridy = 1;
        loginLabelConstraints.gridwidth = 1;
        loginLabelConstraints.gridheight = 1;
        jPanel.add(loginLabel, loginLabelConstraints);

        //Размещаем loginField
        GridBagConstraints loginFieldConstraints = new GridBagConstraints();
        loginFieldConstraints.weightx = 0;
        loginFieldConstraints.weighty = 0;
        loginFieldConstraints.gridx = 1;
        loginFieldConstraints.gridy = 1;
        loginFieldConstraints.gridwidth = 2;
        loginFieldConstraints.gridheight = 1;
        jPanel.add(loginField, loginFieldConstraints);

        //Размещаем passwordLabel
        GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
        passwordLabelConstraints.weightx = 0;
        passwordLabelConstraints.weighty = 0;
        passwordLabelConstraints.gridx = 0;
        passwordLabelConstraints.gridy = 2;
        passwordLabelConstraints.gridwidth = 1;
        passwordLabelConstraints.gridheight = 1;
        jPanel.add(passwordLabel, passwordLabelConstraints);

        //Размещаем passwordField
        GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
        passwordFieldConstraints.weightx = 0;
        passwordFieldConstraints.weighty = 0;
        passwordFieldConstraints.gridx = 1;
        passwordFieldConstraints.gridy = 2;
        passwordFieldConstraints.gridwidth = 2;
        passwordFieldConstraints.gridheight = 1;
        jPanel.add(passwordField, passwordFieldConstraints);

        //Размещаем loginRadioButton
        GridBagConstraints loginRadioButtonConstraints = new GridBagConstraints();
        loginRadioButtonConstraints.weightx = 0;
        loginRadioButtonConstraints.weighty = 0;
        loginRadioButtonConstraints.gridx = 0;
        loginRadioButtonConstraints.gridy = 3;
        loginRadioButtonConstraints.gridwidth = 1;
        loginRadioButtonConstraints.gridheight = 1;
        jPanel.add(loginRadioButton, loginRadioButtonConstraints);

        //Размещаем registerRadioButton
        GridBagConstraints registerRadioButtonConstraints = new GridBagConstraints();
        registerRadioButtonConstraints.weightx = 0;
        registerRadioButtonConstraints.weighty = 0;
        registerRadioButtonConstraints.gridx = 1;
        registerRadioButtonConstraints.gridy = 3;
        registerRadioButtonConstraints.gridwidth = 1;
        registerRadioButtonConstraints.gridheight = 1;
        jPanel.add(registerRadioButton, registerRadioButtonConstraints);

        //Размещаем enterButton
        GridBagConstraints enterButtonConstraint = new GridBagConstraints();
        enterButtonConstraint.weightx = 0;
        enterButtonConstraint.weighty = 0;
        enterButtonConstraint.gridx = 0;
        enterButtonConstraint.gridy = 4;
        enterButtonConstraint.gridwidth = 2;
        enterButtonConstraint.gridheight = 1;
        jPanel.add(enterButton, enterButtonConstraint);
    }

    private static class enterButtonAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            Response response = Client.establishConnection(login, password, !loginRadioButton.isSelected());
            String definition = response.getDefinition();
            int code = response.getCode();
            System.out.println(CommandManager.getCommands());
            if (code == 0) {
                CommandManager.setLogin(login);
                hide();
                CollectionManager.updateCollection();
                BasicWindow.show();
            } else
                JOptionPane.showMessageDialog(jFrame, definition, StartWindow.error, JOptionPane.ERROR_MESSAGE);


            loginField.setText("");
            passwordField.setText("");
        }
    }
}
