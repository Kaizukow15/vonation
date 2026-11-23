import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RecordsAdminPage {

    private static final Color BLUE = new Color(0x2563eb);
    private static final Color BLUE_LIGHT = new Color(0xeef2ff);
    private static final Color GREEN = new Color(0x16a34a);
    private static final Color ORANGE_LIGHT = new Color(0xffedd5);
    private static final Color PURPLE_LIGHT = new Color(0xede9fe);
    private static final Color YELLOW_LIGHT = new Color(0xfef9c3);
    private static final Color PAGE_BG = new Color(0xf3f4f6);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0xe5e7eb);
    private static final Color TEXT_DARK = new Color(0x111827);
    private static final Color TEXT_MUTED = new Color(0x6b7280);
    private static final Font SANS = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font SANS_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final JFrame owner;

    public RecordsAdminPage(JFrame owner) {
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
        body.add(buildPanelsRow());

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

        JLabel icon = new JLabel("\uD83D\uDCCB", SwingConstants.CENTER);
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
        JLabel title = new JLabel("Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Access and manage all system records \u2014 volunteers, donations, events, and partners.");
        sub.setFont(SANS);
        sub.setForeground(TEXT_MUTED);
        textBox.add(title);
        textBox.add(sub);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildStatRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        row.add(statCard("\uD83D\uDC65", PURPLE_LIGHT, "Volunteer Records", "8"));
        row.add(statCard("\uD83D\uDCB0", ORANGE_LIGHT, "Donation Records", "8"));
        row.add(statCard("\uD83D\uDCC5", BLUE_LIGHT, "Event Registrations", "6"));
        row.add(statCard("\uD83E\uDD1D", YELLOW_LIGHT, "Partner Organizations", "12"));
        return row;
    }

    private JPanel statCard(String icon, Color iconBg, String title, String value) {
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

        JLabel viewAll = new JLabel("View all \u2192");
        viewAll.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        viewAll.setForeground(BLUE);
        viewAll.setAlignmentX(Component.LEFT_ALIGNMENT);

        textBox.add(titleLabel);
        textBox.add(valueLabel);
        textBox.add(viewAll);

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildPanelsRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 16);
        row.add(buildTableCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        row.add(buildRightColumn(), gbc);

        return row;
    }

    private JPanel buildTableCard() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(0, 10));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tabs.setOpaque(false);
        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabs.add(tabButton("All Records", true));
        tabs.add(tabButton("Volunteers", false));
        tabs.add(tabButton("Donations", false));
        tabs.add(tabButton("Events", false));
        top.add(tabs);
        top.add(Box.createVerticalStrut(10));

        JTextField search = new JTextField();
        search.setFont(SANS);
        search.setAlignmentX(Component.LEFT_ALIGNMENT);
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)));
        top.add(search);

        card.add(top, BorderLayout.NORTH);

        String[] columns = {"", "ID", "TYPE", "NAME / TITLE", "DETAILS", "DATE", "STATUS", "ACTIONS"};
        Object[][] data = {
                {false, "V-001", "Volunteer", "Juan Dela Cruz", "Event Support", "Sep 10, 2025", "Active", "View"},
                {false, "V-002", "Volunteer", "Maria Santos", "Relief Operations", "Sep 9, 2025", "Active", "View"},
                {false, "V-003", "Volunteer", "Rafael Garcia", "Logistics", "Sep 8, 2025", "Active", "View"},
                {false, "V-004", "Volunteer", "Ana Reyes", "Medical Support", "Sep 7, 2025", "Pending", "View"},
                {false, "V-005", "Volunteer", "Earlene Lopez", "Community Outreach", "Sep 6, 2025", "Active", "View"},
                {false, "V-006", "Volunteer", "Liza Mendoza", "Relief Operations", "Sep 5, 2025", "Inactive", "View"},
                {false, "V-007", "Volunteer", "Carlo Ramos", "Event Support", "Sep 4, 2025", "Active", "View"},
                {false, "V-008", "Volunteer", "Erik Santos", "Logistics", "Sep 3, 2025", "Pending", "View"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : String.class;
            }
            @Override public boolean isCellEditable(int row, int col) { return col == 0; }
        };
        JTable table = new JTable(model);
        table.setFont(SANS);
        table.setRowHeight(32);
        table.getTableHeader().setFont(SANS_BOLD);
        table.getTableHeader().setBackground(PAGE_BG);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getColumnModel().getColumn(0).setMaxWidth(30);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        tableScroll.setPreferredSize(new Dimension(100, 300));
        card.add(tableScroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JLabel showing = new JLabel("Showing 1-8 of 22 records");
        showing.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showing.setForeground(TEXT_MUTED);
        footer.add(showing, BorderLayout.WEST);

        JPanel pageNums = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pageNums.setOpaque(false);
        pageNums.add(pageButton("\u2039", false));
        pageNums.add(pageButton("1", true));
        pageNums.add(pageButton("2", false));
        pageNums.add(pageButton("3", false));
        pageNums.add(pageButton("\u203A", false));
        footer.add(pageNums, BorderLayout.EAST);

        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JButton tabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(SANS_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (active) {
            btn.setBackground(BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(PAGE_BG);
            btn.setForeground(TEXT_DARK);
        }
        return btn;
    }

    private JButton pageButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(SANS_BOLD);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(30, 28));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (active) {
            btn.setBackground(BLUE);
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(TEXT_DARK);
            btn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        }
        return btn;
    }

    private JPanel buildRightColumn() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        col.add(buildDonationOverviewPanel());
        col.add(Box.createVerticalStrut(16));
        col.add(buildRecentActivityPanel());
        return col;
    }

    private JPanel buildDonationOverviewPanel() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("\uD83D\uDD25 Donation Overview");
        title.setFont(SANS_BOLD);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(donationRow("Total Donations", "\u20B125,780", null));
        list.add(divider());
        list.add(donationRow("Cash", "\u20B115,780", "61%"));
        list.add(divider());
        list.add(donationRow("Physical Goods", "12 items", "22%"));
        list.add(divider());
        list.add(donationRow("Partners", "\u20B110,000", "17%"));

        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel donationRow(String label, String value, String pct) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelL = new JLabel(label);
        labelL.setFont(SANS);
        labelL.setForeground(TEXT_MUTED);
        row.add(labelL, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        JLabel valueL = new JLabel(value);
        valueL.setFont(SANS_BOLD);
        valueL.setForeground(TEXT_DARK);
        right.add(valueL);
        if (pct != null) {
            JLabel pctL = new JLabel(pct);
            pctL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            pctL.setForeground(TEXT_MUTED);
            right.add(pctL);
        }
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private JPanel buildRecentActivityPanel() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("\u23F1 Recent Activity");
        title.setFont(SANS_BOLD);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(activityRow("\uD83D\uDCB0", "Maria Santos donated \u20B15,000.00", "Sep 29, 2025 \u00B7 11:15 AM"));
        list.add(divider());
        list.add(activityRow("\uD83D\uDCC5", "Relief Goods Distribution", "Sep 28, 2025 \u00B7 2:00 PM"));
        list.add(divider());
        list.add(activityRow("\uD83D\uDC65", "Juan Dela Cruz registered as volunteer", "Sep 27, 2025 \u00B7 9:30 AM"));
        list.add(divider());
        list.add(activityRow("\uD83E\uDD1D", "Global Aid Partners updated", "Sep 27, 2025 \u00B7 10:45 AM"));
        list.add(divider());
        list.add(activityRow("\uD83D\uDCB0", "Ana Reyes donated \u20B13,500.00", "Sep 26, 2025 \u00B7 1:20 PM"));

        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel activityRow(String icon, String text, String time) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(PAGE_BG);
        iconLabel.setPreferredSize(new Dimension(30, 30));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(SANS);
        textLabel.setForeground(TEXT_DARK);
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(TEXT_MUTED);
        textBox.add(textLabel);
        textBox.add(timeLabel);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(textBox, BorderLayout.CENTER);
        return row;
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

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }
}