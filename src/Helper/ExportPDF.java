package Helper;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Export JTable ke PDF format Surat Resmi Indonesia.
 *
 * <p>Dipanggil dari panel manapun via satu tombol Export:</p>
 * <pre>
 * ExportHelper.Options opts = ExportHelper.showDialog(table, "Data Buku");
 * if (opts == null) return;
 * opts.applyToTable(table);
 * if (opts.type == ExportHelper.Type.PDF)
 *     ExportPDF.fromTable(table, "Data Buku", opts);
 * ExportHelper.clearOptions(table);
 * </pre>
 *
 * <p>Fitur yang dikontrol via {@link ExportHelper.Options}:</p>
 * <ul>
 *   <li>Ukuran kertas: A4, F4 (Folio), Letter</li>
 *   <li>Orientasi: Portrait / Landscape</li>
 *   <li>Filter kolom: hanya kolom yang dicentang user yang dicetak</li>
 *   <li>Label periode filter tanggal ditampilkan di identitas dan judul</li>
 *   <li>Posisi tanda tangan adaptif: mepet ke bawah tanpa membuat halaman baru</li>
 * </ul>
 */
public class ExportPDF {

    // ================================================================
    // CONFIG — sesuaikan dengan instansi
    // ================================================================
    private static final String NAMA_INSTANSI   = "PERPUSTAKAAN UNIVERSITAS INDRAPRASTA PGRI";
    private static final String ALAMAT_1        = "Jl. Nangka No. 58 C, Tanjung Barat, Jagakarsa";
    private static final String ALAMAT_2        = "Jakarta Selatan 12530  -  Telp. (021) 7818718";
    private static final String KOTA            = "Jakarta Selatan";
    private static final String KEPALA_NAMA     = ".......................................";
    private static final String KEPALA_NIP      = "NIP. .......................";
    private static final String JABATAN_KEPALA  = "Kepala Perpustakaan";
    private static final String PETUGAS_NAMA    = ".......................................";
    private static final String PETUGAS_NIP     = "NIP. .......................";
    private static final String JABATAN_PETUGAS = "Petugas Perpustakaan";
    // ================================================================

    // Margin standar Indonesia: kiri 3cm, kanan 2cm, atas 2cm, bawah 2.5cm
    private static final float ML = 85f;
    private static final float MR = 56f;
    private static final float MT = 56f;
    private static final float MB = 71f;

    // Tinggi blok tanda tangan
    private static final float TTD_H   = 120f;
    private static final float TTD_GAP = 14f;

    // Warna
    private static final Color NAVY      = new Color(28,  57, 107);
    private static final Color NAVY_LT   = new Color(44,  82, 145);
    private static final Color ROW_ALT   = new Color(240, 245, 255);
    private static final Color DARK_GRAY = new Color(60,  60,  60);

    // Font (inisialisasi sekali)
    private static final Font F_NORMAL;
    private static final Font F_SMALL;
    private static final Font F_HEADER;

    static {
        F_NORMAL = FontFactory.getFont(FontFactory.HELVETICA,      9f,   Font.NORMAL, Color.BLACK);
        F_SMALL  = FontFactory.getFont(FontFactory.HELVETICA,      7.5f, Font.NORMAL, DARK_GRAY);
        F_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8f,   Font.NORMAL, Color.WHITE);
    }

    // ================================================================
    // Public API
    // ================================================================

    /**
     * Mulai proses export PDF.
     *
     * @param table        JTable yang sudah di-filter/sort via {@code opts.applyToTable()}
     * @param judulLaporan Judul laporan, contoh: "Data Buku"
     * @param opts         Opsi dari {@link ExportHelper#showDialog}
     */
    public static void fromTable(JTable table,
                                  String judulLaporan,
                                  ExportHelper.Options opts) {

        // -- Tentukan ukuran kertas --
        Rectangle pageSize = resolvePageSize(opts.paperSize, opts.landscape);

        // -- Hitung kolom yang ditampilkan --
        int totalCols = table.getColumnCount();
        int visCols   = 0;
        for (boolean v : opts.visibleCols) if (v) visCols++;
        if (visCols == 0) {
            JOptionPane.showMessageDialog(null,
                "Pilih minimal satu kolom untuk diekspor.",
                "Tidak Ada Kolom", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // -- Pilih file tujuan --
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Simpan PDF — " + judulLaporan);
        String ts   = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String slug = judulLaporan.replaceAll("[^a-zA-Z0-9]", "_");
        fc.setSelectedFile(new File(slug + "_" + ts + ".pdf"));
        if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File out = fc.getSelectedFile();
        if (!out.getName().toLowerCase().endsWith(".pdf"))
            out = new File(out.getAbsolutePath() + ".pdf");

        // -- Buat dokumen --
        Document doc = new Document(pageSize, ML, MR, MT, MB);

        try (FileOutputStream fos = new FileOutputStream(out)) {
            PdfWriter writer = PdfWriter.getInstance(doc, fos);
            doc.open();

            String tglCetak = new SimpleDateFormat("dd MMMM yyyy",
                new java.util.Locale("id", "ID")).format(new Date());

            int visibleRowCount = table.getRowCount();

            // -- Bangun isi dokumen --
            buildKopSurat(doc);
            buildIdentitas(doc, generateNomor(), tglCetak,
                buildPerihal(judulLaporan, opts.periodeLabel),
                visibleRowCount, opts.periodeLabel);
            buildJudul(doc, judulLaporan.toUpperCase(), opts.periodeLabel);
            buildTabel(doc, table, opts.visibleCols);
            buildPenutup(doc);

            // -- Tanda tangan adaptif --
            float currentY    = writer.getVerticalPosition(false);
            float spaceNeeded = TTD_H + TTD_GAP;
            float spaceLeft   = currentY - MB;

            if (spaceLeft > spaceNeeded) {
                float spacerH = spaceLeft - spaceNeeded;
                doc.add(new Paragraph(" "));
                PdfPTable sp = new PdfPTable(1);
                sp.setWidthPercentage(100);
                PdfPCell sc = new PdfPCell(new Phrase(""));
                sc.setFixedHeight(Math.max(0f, spacerH - 14f));
                sc.setBorder(Rectangle.NO_BORDER);
                sp.addCell(sc);
                doc.add(sp);
            }

            buildTandaTangan(writer, tglCetak, pageSize);
            doc.close();

            JOptionPane.showMessageDialog(null,
                "PDF berhasil disimpan:\n" + out.getAbsolutePath(),
                "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Gagal export PDF:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================================================================
    // Section builders
    // ================================================================

    private static void buildKopSurat(Document doc) throws Exception {
        PdfPTable kop = new PdfPTable(new float[]{1.4f, 5f});
        kop.setWidthPercentage(100);
        kop.setSpacingAfter(0);

        PdfPCell cLogo = new PdfPCell();
        cLogo.setBorder(Rectangle.NO_BORDER);
        cLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        try {
            URL url = ExportPDF.class.getResource("/Assets/logo.png");
            if (url != null) {
                Image logo = Image.getInstance(url);
                logo.scaleToFit(70, 70);
                cLogo.addElement(logo);
            }
        } catch (Exception ignored) {}
        kop.addCell(cLogo);

        PdfPCell cTeks = new PdfPCell();
        cTeks.setBorder(Rectangle.NO_BORDER);
        cTeks.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font fNama   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13f, Font.NORMAL, NAVY);
        Font fAlamat = FontFactory.getFont(FontFactory.HELVETICA,     8.5f, Font.NORMAL, DARK_GRAY);
        Paragraph pNama = new Paragraph(NAMA_INSTANSI, fNama);
        pNama.setAlignment(Element.ALIGN_LEFT);
        cTeks.addElement(pNama);
        cTeks.addElement(new Paragraph(ALAMAT_1, fAlamat));
        cTeks.addElement(new Paragraph(ALAMAT_2, fAlamat));
        kop.addCell(cTeks);
        doc.add(kop);

        addHLine(doc, NAVY,    3.5f, 4f,   0f);
        addHLine(doc, NAVY_LT, 1.5f, 1.5f, 6f);
    }

    private static void addHLine(Document doc, Color color,
                                  float h, float before, float after)
            throws Exception {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(before);
        t.setSpacingAfter(after);
        PdfPCell c = new PdfPCell(new Phrase(""));
        c.setBackgroundColor(color);
        c.setBorder(Rectangle.NO_BORDER);
        c.setFixedHeight(h);
        t.addCell(c);
        doc.add(t);
    }

    private static void buildIdentitas(Document doc, String nomor,
            String tglCetak, String perihal, int total,
            String periodeLabel) throws Exception {
        Font fL = FontFactory.getFont(FontFactory.HELVETICA,      8.5f, Font.NORMAL, DARK_GRAY);
        Font fV = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, Font.NORMAL, Color.BLACK);

        PdfPTable t = new PdfPTable(new float[]{2f, 0.25f, 5f});
        t.setWidthPercentage(65);
        t.setHorizontalAlignment(Element.ALIGN_LEFT);
        t.setSpacingAfter(8);

        addRow(t, "Nomor",         nomor,           fL, fV);
        addRow(t, "Tanggal Cetak", tglCetak,        fL, fV);
        addRow(t, "Perihal",       perihal,          fL, fV);
        addRow(t, "Total Data",    total + " baris", fL, fV);
        if (!periodeLabel.isEmpty())
            addRow(t, "Periode", periodeLabel, fL, fV);
        doc.add(t);
    }

    private static void addRow(PdfPTable t, String lbl, String val,
                                Font fL, Font fV) {
        for (PdfPCell c : new PdfPCell[]{nb(lbl, fL), nb(":", fL), nb(val, fV)}) {
            c.setPaddingTop(1.5f);
            c.setPaddingBottom(1.5f);
            t.addCell(c);
        }
    }

    private static PdfPCell nb(String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private static void buildJudul(Document doc, String judulUp,
                                    String periodeLabel) throws Exception {
        Font fJ = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 12f, Font.UNDERLINE, Color.BLACK);
        Paragraph p = new Paragraph("LAPORAN " + judulUp, fJ);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(periodeLabel.isEmpty() ? 8f : 2f);
        doc.add(p);

        if (!periodeLabel.isEmpty()) {
            Font fP = FontFactory.getFont(
                FontFactory.HELVETICA, 9f, Font.ITALIC, DARK_GRAY);
            Paragraph pP = new Paragraph("Periode: " + periodeLabel, fP);
            pP.setAlignment(Element.ALIGN_CENTER);
            pP.setSpacingAfter(8f);
            doc.add(pP);
        }
    }

    /**
     * Render tabel ke PDF.
     * Hanya kolom dengan {@code visibleCols[i] == true} yang dicetak.
     */
    private static void buildTabel(Document doc, JTable table,
                                    boolean[] visibleCols) throws Exception {
        int totalCols = table.getColumnCount();
        int rows      = table.getRowCount();

        // Hitung jumlah kolom visible
        int visCnt = 0;
        for (boolean v : visibleCols) if (v) visCnt++;

        // Buat array indeks kolom yang visible
        int[] visIdx = new int[visCnt];
        int   ptr    = 0;
        for (int c = 0; c < totalCols; c++)
            if (visibleCols[c]) visIdx[ptr++] = c;

        float[] widths = new float[visCnt];
        for (int i = 0; i < visCnt; i++) widths[i] = 1f;

        PdfPTable t = new PdfPTable(widths);
        t.setWidthPercentage(100);
        t.setSpacingAfter(10f);
        t.setHeaderRows(1);

        // Header
        for (int i = 0; i < visCnt; i++) {
            PdfPCell h = new PdfPCell(
                new Phrase(table.getColumnName(visIdx[i]), F_HEADER));
            h.setBackgroundColor(NAVY);
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            h.setVerticalAlignment(Element.ALIGN_MIDDLE);
            h.setPadding(5f);
            h.setBorderColor(NAVY_LT);
            t.addCell(h);
        }

        // Data rows
        for (int r = 0; r < rows; r++) {
            Color bg = (r % 2 == 0) ? Color.WHITE : ROW_ALT;
            for (int i = 0; i < visCnt; i++) {
                Object val  = table.getValueAt(r, visIdx[i]);
                String text = (val == null) ? "" : val.toString();

                // Format Rupiah untuk kolom Denda
                String colName = table.getColumnName(visIdx[i]).toLowerCase();
                if (colName.contains("denda")) {
                    try {
                        long n = Long.parseLong(text.trim());
                        text = "Rp " + String.format("%,d", n).replace(',', '.');
                    } catch (NumberFormatException ignored) {}
                }

                PdfPCell d = new PdfPCell(
                    new Phrase(text, text.isEmpty() ? F_SMALL : F_NORMAL));
                d.setBackgroundColor(bg);
                d.setPadding(4f);
                d.setBorderColor(new Color(210, 215, 230));
                d.setVerticalAlignment(Element.ALIGN_MIDDLE);
                t.addCell(d);
            }
        }

        if (rows == 0) {
            PdfPCell empty = new PdfPCell(
                new Phrase("Tidak ada data yang sesuai filter.", F_SMALL));
            empty.setColspan(visCnt);
            empty.setHorizontalAlignment(Element.ALIGN_CENTER);
            empty.setPadding(8f);
            t.addCell(empty);
        }
        doc.add(t);
    }

    private static void buildPenutup(Document doc) throws Exception {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 9f, Font.NORMAL, Color.BLACK);
        Paragraph p = new Paragraph(
            "Demikian laporan ini dibuat dengan sebenarnya "
            + "untuk dipergunakan sebagaimana mestinya.", f);
        p.setSpacingBefore(2f);
        p.setSpacingAfter(8f);
        doc.add(p);
    }

    /**
     * Tanda tangan absolut di bawah halaman terakhir.
     * Mendukung landscape: pageSize sudah dirotasi sebelum masuk ke sini.
     */
    private static void buildTandaTangan(PdfWriter writer,
                                          String tglCetak,
                                          Rectangle pageSize) throws Exception {
        PdfContentByte cb = writer.getDirectContent();

        float pageW  = pageSize.getWidth();
        float totalW = pageW - ML - MR;
        float gap    = 20f;
        float colW   = (totalW - gap) / 2f;
        float yTop   = MB + TTD_H;

        Font fLabel = FontFactory.getFont(
            FontFactory.HELVETICA,      9f, Font.NORMAL,    Color.BLACK);
        Font fNama  = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 9f, Font.UNDERLINE, Color.BLACK);
        Font fNip   = FontFactory.getFont(
            FontFactory.HELVETICA,      8f, Font.NORMAL,    DARK_GRAY);

        String tgl = KOTA + ", " + tglCetak;

        writeTtdCol(cb, ML,              yTop, colW,
            tgl, JABATAN_PETUGAS, PETUGAS_NAMA, PETUGAS_NIP,
            fLabel, fNama, fNip);
        writeTtdCol(cb, ML + colW + gap, yTop, colW,
            tgl, JABATAN_KEPALA,  KEPALA_NAMA,  KEPALA_NIP,
            fLabel, fNama, fNip);
    }

    private static void writeTtdCol(PdfContentByte cb,
            float x, float yTop, float w,
            String tgl, String jabatan, String nama, String nip,
            Font fLabel, Font fNama, Font fNip) throws Exception {
        ColumnText ct = new ColumnText(cb);
        ct.setSimpleColumn(x, MB, x + w, yTop);
        ct.setAlignment(Element.ALIGN_CENTER);

        Paragraph pTgl = new Paragraph(tgl, fLabel);
        pTgl.setAlignment(Element.ALIGN_CENTER);
        ct.addElement(pTgl);

        Paragraph pJab = new Paragraph(jabatan, fLabel);
        pJab.setAlignment(Element.ALIGN_CENTER);
        ct.addElement(pJab);

        Paragraph pSpace = new Paragraph(" ", fLabel);
        pSpace.setSpacingAfter(60f);
        ct.addElement(pSpace);

        Paragraph pNama = new Paragraph(nama, fNama);
        pNama.setAlignment(Element.ALIGN_CENTER);
        ct.addElement(pNama);

        Paragraph pNip = new Paragraph(nip, fNip);
        pNip.setAlignment(Element.ALIGN_CENTER);
        ct.addElement(pNip);

        ct.go();
    }

    // ================================================================
    // Utilities
    // ================================================================

    /**
     * Resolve ukuran kertas dari string pilihan user.
     * Jika landscape, rotate pageSize 90 derajat.
     */
    private static Rectangle resolvePageSize(String paperSize,
                                              boolean landscape) {
        Rectangle base;
        switch (paperSize) {
            case "F4":     base = new Rectangle(595f, 935f); break; // 210x330mm
            case "Letter": base = PageSize.LETTER;           break;
            default:       base = PageSize.A4;               // A4
        }
        return landscape ? base.rotate() : base;
    }

    private static String generateNomor() {
        String[] romawi = {"","I","II","III","IV","V","VI",
                           "VII","VIII","IX","X","XI","XII"};
        int bln = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int thn = Calendar.getInstance().get(Calendar.YEAR);
        int pfx = 100 + (int) (Math.random() * 900);
        return pfx + "/PERPUS/" + romawi[bln] + "/" + thn;
    }

    private static String buildPerihal(String judul, String periodeLabel) {
        return periodeLabel.isEmpty()
            ? "Laporan " + judul
            : "Laporan " + judul + " Periode " + periodeLabel;
    }
}
