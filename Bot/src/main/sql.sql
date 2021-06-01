create table if not exists guild_servers (
    uuid varchar(36) not null primary key unique,
    client_secret varchar(36) not null unique,
    guild_id varchar(32) not null unique,
    data timestamp default now() not null
);

create table if not exists  guild_servers_ip (
    id int not null auto_increment primary key,
    ip varchar(150) not null,
    guild_uuid varchar(36) not null,
    foreign key guild_servers_ip(guild_uuid) references guild_servers(uuid)
    on delete cascade
);

create table if not exists synced_accounts (
    uuid varchar(36) not null primary key unique,
    user_id varchar(32) not null,
    guild_id varchar(32) not null,
    username varchar(16) not null,
    data datetime not null default now()
);

create table if not exists synced_tokens (
    uuid varchar(36) not null primary key unique,
    user_id varchar(32) not null,
    guild_id varchar(32) not null,
    username varchar(16) not null,
    data datetime not null default now()
);

create event if not exists delete_synced_tokens
on schedule every 1 minute
do
    delete from synced_tokens where timestampdiff(MINUTE, data, now()) > 1


