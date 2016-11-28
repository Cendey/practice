CREATE DATABASE IF NOT EXISTS `webservice`;
USE `webservice`;

DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
  `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(145) NOT NULL,
  `version`     VARCHAR(100) NOT NULL,
  `author`      VARCHAR(145) NOT NULL,
  `publisher`   VARCHAR(200) DEFAULT NULL,
  `publishdate` DATE         DEFAULT NULL,
  `isbn`        VARCHAR(14)           NOT NULL,
  `paperback`   INT(8)                DEFAULT NULL,
  `summary`     VARCHAR(1024)         DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `book_UNIQUE` (`isbn`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;


LOCK TABLES `books` WRITE;
INSERT INTO `books` VALUES
  (1, 'Mockingjay (The Hunger Games)', '1st Edition', 'Suzanne Collins', 'Large Print Press; Lrg edition', '2012-03-16',
   '978-1594135866', 504,
   'Powerful and haunting, the thrilling final installment of Suzanne Collins’ groundbreaking Hunger Games trilogy promises to be one of the most talked-about books of the year. Against all odds, Katniss Everdeen has survived the Hunger Games twice.'),
  (2, 'The Fault in Our Stars', '1st Edition', 'John Green', 'Large Print Press', '2014-04-10', '978-1594137907', 364,
   'Insightful, bold, irreverent, and raw, The Fault in Our Stars is award-winning-author John Green s most ambitious and heartbreaking work yet, brilliantly exploring the funny, thrilling, and tragic business of being alive and in love.'),
  (3, 'All the Light We Cannot See', '1st Edition', ' Anthony Doerr', 'Scribner', '2014-07-10', '978-1501132872', 531,
   'From the highly acclaimed, multiple award-winning Anthony Doerr, the beautiful, stunningly ambitious instant New York Times bestseller about a blind French girl and a German boy whose paths collide in occupied France as both try to survive the devastation of World War II.');
UNLOCK TABLES;