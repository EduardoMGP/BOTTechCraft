package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.DBStorage;
import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import main.framework.sql.models.Values;
import main.framework.sql.models.Where;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandSincronizar implements CommandsGuild {

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("TechCraftBrasil");
        embedBuilder.setTitle("Sincronizar seu nick com o bot");
        embedBuilder.setColor(new Color(255, 55, 110));
        embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
        embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");


        if(args.length == 0){
            embedBuilder.setDescription("" +
                    user.getAsMention() + " Ei, preciso saber qual seu nick dentro do servidor :) \n" +
                    "Utilize o comando da seguinte forma: ");
            embedBuilder.addField(Bot.getPrefix() + "sincronizar username", "Utilize este comando para sincronizar o bot com o servidor", true);
        } else {

            try {

                List<Map<String, Object>> result = DBStorage.db().select("synced_tokens",
                        new Where()
                                .where("user_id", user.getId())
                                .where("guild_id", guild.getId())
                ).map();
                if(result.size() == 0) {
                    String uuid = UUID.randomUUID().toString();
                    DBStorage.db().insert("synced_tokens",
                            new Values()
                                    .value("uuid", uuid)
                                    .value("user_id", user.getId())
                                    .value("guild_id", guild.getId())
                                    .value("username", args[0])
                    );
                    EmbedBuilder privateEmbed = new EmbedBuilder();
                    privateEmbed.setAuthor("TechCraftBrasil");
                    privateEmbed.setTitle("Sincronizar seu nick com o bot");
                    privateEmbed.setColor(new Color(255, 55, 110));
                    privateEmbed.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                    privateEmbed.setThumbnail("https://i.imgur.com/4i4PZhJ.png");

                    privateEmbed.setDescription("Muito bem, seu token de sincronização do username com sua conta no discord foi gerado e tem duração de 10 minutos, " +
                            " o próximo passo agora deverá ser realizado dentro do servidor. \n" +
                            "Por gentileza efetue login no servidor com o username **"+args[0]+"** e utilize o comando abaixo.");
                    embedBuilder.addField("/discord token " + uuid, "Utilize este comando dentro do servidor para finalizar a sincronização", false);
                    embedBuilder.addField("Não passe esse token para ninguém" + uuid, "", false);
                } else {
                    String uuid = result.get(0).get("uuid").toString();
                    embedBuilder.setDescription("Você ja gerou um token, o próximo passo agora deverá ser realizado dentro do servidor. \n" +
                            "Por gentileza efetue login no servidor com o username **"+args[0]+"** e utilize o comando abaixo.");
                    embedBuilder.addField("/discord token " + uuid, "Utilize este comando dentro do servidor para finalizar a sincronização", false);
                }
            } catch (Exception e){
                embedBuilder.setDescription(user.getAsMention() + " Desulpe-me, houve um erro :(");
            }
        }
        channel.sendMessage(embedBuilder.build()).queue();
    }


    @Override
    public @NotNull String getName() {
        return "sincronizar";
    }

    @Override
    public String[] getAlias() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Sincronize seu nick no servidor com o bot do discord";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  Permission.ADMINISTRATOR;
    }
}
