
-- Drop existing table if it exists
DROP TABLE IF EXISTS network_node;

-- Create an empty network_node table
CREATE TABLE network_node (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DECIMAL(10, 6) NOT NULL,
    longitude DECIMAL(10, 6) NOT NULL,
    node_location VARCHAR(255) NOT NULL,
    node_name VARCHAR(255) NOT NULL
);