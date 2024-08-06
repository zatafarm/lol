-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: mooland
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `bj`
--

DROP TABLE IF EXISTS `bj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bj` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `BJName` varchar(100) NOT NULL,
  `BJID` varchar(100) DEFAULT NULL,
  `LOLNickname` varchar(100) DEFAULT NULL,
  `LOLpuuid` varchar(400) DEFAULT NULL,
  `LOLTier` varchar(100) DEFAULT NULL,
  `LOLRank` varchar(45) DEFAULT NULL,
  `LOLBTier` varchar(100) DEFAULT NULL,
  `LOLMPosition` varchar(45) DEFAULT NULL,
  `LOLSPosition` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`,`BJName`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bj`
--

LOCK TABLES `bj` WRITE;
/*!40000 ALTER TABLE `bj` DISABLE KEYS */;
/*!40000 ALTER TABLE `bj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `bno` int NOT NULL AUTO_INCREMENT,
  `title` varchar(300) NOT NULL,
  `content` text NOT NULL,
  `cdate` datetime NOT NULL,
  `ddate` datetime DEFAULT NULL,
  `del` int NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`bno`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `gameid` int NOT NULL AUTO_INCREMENT,
  `createid` bigint NOT NULL,
  `gamedate` datetime NOT NULL,
  `mlt` int DEFAULT NULL COMMENT '1 단판 3 3/2 5 5/3',
  `ltn` varchar(100) DEFAULT NULL,
  `rtn` varchar(100) DEFAULT NULL,
  `win` int NOT NULL DEFAULT '0' COMMENT '1 red 2 blue',
  `fin` int NOT NULL DEFAULT '0' COMMENT '0 경기 미완 reback 1 경기 완',
  `lscore` int DEFAULT NULL,
  `rscore` int DEFAULT NULL,
  PRIMARY KEY (`gameid`,`createid`),
  KEY `id_idx` (`createid`),
  CONSTRAINT `id` FOREIGN KEY (`createid`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamesave`
--

DROP TABLE IF EXISTS `gamesave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gamesave` (
  `gameid` int NOT NULL,
  `p1` varchar(100) DEFAULT NULL,
  `p2` varchar(100) DEFAULT NULL,
  `p3` varchar(100) DEFAULT NULL,
  `p4` varchar(100) DEFAULT NULL,
  `p5` varchar(100) DEFAULT NULL,
  `team` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamesave`
--

LOCK TABLES `gamesave` WRITE;
/*!40000 ALTER TABLE `gamesave` DISABLE KEYS */;
/*!40000 ALTER TABLE `gamesave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamewl`
--

DROP TABLE IF EXISTS `gamewl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gamewl` (
  `no` int NOT NULL AUTO_INCREMENT,
  `gameid` int NOT NULL,
  `bjname` varchar(100) NOT NULL,
  `lane` varchar(45) NOT NULL,
  `RB` int NOT NULL DEFAULT '0' COMMENT '1 red 2 blue',
  `setwin` int NOT NULL COMMENT '1 승리 2패배 ',
  PRIMARY KEY (`no`),
  KEY `gameid_idx` (`gameid`),
  CONSTRAINT `gameid` FOREIGN KEY (`gameid`) REFERENCES `game` (`gameid`)
) ENGINE=InnoDB AUTO_INCREMENT=2101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamewl`
--

LOCK TABLES `gamewl` WRITE;
/*!40000 ALTER TABLE `gamewl` DISABLE KEYS */;
/*!40000 ALTER TABLE `gamewl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `cdate` date DEFAULT NULL,
  `ddate` date DEFAULT NULL,
  `del` bit(1) NOT NULL,
  `point` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER','BJ') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-06 13:20:40
