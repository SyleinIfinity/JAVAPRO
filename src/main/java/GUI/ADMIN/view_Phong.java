package GUI.ADMIN;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import BLL.ADMIN.ctl_Phong;
import DLL.DA.LoaiPhong;
import DLL.DA.Phong;
import GUI.view_main;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class view_Phong extends JPanel {
    private JPanel pnForm;
    private JTable tblPhong;
    private JTextField txtMaPhong, txtSoPhong, txtSoTang, txtMaChiNhanh;
    private JComboBox<String> cmbLoaiPhong, cmbTrangThai;
    private JTextField txtTimKiem;
    private JComboBox<String> cmbLoc;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem, btnLamMoi;
    private DefaultTableModel model;
    private Color mauChinh = new Color(41, 128, 185);
    private Color mauPhu = new Color(52, 152, 219);
    private Color mauNhan = new Color(230, 126, 34);
    private Color mauNen = new Color(236, 240, 241);
    view_main vMain;
    ctl_Phong controller;

    public view_Phong(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel searchPanel = taoSearchFilterPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = taoTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = taoButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JPanel formPanel = taoFormPanel();
        add(formPanel, BorderLayout.EAST);
        controller = new ctl_Phong(this, vMain);
    }
    
    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauChinh);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoSearchFilterPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(mauNen);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel searchInputPanel = new JPanel(new BorderLayout(10, 0));
        searchInputPanel.setBackground(mauNen);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        searchInputPanel.add(lblSearch, BorderLayout.WEST);
        searchInputPanel.add(txtTimKiem, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new BorderLayout(10, 0));
        filterPanel.setBackground(mauNen);

        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        cmbLoc = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt trước", "Có người ở", "Bảo trì"});
        cmbLoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLoc.setBackground(Color.WHITE);

        filterPanel.add(lblFilter, BorderLayout.WEST);
        filterPanel.add(cmbLoc, BorderLayout.CENTER);

        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel taoFormPanel() {
        pnForm = new JPanel();
        pnForm.setLayout(new GridBagLayout());
        pnForm.setBackground(Color.WHITE);
        pnForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(mauChinh, 2),
                        "Thông tin phòng",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 16),
                        mauChinh
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnForm.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Mã phòng (read-only, auto-generated)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMaPhong = new JLabel("Mã phòng:");
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblMaPhong, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMaPhong = createStyledTextField();
        txtMaPhong.setEditable(false);
        txtMaPhong.setBackground(new Color(245, 245, 245));
        pnForm.add(txtMaPhong, gbc);

        // Số phòng
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel lblSoPhong = new JLabel("Số phòng:");
        lblSoPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblSoPhong, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtSoPhong = createStyledTextField();
        pnForm.add(txtSoPhong, gbc);

        // Loại phòng
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel lblLoaiPhong = new JLabel("Loại phòng:");
        lblLoaiPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblLoaiPhong, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbLoaiPhong = new JComboBox<>();
        cmbLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLoaiPhong.setBackground(Color.WHITE);
        cmbLoaiPhong.setPreferredSize(new Dimension(200, 35));
        cmbLoaiPhong.addItem("-- Chọn loại phòng --");
        pnForm.add(cmbLoaiPhong, gbc);

        // Số tầng
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        JLabel lblSoTang = new JLabel("Số tầng:");
        lblSoTang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblSoTang, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtSoTang = createStyledTextField();
        pnForm.add(txtSoTang, gbc);

        // Mã chi nhánh
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        JLabel lblMaChiNhanh = new JLabel("Mã chi nhánh:");
        lblMaChiNhanh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblMaChiNhanh, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMaChiNhanh = createStyledTextField();
        pnForm.add(txtMaChiNhanh, gbc);

        // Trạng thái
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnForm.add(lblTrangThai, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbTrangThai = new JComboBox<>(new String[]{"Trống", "Đã đặt trước", "Có người ở", "Bảo trì"});
        cmbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTrangThai.setBackground(Color.WHITE);
        cmbTrangThai.setPreferredSize(new Dimension(200, 35));
        pnForm.add(cmbTrangThai, gbc);

        // Clear button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        btnLamMoi = taoStyledButton("Làm mới", new Color(149, 165, 166));
        pnForm.add(btnLamMoi, gbc);

        return pnForm;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setPreferredSize(new Dimension(200, 35));
        return textField;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã phòng", "Số phòng", "Loại phòng", "Tầng", "Chi nhánh", "Trạng thái"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPhong = new JTable(model);
        tblPhong.setRowHeight(40);
        tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhong.setSelectionBackground(new Color(241, 196, 15, 100));
        tblPhong.setSelectionForeground(Color.BLACK);
        tblPhong.setShowGrid(true);
        tblPhong.setGridColor(new Color(189, 195, 199));
        tblPhong.setRowSelectionAllowed(true);
        tblPhong.setColumnSelectionAllowed(false);
        tblPhong.setFillsViewportHeight(true);
        tblPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tblPhong.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setOpaque(true);
                label.setBackground(mauChinh);
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, mauNhan));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        caiDatTableColumns();

        JScrollPane scrollPane = new JScrollPane(tblPhong);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {100, 120, 150, 80, 120, 120};
        TableColumnModel columnModel = tblPhong.getColumnModel();

        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidths[i]);

            column.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                    return c;
                }
            });
        }
    }

    private JPanel taoButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(mauNen);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnThem = taoStyledButton("Thêm", new Color(46, 204, 113));
        btnSua = taoStyledButton("Sửa", new Color(52, 152, 219));
        btnXoa = taoStyledButton("Xóa", new Color(231, 76, 60));
        btnTimKiem = taoStyledButton("Tìm Kiếm", new Color(149, 165, 166));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnTimKiem);

        return buttonPanel;
    }

    private JButton taoStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(toiMau(bgColor, 0.1f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private Color toiMau(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }

    public void clearForm() {
        txtMaPhong.setText("");
        txtSoPhong.setText("");
        cmbLoaiPhong.setSelectedIndex(0);
        txtSoTang.setText("");
        txtMaChiNhanh.setText("");
        cmbTrangThai.setSelectedIndex(0);
        tblPhong.clearSelection();
    }

    // ===========================================
    // METHODS REQUIRED BY CONTROLLER (MVC)
    // ===========================================

    // Getter methods for controller access
    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnSua() {
        return btnSua;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }

    public JButton getBtnLamMoi() {
        return btnLamMoi;
    }

    public JComboBox<String> getCmbLoc() {
        return cmbLoc;
    }

    public JTable getTblPhong() {
        return tblPhong;
    }

    public String getSearchKeyword() {
        return txtTimKiem.getText().trim();
    }

    public String getSelectedFilter() {
        return cmbLoc.getSelectedItem().toString();
    }

    public String getSelectedPhongId() {
        int selectedRow = tblPhong.getSelectedRow();
        if (selectedRow >= 0) {
            return model.getValueAt(selectedRow, 0).toString();
        }
        return null;
    }

    // Method to get Phong object from form data
    public Phong getPhongFromForm() {
        Phong phong = new Phong();
        
        // Don't set maPhong for new records (auto-generated)
        if (!txtMaPhong.getText().trim().isEmpty()) {
            phong.setMaPhong(txtMaPhong.getText().trim());
        }
        
        phong.setSoPhong(txtSoPhong.getText().trim());
        
        // Extract maLoaiPhong from combo box selection
        String selectedLoaiPhong = cmbLoaiPhong.getSelectedItem().toString();
        if (!selectedLoaiPhong.equals("-- Chọn loại phòng --")) {
            String maLoaiPhong = selectedLoaiPhong.split(" - ")[0];
            phong.setMaLoaiPhong(maLoaiPhong);
        }
        
        // Parse số tầng
        try {
            if (!txtSoTang.getText().trim().isEmpty()) {
                phong.setSoTang(Integer.parseInt(txtSoTang.getText().trim()));
            }
        } catch (NumberFormatException e) {
            phong.setSoTang(1); // Default value
        }
        
        phong.setMaChiNhanh(txtMaChiNhanh.getText().trim());
        phong.setTrangThai(cmbTrangThai.getSelectedItem().toString());
        
        return phong;
    }

    // Form validation method
    public boolean validateForm() {
        if (txtSoPhong.getText().trim().isEmpty()) {
            showError("Vui lòng nhập số phòng!");
            txtSoPhong.requestFocus();
            return false;
        }
        
        if (cmbLoaiPhong.getSelectedIndex() == 0) {
            showError("Vui lòng chọn loại phòng!");
            cmbLoaiPhong.requestFocus();
            return false;
        }
        
        if (txtSoTang.getText().trim().isEmpty()) {
            showError("Vui lòng nhập số tầng!");
            txtSoTang.requestFocus();
            return false;
        }
        
        try {
            Integer.parseInt(txtSoTang.getText().trim());
        } catch (NumberFormatException e) {
            showError("Số tầng phải là số nguyên!");
            txtSoTang.requestFocus();
            return false;
        }
        
        if (txtMaChiNhanh.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã chi nhánh!");
            txtMaChiNhanh.requestFocus();
            return false;
        }
        
        return true;
    }

    // Method to set LoaiPhong data to combo box
    public void setLoaiPhongData(List<LoaiPhong> loaiPhongList) {
        cmbLoaiPhong.removeAllItems();
        cmbLoaiPhong.addItem("-- Chọn loại phòng --");
        
        for (LoaiPhong lp : loaiPhongList) {
            cmbLoaiPhong.addItem(lp.getMaLoaiPhong() + " - " + lp.getTenLoaiPhong());
        }
    }

    // Method to set table data
    public void setTableData(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    // Method to set form data for editing
    public void setFormData(Phong phong) {
        if (phong != null) {
            txtMaPhong.setText(phong.getMaPhong());
            txtSoPhong.setText(phong.getSoPhong());
            txtSoTang.setText(String.valueOf(phong.getSoTang()));
            txtMaChiNhanh.setText(phong.getMaChiNhanh());
            cmbTrangThai.setSelectedItem(phong.getTrangThai());
            
            // Set loại phòng combo box
            for (int i = 0; i < cmbLoaiPhong.getItemCount(); i++) {
                String item = cmbLoaiPhong.getItemAt(i);
                if (item.startsWith(phong.getMaLoaiPhong())) {
                    cmbLoaiPhong.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // Message display methods
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmAction(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Xác nhận", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    // Legacy method to maintain compatibility (if needed)
    public void showMessage(String message) {
        showInfo(message);
    }

    // Method to get DefaultTableModel for controller access
    public DefaultTableModel getTableModel() {
        return model;
    }

    // Method to refresh loại phòng combo box (if needed independently)
    public void refreshLoaiPhongComboBox() {
        // This method should be called by controller after it loads new data
        // The controller will call setLoaiPhongData() instead
    }

    // Legacy method for backward compatibility
    public void updateTableData(Object[][] data) {
        setTableData(data);
    }
}