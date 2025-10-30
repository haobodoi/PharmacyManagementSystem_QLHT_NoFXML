package com.example.pharmacymanagementsystem_qlht.view.CN_ThongKe;

import com.example.pharmacymanagementsystem_qlht.model.ThongKeBanHang;
import com.example.pharmacymanagementsystem_qlht.model.ThongKeSanPham;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Lớp View (Thay thế cho FXML)
 * Chỉ chịu trách nhiệm khởi tạo và sắp xếp bố cục các thành phần giao diện.
 */
public class ThongKeBanHang_View {

    // --- Khai báo public các thành phần để Controller truy cập ---

    // Panel trái
    public Button btnBang = new Button("Bảng");
    public Button btnBieuDo = new Button("Biểu đồ");
    public Button btnXuat = new Button("Xuất File 💾");
    public ComboBox<String> cboThoiGian = new ComboBox<>();
    public ComboBox<String> cboXuatfile = new ComboBox<>();
    public DatePicker dateTu = new DatePicker();
    public DatePicker dateDen = new DatePicker();
    public Label lblTu = new Label("Từ:");
    public Label lblDen = new Label("Đến:");

    // Panel phải - Bảng Doanh Thu
    public TableView<ThongKeBanHang> tableDoanhThu = new TableView<>();
    public TableColumn<ThongKeBanHang, String> cotTG = new TableColumn<>("Thời gian");
    public TableColumn<ThongKeBanHang, Integer> cotSLHoaDon = new TableColumn<>("Số lượng HĐ");
    public TableColumn<ThongKeBanHang, Double> cotTongGT = new TableColumn<>("Tổng giá trị");
    public TableColumn<ThongKeBanHang, Double> cotGG = new TableColumn<>("Giảm giá");
    public TableColumn<ThongKeBanHang, Integer> cotDT = new TableColumn<>("Số lượng đơn trả");
    public TableColumn<ThongKeBanHang, Double> cotGTDonTra = new TableColumn<>("Giá trị đơn trả");
    public TableColumn<ThongKeBanHang, Double> cotDoanhThu = new TableColumn<>("Doanh thu");

    // Panel phải - Biểu đồ
    public CategoryAxis xAxis = new CategoryAxis();
    public BarChart<String, Number> chartDoanhThu = new BarChart<>(xAxis, new NumberAxis());

    // Panel phải - Bảng Top Sản Phẩm
    public TableView<ThongKeSanPham> tableTopSanPham = new TableView<>();
    public TableColumn<ThongKeSanPham, String> cotMaThuoc = new TableColumn<>("Mã thuốc");
    public TableColumn<ThongKeSanPham, String> cotTenThuoc = new TableColumn<>("Tên thuốc");
    public TableColumn<ThongKeSanPham, Integer> cotSL = new TableColumn<>("Số lượng");
    public TableColumn<ThongKeSanPham, Double> cotThanhTien = new TableColumn<>("Thành tiền");

    /**
     * Phương thức chính để dựng giao diện
     * @return một Parent node chứa toàn bộ giao diện
     */
    public Parent createContent() {
        // --- Cấu hình Bảng Doanh Thu ---
        cotTG.setPrefWidth(147.33); cotTG.setStyle("-fx-alignment: CENTER;");
        cotSLHoaDon.setPrefWidth(170.66); cotSLHoaDon.setStyle("-fx-alignment: CENTER;");
        cotTongGT.setPrefWidth(129.66); cotTongGT.setStyle("-fx-alignment: CENTER;");
        cotGG.setPrefWidth(142.99); cotGG.setStyle("-fx-alignment: CENTER;");
        cotDT.setPrefWidth(150.66); cotDT.setStyle("-fx-alignment: CENTER;");
        cotGTDonTra.setPrefWidth(193.33); cotGTDonTra.setStyle("-fx-alignment: CENTER;");
        cotDoanhThu.setPrefWidth(229.66); cotDoanhThu.setStyle("-fx-alignment: CENTER;");
        tableDoanhThu.getColumns().addAll(cotTG, cotSLHoaDon, cotTongGT, cotGG, cotDT, cotGTDonTra, cotDoanhThu);
        tableDoanhThu.setPrefHeight(510.0);
        tableDoanhThu.setPrefWidth(1161.0);

        // --- Cấu hình Biểu đồ ---
        chartDoanhThu.setPrefHeight(510.0);
        chartDoanhThu.setPrefWidth(1161.0);
        chartDoanhThu.setVisible(false); // Ẩn ban đầu
        xAxis.setSide(javafx.geometry.Side.BOTTOM);

        // --- Cấu hình Bảng Top Sản Phẩm ---
        cotMaThuoc.setPrefWidth(153.0); cotMaThuoc.setStyle("-fx-alignment: CENTER;");
        cotTenThuoc.setPrefWidth(507.0);
        cotSL.setPrefWidth(129.0); cotSL.setStyle("-fx-alignment: CENTER;");
        cotThanhTien.setPrefWidth(370.0); cotThanhTien.setStyle("-fx-alignment: CENTER;");
        tableTopSanPham.getColumns().addAll(cotMaThuoc, cotTenThuoc, cotSL, cotThanhTien);
        tableTopSanPham.setPrefHeight(273.0);
        tableTopSanPham.setPrefWidth(1161.0);

        // --- Dựng VBox bên trái (Panel điều khiển) ---
        VBox leftVBox = new VBox();
        leftVBox.setPrefHeight(1126.0);
        leftVBox.setPrefWidth(449.0);

        // Tiêu đề
        Label titleLabel = new Label("Thốnng kê doanh thu ");
        titleLabel.setFont(new Font(28.0));
        ImageView titleIcon = createIcon("/com/example/pharmacymanagementsystem_qlht/img/bar-chart.png", 33, 40);
        HBox titleHBox = new HBox(titleLabel, new Label("", titleIcon));

        Separator separator = new Separator();
        separator.setPrefWidth(200.0);

        // Kiểu hiển thị
        Label displayLabel = new Label("Kiểu hiển thị");
        displayLabel.setFont(new Font(18.0));

        btnBang.setId("btnBang");
        btnBang.setPrefHeight(61.0);
        btnBang.setPrefWidth(103.0);
        btnBang.setGraphic(createIcon("/com/example/pharmacymanagementsystem_qlht/img/table.png", 40, 38));
        HBox.setMargin(btnBang, new Insets(0, 0, 0, 30.0));

        btnBieuDo.setId("btnBieuDo");
        btnBieuDo.setPrefHeight(62.0);
        btnBieuDo.setPrefWidth(104.0);
        btnBieuDo.setGraphic(createIcon("/com/example/pharmacymanagementsystem_qlht/img/improvement.png", 35, 34));
        HBox.setMargin(btnBieuDo, new Insets(0, 0, 0, 30.0));

        HBox buttonHBox = new HBox(btnBang, btnBieuDo);
        buttonHBox.setAlignment(Pos.CENTER);
        VBox.setMargin(buttonHBox, new Insets(5.0, 0, 0, 0));

        // Thời gian
        Label timeLabel = new Label("Thời gian");
        timeLabel.setFont(new Font(18.0));
        VBox.setMargin(timeLabel, new Insets(10.0, 0, 0, 0));
        cboThoiGian.setPrefHeight(49.0);
        cboThoiGian.setPrefWidth(446.0);
        cboThoiGian.setPromptText("Hôm Nay");

        lblTu.setFont(new Font(18.0));
        dateTu.setPrefHeight(39.0);
        dateTu.setPrefWidth(442.0);

        lblDen.setFont(new Font(18.0));
        dateDen.setPrefHeight(39.0);
        dateDen.setPrefWidth(441.0);

        // Xuất file
        Label exportLabel = new Label("Xuất file");
        exportLabel.setFont(new Font(18.0));
        VBox.setMargin(exportLabel, new Insets(10.0, 0, 0, 0));
        cboXuatfile.setPrefHeight(49.0);
        cboXuatfile.setPrefWidth(441.0);
        cboXuatfile.setPromptText("Chọn định dạng file");

        btnXuat.setPrefHeight(53.0);
        btnXuat.setPrefWidth(438.0);
        VBox.setMargin(btnXuat, new Insets(10.0, 0, 0, 0));

        // Thêm tất cả vào VBox trái
        leftVBox.getChildren().addAll(
                titleHBox, separator, displayLabel, buttonHBox,
                timeLabel, cboThoiGian, lblTu, dateTu, lblDen, dateDen,
                exportLabel, cboXuatfile, btnXuat
        );

        // --- Dựng VBox bên phải (Chứa Bảng và Biểu đồ) ---
        VBox rightVBox = new VBox();
        rightVBox.setPrefHeight(1126.0);
        rightVBox.setPrefWidth(1161.0);

        Label revenueLabel = new Label("Doanh thu");
        revenueLabel.setFont(new Font(18.0));
        revenueLabel.setAlignment(Pos.CENTER);
        revenueLabel.setPrefWidth(1167.0);

        Label topProductLabel = new Label("Top 5 sản phẩm bán chạy🔥");
        topProductLabel.setTextFill(javafx.scene.paint.Color.rgb(198, 49, 49));
        topProductLabel.setFont(new Font(24.0));
        VBox.setMargin(topProductLabel, new Insets(8.0, 0, 8.0, 10.0));

        rightVBox.getChildren().addAll(revenueLabel, tableDoanhThu, chartDoanhThu, topProductLabel, tableTopSanPham);

        // --- Dựng HBox gốc ---
        HBox mainHBox = new HBox(leftVBox, rightVBox);

        // --- Dựng Pane gốc ---
        Pane root = new Pane();
        root.setPrefHeight(895.0);
        root.setPrefWidth(1646.0);

        // Đặt HBox vào trong Pane
        mainHBox.setLayoutX(14.0);
        mainHBox.setLayoutY(14.0);
        root.getChildren().add(mainHBox);

        return root;
    }

    /**
     * Hàm trợ giúp tạo ImageView
     */
    private ImageView createIcon(String path, double height, double width) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            ImageView icon = new ImageView(image);
            icon.setFitHeight(height);
            icon.setFitWidth(width);
            icon.setPreserveRatio(true);
            return icon;
        } catch (Exception e) {
            System.err.println("Không tải được icon: " + path);
            return new ImageView();
        }
    }
}