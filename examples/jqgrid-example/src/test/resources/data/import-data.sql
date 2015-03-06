insert into SS_TEAM (ID,NAME,MASTER_ID) values('1','Dolphin','1');

insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('1','张三','北京',136988887788,'1');
insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('2','李四','上海',136988887788,'1');
insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('3','黄五','广州',136988887788,'1');
insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('4','曾六','深圳',136988887788,'1');
insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('5','卢七','广西',136988887788,'1');
insert into SS_PERSON (ID,NAME,ADDRESS,PHONE,TEAM_ID) values('6','唐八','南宁',136988887788,'1');

insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('1','admin','Admin','admin@springside.org.cn','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('2','user','Calvin','user@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('3','user2','Jack','jack@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('4','user3','Kate','kate@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('5','user4','Sawyer','sawyer@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('6','user5','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');

insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('7','user6','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('8','user7','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('9','user8','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('10','user9','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('11','user10','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('12','user11','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('13','user12','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('14','user13','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('15','user14','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('16','user15','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('17','user16','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('18','user17','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('19','user18','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');
insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('20','user19','Ben','ben@springside.org.cn','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled');

insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ) values('1','sys:manager','系统管理',0,0,0,1,1,null);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ) values('2','user:manager','用户管理',0,0,1,1,2,'1');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ) values('3','user:view','查看',0,1,2,1,2,'2');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ) values('4','user:edit','编辑',0,1,2,1,3,'2');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ) values('5','user:delete','删除',0,1,2,1,4,'2');

insert into SS_ROLE (ID,NAME) values('1','Admin');
insert into SS_ROLE (ID,NAME) values('2','User');
insert into SS_ROLE (ID,NAME) values('3','User2');
insert into SS_ROLE (ID,NAME) values('4','User3');
insert into SS_ROLE (ID,NAME) values('5','User4');
insert into SS_ROLE (ID,NAME) values('6','User5');
insert into SS_ROLE (ID,NAME) values('7','User6');
insert into SS_ROLE (ID,NAME) values('8','User8');


insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','1');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','2');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','3');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','4');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','5');

insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('2','1');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('2','2');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('2','3');

insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('1','1');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('1','2');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('2','2');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('3','2');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('4','2');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('5','2');
insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('6','2');