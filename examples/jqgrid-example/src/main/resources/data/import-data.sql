insert into SS_USER (ID,LOGIN_NAME,NAME,EMAIL,PASSWORD,SALT,STATUS) values('1','admin','超级用户',null,'691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','enabled');

insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_,MENU ) values('1','sys:manager','系统管理',0,0,0,1,1,null,1);

insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU,URL) values('2','user:manager','用户管理',0,0,1,1,2,'1',1,'security/user');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('3','user:view','查看',0,1,2,1,2,'2',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('4','user:edit','编辑',0,1,2,1,3,'2',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('5','user:delete','删除',0,1,2,1,4,'2',0);


insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU,URL) values('6','role:manager','角色管理',0,0,1,1,3,'1',1,'security/role');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('7','role:view','查看',0,1,2,1,2,'6',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('8','role:edit','编辑',0,1,2,1,3,'6',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('9','role:delete','删除',0,1,2,1,4,'6',0);

insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU,URL) values('10','permission:manager','权限管理',0,0,1,1,4,'1',1,'security/permission');
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('11','permission:view','查看',0,1,2,1,2,'10',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('12','permission:edit','编辑',0,1,2,1,3,'10',0);
insert into SS_PERMISSION (ID,NAME,DISPLAY_NAME,EXPANDED_ ,LEAF_ ,LEVEL_ ,LOADED_ ,ORDER_ ,PARENT_ ,MENU) values('13','permission:delete','删除',0,1,2,1,4,'10',0);


insert into SS_ROLE (ID,NAME,NOTE) values('1','Admin','系统管理员');


insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','1');

insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','2');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','3');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','4');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','5');

insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','6');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','7');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','8');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','9');

insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','10');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','11');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','12');
insert into SS_ROLE_PERMISSION (ROLE_ID,PERMISSION_ID) values('1','13');

insert into SS_USER_ROLE (USER_ID,ROLE_ID) values('1','1');