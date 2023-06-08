CREATE DATABASE  IF NOT EXISTS `pc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pc`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: pc
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES (5,'AMD'),(1,'ASRock'),(2,'ASUS'),(3,'Gigabyte'),(6,'INTEL'),(4,'MSI'),(7,'NVIDIA');
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chipset`
--

DROP TABLE IF EXISTS `chipset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chipset` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chipset`
--

LOCK TABLES `chipset` WRITE;
/*!40000 ALTER TABLE `chipset` DISABLE KEYS */;
INSERT INTO `chipset` VALUES (9,'A320'),(8,'A520'),(10,'A68H'),(7,'B360'),(6,'B365'),(5,'B450'),(4,'B550'),(3,'H310'),(2,'H410'),(1,'H460');
/*!40000 ALTER TABLE `chipset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cpu`
--

DROP TABLE IF EXISTS `cpu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cpu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `model` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `cores` int NOT NULL,
  `threads` int NOT NULL,
  `frequency` int NOT NULL,
  `brand_id` int NOT NULL,
  `socket_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `model_UNIQUE` (`model`),
  KEY `fk_cpu_brand1_idx` (`brand_id`),
  KEY `fk_cpu_socket1_idx` (`socket_id`),
  CONSTRAINT `fk_cpu_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_cpu_socket1` FOREIGN KEY (`socket_id`) REFERENCES `socket` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cpu`
--

LOCK TABLES `cpu` WRITE;
/*!40000 ALTER TABLE `cpu` DISABLE KEYS */;
INSERT INTO `cpu` VALUES (1,'Intel Core i7-9700K',359.99,8,8,3600,6,1),(2,'AMD Ryzen 7 5800X',449.99,8,16,3800,5,3),(3,'Intel Core i5-10400F',179.99,6,12,2900,6,2),(4,'AMD Ryzen 5 3600',199.99,6,12,3600,5,3),(5,'Intel Core i9-9900K',499.99,8,16,3600,6,1),(6,'AMD Ryzen 9 5950X',799.99,16,32,3400,5,3),(7,'Intel Core i7-10700K',349.99,8,16,3800,6,2),(8,'AMD Ryzen 7 3700X',329.99,8,16,3600,5,3),(9,'Intel Core i5-11600K',269.99,6,12,3900,6,2),(10,'AMD Ryzen 5 5600X',299.99,6,12,3700,5,3),(11,'Intel Core i3-10100',139.99,4,8,3600,6,2),(12,'AMD Ryzen 3 3300X',139.99,4,8,3800,5,3),(13,'Intel Pentium Gold G6400',89.99,2,4,4000,6,2),(14,'AMD Athlon 3000G',69.99,2,4,3500,5,3),(15,'Intel Celeron G5920',59.99,2,2,3500,6,2),(16,'AMD Athlon 200GE',54.99,2,4,3200,5,3),(17,'Intel Xeon W-3175X',2999.99,28,56,3100,6,1),(18,'AMD Threadripper 3990X',4299.99,64,128,2900,5,3),(19,'Intel Core i9-11900K',549.99,8,16,3500,6,2);
/*!40000 ALTER TABLE `cpu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gpu`
--

DROP TABLE IF EXISTS `gpu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gpu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `model` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `cores` int NOT NULL,
  `memory` int NOT NULL,
  `frequency` int NOT NULL,
  `brand_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `model_UNIQUE` (`model`),
  KEY `fk_gpu_brand1_idx` (`brand_id`),
  CONSTRAINT `fk_gpu_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gpu`
--

LOCK TABLES `gpu` WRITE;
/*!40000 ALTER TABLE `gpu` DISABLE KEYS */;
INSERT INTO `gpu` VALUES (1,'NVIDIA GeForce RTX 3080',699.99,8704,10,1710,7),(2,'AMD Radeon RX 6800 XT',649.99,4608,16,2250,5),(3,'NVIDIA GeForce RTX 3060',329.99,3584,12,1780,7),(4,'ASUS GeForce GTX 1650',169.99,896,4,1660,2),(5,'NVIDIA GeForce RTX 3090',1499.99,10496,24,1700,7),(6,'AMD Radeon RX 6900 XT',999.99,5120,16,2250,5),(7,'NVIDIA GeForce RTX 3070',499.99,5888,8,1500,7),(8,'ASUS GeForce GTX 1660 Super',239.99,1408,6,1780,2),(9,'NVIDIA GeForce RTX 3060 Ti',399.99,4864,8,1670,7),(10,'AMD Radeon RX 6700 XT',579.99,2560,12,2450,5),(11,'NVIDIA GeForce RTX 3080 Ti',1199.99,10240,12,1670,7),(12,'Gigabyte GeForce GTX 1650 Super',179.99,1280,4,1720,3),(13,'AMD Radeon RX 6600 XT',399.99,2048,8,2590,5),(14,'NVIDIA GeForce GTX 1660',229.99,1408,6,1530,7),(15,'ASUS GeForce GT 1030',89.99,384,2,1230,2),(16,'NVIDIA GeForce GT 710',49.99,192,2,954,7),(17,'AMD Radeon RX 550',99.99,512,4,1180,5),(18,'NVIDIA GeForce MX350',159.99,640,2,1470,7),(19,'MSI GeForce GT 730',59.99,384,2,902,4);
/*!40000 ALTER TABLE `gpu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mbrd`
--

DROP TABLE IF EXISTS `mbrd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mbrd` (
  `id` int NOT NULL AUTO_INCREMENT,
  `model` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `brand_id` int NOT NULL,
  `socket_id` int NOT NULL,
  `chipset_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `model_UNIQUE` (`model`),
  KEY `fk_mbrd_brand1_idx` (`brand_id`),
  KEY `fk_mbrd_socket1_idx` (`socket_id`),
  KEY `fk_mbrd_chipset1_idx` (`chipset_id`),
  CONSTRAINT `fk_mbrd_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_mbrd_chipset1` FOREIGN KEY (`chipset_id`) REFERENCES `chipset` (`id`),
  CONSTRAINT `fk_mbrd_socket1` FOREIGN KEY (`socket_id`) REFERENCES `socket` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mbrd`
--

LOCK TABLES `mbrd` WRITE;
/*!40000 ALTER TABLE `mbrd` DISABLE KEYS */;
INSERT INTO `mbrd` VALUES (1,'ASRock B550 Pro4',129.99,1,3,4),(2,'MSI B450 TOMAHAWK MAX',114.99,4,3,5),(3,'ASUS ROG Strix B365-F Gaming',129.99,2,1,6),(4,'Gigabyte A520 AORUS ELITE',79.99,3,3,8),(5,'ASRock Z490 Steel Legend',189.99,1,1,1),(6,'MSI MAG B550 TOMAHAWK',179.99,4,3,4),(7,'ASUS PRIME B450-PLUS',109.99,2,3,5),(8,'Gigabyte B365M DS3H',89.99,3,1,6),(9,'ASUS ROG Strix X570-E Gaming',299.99,2,3,4),(10,'MSI MPG B550 GAMING PLUS',159.99,4,3,4),(11,'Gigabyte B450 AORUS ELITE',119.99,3,3,5),(12,'ASRock B460 Steel Legend',119.99,1,1,1),(13,'MSI MAG B550M MORTAR',149.99,4,3,4),(14,'ASUS TUF GAMING B450-PRO',129.99,2,3,5),(15,'Gigabyte B550M AORUS ELITE',139.99,3,3,4),(16,'ASRock H470 Steel Legend',139.99,1,1,1),(17,'MSI MAG B560 TOMAHAWK WIFI',169.99,4,3,4),(18,'ASUS ROG Strix B550-F Gaming',199.99,2,3,4),(19,'Gigabyte B450M DS3H',79.99,3,3,5);
/*!40000 ALTER TABLE `mbrd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socket`
--

DROP TABLE IF EXISTS `socket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `socket` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socket`
--

LOCK TABLES `socket` WRITE;
/*!40000 ALTER TABLE `socket` DISABLE KEYS */;
INSERT INTO `socket` VALUES (3,'AM4'),(4,'FM2+'),(1,'LGA 1151-v2'),(2,'LGA 1200');
/*!40000 ALTER TABLE `socket` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-08 18:53:41
