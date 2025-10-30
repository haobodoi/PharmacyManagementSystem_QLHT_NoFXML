package com.example.pharmacymanagementsystem_qlht.view.CN_ThongKe;

import com.example.pharmacymanagementsystem_qlht.model.ThongKeTonKho;
import com.example.pharmacymanagementsystem_qlht.model.ThuocHetHan;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;

/**
 * Lớp View (Thay thế cho FXML)
 * Lớp này chỉ chịu trách nhiệm khởi tạo và sắp xếp bố cục các thành phần giao diện.
 * Nó KHÔNG chứa logic nghiệp vụ hay xử lý sự kiện.
 * Nó cung cấp các thành phần (public) để Controller có thể truy cập và gắn logic vào.
 */
public class ThongKeXNT_View {

    // --- Khai báo public các thành phần để Controller có thể truy cập ---

    // Bảng Tồn kho
    public TableView<ThongKeTonKho> tbTon = new TableView<>();
    public TableColumn<ThongKeTonKho, Integer> ColTDK = new TableColumn<>("Tồn đầu kỳ");
    public TableColumn<ThongKeTonKho, String> colDVT = new TableColumn<>("ĐVT");
    public TableColumn<ThongKeTonKho, String> colMaThuoc = new TableColumn<>("Mã thuốc");
    public TableColumn<ThongKeTonKho, Integer> colNTK = new TableColumn<>("Nhập trong kỳ");
    public TableColumn<ThongKeTonKho, String> colTenThuoc = new TableColumn<>("Tên thuốc");
    public TableColumn<ThongKeTonKho, Integer> colTCK = new TableColumn<>("Tồn cuối kỳ");
    public TableColumn<ThongKeTonKho, Integer> colXTK = new TableColumn<>("Xuất trong kỳ");

    // Bảng Hết hạn
    public TableView<ThuocHetHan> tbHetHan = new TableView<>();
    public TableColumn<ThuocHetHan, String> colMaThuocHH = new TableColumn<>("Mã Thuốc");
    public TableColumn<ThuocHetHan, LocalDate> colNgayHH = new TableColumn<>("Ngày hết hạn");
    public TableColumn<ThuocHetHan, Integer> colSoLuong = new TableColumn<>("Số Lượng");
    public TableColumn<ThuocHetHan, String> cotTenThuocHH = new TableColumn<>("Tên Thuốc");

    // Panel bên trái
    public Button btnXuat = new Button("Xuất File 💾");
    public ComboBox<String> cboThoiGian = new ComboBox<>();
    public ComboBox<String> cboXuat = new ComboBox<>();
    public DatePicker dateDen = new DatePicker();
    public DatePicker dateTu = new DatePicker();
    public TextField txtTimNhanh = new TextField();
    public Label lblTu = new Label("Từ:");
    public Label lblDen = new Label("Đến:");

    private Pane root; // Pane gốc

    /**
     * Phương thức chính để dựng giao diện, tương đương với việc tải FXML
     * @return một Parent node chứa toàn bộ giao diện
     */
    public Parent createContent() {
        // --- Cấu hình các cột cho bảng ---
        // Bảng Tồn kho
        colMaThuoc.setPrefWidth(92.33);
        colTenThuoc.setPrefWidth(186.0);
        colDVT.setPrefWidth(112.0);
        ColTDK.setPrefWidth(184.66);
        colNTK.setPrefWidth(173.66);
        colXTK.setPrefWidth(224.33);
        colTCK.setPrefWidth(246.0);
        tbTon.getColumns().addAll(colMaThuoc, colTenThuoc, colDVT, ColTDK, colNTK, colXTK, colTCK);
        tbTon.setPrefHeight(538.0);
        tbTon.setPrefWidth(1223.0);

        // Bảng Hết hạn
        colMaThuocHH.setPrefWidth(197.66);
        cotTenThuocHH.setPrefWidth(478.33);
        colSoLuong.setPrefWidth(164.66);
        colNgayHH.setPrefWidth(379.0);
        tbHetHan.getColumns().addAll(colMaThuocHH, cotTenThuocHH, colSoLuong, colNgayHH);
        tbHetHan.setPrefHeight(259.0);
        tbHetHan.setPrefWidth(1161.0);


        // --- Dựng VBox bên trái (Panel điều khiển) ---
        VBox leftVBox = new VBox();
        leftVBox.setPrefHeight(863.0);
        leftVBox.setPrefWidth(394.0);

        // Tiêu đề
        Label titleLabel = new Label("Báo cáo Xuất-Nhập-Tồn");
        titleLabel.setFont(new Font(28.0));
        titleLabel.setPrefHeight(34.0);
        titleLabel.setPrefWidth(316.0);

        // Icon
        ImageView titleIcon = new ImageView();
        try {
            Image iconImage = new Image(getClass().getResourceAsStream("/com/example/pharmacymanagementsystem_qlht/img/boxes-11430.png"));
            titleIcon.setImage(iconImage);
            titleIcon.setFitHeight(40.0);
            titleIcon.setFitWidth(44.0);
            titleIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Không tải được icon: " + e.getMessage());
        }
        Label iconLabel = new Label();
        iconLabel.setGraphic(titleIcon);
        iconLabel.setPrefHeight(38.0);
        iconLabel.setPrefWidth(30.0);
        HBox.setMargin(iconLabel, new Insets(0, 0, 0, 10.0));

        HBox titleHBox = new HBox(titleLabel, iconLabel);
        titleHBox.setPrefHeight(41.0);
        titleHBox.setPrefWidth(281.0);

        Separator separator = new Separator();
        separator.setPrefWidth(200.0);

        // Tìm nhanh
        Label searchLabel = new Label("Tìm nhanh");
        searchLabel.setFont(new Font(18.0));
        txtTimNhanh.setPromptText("Nhập mã, tên");
        txtTimNhanh.setPrefHeight(40.0);

        HBox spacer = new HBox();
        spacer.setAlignment(javafx.geometry.Pos.CENTER);
        VBox.setMargin(spacer, new Insets(5.0, 0, 0, 0));

        // Thời gian
        Label timeLabel = new Label("Thời gian");
        timeLabel.setFont(new Font(18.0));
        timeLabel.setPrefHeight(27.0);
        VBox.setMargin(timeLabel, new Insets(10.0, 0, 0, 0));
        cboThoiGian.setPrefHeight(49.0);
        cboThoiGian.setPrefWidth(378.0);

        // Từ/Đến
        lblTu.setFont(new Font(18.0));
        dateTu.setPrefHeight(52.0);
        dateTu.setPrefWidth(377.0);
        lblDen.setFont(new Font(18.0));
        dateDen.setPrefHeight(52.0);
        dateDen.setPrefWidth(376.0);

        // Xuất file
        Label exportLabel = new Label("Xuất file");
        exportLabel.setFont(new Font(18.0));
        VBox.setMargin(exportLabel, new Insets(10.0, 0, 0, 0));
        cboXuat.setPrefHeight(49.0);
        cboXuat.setPrefWidth(375.0);
        cboXuat.setPromptText("Chọn đinh dạng ");

        btnXuat.setMnemonicParsing(false);
        btnXuat.setPrefHeight(62.0);
        btnXuat.setPrefWidth(438.0);
        VBox.setMargin(btnXuat, new Insets(10.0, 0, 0, 0));

        // Thêm tất cả vào VBox trái
        leftVBox.getChildren().addAll(
                titleHBox, separator, searchLabel, txtTimNhanh, spacer,
                timeLabel, cboThoiGian, lblTu, dateTu, lblDen, dateDen,
                exportLabel, cboXuat, btnXuat
        );

        // --- Dựng VBox bên phải (Chứa 2 bảng) ---
        VBox rightVBox = new VBox();

        Label warningLabel = new Label("Những sản phẩm đã hết hạn⚠");
        warningLabel.setTextFill(javafx.scene.paint.Color.rgb(196, 35, 35));
        warningLabel.setFont(new Font(18.0));
        VBox.setMargin(warningLabel, new Insets(10.0, 0, 10.0, 10.0));

        rightVBox.getChildren().addAll(tbTon, warningLabel, tbHetHan);

        // --- Dựng HBox gốc (chứa 2 VBox) ---
        HBox mainHBox = new HBox(leftVBox, rightVBox);

        // --- Dựng Pane gốc ---
        root = new Pane();
        root.setPrefHeight(895.0);
        root.setPrefWidth(1646.0);

        // Đặt HBox vào trong Pane (giống FXML)
        mainHBox.setLayoutX(14.0);
        mainHBox.setLayoutY(14.0);
        root.getChildren().add(mainHBox);

        return root;
    }
}