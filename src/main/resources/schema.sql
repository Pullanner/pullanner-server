DROP TABLE IF EXISTS plan_roadmap;
DROP TABLE IF EXISTS plan;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS user_roadmap;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS roadmap;

CREATE TABLE roadmap
(
    roadmap_id INT AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    PRIMARY KEY (roadmap_id)
);

CREATE TABLE `user`
(
    user_id                 BIGINT AUTO_INCREMENT,
    email                   VARCHAR(255) NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    nickname                VARCHAR(15),
    profile_image_url       VARCHAR(500),
    profile_image_file_name VARCHAR(255),
    provider                VARCHAR(255) NOT NULL,
    role                    VARCHAR(255) NOT NULL,
    created_date            TIMESTAMP    NOT NULL,
    modified_date           TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE INDEX index_email_provider (email, provider),
    UNIQUE INDEX index_nickname (nickname)
);

CREATE TABLE user_roadmap
(
    user_roadmap_id BIGINT AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    roadmap_id      INT    NOT NULL,
    PRIMARY KEY (user_roadmap_id),
    FOREIGN KEY (user_id) REFERENCES `user` (user_id),
    FOREIGN KEY (roadmap_id) REFERENCES roadmap (roadmap_id)
);

CREATE TABLE article
(
    article_id    BIGINT AUTO_INCREMENT,
    user_id       BIGINT        NOT NULL,
    title         VARCHAR(100)  NOT NULL,
    content       VARCHAR(1500) NOT NULL,
    hit           INT DEFAULT 0,
    created_date  TIMESTAMP     NOT NULL,
    modified_date TIMESTAMP     NOT NULL,
    PRIMARY KEY (article_id),
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
);

CREATE TABLE plan
(
    plan_id       BIGINT AUTO_INCREMENT,
    user_id       BIGINT       NOT NULL,
    plan_type     VARCHAR(255) NOT NULL COMMENT 'strength | master',
    name          VARCHAR(255) NOT NULL,
    note          VARCHAR(300) NOT NULL,
    plan_date     VARCHAR(255) NOT NULL,
    plan_time     VARCHAR(255) NOT NULL,
    main_color    VARCHAR(255) NOT NULL,
    created_date  TIMESTAMP    NOT NULL,
    modified_date TIMESTAMP    NOT NULL,
    PRIMARY KEY (plan_id),
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
);

CREATE TABLE plan_roadmap
(
    plan_roadmap_id BIGINT AUTO_INCREMENT,
    plan_id         BIGINT NOT NULL,
    user_roadmap_id BIGINT NOT NULL,
    PRIMARY KEY (plan_roadmap_id),
    FOREIGN KEY (plan_id) REFERENCES plan (plan_id),
    FOREIGN KEY (user_roadmap_id) REFERENCES user_roadmap (user_roadmap_id)
);
