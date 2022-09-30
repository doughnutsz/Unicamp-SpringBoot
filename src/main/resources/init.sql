use unicamp;
SET
foreign_key_checks = 0;

create table user
(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户编号',
    name VARCHAR(100) NOT NULL COMMENT '用户名',
    is_admin BOOL NOT NULL COMMENT '是否为管理员',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    description VARCHAR(255) COMMENT '自我介绍',
    primary key (id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET = UTF8MB4;

create table category (
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '类别编号',
    name VARCHAR(100) NOT NULL COMMENT '类别名',
    description VARCHAR(255) COMMENT '类别介绍',
    PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

create table course (
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程编号',
    category_id BIGINT COMMENT '所属类别编号',
    name VARCHAR(100) NOT NULL COMMENT '课程名',
    provider VARCHAR(100) NOT NULL COMMENT '提供方',
    description VARCHAR(255) COMMENT '课程介绍',
    difficulty INT UNSIGNED COMMENT '星级难度',
    est_hour INT UNSIGNED COMMENT '预计学时',
    website VARCHAR(255) COMMENT '课程官网',
    video VARCHAR(255) COMMENT '视频链接',
    assignment VARCHAR(255) COMMENT '作业链接',
    foreign key (category_id) references category (id) on delete set null,
    PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

create table prerequisite(
    id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '要求编号',
    pre_id BIGINT COMMENT '先修课程编号',
    post_id BIGINT COMMENT '后修课程编号',
    foreign key (pre_id) references course (id) on delete cascade,
    foreign key (post_id) references course (id) on delete cascade,
    PRIMARY KEY (id)
)ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

insert into user(name, is_admin, password, description)
value ("admin",true,"9C@e12sq","吾心吾行澄如明镜，所作所为皆属正义！");

SET
foreign_key_checks = 1;