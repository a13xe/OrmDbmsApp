-- DELETE FROM gpu;
-- DELETE FROM mbrd;
-- DELETE FROM cpu;
-- DELETE FROM socket_to_chipset;
-- DELETE FROM chipset;
-- DELETE FROM socket;
-- DELETE FROM brand;

INSERT INTO brand (id, name)
VALUES
  (1, 'Intel'),
  (2, 'AMD'),
  (3, 'NVIDIA'),
  (4, 'AMD Radeon');
  
INSERT INTO chipset (id, name)
VALUES
  (1, 'B360'),
  (2, 'X470'),
  (3, 'Z390'),
  (4, 'B450'),
  (5, 'RTX 2080 Ti'),
  (6, 'GTX 1080 Ti');
  
INSERT INTO socket (id, name)
VALUES
  (1, 'LGA 1151'),
  (2, 'AM4'),
  (3, 'TR4');
  
INSERT INTO socket_to_chipset (id, socket_id, chipset_id)
VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 1, 3),
  (4, 2, 4),
  (5, 3, 5),
  (6, 3, 6);
  
INSERT INTO cpu (id, model, price, cores, threads, frequency, brand_id, socket_id)
VALUES
  (1, 'Core i5-8400', 199.99, 6, 6, 2800, 1, 1),
  (2, 'Ryzen 5 3600', 199.99, 6, 12, 3600, 2, 2),
  (3, 'Core i7-9700K', 399.99, 8, 8, 3600, 1, 3),
  (4, 'Ryzen 7 3700X', 329.99, 8, 16, 3600, 2, 2),
  (5, 'Core i9-9900K', 499.99, 8, 16, 3600, 1, 3);
  
INSERT INTO gpu (id, model, price, cores, memory, frequency, brand_id)
VALUES
  (1, 'GeForce RTX 3080', 699.99, 8704, 10, 1440, 3),
  (2, 'GeForce RTX 3070', 499.99, 5888, 8, 1730, 3),
  (3, 'Radeon RX 6800 XT', 649.99, 4608, 16, 2250, 4),
  (4, 'Radeon RX 5700 XT', 399.99, 2560, 8, 1910, 4);
  
INSERT INTO mbrd (id, model, price, brand_id, socket_id, chipset_id)
VALUES
  (1, 'ASUS ROG Strix B360-F', 129.99, 1, 1, 1),
  (2, 'MSI X470 Gaming Pro Carbon', 179.99, 2, 2, 2),
  (3, 'Gigabyte Z390 AORUS Ultra', 249.99, 1, 1, 3),
  (4, 'ASRock B450M Steel Legend', 99.99, 3, 1, 4);