package Helper;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Export JTable ke file CSV / TSV.
 *
 * <p>Dipanggil dari panel manapun via satu tombol Export:</p>
 * <pre>
 * ExportHelper.Options opts = ExportHelper.showDialog(table, "Data Buku");
 * if (opts == null) return;
 * opts.applyToTable(table);
 * if (opts.type == ExportHelper.Type.CSV)
 *     ExportCSV.fromTable(table, "Data Buku", opts);
 * ExportHelper.clearOptions(table);
 * </pre>
 *
 * <p>Fitur yang dikontrol via {@link ExportHelper.Options}:</p>
 * <ul>
 *   <li>Filter kolom: hanya kolom yang dicentang user yang diekspor</li>
 *   <li>Pemisah kolom: koma, titik koma, atau tab</li>
 *   <li>Encoding: UTF-8 dengan BOM agar Excel Indonesia terbaca benar</li>
 *   <li>Nilai yang mengandung pemisah / newline / tanda kutip otomatis di-escape</li>
 * </ul>
 */
public class ExportCSV {

    /**
     * Mulai proses export CSV.
     *
     * @param table        JTable yang sudah di-filter/sort via {@code opts.applyToTable()}
     * @param judulLaporan Judul laporan, contoh: "Data Buku"
     * @param opts         Opsi dari {@link ExportHelper#showDialog}
     */
    public static void fromTable(JTable table,
                                  String judulLaporan,
                                  ExportHelper.Options opts) {

        // -- Validasi minimal 1 kolom dipilih --
        int visCnt = 0;
        for (boolean v : opts.visibleCols) if (v) visCnt++;
        if (visCnt == 0) {
            JOptionPane.showMessageDialog(null,
                "Pilih minimal satu kolom untuk diekspor.",
                "Tidak Ada Kolom", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // -- Pilih file tujuan --
        JFileChooser fc = new JFileChooser();
        String ext  = opts.csvSeparator == '\t' ? ".tsv" : ".csv";
        String desc = opts.csvSeparator == '\t' ? "TSV Files (*.tsv)" : "CSV Files (*.csv)";
        fc.setDialogTitle("Simpan " + ext.substring(1).toUpperCase()
            + " \u2014 " + judulLaporan);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            desc, ext.substring(1)));

        String ts   = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String slug = judulLaporan.replaceAll("[^a-zA-Z0-9]", "_");
        fc.setSelectedFile(new File(slug + "_" + ts + ext));

        if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File out = fc.getSelectedFile();
        if (!out.getName().toLowerCase().endsWith(ext))
            out = new File(out.getAbsolutePath() + ext);

        // -- Tulis file --
        try (OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(out), java.nio.charset.StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            // BOM UTF-8 agar Excel Indonesia tidak salah baca
            bw.write('\uFEFF');

            int totalCols = table.getColumnCount();
            int rows      = table.getRowCount();
            char sep      = opts.csvSeparator;

            // -- Baris komentar info (diawali #, diabaikan oleh Excel) --
            bw.write("# Laporan: " + judulLaporan);
            bw.newLine();
            bw.write("# Tanggal cetak: "
                + new SimpleDateFormat("dd MMMM yyyy HH:mm",
                    new java.util.Locale("id", "ID")).format(new Date()));
            bw.newLine();
            if (!opts.periodeLabel.isEmpty()) {
                bw.write("# Periode: " + opts.periodeLabel);
                bw.newLine();
            }
            bw.write("# Total baris data: " + rows);
            bw.newLine();

            // -- Baris header kolom --
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int c = 0; c < totalCols; c++) {
                if (!opts.visibleCols[c]) continue;
                if (!first) sb.append(sep);
                sb.append(escape(table.getColumnName(c), sep));
                first = false;
            }
            bw.write(sb.toString());
            bw.newLine();

            // -- Baris data --
            for (int r = 0; r < rows; r++) {
                sb.setLength(0);
                first = true;
                for (int c = 0; c < totalCols; c++) {
                    if (!opts.visibleCols[c]) continue;
                    if (!first) sb.append(sep);
                    Object val  = table.getValueAt(r, c);
                    String text = (val == null) ? "" : val.toString();
                    sb.append(escape(text, sep));
                    first = false;
                }
                bw.write(sb.toString());
                bw.newLine();
            }

            JOptionPane.showMessageDialog(null,
                ext.substring(1).toUpperCase() + " berhasil disimpan:\n"
                + out.getAbsolutePath()
                + "\n\n" + rows + " baris  \u00B7  " + visCnt + " kolom",
                "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Gagal export " + ext.substring(1).toUpperCase()
                + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================================================================
    // Private helpers
    // ================================================================

    /**
     * Escape nilai sel untuk CSV/TSV.
     * <ul>
     *   <li>Jika nilai mengandung karakter pemisah, newline, atau tanda kutip
     *       ganda: apit dengan {@code "} dan escape {@code "} menjadi {@code ""}.</li>
     *   <li>Jika tidak: kembalikan nilai as-is.</li>
     * </ul>
     *
     * @param value nilai sel
     * @param sep   karakter pemisah yang dipakai
     * @return nilai yang sudah di-escape
     */
    private static String escape(String value, char sep) {
        if (value == null) return "";
        // Perlu di-quote jika mengandung sep, newline, atau tanda kutip
        boolean needsQuote = value.indexOf(sep) >= 0
            || value.indexOf('"')  >= 0
            || value.indexOf('\n') >= 0
            || value.indexOf('\r') >= 0;
        if (!needsQuote) return value;
        // Escape tanda kutip ganda dengan dua tanda kutip
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
