-- Insert User (Password is 'password')
INSERT INTO users (id, login, email, password, role)
VALUES (1, 'Test Admin', 'admin@urbanburger.com', '$2a$10$wPHxwfsfTnOJAdgYcerBt.od6431oMjx.uKoveAP8Z3O5MUU3ix5y', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Burgers
INSERT INTO menu_items (name, ingredients, price, image_url, category, featured, user_id)
VALUES 
('Classic Smash', 'Pão brioche, 2 smash burgers 80g, queijo cheddar e maionese da casa.', 28.00, 'burger-1.jpg', 'Burgers', true, 1),
('Bacon Supreme', 'Pão australiano, burger 180g, bacon crocante, cebola caramelizada e cheddar.', 35.00, 'burger-2.jpg', 'Burgers', true, 1),
('Chicken Crispy', 'Pão de gergelim, filé de frango empanado, alface, tomate e molho especial.', 25.00, 'burger-3.jpg', 'Burgers', false, 1),
('Veggie Future', 'Pão integral, burger de plantas, rúcula, tomate seco e maionese vegana.', 32.00, 'burger-4.jpg', 'Burgers', false, 1),
('Double Trouble', 'Pão brioche, 2 burgers 180g, dobro de queijo, bacon e picles.', 42.00, 'burger-5.jpg', 'Burgers', true, 1);

-- Sides
INSERT INTO menu_items (name, ingredients, price, image_url, category, featured, user_id)
VALUES 
('Batata Frita Rústica', 'Batatas cortadas à mão com alecrim e alho.', 15.00, 'fries-1.jpg', 'Acompanhamentos', false, 1),
('Onion Rings', 'Anéis de cebola empanados e crocantes.', 18.00, 'onion-rings.jpg', 'Acompanhamentos', false, 1),
('Nuggets Caseiros', '6 unidades de nuggets feitos na casa com molho barbecue.', 20.00, 'nuggets.jpg', 'Acompanhamentos', true, 1);

-- Drinks
INSERT INTO menu_items (name, ingredients, price, image_url, category, featured, user_id)
VALUES 
('Coca-Cola Lata', 'Lata 350ml gelada.', 6.00, 'coke.jpg', 'Bebidas', false, 1),
('Milkshake de Morango', 'Feito com sorvete de creme e morangos frescos.', 18.00, 'milkshake.jpg', 'Bebidas', true, 1),
('Suco de Laranja', 'Natural, feito na hora 500ml.', 10.00, 'oj.jpg', 'Bebidas', false, 1);

-- Reset sequence to avoid collision
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
