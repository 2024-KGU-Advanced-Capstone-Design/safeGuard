CREATE TABLE member (
                        member_id VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        latitude DOUBLE NOT NULL,
                        longitude DOUBLE NOT NULL,
                        fcm_token varchar(255) NOT NULL,
                        PRIMARY KEY (member_id)
);

CREATE TABLE child (
                       child_name VARCHAR(255) NOT NULL,
                       child_password VARCHAR(255) NOT NULL,
                       latitude DOUBLE NOT NULL,
                       longitude DOUBLE NOT NULL,
                       PRIMARY KEY (child_name)
);

CREATE TABLE parenting (
                           parenting_id BIGINT NOT NULL Auto_Increment,
                           parent_id VARCHAR(255) NOT NULL,
                           child_name VARCHAR(255) NOT NULL,
                           PRIMARY KEY (parenting_id),
                           FOREIGN KEY (parent_id) REFERENCES Member (member_id),
                           FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE helping (
                         helping_id BIGINT NOT NULL,
                         helper_id VARCHAR(255) NOT NULL,
                         child_name VARCHAR(255) NOT NULL,
                         PRIMARY KEY (helping_id),
                         FOREIGN KEY (helper_id) REFERENCES Member (member_id),
                         FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE coordinate (
                            coordinate_id BIGINT NOT NULL AUTO_INCREMENT,
                            child_name VARCHAR(255) NOT NULL,
                            is_living_area TINYINT(1) NOT NULL,
                            x_of_north_west DOUBLE NOT NULL,
                            y_of_north_west DOUBLE NOT NULL,
                            x_of_north_east DOUBLE NOT NULL,
                            y_of_north_east DOUBLE NOT NULL,
                            x_of_south_west DOUBLE NOT NULL,
                            y_of_south_west DOUBLE NOT NULL,
                            x_of_south_east DOUBLE NOT NULL,
                            y_of_south_east DOUBLE NOT NULL,
                            PRIMARY KEY (coordinate_id),
                            FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE notice (
                        notice_id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(255) NOT NULL,
                        content TEXT NOT NULL,
                        level ENUM('INFO', 'WARN', 'FATAL') NOT NULL,
                        child_name VARCHAR(255) NOT NULL,
                        created_at DATETIME NOT NULL,
                        PRIMARY KEY (notice_id),
                        FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE emergency (
                        emergency_id BIGINT NOT NULL AUTO_INCREMENT,
                        title VARCHAR(255) NOT NULL,
                        content TEXT NOT NULL,
                        sender_id BIGINT NOT NULL ,
                        child_name VARCHAR(255) NOT NULL,
                        created_at DATETIME NOT NULL,
                        PRIMARY KEY (emergency_id),
                        FOREIGN KEY (sender_id) REFERENCES Member (member_id),
                        FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE confirm (
                         confirm_id BIGINT NOT NULL AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         content TEXT NOT NULL,
                         type ENUM('ARRIVED', 'DEPART', 'UNCONFIRMED') NOT NULL,
                         helping_id BIGINT NOT NULL ,
                         child_name BIGINT NOT NULL,
                         created_at DATETIME NOT NULL,
                         PRIMARY KEY (confirm_id),
                         FOREIGN KEY (helping_id) REFERENCES Helping (helping_id),
                         FOREIGN KEY (child_name) REFERENCES Child (child_name)
);

CREATE TABLE comment (
                         comment_id BIGINT NOT NULL AUTO_INCREMENT,
                         emergency_id BIGINT NOT NULL,
                         commentator_id VARCHAR(255) NOT NULL,
                         comment TEXT NOT NULL,
                         commented_at DATETIME NOT NULL,
                         PRIMARY KEY (comment_id),
                         FOREIGN KEY (emergency_id) REFERENCES Emergency (emergency_id)
);

CREATE TABLE jwt_token (
                           token_id BIGINT NOT NULL AUTO_INCREMENT,
                           access_token VARCHAR(255) NOT NULL,
                           grant_type VARCHAR(255) NOT NULL,
                           refresh_token VARCHAR(255) NOT NULL,
                           is_black_list TINYINT NOT NULL,
                           PRIMARY KEY (token_id)
);