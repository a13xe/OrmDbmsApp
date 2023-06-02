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
  (6, 'Ryzen 9 3900X', 449.99, 12, 24, 3800, 2, 3),
  (7, 'Core i5-11600K', 299.99, 6, 12, 4900, 1, 4),
  (8, 'Ryzen 7 5800X', 449.99, 8, 16, 4700, 2, 4),
  (9, 'Core i7-10700K', 399.99, 8, 16, 3800, 1, 3),
  (10, 'Ryzen 5 5600X', 299.99, 6, 12, 4600, 2, 4);
  
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