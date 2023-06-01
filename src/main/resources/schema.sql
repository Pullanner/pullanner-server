DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    user_id BIGINT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    nickname VARCHAR(15),
    picture VARCHAR(500),
    provider VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE INDEX index_email_provider (email, provider),
    UNIQUE INDEX index_nickname (nickname)
);

CREATE TABLE article
(
    article_id BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(1500) NOT NULL,
    hit INT DEFAULT 0,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    PRIMARY KEY (article_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);
