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
