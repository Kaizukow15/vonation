import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VolunteerRegistrationPage {

    private static final Color BLUE = new Color(0x2563eb);
    private static final Color BLUE_LIGHT = new Color(0xeef2ff);
    private static final Color PAGE_BG = new Color(0xf3f4f6);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xe5e7eb);
    private static final Color TEXT_DARK = new Color(0x111827);
    private static final Color TEXT_MUTED = new Color(0x6b7280);
    private static final Font SANS = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SANS_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final JTextField fullNameField = new JTextField();
    private final JTextField contactField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JComboBox<String> availabilityBox =
            new JComboBox<>(new String[]{"Weekdays", "Weekends", "Anytime"});
    private final JTextArea skillsArea = new JTextArea(3, 20);
    private final JLabel statusLabel = new JLabel(" ");

    private final Runnable onBack;

    public VolunteerRegistrationPage(String username, Runnable onBack) {
        this.onBack = onBack;
    }

    public JComponent build() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        body.add(buildTitleCard());
        body.add(Box.createVerticalStrut(16));
        body.add(buildFormCard());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PAGE_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel buildTitleCard() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel icon = new JLabel("\uD83D\uDCDD", SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(BLUE_LIGHT);
        icon.setPreferredSize(new Dimension(44, 44));
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconWrap.setOpaque(false);
        iconWrap.add(icon);

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("Volunteer Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Fill out your details to register as a volunteer.");
        sub.setFont(SANS);
        sub.setForeground(TEXT_MUTED);
        textBox.add(title);
        textBox.add(sub);

        JButton backBtn = new JButton("\u2190 Back");
        backBtn.setFont(SANS_BOLD);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(backBtn);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel buildFormCard() {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(formLabel("Full Name"));
        styleField(fullNameField);
        card.add(fullNameField);
        card.add(Box.createVerticalStrut(14));

        card.add(formLabel("Contact Number"));
        styleField(contactField);
        card.add(contactField);
        card.add(Box.createVerticalStrut(14));

        card.add(formLabel("Address"));
        styleField(addressField);
        card.add(addressField);
        card.add(Box.createVerticalStrut(14));

        card.add(formLabel("Availability"));
        availabilityBox.setFont(SANS);
        availabilityBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        availabilityBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        card.add(availabilityBox);
        card.add(Box.createVerticalStrut(14));

        card.add(formLabel("Skills / Notes"));
        skillsArea.setFont(SANS);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);
        skillsArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 8, 6, 8)));
        JScrollPane skillsScroll = new JScrollPane(skillsArea);
        skillsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        skillsScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.add(skillsScroll);
        card.add(Box.createVerticalStrut(10));

        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(0x16a34a));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(10));

        JButton submitBtn = new JButton("Submit Registration");
        submitBtn.setFont(SANS_BOLD);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(BLUE);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(e -> submit());
        card.add(submitBtn);

        return card;
    }

    private void submit() {
        if (fullNameField.getText().trim().isEmpty() || contactField.getText().trim().isEmpty()) {
            statusLabel.setForeground(new Color(0x9b2c2c));
            statusLabel.setText("Please fill out your full name and contact number.");
            return;
        }

        // Palitan ito ng totoong pag-save (database, file, atbp.)
        statusLabel.setForeground(new Color(0x16a34a));
        statusLabel.setText("Registration submitted! We will contact you soon.");
    }

    private JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SANS_BOLD);
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 6, 0));
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(SANS);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 8, 6, 8)));
    }

    private JPanel roundedCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(14, 16, 14, 16)));
        return card;
    }
}