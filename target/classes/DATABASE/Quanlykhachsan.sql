--Kiểm tra xem database đã tồn tại hay chưa, tồn tại thì xóa
IF EXISTS (SELECT * FROM sys.databases WHERE name = N'quanlykhachsan')
BEGIN
    -- Đóng tất cả các kết nối đến cơ sở dữ liệu
    EXECUTE sp_MSforeachdb 'IF ''?'' = ''quanlykhachsan'' 
    BEGIN
        DECLARE @sql AS NVARCHAR(MAX) = ''USE [?]; ALTER DATABASE [?] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;''
        EXEC (@sql)
    END'
    -- Xóa tất cả các kết nối tới cơ sở dữ liệu (thực hiện qua hệ thống master)
    USE master;
    -- Xóa cơ sở dữ liệu nếu tồn tại
    DROP DATABASE quanlykhachsan;
END

go
--tạo database tên "QLBH" - Quản lý bán hàng
create database quanlykhachsan;

go
--Sử dụng database "QLBH" -- Quản lý bán hàng
USE quanlykhachsan;

GO
-- Bảng Role (Phân quyền)
CREATE TABLE VaiTro
(
    maVaiTro CHAR(5) PRIMARY KEY,
    tenVaiTro NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(255)
);

GO
-- Bảng User (Người dùng)
CREATE TABLE NguoiDung
(
    maNguoiDung CHAR(5) PRIMARY KEY,
    tenNguoiDung NVARCHAR(100) NOT NULL,
	ngaySinh DATETIME null,
    SDT VARCHAR(10) UNIQUE CHECK (SDT LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    email NVARCHAR(100) UNIQUE CHECK (email LIKE '%@gmail.com' or email LIKE '%@sv.ute.udn.vn'),
    matKhau NVARCHAR(255) NOT NULL,
	soDuTaiKhoan decimal(20,2) default 0,
    maVaiTro CHAR(5),
    trangThai BIT NOT NULL DEFAULT 1  -- 1: Hoạt động, 0: Khóa
    FOREIGN KEY (maVaiTro) REFERENCES VaiTro(maVaiTro),
);

Go
-- Bảng Cơ sở khách sạn
CREATE TABLE ChiNhanhKhachSan
(
    maChiNhanh CHAR(5) PRIMARY KEY,
    tenChiNhanh NVARCHAR(100) NOT NULL,
    diaChi NVARCHAR(255) NULL,
    SDT VARCHAR(10) UNIQUE CHECK (SDT LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')
);

Go
-- Bảng Loại phòng
CREATE TABLE LoaiPhong
(
    maLoaiPhong CHAR(5) PRIMARY KEY,
    tenLoaiPhong NVARCHAR(50) NOT NULL,
	soLuongToiDa int,
    moTa NVARCHAR(255),
    giaPhong DECIMAL(15,2)
);

Go
-- Bảng Phòng
CREATE TABLE Phong
(
    maPhong CHAR(5) PRIMARY KEY,
    soPhong VARCHAR(10) NOT NULL,
    maLoaiPhong CHAR(5),
    soTang INT NOT NULL,
    maChiNhanh CHAR(5),
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Trống', N'Có người ở', N'Đã đặt trước', N'Bảo trì')) NOT NULL,
    FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong),
    FOREIGN KEY (maChiNhanh) REFERENCES ChiNhanhKhachSan(maChiNhanh)
);

Go
--Bảng Loại Dịch Vụ
CREATE TABLE LoaiDichVu
(
	maLoaiDichVu char(5) PRIMARY KEY,
	tenLoaiDichVu NVARCHAR(255) not null,
	giaDichVu decimal(15,2),
	moTa NVARCHAR(255)
);

Go
--Bảng dịch vụ
CREATE TABLE DichVu
(
	maDichVu char(5) PRIMARY KEY,
	tenDichVu NVARCHAR(255) not null,
	maLoaiDichVu char(5),
	FOREIGN KEY (maLoaiDichVu) REFERENCES LoaiDichVu(maLoaiDichVu)
);

Go
-- Bảng Đặt phòng
CREATE TABLE DatPhong
(
    maDatPhong CHAR(5) PRIMARY KEY,
    maNguoiDung CHAR(5),
    maPhong CHAR(5),
	soNguoi int,
    dichVuSuDung CHAR(5),
    ngayThuePhong DATETIME NOT NULL,
    ngayTraPhong DATETIME NOT NULL,
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Đã đặt', N'Hoàn thành', N'Hủy')) NOT NULL,
	--check (check_in_date >= getdate()),
	--check (check_out_date <= getdate()),
	-- check (ngayThuePhong < ngayTraPhong),
    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong),
    FOREIGN KEY (dichVuSuDung) REFERENCES DichVu(maDichVu)
);

Go
-- Bảng Hóa đơn
CREATE TABLE HoaDon
(
    maHoaDon CHAR(5) PRIMARY KEY,
    maDatPhong CHAR(5),
    maDichVu CHAR(5),
	nhanVienPhuTrach CHAR(5) null,
    tongTien DECIMAL(15,2) NOT NULL,
    ngayGiaoDich DATETIME NOT NULL,
    phuongThucThanhToan NVARCHAR(20) CHECK (phuongThucThanhToan IN (N'Ví điện tử', N'Tiền mặt', N'Thẻ')) NOT NULL,
    FOREIGN KEY (maDatPhong) REFERENCES DatPhong(maDatPhong),
	--FOREIGN KEY (nhanVienPhuTrach) REFERENCES NguoiDung(maNguoiDung),
);
go
INSERT INTO VaiTro (maVaiTro, tenVaiTro, moTa)
VALUES 
    ('VT001', N'Quản trị viên', N'Tài khoản quản trị hệ thống'),
    ('VT002', N'Nhân viên', N'Nhân viên khách sạn'),
    ('VT003', N'Khách hàng', N'Tài khoản khách hàng sử dụng dịch vụ');
go
INSERT INTO NguoiDung (maNguoiDung, tenNguoiDung, ngaySinh, SDT, email, matKhau, soDuTaiKhoan, maVaiTro)
VALUES 
    ('ND001', N'Nguyễn Văn A', '1990-05-12', '0912345678', '23115053122114@sv.ute.udn.vn', '123456', 0, 'VT001'),
    ('ND002', N'Lê Thị B', '1995-07-20', '0987654321', '23115053122111@sv.ute.udn.vn', '123456', 0, 'VT002'),
    ('ND003', N'Trần Văn C', '1988-03-10', '0901122334', '23115053122123@sv.ute.udn.vn', '123456', 0, 'VT003'),
    ('ND004', N'Phạm Thị D', '2000-08-08', '0933445566', 'phamthid@sv.ute.udn.vn', 'pass123', 0, 'VT003'),
    ('ND005', N'Hồ Minh E', '1992-11-11', '0977556677', '23115053122120@sv.ute.udn.vn', 'qwerty', 0, 'VT002');

go
INSERT INTO ChiNhanhKhachSan (maChiNhanh, tenChiNhanh, diaChi, SDT)
VALUES 
('CN001', N'Khách sạn Sông Hàn', N'123 Lê Duẩn, Đà Nẵng', '0236378989'),
('CN002', N'Khách sạn Biển Xanh', N'45 Trần Phú, Nha Trang', '0258377888'),
('CN003', N'Khách sạn Ánh Dương', N'89 Nguyễn Văn Linh, Hà Nội', '0246677889'),
('CN004', N'Khách sạn Mặt Trời', N'12 Trường Chinh, TP. HCM', '0283344556'),
('CN005', N'Khách sạn Vườn Xoài', N'67 Phạm Văn Đồng, Cần Thơ', '0292388999'),
('CN006', N'Khách sạn Sao Mai', N'234 Lý Thường Kiệt, Huế', '0234388123'),
('CN007', N'Khách sạn Bạch Dương', N'78 Nguyễn Huệ, Đà Lạt', '0263388123'),
('CN008', N'Khách sạn Đại Dương', N'09 Lý Tự Trọng, Hải Phòng', '0225388123'),
('CN009', N'Khách sạn Hương Tràm', N'123 Nguyễn Trãi, Vũng Tàu', '0254388123'),
('CN010', N'Khách sạn Trúc Xanh', N'345 CMT8, Bình Dương', '0274388123');
GO
INSERT INTO LoaiPhong (maLoaiPhong, tenLoaiPhong, soLuongToiDa, moTa, giaPhong)
VALUES 
    ('LP001', N'Phòng đơn', 1, N'Phòng dành cho 1 người', 300000),
    ('LP002', N'Phòng đôi', 2, N'Phòng dành cho 2 người', 500000),
    ('LP003', N'Phòng gia đình', 4, N'Phòng rộng cho gia đình', 800000),
    ('LP004', N'Suite', 2, N'Phòng cao cấp', 1500000);
GO
INSERT INTO Phong (maPhong, soPhong, maLoaiPhong, soTang, maChiNhanh, trangThai)
VALUES 
    ('P001', '101', 'LP001', 1, 'CN001', N'Trống'),
    ('P002', '102', 'LP002', 1, 'CN001', N'Có người ở'),
    ('P003', '201', 'LP003', 2, 'CN002', N'Đã đặt trước'),
    ('P004', '202', 'LP004', 2, 'CN002', N'Trống'),
    ('P005', '301', 'LP002', 3, 'CN003', N'Bảo trì');
GO
INSERT INTO LoaiDichVu (maLoaiDichVu, tenLoaiDichVu, giaDichVu, moTa)
VALUES 
    ('LDV01', N'Dịch vụ ăn uống', 100000, N'Bao gồm ăn sáng/trưa/tối'),
    ('LDV02', N'Dịch vụ giặt ủi', 50000, N'Giặt ủi quần áo'),
    ('LDV03', N'Dịch vụ spa', 200000, N'Spa thư giãn'),
    ('LDV04', N'Dịch vụ xe đưa đón', 150000, N'Xe đưa đón sân bay');
go
INSERT INTO DichVu (maDichVu, tenDichVu, maLoaiDichVu)
VALUES 
('DV001', N'Bữa sáng buffet', 'LDV01'),
('DV002', N'Giặt nhanh 24h', 'LDV02'),
('DV003', N'Massage thư giãn', 'LDV03'),
('DV004', N'Đưa đón sân bay', 'LDV04');
go
INSERT INTO DatPhong (maDatPhong, maNguoiDung, maPhong, soNguoi, dichVuSuDung, ngayThuePhong, ngayTraPhong, trangThai)
VALUES 
('DP001', 'ND002', 'P001', 1, 'DV001', '2025-06-01', '2025-06-03', N'Đã đặt'),
('DP002', 'ND003', 'P003', 2, 'DV002', '2025-06-05', '2025-06-07', N'Đã đặt'),
('DP003', 'ND004', 'P002', 2, 'DV004', '2025-05-25', '2025-05-28', N'Hoàn thành');
GO
INSERT INTO HoaDon (maHoaDon, maDatPhong, maDichVu, nhanVienPhuTrach, tongTien, ngayGiaoDich, phuongThucThanhToan)
VALUES 
('HD001', 'DP001', 'DV001', 'ND002', 600000, '2025-06-03', N'Tiền mặt'),
('HD002', 'DP002', 'DV002', 'ND002', 550000, '2025-06-07', N'Thẻ'),
('HD003', 'DP003', 'DV004', 'ND002', 700000, '2025-05-28', N'Ví điện tử');

GO

Go

-------------Dich Vu-------------------
CREATE PROCEDURE sp_LayDanhSachDichVu
AS
BEGIN
    SELECT * FROM DichVu
END;
GO

--insert
CREATE PROCEDURE sp_ThemDichVu
    @maDichVu CHAR(5),
    @tenDichVu NVARCHAR(255),
    @maLoaiDichVu CHAR(5)
AS
BEGIN
    INSERT INTO DichVu VALUES (@maDichVu, @tenDichVu, @maLoaiDichVu)
END;
go

--update
CREATE PROCEDURE sp_CapNhatDichVu
    @maDichVu CHAR(5),
    @tenDichVu NVARCHAR(255),
    @maLoaiDichVu CHAR(5)
AS
BEGIN
    UPDATE DichVu
    SET tenDichVu = @tenDichVu,
        maLoaiDichVu = @maLoaiDichVu
    WHERE maDichVu = @maDichVu
END
go
--delete
CREATE PROCEDURE sp_XoaDichVu
    @maDichVu CHAR(5)
AS
BEGIN
    DELETE FROM DichVu WHERE maDichVu = @maDichVu
END
GO
-------------Loai dich vu------------------
--insert
CREATE PROCEDURE sp_ThemLoaiDichVu
    @maLoaiDichVu CHAR(5),
    @tenLoaiDichVu NVARCHAR(255),
    @giaDichVu DECIMAL(15,2),
    @moTa NVARCHAR(255)
AS
BEGIN
    INSERT INTO LoaiDichVu VALUES (@maLoaiDichVu, @tenLoaiDichVu, @giaDichVu, @moTa)
END
GO
--update
CREATE PROCEDURE sp_CapNhatLoaiDichVu
    @maLoaiDichVu CHAR(5),
    @tenLoaiDichVu NVARCHAR(255),
    @giaDichVu DECIMAL(15,2),
    @moTa NVARCHAR(255)
AS
BEGIN
    UPDATE LoaiDichVu
    SET tenLoaiDichVu = @tenLoaiDichVu,
        giaDichVu = @giaDichVu,
        moTa = @moTa
    WHERE maLoaiDichVu = @maLoaiDichVu
END
GO
--delete
CREATE PROCEDURE sp_XoaLoaiDichVu
    @maLoaiDichVu CHAR(5)
AS
BEGIN
    DELETE FROM LoaiDichVu WHERE maLoaiDichVu = @maLoaiDichVu
END
GO
--select
CREATE PROCEDURE sp_LayDanhSachLoaiDichVu
AS
BEGIN
    SELECT * FROM LoaiDichVu
END
GO

--------------Nguoi Dung-----------------
--insert
CREATE PROCEDURE sp_ThemNguoiDung
    @maNguoiDung CHAR(5),
    @tenNguoiDung NVARCHAR(100),
    @ngaySinh VARCHAR(15),
    @SDT VARCHAR(10),
    @email NVARCHAR(100),
    @matKhau NVARCHAR(255),
    @soDuTaiKhoan DECIMAL(20,2),
    @maVaiTro CHAR(5)
AS
BEGIN
    INSERT INTO NguoiDung VALUES (@maNguoiDung, @tenNguoiDung, @ngaySinh, @SDT, @email, @matKhau, @soDuTaiKhoan, @maVaiTro)
END
GO
--update
CREATE PROCEDURE sp_CapNhatNguoiDung
    @maNguoiDung CHAR(5),
    @tenNguoiDung NVARCHAR(100),
    @ngaySinh VARCHAR(15),
    @SDT VARCHAR(10),
    @email NVARCHAR(100),
    @matKhau NVARCHAR(255),
    @soDuTaiKhoan DECIMAL(20,2),
    @maVaiTro CHAR(5)
AS
BEGIN
    UPDATE NguoiDung
    SET tenNguoiDung = @tenNguoiDung,
        ngaySinh = @ngaySinh,
        SDT = @SDT,
        email = @email,
        matKhau = @matKhau,
        soDuTaiKhoan = @soDuTaiKhoan,
        maVaiTro = @maVaiTro
    WHERE maNguoiDung = @maNguoiDung
END
GO
--delete    
CREATE PROCEDURE sp_XoaNguoiDung
    @maNguoiDung CHAR(5)
AS
BEGIN
    DELETE FROM NguoiDung WHERE maNguoiDung = @maNguoiDung
END
GO
--select
CREATE PROCEDURE sp_LayDanhSachNguoiDung
AS
BEGIN
    SELECT * FROM NguoiDung
END
GO

----------------Vai Tro------------------
--insert
CREATE PROCEDURE sp_ThemVaiTro
    @maVaiTro CHAR(5),
    @tenVaiTro NVARCHAR(50),
    @moTa NVARCHAR(255)
AS
BEGIN
    INSERT INTO VaiTro 
    VALUES (@maVaiTro, @tenVaiTro, @moTa)
END
GO
--update
CREATE PROCEDURE sp_CapNhatVaiTro
    @maVaiTro CHAR(5),
    @tenVaiTro NVARCHAR(50),
    @moTa NVARCHAR(255)
AS
BEGIN
    UPDATE VaiTro
    SET tenVaiTro = @tenVaiTro,
        moTa = @moTa
    WHERE maVaiTro = @maVaiTro
END
GO  
--delete
CREATE PROCEDURE sp_XoaVaiTro
    @maVaiTro CHAR(5)
AS
BEGIN
    DELETE FROM VaiTro WHERE maVaiTro = @maVaiTro
END
GO
--select
CREATE PROCEDURE sp_LayDanhSachVaiTro
AS
BEGIN
    SELECT * FROM VaiTro
END
GO

---------------------------------------------------------Chi Nhanh Khach San ---------------------------
--insert
CREATE PROCEDURE sp_ThemChiNhanhKhachSan
    @maChiNhanh CHAR(5),
    @tenChiNhanh NVARCHAR(100),
    @diaChi NVARCHAR(255),
    @SDT VARCHAR(10)
AS
BEGIN
    INSERT INTO ChiNhanhKhachSan VALUES (@maChiNhanh, @tenChiNhanh, @diaChi, @SDT)
END
GO

--update
CREATE PROCEDURE sp_CapNhatChiNhanhKhachSan
    @maChiNhanh CHAR(5),
    @tenChiNhanh NVARCHAR(100),
    @diaChi NVARCHAR(255),
    @SDT VARCHAR(10)
AS
BEGIN
    UPDATE ChiNhanhKhachSan
    SET tenChiNhanh = @tenChiNhanh,
        diaChi = @diaChi,
        SDT = @SDT
    WHERE maChiNhanh = @maChiNhanh
END
GO

--delete
CREATE PROCEDURE sp_XoaChiNhanhKhachSan
    @maChiNhanh CHAR(5)
AS
BEGIN
    DELETE FROM ChiNhanhKhachSan WHERE maChiNhanh = @maChiNhanh
END
GO

--select
CREATE PROCEDURE sp_LayDanhSachChiNhanh
AS
BEGIN
    SELECT * FROM ChiNhanhKhachSan
END
GO
-----------------------------------------------------------Loai Phong---------------------------

--insert
CREATE PROCEDURE sp_ThemLoaiPhong
    @maLoaiPhong CHAR(5),
    @tenLoaiPhong NVARCHAR(50),
    @soLuongToiDa INT,
    @moTa NVARCHAR(255),
    @giaPhong DECIMAL(15,2)
AS
BEGIN
    INSERT INTO LoaiPhong VALUES (@maLoaiPhong, @tenLoaiPhong, @soLuongToiDa, @moTa, @giaPhong)
END
GO

--update
CREATE PROCEDURE sp_CapNhatLoaiPhong
    @maLoaiPhong CHAR(5),
    @tenLoaiPhong NVARCHAR(50),
    @soLuongToiDa INT,
    @moTa NVARCHAR(255),
    @giaPhong DECIMAL(15,2)
AS
BEGIN
    UPDATE LoaiPhong
    SET tenLoaiPhong = @tenLoaiPhong,
        soLuongToiDa = @soLuongToiDa,
        moTa = @moTa,
        giaPhong = @giaPhong
    WHERE maLoaiPhong = @maLoaiPhong
END
GO

--delete
CREATE PROCEDURE sp_XoaLoaiPhong
    @maLoaiPhong CHAR(5)
AS
BEGIN
    DELETE FROM LoaiPhong WHERE maLoaiPhong = @maLoaiPhong
END
GO

--select
CREATE PROCEDURE sp_LayDanhSachLoaiPhong
AS
BEGIN
    SELECT * FROM LoaiPhong
END
GO

--------------------------------------------------------------Phong------------------------------
--insert
CREATE PROCEDURE sp_ThemPhong
    @maPhong CHAR(5),
    @soPhong VARCHAR(10),
    @maLoaiPhong CHAR(5),
    @soTang INT,
    @maChiNhanh CHAR(5),
    @trangThai NVARCHAR(20)
AS
BEGIN
    INSERT INTO Phong VALUES (@maPhong, @soPhong, @maLoaiPhong, @soTang, @maChiNhanh, @trangThai)
END
GO

--update
CREATE PROCEDURE sp_CapNhatPhong
    @maPhong CHAR(5),
    @soPhong VARCHAR(10),
    @maLoaiPhong CHAR(5),
    @soTang INT,
    @maChiNhanh CHAR(5),
    @trangThai NVARCHAR(20)
AS
BEGIN
    UPDATE Phong
    SET soPhong = @soPhong,
        maLoaiPhong = @maLoaiPhong,
        soTang = @soTang,
        maChiNhanh = @maChiNhanh,
        trangThai = @trangThai
    WHERE maPhong = @maPhong
END
GO

--delete
CREATE PROCEDURE sp_XoaPhong
    @maPhong CHAR(5)
AS
BEGIN
    DELETE FROM Phong WHERE maPhong = @maPhong
END
GO

--select
CREATE PROCEDURE sp_LayDanhSachPhong
AS
BEGIN
    SELECT * FROM Phong
END
GO

-----------------------------------------------------Dat Phong---------------------------
--insert
CREATE PROCEDURE sp_ThemDatPhong
    @maDatPhong CHAR(5),
    @maNguoiDung CHAR(5),
    @maPhong CHAR(5),
    @soNguoi INT,
    @dichVuSuDung CHAR(5),
    @ngayThuePhong VARCHAR(15),
    @ngayTraPhong VARCHAR(15),
    @trangThai NVARCHAR(20)
AS
BEGIN
    INSERT INTO DatPhong 
    VALUES (@maDatPhong, @maNguoiDung, @maPhong, @soNguoi, @dichVuSuDung, @ngayThuePhong, @ngayTraPhong, @trangThai)
END
GO

--Update
CREATE PROCEDURE sp_CapNhatDatPhong
    @maDatPhong CHAR(5),
    @maNguoiDung CHAR(5),
    @maPhong CHAR(5),
    @soNguoi INT,
    @dichVuSuDung CHAR(5),
    @ngayThuePhong VARCHAR(15),
    @ngayTraPhong VARCHAR(15),
    @trangThai NVARCHAR(20)
AS
BEGIN
    UPDATE DatPhong
    SET maNguoiDung = @maNguoiDung,
        maPhong = @maPhong,
        soNguoi = @soNguoi,
        dichVuSuDung = @dichVuSuDung,
        ngayThuePhong = @ngayThuePhong,
        ngayTraPhong = @ngayTraPhong,
        trangThai = @trangThai
    WHERE maDatPhong = @maDatPhong
END
GO

--Delete
CREATE PROCEDURE sp_XoaDatPhong
    @maDatPhong CHAR(5)
AS
BEGIN
    DELETE FROM DatPhong WHERE maDatPhong = @maDatPhong
END
GO

--Lấy danh sách
CREATE PROCEDURE sp_LayDanhSachDatPhong
AS
BEGIN
    SELECT * FROM DatPhong
END
GO

--------------------------------------------------------Hóa Đơn------------------------------------
--insert
CREATE PROCEDURE sp_ThemHoaDon
    @maHoaDon CHAR(5),
    @maDatPhong CHAR(5),
    @maDichVu CHAR(5),
    @nhanVienPhuTrach CHAR(5),
    @tongTien DECIMAL(15,2),
    @phuongThucThanhToan NVARCHAR(20)
AS
BEGIN
    INSERT INTO HoaDon(maHoaDon, maDatPhong, maDichVu, nhanVienPhuTrach, tongTien, phuongThucThanhToan)
    VALUES (@maHoaDon, @maDatPhong, @maDichVu, @nhanVienPhuTrach, @tongTien, @phuongThucThanhToan)
END
GO

--Update
CREATE PROCEDURE sp_CapNhatHoaDon
    @maHoaDon CHAR(5),
    @maDatPhong CHAR(5),
    @maDichVu CHAR(5),
    @nhanVienPhuTrach CHAR(5),
    @tongTien DECIMAL(15,2),
    @phuongThucThanhToan NVARCHAR(20)
AS
BEGIN
    UPDATE HoaDon
    SET maDatPhong = @maDatPhong,
        maDichVu = @maDichVu,
        nhanVienPhuTrach = @nhanVienPhuTrach,
        tongTien = @tongTien,
        phuongThucThanhToan = @phuongThucThanhToan
    WHERE maHoaDon = @maHoaDon
END
GO

--Delete
CREATE PROCEDURE sp_XoaHoaDon
    @maHoaDon CHAR(5)
AS
BEGIN
    DELETE FROM HoaDon WHERE maHoaDon = @maHoaDon
END
GO

--Lấy danh sách
CREATE PROCEDURE sp_LayDanhSachHoaDon
AS
BEGIN
    SELECT * FROM HoaDon
END
GO

-------Trigger-------Trigger-------Trigger-------Trigger-------Trigger-------Trigger-------

