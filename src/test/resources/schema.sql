DROP TABLE IF EXISTS book;
CREATE TABLE book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(13) NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    borrower_id BIGINT,
    created_by VARCHAR(100) DEFAULT 'system',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_isbn ON book(isbn);
CREATE INDEX idx_title ON book(title);
CREATE INDEX idx_author ON book(author);
CREATE INDEX idx_isbn_title_author ON book(isbn, title, author);


DROP TABLE IF EXISTS borrower;
CREATE TABLE borrower (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_by VARCHAR(100) DEFAULT 'system',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_name ON borrower(name);
CREATE INDEX idx_email ON borrower(email);


DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    borrower_id BIGINT NOT NULL,
    return_date TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'system',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_trx_book_id ON `transaction`(book_id);
CREATE INDEX idx_trx_borrower_id ON `transaction`(borrower_id);


DROP TABLE IF EXISTS transaction_archive;
CREATE TABLE transaction_archive (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    borrower_id BIGINT NOT NULL,
    return_date TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'system',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_trxach_book_id ON transaction_archive(book_id);
CREATE INDEX idx_trxach_borrower_id ON transaction_archive(borrower_id);
