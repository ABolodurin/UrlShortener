CREATE TABLE tokens
(
    token      varchar(31)  NOT NULL,
    long_url   varchar(255) NOT NULL,
    created_at timestamp    NOT NULL,
    expired_at timestamp    NOT NULL
);