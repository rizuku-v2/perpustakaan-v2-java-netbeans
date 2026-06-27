-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 27, 2026 at 09:22 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_perpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id` int(10) UNSIGNED NOT NULL,
  `nama` varchar(100) NOT NULL,
  `j_rusak` int(11) NOT NULL DEFAULT 0,
  `j_baik` int(11) NOT NULL DEFAULT 0,
  `kategori` varchar(100) DEFAULT NULL,
  `penulis` varchar(100) DEFAULT NULL,
  `penerbit` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`id`, `nama`, `j_rusak`, `j_baik`, `kategori`, `penulis`, `penerbit`) VALUES
(1, 'Pemrograman Java Dasar', 0, 5, 'Teknologi Informasi', 'Bambang Hariyanto', 'Informatika Bandung'),
(2, 'Basis Data dan SQL', 1, 4, 'Teknologi Informasi', 'Harianto Kristanto', 'Andi Offset'),
(3, 'Algoritma dan Pemrograman', 3, 8, 'Teknologi Informasi', 'Informatika Bandung', 'Rinaldi Munir'),
(4, 'Sistem Informasi Manajemen', 0, 6, 'Manajemen', 'Raymond McLeod', 'Salemba Empat'),
(5, 'Jaringan Komputer', 2, 0, 'Teknologi Informasi', 'Andrew S. Tanenbaum', 'Prenhallindo'),
(6, 'Kalkulus Jilid 1', 1, 1, 'Matematika', 'James Stewart', 'Erlangga'),
(7, 'Pengantar Ilmu Komunikasi', 0, 5, 'Ilmu Sosial', 'Dedy Mulyana', 'Rosdakarya'),
(8, 'Manajemen Proyek Perangkat Lunak', 2, 3, 'Teknologi Informasi', 'Roger Pressman', 'Andi Offset'),
(9, 'Bahasa Indonesia untuk Perguruan', 0, 7, 'Bahasa', 'E. Zaenal Arifin', 'Akademika Pressindo'),
(10, 'Statistika untuk Penelitian', 0, 4, 'Matematika', 'Sugiyono', 'Alfabeta'),
(11, 'Aku sang Raja', 3, 7, 'Fantasi', 'Rizki', 'Rizki');

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED DEFAULT NULL,
  `buku_id` int(10) UNSIGNED DEFAULT NULL,
  `tgl` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('dipinjam','dikembalikan','hilang') NOT NULL DEFAULT 'dipinjam',
  `tgl_jatuh_tempo` timestamp NULL DEFAULT NULL,
  `kondisi` enum('baik','rusak') NOT NULL DEFAULT 'baik'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pengembalian`
--

CREATE TABLE `pengembalian` (
  `id` int(10) UNSIGNED NOT NULL,
  `tgl` timestamp NOT NULL DEFAULT current_timestamp(),
  `denda` int(11) NOT NULL DEFAULT 0,
  `peminjaman_id` int(10) UNSIGNED DEFAULT NULL,
  `status` enum('baik','rusak','hilang') NOT NULL DEFAULT 'baik'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `nama` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(60) NOT NULL,
  `NIK` varchar(50) NOT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nama`, `username`, `email`, `NIK`, `alamat`) VALUES
(2, 'Budi Santoso', 'budisantoso', 'budi.santoso@email.com', '3201010201900002', 'Jl. Sudirman No. 45, Jakarta Pusat'),
(3, 'Citra Dewi', 'citradewi', 'citra.dewi@email.com', '3201010301900003', 'Jl. Gatot Subroto Blok B No. 7, Bekasi'),
(4, 'Dina Marlina', 'dinamarlina', 'dina.marlina@email.com', '3201010401900004', 'Jl. Pahlawan No. 3, Depok'),
(5, 'Eko Prasetyo', 'ekoprasetyo', 'eko.prasetyo@email.com', '3201010501900005', 'Jl. Ahmad Yani No. 88, Tangerang'),
(6, 'Fitri Rahayu', 'fitrirahayu', 'fitri.rahayu@email.com', '3201010601900006', 'Jl. Diponegoro No. 21, Bogor'),
(7, 'Gunawan Hadi', 'gunawanhadi', 'sdqw@sadqw.com', '123123324', 'asdasdaasd'),
(8, 'Hana Pertiwi', 'hanapertiwi', 'hana.pertiwi@email.com', '3201010801900008', 'Jl. Veteran No. 5, Jakarta Timur'),
(10, 'wdasdsq', 'asda', 'asdq', '12313', 'dsqwqdasd');

-- --------------------------------------------------------

--
-- Table structure for table `visitors`
--

CREATE TABLE `visitors` (
  `id` int(10) UNSIGNED NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `user_id` int(10) UNSIGNED DEFAULT NULL,
  `tgl` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `visitors`
--

INSERT INTO `visitors` (`id`, `nama`, `user_id`, `tgl`) VALUES
(1, 'Andi Saputra', NULL, '2026-06-01 01:05:00'),
(2, 'Budi Santoso', 2, '2026-06-02 02:10:00'),
(3, 'Citra Dewi', 3, '2026-06-03 03:20:00'),
(4, 'Dina Marlina', 4, '2026-06-05 01:30:00'),
(5, 'Eko Prasetyo', 5, '2026-06-07 04:00:00'),
(6, 'Andi Saputra', NULL, '2026-06-10 01:15:00'),
(7, 'Budi Santoso', 2, '2026-06-12 02:45:00'),
(8, 'Fitri Rahayu', 6, '2026-06-15 03:00:00'),
(9, 'Citra Dewi', 3, '2026-06-18 01:50:00'),
(10, 'Eko Prasetyo', 5, '2026-06-23 02:30:00'),
(11, 'Budi Santoso', 2, '2026-06-27 03:22:14'),
(12, 'Citra Dewi', 3, '2026-06-27 03:22:17'),
(13, 'sasaa', NULL, '2026-06-27 03:22:22'),
(14, 'Dina Marlina', 4, '2026-06-27 06:55:42'),
(15, 'aaddwqdas', NULL, '2026-06-27 06:55:48');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `buku_id` (`buku_id`),
  ADD KEY `idx_peminjaman_status` (`status`),
  ADD KEY `idx_peminjaman_tgl` (`tgl`);

--
-- Indexes for table `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD PRIMARY KEY (`id`),
  ADD KEY `peminjaman_id` (`peminjaman_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `visitors`
--
ALTER TABLE `visitors`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_visitors_tgl` (`tgl`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buku`
--
ALTER TABLE `buku`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `pengembalian`
--
ALTER TABLE `pengembalian`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `visitors`
--
ALTER TABLE `visitors`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `fk_pinjam_buku` FOREIGN KEY (`buku_id`) REFERENCES `buku` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_pinjam_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD CONSTRAINT `fk_kembali_pinjam` FOREIGN KEY (`peminjaman_id`) REFERENCES `peminjaman` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `visitors`
--
ALTER TABLE `visitors`
  ADD CONSTRAINT `fk_visitors_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
