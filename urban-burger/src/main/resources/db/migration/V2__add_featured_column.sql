ALTER TABLE menu_items ADD COLUMN featured BOOLEAN DEFAULT FALSE;

-- Update specific items to be featured (using names from previous mock data)
UPDATE menu_items SET featured = TRUE WHERE name IN ('X-Bacon', 'French Fries', 'Coca-Cola 350ml');

-- Fallback: if those specific names don't exist, just pick 3 random ones
UPDATE menu_items SET featured = TRUE WHERE id IN (
    SELECT id FROM menu_items ORDER BY RANDOM() LIMIT 3
) AND (SELECT count(*) FROM menu_items WHERE featured = TRUE) < 3;
