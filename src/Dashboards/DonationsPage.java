import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DonationsPage extends JFrame {

    private static final Color BLUE = new Color(0x2563eb);
    private static final Color BLUE_LIGHT = new Color(0xeef2ff);
    private static final Color GREEN = new Color(0x16a34a);
    private static final Color GREEN_LIGHT = new Color(0xdcfce7);
    private static final Color ORANGE_LIGHT = new Color(0xffedd5);
    private static final Color PAGE_BG = new Color(0xf3f4f6);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xe5e7eb);
    private static final Color TEXT_DARK = new Color(0x111827);
    private static final Color TEXT_MUTED = new Color(0x6b7280);
    private static final Font SANS = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SANS_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public DonationsPage(String username) {
        super("Donations");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(buildHeaderBar(), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildHeaderBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(10, 20, 10, 20)));

        JLabel brand = new JLabel("<html><b>Volunteer and Donation Management System</b><br>"
                + "<span style='font-size:9px;color:#6b7280'>Peace, Justice and Strong Institutions</span></html>");
        brand.setFont(SANS);
        bar.add(brand, BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        nav.setOpaque(false);
        nav.add(navItem("\uD83C\uDFE0 Home", false));
        nav.add(navItem("\uD83D\uDD25 Donations", true));
        nav.add(navItem("\uD83D\uDC65 Volunteers & Events", false));
        bar.add(nav, BorderLayout.CENTER);

        return bar;
    }

    private JLabel navItem(String text, boolean active) {
        JLabel label = new JLabel(text);
        label.setFont(SANS_BOLD);
        label.setForeground(active ? BLUE : TEXT_DARK);
        label.setOpaque(active);
        label.setBackground(BLUE_LIGHT);
        label.setBorder(new EmptyBorder(6, 12, 6, 12));
        return label;
    }

    private JComponent buildBody() {
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel icon = new JLabel("\uD83D\uDCB0", SwingConstants.CENTER);
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
        JLabel title = new JLabel("My Donations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Your personal donation history and contributions to the community.");
        sub.setFont(SANS);
        sub.setForeground(TEXT_MUTED);
        textBox.add(title);
        textBox.add(sub);

        JButton donateBtn = new JButton("\uD83D\uDED2 Donate");
        donateBtn.setFont(SANS_BOLD);
        donateBtn.setForeground(Color.WHITE);
        donateBtn.setBackground(BLUE);
        donateBtn.setFocusPainted(false);
        donateBtn.setBorderPainted(false);
        donateBtn.setOpaque(true);
        donateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        donateBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Donate form goes here."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(donateBtn);

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

        row.add(statCard("\uD83D\uDCB0", BLUE_LIGHT, "Total Donations Made", "1", "My contributions"));
        row.add(statCard("\uD83D\uDCB5", GREEN_LIGHT, "Cash Donations", "1", "Cash given"));
        row.add(statCard("\uD83D\uDCE6", ORANGE_LIGHT, "In-Kind Donations", "0", "Goods donated"));
        return row;
    }

    private JPanel statCard(String icon, Color iconBg, String title, String value, String sub) {
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
        subLabel.setForeground(GREEN);
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
        search.putClientProperty("JTextField.placeholderText", "Search donations...");
        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All Types", "Cash", "In-Kind"});
        typeFilter.setFont(SANS);
        searchRow.add(search, BorderLayout.CENTER);
        searchRow.add(typeFilter, BorderLayout.EAST);
        card.add(searchRow, BorderLayout.NORTH);

        String[] columns = {"ID", "TYPE", "AMOUNT / ITEMS", "DATE", "NOTE", "STATUS", "ACTIONS"};
        Object[][] data = {
                {"D-0001", "Cash", "22", "Sep 30, 2026", "2", "Received", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(SANS);
        table.setRowHeight(30);
        table.getTableHeader().setFont(SANS_BOLD);
        table.getTableHeader().setBackground(PAGE_BG);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        tableScroll.setPreferredSize(new Dimension(100, 120));
        card.add(tableScroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("Showing 1-1 of 1 donations");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(TEXT_MUTED);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JPanel roundedCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(14, 16, 14, 16)));
        return card;
    }
}