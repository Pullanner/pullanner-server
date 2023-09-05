DROP TABLE IF EXISTS plan_workout;
DROP TABLE IF EXISTS plan;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS user_workout;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS workout;

CREATE TABLE workout
(
    workout_id INT AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    created_date            TIMESTAMP    NOT NULL,
    modified_date           TIMESTAMP    NOT NULL,
    PRIMARY KEY (workout_id)
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
    experience_point        INT          DEFAULT 0,
    created_date            TIMESTAMP    NOT NULL,
    modified_date           TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE INDEX index_email_provider (email, provider),
    UNIQUE INDEX index_nickname (nickname)
);

CREATE TABLE user_workout
(
    user_workout_id BIGINT AUTO_INCREMENT,
    user_workout_user_id         BIGINT NOT NULL,
    user_workout_workout_id      INT    NOT NULL,
    created_date            TIMESTAMP    NOT NULL,
    modified_date           TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_workout_id),
    FOREIGN KEY (user_workout_user_id) REFERENCES `user` (user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_workout_workout_id) REFERENCES workout (workout_id)
);

CREATE TABLE article
(
    article_id    BIGINT AUTO_INCREMENT,
    article_user_id       BIGINT        NOT NULL,
    title         VARCHAR(100)  NOT NULL,
    content       VARCHAR(1500) NOT NULL,
    hit           INT DEFAULT 0,
    created_date  TIMESTAMP     NOT NULL,
    modified_date TIMESTAMP     NOT NULL,
    PRIMARY KEY (article_id),
    FOREIGN KEY (article_user_id) REFERENCES `user` (user_id) ON DELETE CASCADE
);

CREATE TABLE plan
(
    plan_id       BIGINT AUTO_INCREMENT,
    plan_user_id  BIGINT       NOT NULL,
    plan_type     VARCHAR(255) NOT NULL COMMENT 'strength | master',
    name          VARCHAR(20) NOT NULL,
    note          VARCHAR(300) NULL,
    completed     BOOLEAN  NOT NULL,
    plan_date TIMESTAMP    NOT NULL,
    created_date  TIMESTAMP    NOT NULL,
    modified_date TIMESTAMP    NOT NULL,
    PRIMARY KEY (plan_id),
    FOREIGN KEY (plan_user_id) REFERENCES `user` (user_id) ON DELETE CASCADE
);

CREATE TABLE plan_workout
(
    plan_workout_id BIGINT AUTO_INCREMENT,
    plan_workout_plan_id         BIGINT NOT NULL,
    plan_workout_workout_id      INT NOT NULL,
    count_per_set   INT NOT NULL,
    set_count       INT NOT NULL,
    done            BOOLEAN NOT NULL,
    created_date            TIMESTAMP    NOT NULL,
    modified_date           TIMESTAMP    NOT NULL,
    PRIMARY KEY (plan_workout_id),
    FOREIGN KEY (plan_workout_plan_id) REFERENCES plan (plan_id) ON DELETE CASCADE,
    FOREIGN KEY (plan_workout_workout_id) REFERENCES workout (workout_id)
);

INSERT INTO workout (name, created_date, modified_date)
VALUES ('Hanging', NOW(), NOW()), ('Jumping Pull-up', NOW(), NOW()), ('Band Pull-up', NOW(), NOW()), ('Chin-up', NOW(), NOW()), ('Pull-up', NOW(), NOW()), ('Chest to Bar Pull-up', NOW(), NOW()), ('Archer Pull-up', NOW(), NOW()), ('Muscle up', NOW(), NOW());
