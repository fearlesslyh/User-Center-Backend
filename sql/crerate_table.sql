-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(255)              null comment '用户名',
    userAccount  varchar(256)              null comment '账户',
    avatar       varchar(1024)             null comment '头像',
    gender       tinyint                   null comment '性别',
    userPassword varchar(512)              not null comment '密码',
    phone        varchar(128)              null comment '电话',
    email        varchar(512)              null comment '邮箱',
    userStatus   int                       null comment '用户状态',
    createTime   timestamp default (now()) null comment '创建时间',
    updateTime   timestamp default (now()) null comment '修改时间',
    isDelete     tinyint   default 0       not null comment '是否删除',
    userRole     int       default 0       not null comment '普通用户为0；管理员为1'
)
    comment '用户表';

