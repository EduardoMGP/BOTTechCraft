package br.com.techcraftbrasil.app;

import br.com.techcraftbrasil.app.bot.Bot;
import main.framework.sql.ConnectionException;
import main.framework.sql.SQL;
import main.framework.sql.SQLConnection;
import main.framework.sql.SQLConnectionBuilder;
import main.framework.sql.models.Set;
import main.framework.sql.models.Values;
import main.framework.sql.models.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBStorage {
    private static SQL DATABASE;

    public DBStorage() throws SQLException, ConnectionException {
        SQLConnectionBuilder.addConnections(new SQLConnection("default", "bot_discord", "localhost", 3306,"bot_discord", ""));
        DATABASE = new SQL("default");

        DATABASE.query("create table if not exists synced_tokens (\n" +
                "    uuid varchar(36) not null primary key unique,\n" +
                "    user_id varchar(32) not null,\n" +
                "    guild_id varchar(32) not null,\n" +
                "    username varchar(16) not null,\n" +
                "    data datetime not null default now()\n" +
                ")");

        DATABASE.query("create event if not exists delete_synced_tokens\n" +
                "on schedule every 1 minute\n" +
                "do\n" +
                "    delete from synced_tokens where timestampdiff(MINUTE, data, now()) > 9");

        DATABASE.query("create table if not exists synced_accounts (\n" +
                "    uuid varchar(36) not null primary key unique,\n" +
                "    user_id varchar(32) not null,\n" +
                "    guild_id varchar(32) not null,\n" +
                "    username varchar(16) not null,\n" +
                "    data datetime not null default now()\n" +
                ");");

        DATABASE.query("create table if not exists guild_servers (\n" +
                "    uuid varchar(36) not null primary key unique,\n" +
                "    client_secret varchar(36) not null unique,\n" +
                "    guild_id varchar(32) not null unique,\n" +
                "    data timestamp default now() not null\n" +
                ");");

        DATABASE.query("create table if not exists  guild_servers_ip (\n" +
                "    id int not null auto_increment primary key,\n" +
                "    ip varchar(150) not null,\n" +
                "    guild_uuid varchar(36) not null,\n" +
                "    foreign key guild_servers_ip(guild_uuid) references guild_servers(uuid)\n" +
                "    on delete cascade\n" +
                ")");

        DATABASE.query("" +
                "create table if not exists bot_private_blacklist (\n" +
                "    discord varchar(30) not null primary key\n" +
                ")" +
                "");
    }

    public static SQL db(){
        return DATABASE;
    }
    public List<Long> getMembersDisableMessage(){
        List<Long> members = new ArrayList<>();
        try {
            List<Map<String, Object>> result = DATABASE.select("bot_private_blacklist").map();
            if(result.size() > 0)
                for (Map<String, Object> r : result)
                    members.add(Long.parseLong(r.get("discord") + ""));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return members;

    }
    public boolean isDisableMessage(long memberID){
        try {
            List<Map<String, Object>> result = DATABASE.select("bot_private_blacklist").map();
            return result.size() != 0;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void setDisableMessage(long memberID, boolean disable){
        try {
            if(disable) {
                if (!isDisableMessage(memberID))
                    DATABASE.insert("bot_private_blacklist", new Values().value("discord", memberID + ""));
            } else
                DATABASE.delete("bot_private_blacklist", new Where().where("discord", memberID));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public String getDiscordFromUsername(String username, String guild){
        try {
            List<Map<String, Object>> result =
                    DATABASE.select("synced_accounts",
                            new Where()
                                    .where("guild_id", guild)
                                    .where("username", username)
                    ).map();
            return result.size() == 0 ? null : result.get(0).get("user_id").toString();
        } catch (Exception e){
            return null;
        }
    }

    public boolean synchronizeDiscord(String token, String guild_id){
        try {
            List<Map<String, Object>> result =
                    DATABASE.select("synced_tokens",
                            new Where()
                                    .where("guild_id", guild_id)
                                    .where("uuid", token)
                    ).map();
            if(result.size() != 0) {

                String user_id = result.get(0).get("user_id").toString();
                String username = result.get(0).get("username").toString();

                result = DATABASE.select("synced_accounts",
                        new Where()
                                .where("guild_id", guild_id)
                                .where("user_id", user_id)
                ).map();
                if(result.size() != 0)

                    DATABASE.update("synced_accounts",
                            new Set().set("username", username)
                    );

                else
                    DATABASE.insert("synced_accounts",
                            new Values()
                                    .value("uuid", UUID.randomUUID().toString())
                                    .value("user_id", user_id)
                                    .value("guild_id", guild_id)
                                    .value("username", username)
                    );

                return true;
            } else return false;
        } catch (Exception e){
            return false;
        }
    }

    public String checkCredentials(String hash, String ip){
        System.out.println(ip);
        Bot.print("Verificando permissão de acesso para o IP: " + ip);
        try {
            List<Map<String, Object>> result =
                    DATABASE.select_query("" +
                            "SELECT guild_id, uuid FROM guild_servers WHERE " +
                            "(select sha1(concat(md5(uuid), md5(client_secret)))) = '" + hash + "'")
                            .map();

            if(result.size() == 0) return null;

            String guild = result.get(0).get("guild_id").toString();
            result = DATABASE.select("guild_servers_ip",
                    new Where()
                            .where("guild_uuid", result.get(0).get("uuid"))
                            .where("ip", ip)
            ).map();

            return result.size() == 0 ? null : guild;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
