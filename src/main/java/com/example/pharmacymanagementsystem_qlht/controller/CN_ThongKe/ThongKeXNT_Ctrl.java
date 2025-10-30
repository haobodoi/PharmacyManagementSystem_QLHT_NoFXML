package com.example.pharmacymanagementsystem_qlht.controller.CN_ThongKe;

// Các import logic (DAO, Model, POI, iText) giữ nguyên
import com.example.pharmacymanagementsystem_qlht.dao.ThongKeXNT_Dao;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeTonKho;
import com.example.pharmacymanagementsystem_qlht.model.ThuocHetHan;

import com.example.pharmacymanagementsystem_qlht.view.CN_ThongKe.ThongKeXNT_View;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
// XÓA: import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
// XÓA: import java.util.ResourceBundle;

// Các import POI và iText giữ nguyên
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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


// Xóa "implements Initializable"
public class ThongKeXNT_Ctrl extends Application {

    // --- 1. XÓA TẤT CẢ CÁC KHAI BÁO @FXML ---
    // (Chúng đã được chuyển sang ThongKeXNT_View)

    // --- 2. KHAI BÁO VIEW ---
    private ThongKeXNT_View view;

    // --- 3. CÁC BIẾN LOGIC (GIỮ NGUYÊN) ---
    private ObservableList<ThongKeTonKho> masterDataTonKho = FXCollections.observableArrayList();
    private ObservableList<ThuocHetHan> dataHetHan = FXCollections.observableArrayList();
    private ThongKeXNT_Dao thongKeDao = new ThongKeXNT_Dao();
    public static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";


    // --- 4. HÀM START (SỬA ĐỔI ĐỂ TẠO VIEW TỪ CODE) ---
    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Khởi tạo View
        view = new ThongKeXNT_View();

        // 2. Dựng giao diện từ View
        Parent root = view.createContent();

        // 3. Gọi hàm setup logic (thay thế cho initialize)
        setupLogic();

        // 4. Tạo Scene và tải CSS (Giữ nguyên)
        Scene scene = new Scene(root);
        try {
            // Lấy CSS từ đường dẫn trong FXML cũ [cite: 1]
            String cssPath = getClass().getResource("/com/example/pharmacymanagementsystem_qlht/css/ThongKeBanHang.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Không thể tải file CSS: " + e.getMessage());
        }

        // 5. Hiển thị Stage (Giữ nguyên)
        primaryStage.setTitle("Báo cáo Xuất - Nhập - Tồn");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- 5. HÀM SETUP LOGIC (ĐỔI TÊN TỪ initialize) ---
    private void setupLogic() {
        // 1. Cấu hình các bảng
        setupThongKeTable();
        setupHetHanTable();
        setupComboBoxes();
        setupSearchFilter();

        // 2. Cấu hình binding (truy cập qua 'view.')
        view.lblTu.managedProperty().bind(view.lblTu.visibleProperty());
        view.dateTu.managedProperty().bind(view.dateTu.visibleProperty());
        view.lblDen.managedProperty().bind(view.lblDen.visibleProperty());
        view.dateDen.managedProperty().bind(view.dateDen.visibleProperty());

        // 3. Tải dữ liệu ban đầu
        loadDataHetHan();
        view.cboThoiGian.setValue("Hôm nay"); // Truy cập qua 'view.'

        // 4. Gắn sự kiện (truy cập qua 'view.')
        view.btnXuat.setOnAction(e -> xuatFile(e));
    }

    // --- 6. CÁC HÀM SETUP (SỬA ĐỔI ĐỂ TRUY CẬP QUA 'view.') ---
    private void setupThongKeTable() {
        view.colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        view.colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        view.colDVT.setCellValueFactory(new PropertyValueFactory<>("dvt"));
        view.ColTDK.setCellValueFactory(new PropertyValueFactory<>("tonDauKy"));
        view.colNTK.setCellValueFactory(new PropertyValueFactory<>("nhapTrongKy"));
        view.colXTK.setCellValueFactory(new PropertyValueFactory<>("xuatTrongKy"));
        view.colTCK.setCellValueFactory(new PropertyValueFactory<>("tonCuoiKy"));
    }

    private void setupHetHanTable() {
        view.colMaThuocHH.setCellValueFactory(new PropertyValueFactory<>("maThuocHH"));
        view.cotTenThuocHH.setCellValueFactory(new PropertyValueFactory<>("tenThuocHH"));
        view.colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        view.colNgayHH.setCellValueFactory(new PropertyValueFactory<>("ngayHetHan"));
        view.tbHetHan.setItems(dataHetHan); // Gắn danh sách dữ liệu vào bảng
    }

    private void setupComboBoxes() {
        view.cboThoiGian.setItems(FXCollections.observableArrayList(
                "Hôm nay", "Tuần này", "Tháng này", "Năm nay", "Tùy chọn"
        ));

        view.cboXuat.setItems(FXCollections.observableArrayList(
                "Excel",
                "PDF"
        ));
        view.cboXuat.setValue("Excel"); // Đặt giá trị mặc định

        view.cboThoiGian.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                handleThoiGianChange();
            }
        });

        view.dateTu.setOnAction(e -> handleDateChange());
        view.dateDen.setOnAction(e -> handleDateChange());

        view.lblTu.setVisible(false);
        view.dateTu.setVisible(false);
        view.lblDen.setVisible(false);
        view.dateDen.setVisible(false);
    }

    private void setupSearchFilter() {
        FilteredList<ThongKeTonKho> filteredData = new FilteredList<>(masterDataTonKho, p -> true);

        view.txtTimNhanh.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(thongKe -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (thongKe.getMaThuoc().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return thongKe.getTenThuoc().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<ThongKeTonKho> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(view.tbTon.comparatorProperty());
        view.tbTon.setItems(sortedData);
    }

    // --- 7. CÁC HÀM XỬ LÝ LOGIC (SỬA ĐỔI ĐỂ TRUY CẬP QUA 'view.') ---
    private void handleThoiGianChange() {
        String selected = view.cboThoiGian.getValue(); // Sửa ở đây
        if (selected == null) return;

        LocalDate today = LocalDate.now();
        LocalDate tu = null;
        LocalDate den = null;

        if (selected.equals("Tùy chọn")) {
            view.lblTu.setVisible(true);     // Sửa ở đây
            view.dateTu.setVisible(true);    // Sửa ở đây
            view.lblDen.setVisible(true);    // Sửa ở đây
            view.dateDen.setVisible(true);   // Sửa ở đây

            if (view.dateTu.getValue() != null && view.dateDen.getValue() != null) { // Sửa ở đây
                loadDataThongKe(view.dateTu.getValue(), view.dateDen.getValue()); // Sửa ở đây
            } else {
                masterDataTonKho.clear();
            }
            return;
        }

        // Nếu không phải "Tùy chọn", ẩn các control
        view.lblTu.setVisible(false);    // Sửa ở đây
        view.dateTu.setVisible(false);   // Sửa ở đây
        view.lblDen.setVisible(false);   // Sửa ở đây
        view.dateDen.setVisible(false);  // Sửa ở đây

        switch (selected) {
            case "Hôm nay":
                tu = today;
                den = today;
                break;
            case "Tuần này":
                tu = today.with(DayOfWeek.MONDAY);
                den = today.with(DayOfWeek.SUNDAY);
                break;
            case "Tháng này":
                tu = today.withDayOfMonth(1);
                den = today.withDayOfMonth(today.lengthOfMonth());
                break;
            case "Năm nay":
                tu = today.withDayOfYear(1);
                den = today.withDayOfYear(today.lengthOfYear());
                break;
        }

        view.dateTu.setValue(tu);    // Sửa ở đây
        view.dateDen.setValue(den);  // Sửa ở đây
        loadDataThongKe(tu, den);
    }

    private void handleDateChange() {
        if ("Tùy chọn".equals(view.cboThoiGian.getValue())) { // Sửa ở đây
            LocalDate tu = view.dateTu.getValue(); // Sửa ở đây
            LocalDate den = view.dateDen.getValue(); // Sửa ở đây

            if (tu != null && den != null) {
                if (den.isBefore(tu)) {
                    showAlert(Alert.AlertType.ERROR, "Lỗi chọn ngày", "Ngày kết thúc không được trước ngày bắt đầu.");
                    masterDataTonKho.clear();
                } else {
                    loadDataThongKe(tu, den);
                }
            }
        }
    }

    // --- CÁC HÀM SAU GIỮ NGUYÊN (VÌ KHÔNG TRUY CẬP TRỰC TIẾP VÀO FXML) ---

    private void loadDataThongKe(LocalDate tu, LocalDate den) {
        masterDataTonKho.clear();
        try {
            masterDataTonKho.setAll(thongKeDao.getThongKeXNT(tu, den));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải dữ liệu thống kê: " + e.getMessage());
        }
    }

    private void loadDataHetHan() {
        dataHetHan.clear();
        try {
            dataHetHan.setAll(thongKeDao.getThuocHetHan());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách thuốc hết hạn: " + e.getMessage());
        }
    }

    // XUẤT FILE

    // (Sửa 1 dòng trong hàm này)
    private void xuatFile(ActionEvent event) {
        String selectedFormat = view.cboXuat.getValue(); // Sửa ở đây
        if (selectedFormat == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn định dạng", "Vui lòng chọn định dạng file (Excel hoặc PDF) để xuất.");
            return;
        }

        if (masterDataTonKho.isEmpty() && dataHetHan.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Không có dữ liệu", "Không có dữ liệu để xuất.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu báo cáo");
        fileChooser.setInitialFileName("BaoCao_XNT_" + LocalDate.now());

        if (selectedFormat.equals("Excel")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
            File file = fileChooser.showSaveDialog(view.btnXuat.getScene().getWindow()); // Sửa ở đây
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
            File file = fileChooser.showSaveDialog(view.btnXuat.getScene().getWindow()); // Sửa ở đây
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

    // (Hàm này giữ nguyên)
    private void xuatExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);


            Sheet sheet1 = workbook.createSheet("Thong Ke XNT");
            String[] headers1 = {"Mã thuốc", "Tên thuốc", "ĐVT", "Tồn đầu kỳ", "Nhập trong kỳ", "Xuất trong kỳ", "Tồn cuối kỳ"};

            Row headerRow1 = sheet1.createRow(0);
            for (int i = 0; i < headers1.length; i++) {
                Cell cell = headerRow1.createCell(i);
                cell.setCellValue(headers1[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum1 = 1;
            for (ThongKeTonKho tk : masterDataTonKho) {
                Row row = sheet1.createRow(rowNum1++);
                row.createCell(0).setCellValue(tk.getMaThuoc());
                row.createCell(1).setCellValue(tk.getTenThuoc());
                row.createCell(2).setCellValue(tk.getDvt());
                row.createCell(3).setCellValue(tk.getTonDauKy());
                row.createCell(4).setCellValue(tk.getNhapTrongKy());
                row.createCell(5).setCellValue(tk.getXuatTrongKy());
                row.createCell(6).setCellValue(tk.getTonCuoiKy());
            }

            for (int i = 0; i < headers1.length; i++) {
                sheet1.autoSizeColumn(i);
            }
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        }
    }

    // (Hàm này giữ nguyên)
    private void xuatPDF(File file) throws IOException {
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

        PdfFont font;
        try {
            font = PdfFontFactory.createFont(FONT_PATH);
        } catch (IOException e) {
            System.err.println("Không tìm thấy font tại: " + FONT_PATH + ". Sử dụng font mặc định.");
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        }
        document.setFont(font);

        document.add(new Paragraph("BÁO CÁO XUẤT - NHẬP - TỒN")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("I. Thống kê Xuất-Nhập-Tồn")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15));

        float[] columnWidths1 = {1.2f, 3f, 1f, 1.5f, 1.5f, 1.5f, 1.5f};
        Table table1 = new Table(UnitValue.createPercentArray(columnWidths1));
        table1.setWidth(UnitValue.createPercentValue(100));

        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Mã thuốc").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Tên thuốc").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("ĐVT").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Tồn đầu kỳ").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Nhập trong kỳ").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Xuất trong kỳ").setBold()));
        table1.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Tồn cuối kỳ").setBold()));

        for (ThongKeTonKho tk : masterDataTonKho) {
            table1.addCell(tk.getMaThuoc());
            table1.addCell(tk.getTenThuoc());
            table1.addCell(tk.getDvt());
            table1.addCell(String.valueOf(tk.getTonDauKy()));
            table1.addCell(String.valueOf(tk.getNhapTrongKy()));
            table1.addCell(String.valueOf(tk.getXuatTrongKy()));
            table1.addCell(String.valueOf(tk.getTonCuoiKy()));
        }
        document.add(table1);

        document.add(new Paragraph("Báo cáo được tạo ngày: " + LocalDate.now())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10)
                .setMarginTop(15));

        document.close();
    }

    // (Hàm này giữ nguyên)
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Hàm main để chạy ứng dụng (Nếu cần)
    public static void main(String[] args) {
        launch(args);
    }
}