--------------------------------------------------------------------------------------------------------------------------------------------------------
--                                            __                            __              __                __                                      --
--                                           /  |                          /  |            /  |              /  |                                     --
--   _______   ______    ______    ______   _$$ |_     ______          ____$$ |  ______   _$$ |_     ______  $$ |____    ______    _______   ______   --
--  /       | /      \  /      \  /      \ / $$   |   /      \        /    $$ | /      \ / $$   |   /      \ $$      \  /      \  /       | /      \  --
-- /$$$$$$$/ /$$$$$$  |/$$$$$$  | $$$$$$  |$$$$$$/   /$$$$$$  |      /$$$$$$$ | $$$$$$  |$$$$$$/    $$$$$$  |$$$$$$$  | $$$$$$  |/$$$$$$$/ /$$$$$$  | --
-- $$ |      $$ |  $$/ $$    $$ | /    $$ |  $$ | __ $$    $$ |      $$ |  $$ | /    $$ |  $$ | __  /    $$ |$$ |  $$ | /    $$ |$$      \ $$    $$ | --
-- $$ \_____ $$ |      $$$$$$$$/ /$$$$$$$ |  $$ |/  |$$$$$$$$/       $$ \__$$ |/$$$$$$$ |  $$ |/  |/$$$$$$$ |$$ |__$$ |/$$$$$$$ | $$$$$$  |$$$$$$$$/  --
-- $$       |$$ |      $$       |$$    $$ |  $$  $$/ $$       |      $$    $$ |$$    $$ |  $$  $$/ $$    $$ |$$    $$/ $$    $$ |/     $$/ $$       | --
--  $$$$$$$/ $$/        $$$$$$$/  $$$$$$$/    $$$$/   $$$$$$$/        $$$$$$$/  $$$$$$$/    $$$$/   $$$$$$$/ $$$$$$$/   $$$$$$$/ $$$$$$$/   $$$$$$$/  --
--                                                                                                                                                    --
--------------------------------------------------------------------------------------------------------------------------------------------------------


-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pc
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `pc` ;

-- -----------------------------------------------------
-- Schema pc
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pc` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `pc` ;

-- -----------------------------------------------------
-- Table `pc`.`brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`brand` ;

CREATE TABLE IF NOT EXISTS `pc`.`brand` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`chipset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`chipset` ;

CREATE TABLE IF NOT EXISTS `pc`.`chipset` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`socket`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`socket` ;

CREATE TABLE IF NOT EXISTS `pc`.`socket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`cpu`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`cpu` ;

CREATE TABLE IF NOT EXISTS `pc`.`cpu` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `model` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `cores` INT NULL DEFAULT NULL,
  `threads` INT NULL DEFAULT NULL,
  `frequency` INT NULL DEFAULT NULL,
  `brand_id` INT NOT NULL,
  `socket_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cpu_brand1_idx` (`brand_id` ASC) VISIBLE,
  INDEX `fk_cpu_socket1_idx` (`socket_id` ASC) VISIBLE,
  CONSTRAINT `fk_cpu_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `pc`.`brand` (`id`),
  CONSTRAINT `fk_cpu_socket1`
    FOREIGN KEY (`socket_id`)
    REFERENCES `pc`.`socket` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`gpu`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`gpu` ;

CREATE TABLE IF NOT EXISTS `pc`.`gpu` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `model` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `cores` INT NULL DEFAULT NULL,
  `memory` INT NULL DEFAULT NULL,
  `frequency` INT NULL DEFAULT NULL,
  `brand_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_gpu_brand1_idx` (`brand_id` ASC) VISIBLE,
  CONSTRAINT `fk_gpu_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `pc`.`brand` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`mbrd`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`mbrd` ;

CREATE TABLE IF NOT EXISTS `pc`.`mbrd` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `model` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `brand_id` INT NOT NULL,
  `socket_id` INT NOT NULL,
  `chipset_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mbrd_brand1_idx` (`brand_id` ASC) VISIBLE,
  INDEX `fk_mbrd_socket1_idx` (`socket_id` ASC) VISIBLE,
  INDEX `fk_mbrd_chipset1_idx` (`chipset_id` ASC) VISIBLE,
  CONSTRAINT `fk_mbrd_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `pc`.`brand` (`id`),
  CONSTRAINT `fk_mbrd_chipset1`
    FOREIGN KEY (`chipset_id`)
    REFERENCES `pc`.`chipset` (`id`),
  CONSTRAINT `fk_mbrd_socket1`
    FOREIGN KEY (`socket_id`)
    REFERENCES `pc`.`socket` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `pc`.`socket_to_chipset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pc`.`socket_to_chipset` ;

CREATE TABLE IF NOT EXISTS `pc`.`socket_to_chipset` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `socket_id` INT NOT NULL,
  `chipset_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_socket_to_chipset_socket1_idx` (`socket_id` ASC) VISIBLE,
  INDEX `fk_socket_to_chipset_chipset1_idx` (`chipset_id` ASC) VISIBLE,
  CONSTRAINT `fk_socket_to_chipset_chipset1`
    FOREIGN KEY (`chipset_id`)
    REFERENCES `pc`.`chipset` (`id`),
  CONSTRAINT `fk_socket_to_chipset_socket1`
    FOREIGN KEY (`socket_id`)
    REFERENCES `pc`.`socket` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


--------------------------------------------------------------------------------------------------------------------------------------------------------
--  __                                            __              __                            __                  __              __                --
-- /  |                                          /  |            /  |                          /  |                /  |            /  |               --
-- $$/  _______    _______   ______    ______   _$$ |_          _$$ |_     ______    _______  _$$ |_           ____$$ |  ______   _$$ |_     ______   --
-- /  |/       \  /       | /      \  /      \ / $$   |        / $$   |   /      \  /       |/ $$   |         /    $$ | /      \ / $$   |   /      \  --
-- $$ |$$$$$$$  |/$$$$$$$/ /$$$$$$  |/$$$$$$  |$$$$$$/         $$$$$$/   /$$$$$$  |/$$$$$$$/ $$$$$$/         /$$$$$$$ | $$$$$$  |$$$$$$/    $$$$$$  | --
-- $$ |$$ |  $$ |$$      \ $$    $$ |$$ |  $$/   $$ | __         $$ | __ $$    $$ |$$      \   $$ | __       $$ |  $$ | /    $$ |  $$ | __  /    $$ | --
-- $$ |$$ |  $$ | $$$$$$  |$$$$$$$$/ $$ |        $$ |/  |        $$ |/  |$$$$$$$$/  $$$$$$  |  $$ |/  |      $$ \__$$ |/$$$$$$$ |  $$ |/  |/$$$$$$$ | --
-- $$ |$$ |  $$ |/     $$/ $$       |$$ |        $$  $$/         $$  $$/ $$       |/     $$/   $$  $$/       $$    $$ |$$    $$ |  $$  $$/ $$    $$ | --
-- $$/ $$/   $$/ $$$$$$$/   $$$$$$$/ $$/          $$$$/           $$$$/   $$$$$$$/ $$$$$$$/     $$$$/         $$$$$$$/  $$$$$$$/    $$$$/   $$$$$$$/  --
--                                                                                                                                                    --
--------------------------------------------------------------------------------------------------------------------------------------------------------


-- Insert data into `brand` table
INSERT INTO `pc`.`brand` (`name`) VALUES
('ASRock'),
('ASUS'),
('Gigabyte'),
('MSI'),
('AMD'),
('INTEL'),
('NVIDIA');


-- Insert data into `chipset` table
INSERT INTO `pc`.`chipset` (`name`) VALUES
('H460'),
('H410'),
('H310'),
('B550'),
('B450'),
('B365'),
('B360'),
('A520'),
('A320'),
('A68H');


-- Insert data into `socket` table
INSERT INTO `pc`.`socket` (`name`) VALUES
('LGA 1151-v2'),
('LGA 1200'),
('AM4'),
('FM2+');


-- Insert data into `socket_to_chipset` table
INSERT INTO `pc`.`socket_to_chipset` (`socket_id`, `chipset_id`) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(3, 3),
(4, 4);


-- Insert data into `cpu` table
INSERT INTO `pc`.`cpu` (`model`, `price`, `cores`, `threads`, `frequency`, `brand_id`, `socket_id`) VALUES
('Intel Core i7-9700K', 359.99, 8, 8, 3600, 6, 1),
('AMD Ryzen 7 5800X', 449.99, 8, 16, 3800, 5, 3),
('Intel Core i5-10400F', 179.99, 6, 12, 2900, 6, 2),
('AMD Ryzen 5 3600', 199.99, 6, 12, 3600, 5, 3),
('Intel Core i9-9900K', 499.99, 8, 16, 3600, 6, 1),
('AMD Ryzen 9 5950X', 799.99, 16, 32, 3400, 5, 3),
('Intel Core i7-10700K', 349.99, 8, 16, 3800, 6, 2),
('AMD Ryzen 7 3700X', 329.99, 8, 16, 3600, 5, 3),
('Intel Core i5-11600K', 269.99, 6, 12, 3900, 6, 2),
('AMD Ryzen 5 5600X', 299.99, 6, 12, 3700, 5, 3),
('Intel Core i3-10100', 139.99, 4, 8, 3600, 6, 2),
('AMD Ryzen 3 3300X', 139.99, 4, 8, 3800, 5, 3),
('Intel Pentium Gold G6400', 89.99, 2, 4, 4000, 6, 2),
('AMD Athlon 3000G', 69.99, 2, 4, 3500, 5, 3),
('Intel Celeron G5920', 59.99, 2, 2, 3500, 6, 2),
('AMD Athlon 200GE', 54.99, 2, 4, 3200, 5, 3),
('Intel Xeon W-3175X', 2999.99, 28, 56, 3100, 6, 1),
('AMD Threadripper 3990X', 4299.99, 64, 128, 2900, 5, 3),
('Intel Core i9-11900K', 549.99, 8, 16, 3500, 6, 2);


-- Insert data into `gpu` table
INSERT INTO `pc`.`gpu` (`model`, `price`, `cores`, `memory`, `frequency`, `brand_id`) VALUES
('NVIDIA GeForce RTX 3080', 699.99, 8704, 10, 1710, 7),
('AMD Radeon RX 6800 XT', 649.99, 4608, 16, 2250, 5),
('NVIDIA GeForce RTX 3060', 329.99, 3584, 12, 1780, 7),
('ASUS GeForce GTX 1650', 169.99, 896, 4, 1660, 2),
('NVIDIA GeForce RTX 3090', 1499.99, 10496, 24, 1700, 7),
('AMD Radeon RX 6900 XT', 999.99, 5120, 16, 2250, 5),
('NVIDIA GeForce RTX 3070', 499.99, 5888, 8, 1500, 7),
('ASUS GeForce GTX 1660 Super', 239.99, 1408, 6, 1780, 2),
('NVIDIA GeForce RTX 3060 Ti', 399.99, 4864, 8, 1670, 7),
('AMD Radeon RX 6700 XT', 579.99, 2560, 12, 2450, 5),
('NVIDIA GeForce RTX 3080 Ti', 1199.99, 10240, 12, 1670, 7),
('Gigabyte GeForce GTX 1650 Super', 179.99, 1280, 4, 1720, 3),
('AMD Radeon RX 6600 XT', 399.99, 2048, 8, 2590, 5),
('NVIDIA GeForce GTX 1660', 229.99, 1408, 6, 1530, 7),
('ASUS GeForce GT 1030', 89.99, 384, 2, 1230, 2),
('NVIDIA GeForce GT 710', 49.99, 192, 2, 954, 7),
('AMD Radeon RX 550', 99.99, 512, 4, 1180, 5),
('NVIDIA GeForce MX350', 159.99, 640, 2, 1470, 7),
('MSI GeForce GT 730', 59.99, 384, 2, 902, 4);


-- Insert data into `mbrd` table
INSERT INTO `pc`.`mbrd` (`model`, `price`, `brand_id`, `socket_id`, `chipset_id`) VALUES
('ASRock B550 Pro4', 129.99, 1, 3, 4),
('MSI B450 TOMAHAWK MAX', 114.99, 4, 3, 5),
('ASUS ROG Strix B365-F Gaming', 129.99, 2, 1, 6),
('Gigabyte A520 AORUS ELITE', 79.99, 3, 3, 8),
('ASRock Z490 Steel Legend', 189.99, 1, 1, 1),
('MSI MAG B550 TOMAHAWK', 179.99, 4, 3, 4),
('ASUS PRIME B450-PLUS', 109.99, 2, 3, 5),
('Gigabyte B365M DS3H', 89.99, 3, 1, 6),
('ASUS ROG Strix X570-E Gaming', 299.99, 2, 3, 4),
('MSI MPG B550 GAMING PLUS', 159.99, 4, 3, 4),
('Gigabyte B450 AORUS ELITE', 119.99, 3, 3, 5),
('ASRock B460 Steel Legend', 119.99, 1, 1, 1),
('MSI MAG B550M MORTAR', 149.99, 4, 3, 4),
('ASUS TUF GAMING B450-PRO', 129.99, 2, 3, 5),
('Gigabyte B550M AORUS ELITE', 139.99, 3, 3, 4),
('ASRock H470 Steel Legend', 139.99, 1, 1, 1),
('MSI MAG B560 TOMAHAWK WIFI', 169.99, 4, 3, 4),
('ASUS ROG Strix B550-F Gaming', 199.99, 2, 3, 4),
('Gigabyte B450M DS3H', 79.99, 3, 3, 5);
