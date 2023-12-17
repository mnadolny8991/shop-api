DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(200),
    role VARCHAR(30)
);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    category_id LONG,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    user_id LONG NOT NULL,
    date_time DATETIME NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

DROP TABLE IF EXISTS order_lines;
CREATE TABLE order_lines (
    id LONG PRIMARY KEY AUTO_INCREMENT,
    order_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);