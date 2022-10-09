create table user
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户编号',
    name VARCHAR(100) NOT NULL default '' COMMENT '用户名',
    is_admin BOOL NOT NULL default false COMMENT '是否为管理员',
    password VARCHAR(100) NOT NULL default '' COMMENT '密码',
    description VARCHAR(255) default '' COMMENT '自我介绍',
    primary key (id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET = UTF8MB4;

create table category
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '父类别编号',
    name VARCHAR(100) NOT NULL default '' COMMENT '父类别名',
    PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

create table subcategory
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '子类别编号',
    category_id BIGINT COMMENT '父类别编号',
    name VARCHAR(100) NOT NULL default '' COMMENT '子类别名',
    PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

create table course
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程编号',
    subcategory_id BIGINT COMMENT '所属子类别编号',
    name VARCHAR(100) NOT NULL default '' COMMENT '课程名',
    provider VARCHAR(100) NOT NULL default '' COMMENT '提供方',
    description VARCHAR(3000) default '' COMMENT '课程介绍',
    difficulty INT UNSIGNED default 0 COMMENT '星级难度',
    est_hour INT UNSIGNED default 0 COMMENT '预计学时',
    website VARCHAR(255) default '' COMMENT '课程官网',
    video VARCHAR(255) default '' COMMENT '视频链接',
    assignment VARCHAR(255) default '' COMMENT '作业链接',
    foreign key (subcategory_id) references subcategory (id) on delete set null,
    PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

create table prerequisite
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程要求编号',
    pre_id BIGINT COMMENT '先修课程编号',
    post_id BIGINT COMMENT '后修课程编号',
    foreign key (pre_id) references course (id) on delete cascade,
    foreign key (post_id) references course (id) on delete cascade,
    PRIMARY KEY (id)
)ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;