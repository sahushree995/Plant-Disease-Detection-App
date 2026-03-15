//ASHIMA SINGH
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginApp extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel messageLabel;

    private final String CREDENTIALS_FILE = System.getProperty("user.home") + File.separator + "credentials.txt";

    public LoginApp() {
        setTitle("Login and Registration System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\hp\\OneDrive\\Desktop\\background.png");
        backgroundPanel.setLayout(new GridBagLayout()); // Center layout
        setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Space around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Panel to hold form fields
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false); // Transparent panel over background
        formPanel.setLayout(new GridLayout(5, 1, 10, 10));

        // Username
        usernameField = new JTextField(20);
        formPanel.add(centeredLabel("Username:"));
        formPanel.add(usernameField);

        // Password
        passwordField = new JPasswordField(20);
        formPanel.add(centeredLabel("Password:"));
        formPanel.add(passwordField);

        // Buttons Panel (Register and Login)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(this);

        loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(this);

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        formPanel.add(buttonPanel);

        // Add form to background
        backgroundPanel.add(formPanel, gbc);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        backgroundPanel.add(messageLabel, gbc);

        setVisible(true);
    }

    private JLabel centeredLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(120, 40)); // Uniform button size
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (e.getSource() == registerButton) {
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both fields!");
                messageLabel.setForeground(Color.RED);
            } else {
                saveCredentials(username, password);
            }
        } else if (e.getSource() == loginButton) {
            if (validateCredentials(username, password)) {
                showWelcomeWindow(username);
            } else {
                messageLabel.setText("Invalid credentials! Try again.");
                messageLabel.setForeground(Color.RED);
            }
        }
    }

    private void saveCredentials(String username, String password) {
        try {
            File file = new File(CREDENTIALS_FILE);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(username + ":" + password + "\n");
            writer.close();

            messageLabel.setText("Registration successful!");
            messageLabel.setForeground(new Color(0, 128, 0));
        } catch (IOException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error saving credentials!");
            messageLabel.setForeground(Color.RED);
        }
    }

    private boolean validateCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials.length == 2 &&
                    credentials[0].equals(username) &&
                    credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void showWelcomeWindow(String username) {
        JFrame welcomeFrame = new JFrame("Welcome");
        welcomeFrame.setSize(800, 500);
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("C:\\Users\\hp\\OneDrive\\Desktop\\background2.png");
        panel.setLayout(new BorderLayout());
        welcomeFrame.setContentPane(panel);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));

        JButton startButton = new JButton("Start");
        styleButton(startButton);
        startButton.setPreferredSize(new Dimension(250, 70));
        startButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(welcomeFrame, "This is where your next screen goes!");
            welcomeFrame.dispose();
        });

        welcomeFrame.add(welcomeLabel, BorderLayout.CENTER);
        welcomeFrame.add(startButton, BorderLayout.SOUTH);

        welcomeFrame.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        new LoginApp();
    }
}

// Custom background panel
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
