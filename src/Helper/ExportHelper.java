package Helper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Dialog terpusat untuk opsi export (PDF / CSV).
 *
 * Cara pakai:
 * <pre>
 *   ExportHelper.Options opts = ExportHelper.showDialog(table, "Data Buku");
 *   if (opts == null) return;
 *   opts.applyToTable(table);
 *   if (opts.type == ExportHelper.Type.PDF)
 *       ExportPDF.fromTable(table, "Data Buku", opts);
 *   else
 *       ExportCSV.fromTable(table, "Data Buku", opts);
 *   ExportHelper.clearOptions(table);
 * </pre>
 */
public class ExportHelper {

    // ====================================================================
    // Public types
    // ====================================================================

    public enum Type { PDF, CSV }

    public static final class Options {
        public final Type      type;
        public final int       sortColIndex;  // -1 = tidak diurutkan
        public final boolean   sortAsc;
        public final String    periodeLabel;  // "" = tanpa filter
        public final boolean[] visibleCols;
        public final boolean   landscape;
        public final String    paperSize;     // "A4", "F4", "Letter"
        public final char      csvSeparator;  // ',', ';', '\t'
        private final TableRowSorter<DefaultTableModel> sorter;

        Options(Type type, int sortColIndex, boolean sortAsc,
                String periodeLabel, boolean[] visibleCols,
                boolean landscape, String paperSize, char csvSeparator,
                TableRowSorter<DefaultTableModel> sorter) {
            this.type         = type;
            this.sortColIndex = sortColIndex;
            this.sortAsc      = sortAsc;
            this.periodeLabel = periodeLabel;
            this.visibleCols  = visibleCols;
            this.landscape    = landscape;
            this.paperSize    = paperSize;
            this.csvSeparator = csvSeparator;
            this.sorter       = sorter;
        }

        public void applyToTable(JTable table) {
            table.setRowSorter(sorter);
        }
    }

    // ====================================================================
    // Public API
    // ====================================================================

    /**
     * Tampilkan dialog export dengan pilihan format PDF / CSV.
     *
     * @param table         JTable sumber data
     * @param judulLaporan  Judul laporan
     * @return Options, atau null jika user klik Batal.
     */
    public static Options showDialog(JTable table, String judulLaporan) {

        final int      colCount = table.getColumnCount();
        final String[] colNames = new String[colCount];
        final List<Integer> dateCols = new ArrayList<>();

        for (int c = 0; c < colCount; c++) {
            colNames[c] = table.getColumnName(c);
            if (table.getRowCount() > 0) {
                Object v = table.getValueAt(0, c);
                if (v != null && looksLikeDate(v.toString())) dateCols.add(c);
            }
            String lc = colNames[c].toLowerCase();
            if ((lc.contains("tanggal") || lc.contains("tgl")
                    || lc.contains("tempo") || lc.contains("date"))
                    && !dateCols.contains(c)) dateCols.add(c);
        }

        // ----------------------------------------------------------------
        // State: format terpilih
        // ----------------------------------------------------------------
        final Type[] selectedType = { Type.PDF };

        // ----------------------------------------------------------------
        // Dialog
        // ----------------------------------------------------------------
        JDialog dlg = new JDialog((Frame) null,
            "Export \u2014 " + judulLaporan, true);
        dlg.setLayout(new BorderLayout(0, 0));
        dlg.setSize(560, 490);
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(null);

        // ----------------------------------------------------------------
        // Tombol Export (dibuat di sini agar bisa diakses oleh kartu)
        // ----------------------------------------------------------------
        final JButton btnOk = new JButton("\u2705  Export PDF");
        stylePrimaryBtn(btnOk);

        // ----------------------------------------------------------------
        // Format cards (PDF + CSV)
        // ----------------------------------------------------------------
        final Color COLOR_SELECTED = new Color(220, 234, 255);
        final Color COLOR_NORMAL   = Color.WHITE;
        final Border BORDER_SEL    = BorderFactory.createLineBorder(new Color(200, 50, 50), 2);
        final Border BORDER_NRM    = BorderFactory.createLineBorder(new Color(200, 200, 200), 1);
        final Border CARD_PAD      = BorderFactory.createEmptyBorder(6, 12, 6, 12);

        final JPanel cardPDF = buildFormatCard(
            "\uD83D\uDCCB PDF",
            "",
            COLOR_SELECTED, BORDER_SEL, CARD_PAD);
        final JPanel cardCSV = buildFormatCard(
            "\uD83D\uDCCA CSV / Excel",
            "",
            COLOR_NORMAL, BORDER_NRM, CARD_PAD);

        // ----------------------------------------------------------------
        // Tab Pengaturan: CardLayout untuk swap PDF<->CSV settings
        // ----------------------------------------------------------------
        final CardLayout settingsCard = new CardLayout();
        final JPanel     settingsHost = new JPanel(settingsCard);
        settingsHost.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));

        // PDF settings
        final JComboBox<String> cmbPaper     = new JComboBox<>(
            new String[]{"A4", "F4  (Folio)", "Letter"});
        final JCheckBox         chkLandscape = new JCheckBox(
            "Orientasi Landscape (horizontal)");
        settingsHost.add(buildSettingsPanel(
            "Pengaturan Halaman PDF",
            new String[]{"Ukuran kertas:"},
            new JComponent[]{cmbPaper},
            chkLandscape,
            noteLabel("Kop surat dan blok tanda tangan selalu disertakan.<br>"
                + "Edit konstanta di ExportPDF.java untuk mengubah isian.")
        ), "PDF");

        // CSV settings
        final JComboBox<String> cmbSep = new JComboBox<>(
            new String[]{"Koma  ( , )", "Titik koma  ( ; )", "Tab  ( \\t )"});
        settingsHost.add(buildSettingsPanel(
            "Pengaturan CSV",
            new String[]{"Pemisah kolom:"},
            new JComponent[]{cmbSep},
            null,
            noteLabel("Encoding output: UTF-8 dengan BOM<br>"
                + "agar terbaca benar di Microsoft Excel.")
        ), "CSV");

        settingsCard.show(settingsHost, "PDF"); // default

        // ----------------------------------------------------------------
        // Klik kartu: update visual + swap settings + update tombol
        // ----------------------------------------------------------------
        addClickRecursive(cardPDF, e -> {
            selectedType[0] = Type.PDF;
            cardPDF.setBackground(COLOR_SELECTED);
            cardPDF.setBorder(BorderFactory.createCompoundBorder(BORDER_SEL, CARD_PAD));
            cardCSV.setBackground(COLOR_NORMAL);
            cardCSV.setBorder(BorderFactory.createCompoundBorder(BORDER_NRM, CARD_PAD));
            updateCardChildren(cardPDF, COLOR_SELECTED);
            updateCardChildren(cardCSV, COLOR_NORMAL);
            settingsCard.show(settingsHost, "PDF");
            btnOk.setText("\u2705  Export PDF");
        });

        addClickRecursive(cardCSV, e -> {
            selectedType[0] = Type.CSV;
            cardCSV.setBackground(COLOR_SELECTED);
            cardCSV.setBorder(BorderFactory.createCompoundBorder(BORDER_SEL, CARD_PAD));
            cardPDF.setBackground(COLOR_NORMAL);
            cardPDF.setBorder(BorderFactory.createCompoundBorder(BORDER_NRM, CARD_PAD));
            updateCardChildren(cardCSV, COLOR_SELECTED);
            updateCardChildren(cardPDF, COLOR_NORMAL);
            settingsCard.show(settingsHost, "CSV");
            btnOk.setText("\u2705  Export CSV");
        });

        // ----------------------------------------------------------------
        // Panel format (atas)
        // ----------------------------------------------------------------
        JPanel fmtPanel = new JPanel(new GridBagLayout());
        fmtPanel.setBackground(new Color(248, 249, 252));
        fmtPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        GridBagConstraints fc = new GridBagConstraints();
        fc.anchor = GridBagConstraints.WEST;

        JLabel lblFmt = new JLabel("Format Export:");
        lblFmt.setFont(lblFmt.getFont().deriveFont(Font.BOLD, 12f));
        fc.gridx = 0; fc.gridy = 0; fc.insets = new Insets(0, 0, 0, 14);
        fmtPanel.add(lblFmt, fc);

        fc.gridx = 1; fc.insets = new Insets(0, 0, 0, 10);
        fmtPanel.add(cardPDF, fc);
        fc.gridx = 2; fc.insets = new Insets(0, 0, 0, 0);
        fmtPanel.add(cardCSV, fc);

        // ----------------------------------------------------------------
        // Tab: Urutkan & Filter
        // ----------------------------------------------------------------
        JPanel tabSort = new JPanel(new GridBagLayout());
        tabSort.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 4, 4, 6);

        g.gridx = 0; g.gridy = 0; g.gridwidth = 3; g.weightx = 1;
        tabSort.add(sectionLabel("Urutkan Berdasarkan"), g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        tabSort.add(new JLabel("Kolom:"), g);

        String[] sortItems = new String[colCount + 1];
        sortItems[0] = "(Tidak diurutkan)";
        System.arraycopy(colNames, 0, sortItems, 1, colCount);
        final JComboBox<String> cmbSortCol = new JComboBox<>(sortItems);
        g.gridx = 1; g.weightx = 0.55;
        tabSort.add(cmbSortCol, g);

        final JComboBox<String> cmbSortDir = new JComboBox<>(
            new String[]{"\u2191 A \u2192 Z  (Ascending)",
                         "\u2193 Z \u2192 A  (Descending)"});
        g.gridx = 2; g.weightx = 0.45;
        tabSort.add(cmbSortDir, g);

        final JComboBox<String> cmbDateCol;
        final JSpinner spnFrom, spnTo;
        final JCheckBox chkFrom, chkTo;

        if (!dateCols.isEmpty()) {
            g.gridx = 0; g.gridy = 2; g.gridwidth = 3; g.weightx = 1;
            g.insets = new Insets(14, 4, 2, 6);
            tabSort.add(sectionLabel("Filter Rentang Tanggal"), g);
            g.insets = new Insets(4, 4, 4, 6);

            g.gridwidth = 1;
            g.gridx = 0; g.gridy = 3; g.weightx = 0;
            tabSort.add(new JLabel("Kolom tanggal:"), g);

            String[] dcNames = new String[dateCols.size()];
            for (int i = 0; i < dateCols.size(); i++)
                dcNames[i] = colNames[dateCols.get(i)];
            cmbDateCol = new JComboBox<>(dcNames);
            g.gridx = 1; g.gridwidth = 2; g.weightx = 1;
            tabSort.add(cmbDateCol, g);

            g.gridwidth = 1; g.gridy = 4;
            chkFrom = new JCheckBox("Dari:");
            g.gridx = 0; g.weightx = 0;
            tabSort.add(chkFrom, g);
            spnFrom = makeDateSpinner();
            spnFrom.setEnabled(false);
            g.gridx = 1; g.gridwidth = 2; g.weightx = 1;
            tabSort.add(spnFrom, g);

            g.gridwidth = 1; g.gridy = 5;
            chkTo = new JCheckBox("Sampai:");
            g.gridx = 0; g.weightx = 0;
            tabSort.add(chkTo, g);
            spnTo = makeDateSpinner();
            spnTo.setEnabled(false);
            g.gridx = 1; g.gridwidth = 2; g.weightx = 1;
            tabSort.add(spnTo, g);

            chkFrom.addActionListener(e -> spnFrom.setEnabled(chkFrom.isSelected()));
            chkTo  .addActionListener(e -> spnTo  .setEnabled(chkTo  .isSelected()));
        } else {
            cmbDateCol = null;
            spnFrom    = null; spnTo  = null;
            chkFrom    = null; chkTo  = null;
        }

        g.gridx = 0; g.gridy = 10; g.gridwidth = 3;
        g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        tabSort.add(new JPanel(), g);

        // ----------------------------------------------------------------
        // Tab: Pilih Kolom
        // ----------------------------------------------------------------
        JPanel tabCols = new JPanel(new BorderLayout(6, 6));
        tabCols.setBorder(BorderFactory.createEmptyBorder(10, 14, 8, 14));
        tabCols.add(sectionLabel("Centang kolom yang ingin ditampilkan:"),
            BorderLayout.NORTH);

        JPanel colGrid = new JPanel(new GridLayout(0, 3, 6, 4));
        final JCheckBox[] chkCols = new JCheckBox[colCount];
        for (int i = 0; i < colCount; i++) {
            chkCols[i] = new JCheckBox(colNames[i], true);
            chkCols[i].setFont(chkCols[i].getFont().deriveFont(11.5f));
            colGrid.add(chkCols[i]);
        }
        tabCols.add(new JScrollPane(colGrid), BorderLayout.CENTER);

        JPanel colBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton btnAll  = new JButton("Pilih Semua");
        JButton btnNone = new JButton("Hapus Semua");
        styleSecondaryBtn(btnAll); styleSecondaryBtn(btnNone);
        colBtns.add(btnAll); colBtns.add(btnNone);
        tabCols.add(colBtns, BorderLayout.SOUTH);
        btnAll .addActionListener(e -> { for (JCheckBox cb : chkCols) cb.setSelected(true);  });
        btnNone.addActionListener(e -> { for (JCheckBox cb : chkCols) cb.setSelected(false); });

        // ----------------------------------------------------------------
        // TabbedPane
        // ----------------------------------------------------------------
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("  \uD83D\uDD00  Urutkan & Filter  ", tabSort);
        tabs.addTab("  \uD83D\uDCCB  Pilih Kolom  ",      tabCols);
        tabs.addTab("  \u2699\uFE0F  Pengaturan  ",        settingsHost);
        tabs.setFont(tabs.getFont().deriveFont(11.5f));

        // ----------------------------------------------------------------
        // Bottom bar
        // ----------------------------------------------------------------
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(8, 14, 8, 12)));

        JLabel lblInfo = new JLabel(
            table.getRowCount() + " baris  \u00B7  "
            + colCount + " kolom  \u00B7  " + judulLaporan);
        lblInfo.setForeground(new Color(100, 100, 100));
        lblInfo.setFont(lblInfo.getFont().deriveFont(10.5f));
        bottom.add(lblInfo, BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnCancel = new JButton("Batal");
        styleSecondaryBtn(btnCancel);
        btnRow.add(btnCancel);
        btnRow.add(btnOk);
        bottom.add(btnRow, BorderLayout.EAST);

        dlg.add(fmtPanel, BorderLayout.NORTH);
        dlg.add(tabs,     BorderLayout.CENTER);
        dlg.add(bottom,   BorderLayout.SOUTH);

        // ----------------------------------------------------------------
        // Aksi tombol
        // ----------------------------------------------------------------
        final Options[] result = { null };
        btnCancel.addActionListener(e -> dlg.dispose());

        btnOk.addActionListener(e -> {
            // Sort
            TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>((DefaultTableModel) table.getModel());
            int     sortIdx = cmbSortCol.getSelectedIndex() - 1;
            boolean asc     = cmbSortDir.getSelectedIndex() == 0;
            if (sortIdx >= 0)
                sorter.setSortKeys(Collections.singletonList(
                    new RowSorter.SortKey(sortIdx,
                        asc ? SortOrder.ASCENDING : SortOrder.DESCENDING)));

            // Date filter
            String periodeLabel = "";
            if (!dateCols.isEmpty() && chkFrom != null
                    && (chkFrom.isSelected() || chkTo.isSelected())) {
                final Date from = chkFrom.isSelected()
                    ? normalizeStart((Date) spnFrom.getValue()) : null;
                final Date to   = chkTo.isSelected()
                    ? normalizeEnd((Date) spnTo.getValue())     : null;
                final int  dci  = dateCols.get(cmbDateCol.getSelectedIndex());
                sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                  ? extends Integer> entry) {
                        Date d = parseDate(entry.getStringValue(dci));
                        if (d == null)                      return true;
                        if (from != null && d.before(from)) return false;
                        if (to   != null && d.after(to))    return false;
                        return true;
                    }
                });
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                StringBuilder sb = new StringBuilder();
                if (chkFrom.isSelected()) sb.append(fmt.format(from));
                if (chkFrom.isSelected() && chkTo.isSelected()) sb.append(" s.d. ");
                if (chkTo.isSelected())   sb.append(fmt.format(to));
                periodeLabel = sb.toString();
            }

            // Visible cols
            boolean[] vis = new boolean[colCount];
            for (int i = 0; i < colCount; i++) vis[i] = chkCols[i].isSelected();

            // PDF / CSV settings
            boolean landscape = false;
            String  paper     = "A4";
            char    sep       = ',';
            if (selectedType[0] == Type.PDF) {
                landscape = chkLandscape.isSelected();
                switch (cmbPaper.getSelectedIndex()) {
                    case 1: paper = "F4";     break;
                    case 2: paper = "Letter"; break;
                    default: paper = "A4";
                }
            } else {
                switch (cmbSep.getSelectedIndex()) {
                    case 1: sep = ';';  break;
                    case 2: sep = '\t'; break;
                    default: sep = ',';
                }
            }

            result[0] = new Options(selectedType[0], sortIdx, asc,
                periodeLabel, vis, landscape, paper, sep, sorter);
            dlg.dispose();
        });

        dlg.setVisible(true);
        return result[0];
    }

    /** Hapus RowSorter setelah export. */
    public static void clearOptions(JTable table) {
        table.setRowSorter(null);
    }

    // ====================================================================
    // UI helpers
    // ====================================================================

    private static JPanel buildFormatCard(String title, String subtitle,
            Color bg, Border border, Border padding) {
        JPanel card = new JPanel(new BorderLayout(4, 2));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(border, padding));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(lTitle.getFont().deriveFont(Font.BOLD, 12f));
        lTitle.setBackground(bg);
        lTitle.setOpaque(false);

        JLabel lSub = new JLabel(
            "<html><font color='#555555' size='2'>" + subtitle + "</font></html>");
        lSub.setBackground(bg);
        lSub.setOpaque(false);

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lSub,   BorderLayout.CENTER);
        return card;
    }

    /** Paksa semua child component ikut warna background kartu. */
    private static void updateCardChildren(JPanel card, Color bg) {
        for (Component c : card.getComponents()) {
            c.setBackground(bg);
        }
    }

    private static JPanel buildSettingsPanel(String sectionTitle,
            String[] labels, JComponent[] fields,
            JCheckBox extraCheck, JPanel notePanel) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 4, 5, 8);
        int row = 0;

        g.gridx = 0; g.gridy = row++; g.gridwidth = 2; g.weightx = 1;
        p.add(sectionLabel(sectionTitle), g);

        g.gridwidth = 1;
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = row; g.weightx = 0;
            p.add(new JLabel(labels[i]), g);
            g.gridx = 1; g.weightx = 1;
            p.add(fields[i], g);
            row++;
        }
        if (extraCheck != null) {
            g.gridx = 0; g.gridy = row++; g.gridwidth = 2; g.weightx = 1;
            p.add(extraCheck, g);
        }
        if (notePanel != null) {
            g.gridx = 0; g.gridy = row++; g.gridwidth = 2;
            p.add(notePanel, g);
        }
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        p.add(new JPanel(), g);
        return p;
    }

    private static JPanel noteLabel(String html) {
        JLabel l = new JLabel(
            "<html><i style='color:#666'>" + html + "</i></html>");
        l.setFont(l.getFont().deriveFont(10.5f));
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        p.add(l);
        return p;
    }

    /** Daftarkan MouseListener ke komponen dan semua turunannya. */
    private static void addClickRecursive(Component comp,
            ActionListener action) {
        comp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(
                    new java.awt.event.ActionEvent(comp, 0, ""));
            }
        });
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents())
                addClickRecursive(child, action);
        }
    }

    private static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 11.5f));
        return l;
    }

    private static void stylePrimaryBtn(JButton b) {
        b.setBackground(new Color(28, 57, 107));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void styleSecondaryBtn(JButton b) {
        b.setFont(b.getFont().deriveFont(11.5f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ====================================================================
    // Date helpers
    // ====================================================================

    private static JSpinner makeDateSpinner() {
        SpinnerDateModel m = new SpinnerDateModel(
            new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner sp = new JSpinner(m);
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        return sp;
    }

    private static Date normalizeStart(Date d) {
        Calendar c = Calendar.getInstance(); c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);      c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private static Date normalizeEnd(Date d) {
        Calendar c = Calendar.getInstance(); c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23); c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);      c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    private static Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        for (String fmt : new String[]{
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd",
                "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy"}) {
            try { return new SimpleDateFormat(fmt).parse(s.trim()); }
            catch (ParseException ignored) {}
        }
        return null;
    }

    private static boolean looksLikeDate(String s) {
        return s.matches("\\d{4}-\\d{2}-\\d{2}.*")
            || s.matches("\\d{2}/\\d{2}/\\d{4}.*");
    }
}
