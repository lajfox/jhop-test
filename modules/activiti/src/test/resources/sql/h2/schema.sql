    drop table if exists ss_user_role;
    
    drop table if exists ss_user_permission;
    
    drop table if exists ss_role_permission;

    drop table if exists ss_role;

    drop table if exists ss_user;
    
    drop table if exists ss_permission;
    
    drop table if exists ss_person;
    
    drop table if exists ss_team;

    create table ss_role (
        id varchar(32) ,
    	name varchar(255) not null unique,    
    	note varchar(2000),
        primary key (id)
    );

    create table ss_user (
       	id varchar(32) ,
        login_name varchar(255) not null unique,
        name varchar(64),
        password varchar(255),
        salt varchar(64),
        email varchar(128),
        status varchar(32),
        team_id varchar(255),
        createtime date,
        logintime int,
        primary key (id)
    );
    
    create table ss_permission (
        id varchar(32) ,
    	name varchar(255) not null unique,
    	display_name varchar(255),
    	url varchar(255),
    	menu int,
    	expanded_ int,
    	leaf_ int,
    	level_ int,
    	loaded_ int,
    	order_ double,
    	parent_ varchar(255),
        primary key (id)
    );    

    create table ss_user_role (
        user_id varchar(32) not null,
        role_id varchar(32) not null,
        primary key (user_id, role_id)
    );
    
    create table ss_user_permission (
        user_id varchar(32) not null,
        permission_id varchar(32) not null,
        primary key (user_id, permission_id)
    ); 
    
    create table ss_role_permission (
        role_id varchar(32) not null,
        permission_id varchar(32) not null,
        primary key (role_id, permission_id)
    );  
    
   	create table ss_person (
       	id varchar(32) ,
    	name varchar(255) not null unique,
    	address varchar(255),
    	phone bigint,
    	team_id varchar(32),
        primary key (id)
    );    
    
   	create table ss_team (
       	id varchar(32) ,
    	name varchar(255) not null unique,
    	master_id varchar(32),
        primary key (id)
    );