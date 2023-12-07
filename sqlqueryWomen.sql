CREATE TABLE produits (
    id INT PRIMARY KEY,
    type VARCHAR(255),
    nom VARCHAR(255) UNIQUE,
    prix DECIMAL(10, 2),
    stock INT CHECK(stock >= 0),
    taille INT CHECK(taille BETWEEN 34 AND 54),
    pointure INT CHECK(pointure BETWEEN 36 AND 50),
    CONSTRAINT taille_pointure_check CHECK ((taille IS NULL AND pointure IS NULL) OR (taille IS NOT NULL AND pointure IS NULL) OR (taille IS NULL AND pointure IS NOT NULL))
);
-- Vetements
INSERT INTO produits (id, type, nom, prix, stock, taille, pointure)
VALUES
(1, 'Vetement', 'Pantalon', 29.99, 50, 38, NULL),
(2, 'Vetement', 'Robe', 39.99, 30, 40, NULL),
(3, 'Vetement', 'Sweat', 49.99, 40, 36, NULL),
(4, 'Vetement', 'Veste', 59.99, 20, 42, NULL),
(5, 'Accessoire', 'Sac', 19.99, 100, NULL, NULL),
(6, 'Accessoire', 'Lunettes', 9.99, 80, NULL, NULL),
(7, 'Accessoire', 'Chapeau', 14.99, 60, NULL, NULL),
(8, 'Chaussure', 'Baskets', 79.99, 25, NULL, 38),
(9, 'Chaussure', 'Escarpins', 69.99, 15, NULL, 40),
(10, 'Chaussure', 'Bottes', 89.99, 10, NULL, 36);
SELECT * FROM store.produits;