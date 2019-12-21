-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 06, 2019 at 11:22 AM
-- Server version: 8.0.3-rc-log
-- PHP Version: 7.1.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jodidar`
--

-- --------------------------------------------------------

--
-- Table structure for table `demo`
--

CREATE TABLE `demo` (
  `id` bigint(20) NOT NULL,
  `username` varchar(40) NOT NULL,
  `password` varchar(25) NOT NULL,
  `fullName` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `id` int(11) NOT NULL,
  `senderid` int(11) NOT NULL,
  `receiverid` int(11) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT 'accepted/pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`id`, `senderid`, `receiverid`, `status`) VALUES
(32, 2, 10, 'pending'),
(33, 2, 4, 'pending'),
(55, 9, 1, 'pending'),
(59, 11, 1, 'pending'),
(61, 10, 9, 'pending'),
(62, 10, 1, 'pending'),
(63, 10, 11, 'accepted');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `firstname` varchar(20) NOT NULL,
  `lastname` varchar(20) NOT NULL,
  `email` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `bday` date NOT NULL,
  `gender` varchar(10) NOT NULL,
  `religion` varchar(20) DEFAULT NULL,
  `caste` varchar(20) DEFAULT NULL,
  `location` varchar(20) DEFAULT NULL,
  `education` varchar(30) DEFAULT NULL,
  `profession` varchar(30) DEFAULT NULL,
  `mothertongue` varchar(20) DEFAULT NULL,
  `height` decimal(7,2) DEFAULT '5.50',
  `eating` varchar(30) DEFAULT NULL,
  `manglik` varchar(10) DEFAULT 'No',
  `lookingfor` varchar(10) DEFAULT NULL,
  `about` varchar(255) DEFAULT NULL,
  `image` varchar(250) DEFAULT 'user_male.png',
  `online` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `firstname`, `lastname`, `email`, `password`, `bday`, `gender`, `religion`, `caste`, `location`, `education`, `profession`, `mothertongue`, `height`, `eating`, `manglik`, `lookingfor`, `about`, `image`, `online`) VALUES
(1, 'user', 'one', 'user@one', '32250170a0dca92d53ec9624f336ca24', '1990-09-02', 'male', 'Hindu', NULL, 'Gujarat', 'MBBS', 'Doctor', 'Gujarati', '5.80', 'Veg', 'Yes', 'Female', 'I am 30Yr old  Doctor ', 'user_male.png', 0),
(2, 'user', 'second', 'second@user', '32250170a0dca92d53ec9624f336ca24', '1999-09-02', 'male', 'Hindu', NULL, 'Gujarat', 'MBA', 'Accountant', 'Hindi', '5.50', 'Nonveg', 'Yes', 'Female', 'I am 20 yr old Accountant at Reliance Jio', 'user_male.png', 0),
(3, 'user', 'third', 'third@user', '32250170a0dca92d53ec9624f336ca24', '1970-09-02', 'female', 'Hindu', NULL, 'Gujarat', 'BCom', 'Accountant', 'marathi', '4.80', 'veg', 'Yes', 'male', 'I am 49 yr old accountant', 'user_female.png', 0),
(4, 'random', 'user4', 'random@user', '32250170a0dca92d53ec9624f336ca24', '1987-10-09', 'Male', 'Sikh', NULL, 'Punjab', 'MSC', 'Engineer', 'Punjabi', '5.80', 'Nonveg', 'Yes', 'Female', 'I am very Good Person , send request', 'user_male.png', 0),
(5, 'tester', 'techperson', 'test@user', '32250170a0dca92d53ec9624f336ca24', '1980-11-09', 'male', 'Sikh', NULL, 'Punjab', 'MTech', 'Engineer', 'Punjabi', '5.80', 'Nonveg', 'No', 'Female', 'I am Computer Engineer 39 yrs old', 'user_male.png', 0),
(9, 'temp', 'name', 't@one', '32250170a0dca92d53ec9624f336ca24', '1994-03-21', 'female', 'Christian', NULL, 'Goa', 'MCOM', 'Singer', 'Parsi', '4.90', 'Nonveg', 'No', 'male', 'Just the way I am', 'user_female.png', 0),
(10, 'karan', 'parmar', 'k@p', '32250170a0dca92d53ec9624f336ca24', '1998-09-28', 'Male', 'Hindu', NULL, 'Gujarat', 'Computer Applications', NULL, 'Gujarati', '5.90', 'Veg', 'No', 'Female', 'I am 22 year old Msc it student', 'user_male.png', 0),
(11, 'tony', 'stark', 't@s', '32250170a0dca92d53ec9624f336ca24', '1970-12-01', 'Male', NULL, NULL, NULL, NULL, NULL, NULL, '5.50', NULL, 'No', NULL, NULL, 'user_male.png', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `demo`
--
ALTER TABLE `demo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uniqueName` (`username`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uniqueRequestOnly` (`senderid`,`receiverid`) USING BTREE;

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
