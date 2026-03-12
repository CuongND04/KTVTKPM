-- =========================================================
-- MODULE NHAP LINH KIEN (Command Pattern)
-- Khong luu total_amount trong bang import_invoice/import_detail
-- =========================================================

DROP DATABASE IF EXISTS nhap_linh_kien;
CREATE DATABASE nhap_linh_kien
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE nhap_linh_kien;

-- =========================================================
-- TABLE: staff
-- =========================================================
CREATE TABLE staff (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(120) UNIQUE,
  role VARCHAR(30) NOT NULL
);

-- =========================================================
-- TABLE: supplier
-- =========================================================
CREATE TABLE supplier (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  address VARCHAR(255),
  phone VARCHAR(30),
  email VARCHAR(120)
);

-- =========================================================
-- TABLE: part
-- =========================================================
CREATE TABLE part (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description VARCHAR(255),
  quantity INT NOT NULL DEFAULT 0,
  buy_price DECIMAL(15,2) NOT NULL DEFAULT 0,
  sell_price DECIMAL(15,2) NOT NULL DEFAULT 0
);

-- =========================================================
-- TABLE: import_invoice
-- total_amount la thuoc tinh dan xuat => KHONG luu
-- =========================================================
CREATE TABLE import_invoice (
  id INT AUTO_INCREMENT PRIMARY KEY,
  create_date DATE NOT NULL,
  supplier_id INT NOT NULL,
  create_staff_id INT NOT NULL,
  CONSTRAINT fk_invoice_supplier
    FOREIGN KEY (supplier_id) REFERENCES supplier(id),
  CONSTRAINT fk_invoice_staff
    FOREIGN KEY (create_staff_id) REFERENCES staff(id)
);

-- =========================================================
-- TABLE: import_detail
-- total_amount la thuoc tinh dan xuat => KHONG luu
-- =========================================================
CREATE TABLE import_detail (
  id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_id INT NOT NULL,
  part_id INT NOT NULL,
  quantity INT NOT NULL CHECK (quantity > 0),
  price DECIMAL(15,2) NOT NULL CHECK (price >= 0),
  CONSTRAINT fk_detail_invoice
    FOREIGN KEY (invoice_id) REFERENCES import_invoice(id) ON DELETE CASCADE,
  CONSTRAINT fk_detail_part
    FOREIGN KEY (part_id) REFERENCES part(id)
);

-- =========================================================
-- INDEX
-- =========================================================
CREATE INDEX idx_supplier_name ON supplier(name);
CREATE INDEX idx_part_name ON part(name);
CREATE INDEX idx_invoice_date ON import_invoice(create_date);
CREATE INDEX idx_detail_invoice ON import_detail(invoice_id);
CREATE INDEX idx_detail_part ON import_detail(part_id);

-- =========================================================
-- VIEW DAN XUAT TONG TIEN PHIEU NHAP
-- =========================================================
CREATE OR REPLACE VIEW v_import_invoice_total AS
SELECT
  i.id AS invoice_id,
  i.create_date,
  i.supplier_id,
  i.create_staff_id,
  COALESCE(SUM(d.quantity * d.price), 0) AS total_amount
FROM import_invoice i
LEFT JOIN import_detail d ON d.invoice_id = i.id
GROUP BY i.id, i.create_date, i.supplier_id, i.create_staff_id;

-- =========================================================
-- SEED DATA
-- =========================================================
INSERT INTO staff (id, name, username, password, email, role) VALUES
(1, 'Nhan vien kho Gara', 'kho_gara', '123', 'kho@garaoto.vn', 'KHO');

INSERT INTO supplier (id, name, address, phone, email) VALUES
(1, 'Cong ty Phu tung Oto Sai Gon', 'Quan 12, TP HCM', '0903000001', 'sales@ptotosaigon.vn'),
(2, 'Nha phan phoi Gara Viet', 'Thanh Khe, Da Nang', '0903000002', 'contact@garaviet.vn'),
(3, 'Auto Parts Ha Noi', 'Cau Giay, Ha Noi', '0903000003', 'info@autopartshn.vn'),
(4, 'Phu tung Dong Co 68', 'Ninh Kieu, Can Tho', '0903000004', 'dongco68@gmail.com');

INSERT INTO part (id, name, description, quantity, buy_price, sell_price) VALUES
(1, 'Loc dau dong co', 'Loc dau cho dong co xang 1.5-2.0', 35, 85000, 130000),
(2, 'Bo thang truoc', 'Bo ma phanh truoc cho sedan hang B', 20, 420000, 620000),
(3, 'Bugi Iridium', 'Bugi danh lua tuoi tho cao', 60, 140000, 220000),
(4, 'Ac quy 12V-60Ah', 'Ac quy kho xe du lich', 12, 1450000, 1800000),
(5, 'Loc gio dieu hoa', 'Loc gio cabin than hoat tinh', 40, 110000, 170000);

ALTER TABLE staff AUTO_INCREMENT = 2;
ALTER TABLE supplier AUTO_INCREMENT = 5;
ALTER TABLE part AUTO_INCREMENT = 6;

-- =========================================================
-- TEST NHANH (optional)
-- =========================================================
-- SELECT * FROM v_import_invoice_total ORDER BY invoice_id DESC;