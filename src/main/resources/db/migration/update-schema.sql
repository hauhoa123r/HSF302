CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    full_name     VARCHAR(255) NULL,
    username      VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    phone         VARCHAR(255) NULL,
    `role`        VARCHAR(255) NOT NULL,
    created_at    datetime NULL,
    date_of_birth VARCHAR(255) NULL,
    gender        VARCHAR(255) NULL,
    address       VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);