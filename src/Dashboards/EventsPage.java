import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EventsPage {

    private static final Color BLUE = new Color(0x2563eb);
    private static final Color BLUE_LIGHT = new Color(0xeef2ff);
    private static final Color PAGE_BG = new Color(0xf3f4f6);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xe5e7eb);
    private static final Color TEXT_DARK = new Color(0x111827);
    private static final Color TEXT_MUTED = new Color(0x6b7280);
    private static final Font SANS = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SANS_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final JFrame owner;

    public EventsPage(JFrame owner) {
        this.owner = owner;
    }

    public JComponent build() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        body.add(buildTitleCard());
        body.add(Box.createVerticalStrut(16));
        body.add(buildTableCard());

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

        JLabel icon = new JLabel("\uD83D\uDDD3", SwingConstants.CENTER);
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
        JLabel title = new JLabel("Events & Programs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Create and manage all community events and programs.");
        sub.setFont(SANS);
        sub.setForeground(TEXT_MUTED);
        textBox.add(title);
        textBox.add(sub);

        JButton addBtn = new JButton("+ Add Event");
        addBtn.setFont(SANS_BOLD);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(BLUE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setOpaque(true);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(owner, "Add Event form goes here."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(addBtn);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(0, 10));

        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        JTextField search = new JTextField();
        search.setFont(SANS);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filters.setOpaque(false);
        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All Types", "Environment", "Health", "Relief", "Training"});
        typeFilter.setFont(SANS);
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Status", "Upcoming", "Scheduled", "Registration Open", "Completed"});
        statusFilter.setFont(SANS);
        filters.add(typeFilter);
        filters.add(statusFilter);

        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(filters, BorderLayout.EAST);
        card.add(searchRow, BorderLayout.NORTH);

        String[] columns = {"EVENT NAME", "DATE & TIME", "LOCATION", "TYPE", "STATUS", "JOINED", "ACTIONS"};
        Object[][] data = {
                {"Community Clean-Up Drive", "Sep 25, 2025 / 8:00 AM \u2013 12:00 PM", "Riverside Community Park", "Environment", "Upcoming", "0", ""},
                {"Blood Donation Drive", "Oct 12, 2025 / 9:00 AM \u2013 3:00 PM", "City Health Center", "Health", "Scheduled", "0", ""},
                {"Relief Goods Distribution", "Sep 14, 2025 / 6:00 AM \u2013 12:00 PM", "Brgy. San Antonio, Tuy, Batangas", "Relief", "Upcoming", "0", ""},
                {"Volunteer Training Workshop", "Sep 11, 2025 / 9:00 AM \u2013 4:00 PM", "Municipal Hall, Tuy, Batangas", "Training", "Registration Open", "0", ""},
                {"Tree Planting Activity", "Nov 5, 2025 / 7:00 AM \u2013 11:00 AM", "Tuyon-Tuyon River, Tuy, Batangas", "Environment", "Upcoming", "0", ""},
                {"Medical Mission", "Sep 4, 2025 / 6:00 AM \u2013 3:00 PM", "Barangay Calicanto, Tuy, Batangas", "Health", "Completed", "0", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(SANS);
        table.setRowHeight(34);
        table.getTableHeader().setFont(SANS_BOLD);
        table.getTableHeader().setBackground(PAGE_BG);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        tableScroll.setPreferredSize(new Dimension(100, 240));
        card.add(tableScroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("Showing 1-6 of 6 events");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(TEXT_MUTED);
        card.add(footer, BorderLayout.SOUTH);

        return card;
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