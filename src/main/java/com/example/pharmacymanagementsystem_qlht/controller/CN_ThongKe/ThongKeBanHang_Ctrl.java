package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

// Imports cho việc xuất file
import com.example.pharmacymanagementsystem_qlht.view.CN_ThongKe.ThongKeBanHang_View;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Imports cho Excel (Apache POI)
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Imports cho PDF (iText 7)
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;

// Imports logic
import java.text.DecimalFormat;
import javafx.scene.control.TableCell;
import com.example.pharmacymanagementsystem_qlht.dao.ThongKe_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeSanPham;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.time.LocalDate;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

// Xóa: @FXML, FXMLLoader, Initializable, URL, ResourceBundle

public class ThongKeBanHang_Ctrl extends Application {

    // --- 1. KHAI BÁO VIEW ---
    private ThongKeBanHang_View view;

    // --- 2. CÁC BIẾN LOGIC (GIỮ NGUYÊN) ---
    private ThongKe_Dao tkDao = new ThongKe_Dao();
    private ObservableList<ThongKeBanHang> listThongKe;
    private ObservableList<ThongKeSanPham> listTopSanPham;


    // --- 3. HÀM START (SỬA ĐỔI) ---
    @Override
    public void start(Stage stage) throws Exception {
        // 1. Khởi tạo View
        view = new ThongKeBanHang_View();

        // 2. Dựng giao diện từ View
        Parent root = view.createContent();

        // 3. Gọi hàm setup logic (thay thế cho initialize)
        setupLogic();

        // 4. Tạo Scene và tải CSS (Lấy từ hàm start cũ)
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm());

        // 5. Hiển thị
        stage.setScene(scene);
        stage.show();
    }


    // --- 4. HÀM SETUP LOGIC (ĐỔI TÊN TỪ initialize) ---
    private void setupLogic() {
        // Gắn sự kiện (truy cập qua 'view.')
        view.btnXuat.setOnAction(e -> xuatFile(e));
        view.btnBang.setOnAction(e -> hienThiBang(e));
        view.btnBieuDo.setOnAction(e -> hienThiBieuDo(e));

        // Binding
        view.chartDoanhThu.managedProperty().bind(view.chartDoanhThu.visibleProperty());
        view.tableDoanhThu.managedProperty().bind(view.tableDoanhThu.visibleProperty());

        DecimalFormat formatter = new DecimalFormat("#,##0");

        // Setup ComboBoxes
        view.cboThoiGian.getItems().addAll("Hôm nay", "Tuần này", "Tháng này", "Năm Nay", "Tùy chọn");
        view.cboXuatfile.getItems().addAll("Excel", "PDF");

        // --- Setup Bảng Doanh Thu ---
        view.cotTG.setCellValueFactory(new PropertyValueFactory<>("thoiGian"));
        view.cotSLHoaDon.setCellValueFactory(new PropertyValueFactory<>("soLuongHoaDon"));

        view.cotTongGT.setCellValueFactory(new PropertyValueFactory<>("tongGiaTri"));
        view.cotTongGT.setCellFactory(col -> createFormattedCell(formatter));

        view.cotGG.setCellValueFactory(new PropertyValueFactory<>("giamGia"));
        view.cotGG.setCellFactory(col -> createFormattedCell(formatter));

        view.cotDT.setCellValueFactory(new PropertyValueFactory<>("soLuongDonTra"));

        view.cotGTDonTra.setCellValueFactory(new PropertyValueFactory<>("giaTriDonTra"));
        view.cotGTDonTra.setCellFactory(col -> createFormattedCell(formatter));

        view.cotDoanhThu.setCellValueFactory(new PropertyValueFactory<>("doanhThu"));
        view.cotDoanhThu.setCellFactory(col -> createFormattedCell(formatter));

        // --- Setup Bảng Top Sản Phẩm ---
        view.cotMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        view.cotTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        view.cotSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        view.cotThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        view.cotThanhTien.setCellFactory(col -> createFormattedCell(formatter));

        // --- Setup ẩn/hiện DatePicker ---
        view.lblTu.setVisible(false);
        view.dateTu.setVisible(false);
        view.lblDen.setVisible(false);
        view.dateDen.setVisible(false);

        view.lblTu.managedProperty().bind(view.lblTu.visibleProperty());
        view.dateTu.managedProperty().bind(view.dateTu.visibleProperty());
        view.lblDen.managedProperty().bind(view.lblDen.visibleProperty());
        view.dateDen.managedProperty().bind(view.dateDen.visibleProperty());

        // --- Gắn Listeners ---
        view.cboThoiGian.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isCustom = newValue.equals("Tùy chọn");

                view.lblTu.setVisible(isCustom);
                view.dateTu.setVisible(isCustom);
                view.lblDen.setVisible(isCustom);
                view.dateDen.setVisible(isCustom);

                if (isCustom) {
                    attemptAutoLoadTuyChon();
                } else {
                    loadData(newValue);
                }
            }
        });

        view.dateTu.valueProperty().addListener((options, oldValue, newValue) -> attemptAutoLoadTuyChon());
        view.dateDen.valueProperty().addListener((options, oldValue, newValue) -> attemptAutoLoadTuyChon());

        // --- Tải dữ liệu ban đầu ---
        view.cboThoiGian.setValue("Hôm nay");
        view.chartDoanhThu.setAnimated(false);
    }

    /**
     * Hàm trợ giúp tạo Cell định dạng số
     */
    private <T> TableCell<T, Double> createFormattedCell(DecimalFormat formatter) {
        return new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        };
    }

    // --- 5. CÁC HÀM LOGIC (SỬA ĐỔI ĐỂ DÙNG 'view.') ---

    private void loadData(String thoiGian) {
        listTopSanPham = FXCollections.observableArrayList(tkDao.getTop5SanPham(thoiGian));
        listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang(thoiGian));

        view.tableTopSanPham.setItems(listTopSanPham);
        view.tableDoanhThu.setItems(listThongKe);

        view.chartDoanhThu.getData().clear();
        if (view.xAxis != null) {
            view.xAxis.getCategories().clear();
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        ObservableList<String> categories = FXCollections.observableArrayList();
        for (ThongKeBanHang tk : listThongKe) {
            String label = tk.getThoiGian();
            categories.add(label);
            series.getData().add(new XYChart.Data<>(label, tk.getDoanhThu()));
        }

        view.chartDoanhThu.getData().add(series);
        view.xAxis.setCategories(categories);

        view.chartDoanhThu.setVisible(false);
        view.tableDoanhThu.setVisible(true);

        view.xAxis.setTickLabelRotation(-20);
    }

    private void attemptAutoLoadTuyChon() {
        String selectedTime = view.cboThoiGian.getValue();
        if (selectedTime == null || !selectedTime.equals("Tùy chọn")) {
            return;
        }

        LocalDate tuNgay = view.dateTu.getValue();
        LocalDate denNgay = view.dateDen.getValue();

        if (tuNgay == null || denNgay == null) {
            return;
        }
        if (tuNgay.isAfter(denNgay)) {
            System.out.println("Ngày bắt đầu không thể sau ngày kết thúc");
            view.tableDoanhThu.getItems().clear();
            view.tableTopSanPham.getItems().clear();
            view.chartDoanhThu.getData().clear();
            return;
        }
        loadDataTuyChon(tuNgay, denNgay);
    }


    private void loadDataTuyChon(LocalDate tuNgay, LocalDate denNgay) {
        listTopSanPham = FXCollections.observableArrayList(tkDao.getTop5SanPham_TuyChon(tuNgay, denNgay));
        listThongKe = FXCollections.observableArrayList(tkDao.getThongKeBanHang_TuyChon(tuNgay, denNgay));

        view.tableTopSanPham.setItems(listTopSanPham);
        view.tableDoanhThu.setItems(listThongKe);

        view.chartDoanhThu.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (Tùy chọn)");

        ObservableList<String> categories = FXCollections.observableArrayList();
        for (ThongKeBanHang tk : listThongKe) {
            String tg = tk.getThoiGian() == null ? "" : tk.getThoiGian();
            categories.add(tg);
            series.getData().add(new XYChart.Data<>(tg, tk.getDoanhThu()));
        }

        if (view.xAxis != null) view.xAxis.setCategories(categories);
        view.chartDoanhThu.getData().add(series);

        view.chartDoanhThu.setVisible(false);
        view.tableDoanhThu.setVisible(true);
    }

    // --- 6. CÁC HÀM XUẤT FILE (SỬA ĐỔI ĐỂ DÙNG 'view.') ---

    private void xuatFile(ActionEvent event) {
        String selectedFormat = view.cboXuatfile.getValue();
        if (selectedFormat == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn định dạng", "Vui lòng chọn định dạng file (Excel hoặc PDF) để xuất.");
            return;
        }

        if (listThongKe == null || listTopSanPham == null || listThongKe.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Không có dữ liệu", "Không có dữ liệu thống kê để xuất.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file thống kê");
        fileChooser.setInitialFileName("BaoCao_" + LocalDate.now());

        if (selectedFormat.equals("Excel")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
            File file = fileChooser.showSaveDialog(view.btnXuat.getScene().getWindow());
            if (file != null) {
                try {
                    xuatExcel(file);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất file Excel thành công!");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi xuất file Excel: " + e.getMessage());
                }
            }
        } else if (selectedFormat.equals("PDF")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
            File file = fileChooser.showSaveDialog(view.btnXuat.getScene().getWindow());
            if (file != null) {
                try {
                    xuatPDF(file);
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xuất file PDF thành công!");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi xuất file PDF: " + e.getMessage());
                }
            }
        }
    }

    private void xuatExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheetDT = workbook.createSheet("Thong ke Doanh thu");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            // Lấy header từ text của cột (qua view)
            String[] headersDT = {
                    view.cotTG.getText(), view.cotSLHoaDon.getText(), view.cotTongGT.getText(),
                    view.cotGG.getText(), view.cotDT.getText(), view.cotGTDonTra.getText(), view.cotDoanhThu.getText()
            };

            Row headerRowDT = sheetDT.createRow(0);
            for (int i = 0; i < headersDT.length; i++) {
                Cell cell = headerRowDT.createCell(i);
                cell.setCellValue(headersDT[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNumDT = 1;
            for (ThongKeBanHang tk : listThongKe) {
                Row row = sheetDT.createRow(rowNumDT++);
                row.createCell(0).setCellValue(tk.getThoiGian());
                row.createCell(1).setCellValue(tk.getSoLuongHoaDon());
                row.createCell(2).setCellValue(tk.getTongGiaTri());
                row.createCell(3).setCellValue(tk.getGiamGia());
                row.createCell(4).setCellValue(tk.getSoLuongDonTra());
                row.createCell(5).setCellValue(tk.getGiaTriDonTra());
                row.createCell(6).setCellValue(tk.getDoanhThu());
            }

            for (int i = 0; i < headersDT.length; i++) {
                sheetDT.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    public static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    private void xuatPDF(File file) throws IOException {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont font;
        try {
            font = PdfFontFactory.createFont(FONT_PATH);
        } catch (IOException e) {
            System.err.println("Không tìm thấy font tại: " + FONT_PATH + ". Sử dụng font mặc định.");
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        }
        document.setFont(font);

        document.add(new Paragraph("BÁO CÁO THỐNG KÊ DOANH THU")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("I. Thống kê Doanh thu")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15));

        float[] columnWidthsDT = {2, 1, 1, 1, 1, 1, 1};
        Table tableDT = new Table(UnitValue.createPercentArray(columnWidthsDT));
        tableDT.setWidth(UnitValue.createPercentValue(100));

        // Lấy header từ text của cột (qua view)
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotTG.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotSLHoaDon.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotTongGT.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotGG.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotDT.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotGTDonTra.getText()).setBold()));
        tableDT.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(view.cotDoanhThu.getText()).setBold()));

        for (ThongKeBanHang tk : listThongKe) {
            tableDT.addCell(tk.getThoiGian());
            tableDT.addCell(String.valueOf(tk.getSoLuongHoaDon()));
            tableDT.addCell(String.valueOf(tk.getTongGiaTri()));
            tableDT.addCell(String.valueOf(tk.getGiamGia()));
            tableDT.addCell(String.valueOf(tk.getSoLuongDonTra()));
            tableDT.addCell(String.valueOf(tk.getGiaTriDonTra()));
            tableDT.addCell(String.valueOf(tk.getDoanhThu()));
        }
        document.add(tableDT);
        document.close();
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- 7. XỬ LÝ SỰ KIỆN GIAO DIỆN (Bỏ @FXML) ---
    private void hienThiBieuDo(ActionEvent event) {
        view.chartDoanhThu.setVisible(true);
        view.tableDoanhThu.setVisible(false);
    }

    private void hienThiBang(ActionEvent event) {
        view.chartDoanhThu.setVisible(false);
        view.tableDoanhThu.setVisible(true);
    }

    // Hàm main để chạy (nếu cần test)
    public static void main(String[] args) {
        launch(args);
    }
}