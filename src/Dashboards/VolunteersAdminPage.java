import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VolunteersAdminPage {

    private static final Color BLUE = new Color(0x2563eb);
    private static final Color BLUE_LIGHT = new Color(0xeef2ff);
    private static final Color GREEN = new Color(0x16a34a);
    private static final Color GREEN_LIGHT = new Color(0xdcfce7);
    private static final Color RED = new Color(0xdc2626);
    private static final Color RED_LIGHT = new Color(0xfee2e2);
    private static final Color PURPLE_LIGHT = new Color(0xede9fe);
    private static final Color PAGE_BG = new Color(0xf3f4f6);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xe5e7eb);
    private static final Color TEXT_DARK = new Color(0x111827);
    private static final Color TEXT_MUTED = new Color(0x6b7280);
    private static final Font SANS = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SANS_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final JFrame owner;

    public VolunteersAdminPage(JFrame owner) {
        this.owner = owner;
    }

    public JComponent build() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        body.add(buildTitleCard());
        body.add(Box.createVerticalStrut(16));
        body.add(buildStatRow());
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

        JLabel icon = new JLabel("\uD83E\uDDD1\u200D\uD83E\uDD1D\u200D\uD83E\uDDD1", SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(PURPLE_LIGHT);
        icon.setPreferredSize(new Dimension(44, 44));
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconWrap.setOpaque(false);
        iconWrap.add(icon);

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("Volunteers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Manage and view all registered volunteers, including skills, availability, and activities.");
        sub.setFont(SANS);
        sub.setForeground(TEXT_MUTED);
        textBox.add(title);
        textBox.add(sub);

        JButton addBtn = new JButton("+ Add Volunteer");
        addBtn.setFont(SANS_BOLD);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(BLUE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setOpaque(true);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(owner, "Add Volunteer form goes here."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(addBtn);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel buildStatRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        row.add(statCard("\uD83E\uDDD1\u200D\uD83E\uDD1D\u200D\uD83E\uDDD1", PURPLE_LIGHT, "Total Volunteers", "8", "\u25B2 12% vs. last month", GREEN));
        row.add(statCard("\u2705", GREEN_LIGHT, "Active Volunteers", "5", "\u25B2 8% vs. last month", GREEN));
        row.add(statCard("\u274C", RED_LIGHT, "Inactive Volunteers", "1", "\u25BC 2% vs. last month", RED));
        return row;
    }

    private JPanel statCard(String icon, Color iconBg, String title, String value, String sub, Color subColor) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(iconBg);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconWrap.setOpaque(false);
        iconWrap.add(iconLabel);

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setBorder(new EmptyBorder(10, 0, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(subColor);
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textBox.add(titleLabel);
        textBox.add(valueLabel);
        textBox.add(subLabel);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
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
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Status", "Active", "Inactive", "Pending"});
        statusFilter.setFont(SANS);
        JComboBox<String> roleFilter = new JComboBox<>(new String[]{"All Roles", "Event Support", "Relief Operations", "Logistics", "Medical Support", "Community Outreach"});
        roleFilter.setFont(SANS);
        JButton filterBtn = new JButton("\u2261 Filter");
        filterBtn.setFont(SANS_BOLD);
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setBackground(BLUE);
        filterBtn.setFocusPainted(false);
        filterBtn.setBorderPainted(false);
        filterBtn.setOpaque(true);
        filterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filters.add(statusFilter);
        filters.add(roleFilter);
        filters.add(filterBtn);

        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(filters, BorderLayout.EAST);
        card.add(searchRow, BorderLayout.NORTH);

        String[] columns = {"ID", "NAME", "CONTACT", "ROLE / INTEREST", "STATUS", "DATE REGISTERED", "ACTIONS"};
        Object[][] data = {
                {"V-001", "Juan Dela Cruz", "0917 123 4567", "Event Support", "Active", "Sep 10, 2025", ""},
                {"V-002", "Maria Santos", "0918 765 4321", "Relief Operations", "Active", "Sep 9, 2025", ""},
                {"V-003", "Rafael Garcia", "0906 111 2223", "Logistics", "Active", "Sep 8, 2025", ""},
                {"V-004", "Ana Reyes", "0998 444 5566", "Medical Support", "Pending", "Sep 7, 2025", ""},
                {"V-005", "Earlene Lopez", "0927 777 8888", "Community Outreach", "Active", "Sep 6, 2025", ""},
                {"V-006", "Liza Mendoza", "0915 222 3344", "Relief Operations", "Inactive", "Sep 5, 2025", ""},
                {"V-007", "Carlo Ramos", "0933 555 6677", "Event Support", "Active", "Sep 4, 2025", ""},
                {"V-008", "Erik Santos", "0907 888 7799", "Logistics", "Pending", "Sep 3, 2025", ""}
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
        tableScroll.setPreferredSize(new Dimension(100, 280));
        card.add(tableScroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("Showing 1-8 of 8 volunteers");
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