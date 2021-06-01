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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandServerKey implements CommandsGuild {

    private EmbedBuilder getEmbed(Color color){
        String PepeHappy = Bot.getJDA().getEmotesByName("PepeHappy", true).get(0).getAsMention();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("TechCraftBrasil");
        embedBuilder.setTitle("Credenciais de sincronização " + PepeHappy + PepeHappy + PepeHappy);
        embedBuilder.setColor(color);
        embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
        embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
        return embedBuilder;
    }

    private EmbedBuilder getEmbedErrorCommand(User user){
        String PepeHands = Bot.getJDA().getEmotesByName("PepeHands", true).get(0).getAsMention();

        EmbedBuilder embedBuilder = getEmbed(new Color(255, 55, 110));
        embedBuilder.setDescription(user.getAsMention() + " Você digitou o comando de forma errada " + PepeHands + PepeHands + PepeHands);
        embedBuilder.addField(Bot.getPrefix() + "serverkey gerar [IP's]", "Utilize este comando para gerar uma key privada para utilizar em seu servidor " +
                "\nExemplo: " + Bot.getPrefix() + "serverkey gerar 153.40.10.227 117.212.125.125" +
                "\n**É importante passar os ips que terão acesso ao grupo do discord para poder efetuar alterações, " +
                "passe como argumento somente os ips do seu servidor para garantir mais segurança**", false);
        embedBuilder.addField(Bot.getPrefix() + "serverkey deletar", "Utilize este comando para deletar uma key já gerada", false);
        return embedBuilder;
    }

    private EmbedBuilder getEmbedPrivateMessage(User user, String uuid, String client_secret, String[] ips){
        EmbedBuilder privateEmbed = getEmbed(new Color(0, 255, 110));
        privateEmbed.setDescription(user.getAsMention() + " Olá, aqui esta suas credenciais de sincronização com " +
                "o discord, adicione essas credenciais nas configurações do plugin " +
                "para que o bot possa administrar os cargos em seu grupo, não passe em hipótese alguma " +
                "essas credenciais para ninguém");
        privateEmbed.addField("Secret_Token: ", "||" + uuid + "||", true);
        privateEmbed.addField("Client_Secret: ", "||" + client_secret + "||", true);
        privateEmbed.addField("IP's autorizados: ", "||" + Arrays.asList(ips) + "||", true);
        return  privateEmbed;
    }

    private EmbedBuilder getEmbedHashGenerated(User user){
        EmbedBuilder embedBuilder = getEmbed(new Color(0, 255, 110));
        embedBuilder.setDescription("" +
                user.getAsMention() + " Sua key PRIVADA foi gerada com sucesso, enviamos por mensagem privada, não passe" +
                " a hash para ninguém!" +
                "");
        return embedBuilder;
    }

    private EmbedBuilder getEmbedHashAlreadyGenerated(User user){
        EmbedBuilder embedBuilder = getEmbed(new Color(255, 55, 110));
        embedBuilder.setDescription("" +
                user.getAsMention() + " Sua key PRIVADA foi gerada com sucesso, enviamos por mensagem privada, não passe" +
                " a hash para ninguém!" +
                "");
        return embedBuilder;
    }

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        if(args.length == 0)
            channel.sendMessage(getEmbedErrorCommand(user).build()).queue();
        else if(args[0].equalsIgnoreCase("gerar")){
            if(args.length < 2)
                channel.sendMessage(getEmbedErrorCommand(user).build()).queue();
            else {
                try {

                    channel.retrieveMessageById(message_id).complete().delete().queue();
                    List<Map<String, Object>> result = DBStorage.db().select("guild_servers", new Where().where("guild_id", guild.getId())).map();
                    if (result.size() == 0) {

                        String uuid = UUID.randomUUID().toString();
                        String client_secret = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                        DBStorage.db().insert("guild_servers",
                                new Values()
                                        .value("uuid", uuid)
                                        .value("client_secret", client_secret)
                                        .value("guild_id", guild.getId())
                        );
                        String[] ips = new String[args.length - 1];
                        for (int i = 1; i < args.length; i++) {
                            ips[i-1] = args[i];
                            DBStorage.db().insert("guild_servers_ip",
                                    new Values()
                                            .value("ip", args[i])
                                            .value("guild_uuid", uuid)
                            );
                        }

                        user.openPrivateChannel().complete().sendMessage(getEmbedPrivateMessage(user, uuid, client_secret, ips).build()).queue();
                        channel.sendMessage(getEmbedHashGenerated(user).build()).queue();

                    } else channel.sendMessage(getEmbedHashAlreadyGenerated(user).build()).queue();

                } catch (Exception e) {

                    channel.sendMessage(getEmbedErrorCommand(user).build()).queue();
                    e.printStackTrace();

                }
            }

        } else if(args[0].equalsIgnoreCase("deletar")){
            try {
                List<Map<String, Object>> result = DBStorage.db().select("guild_servers", new Where().where("guild_id", guild.getId())).map();
                if(result.size() != 0){

                    DBStorage.db().delete("guild_servers", new Where().where("guild_id", guild.getId()));
                    EmbedBuilder embedBuilder = getEmbed(new Color(0, 255, 110));
                    embedBuilder.setDescription(user.getAsMention() + " Hash deletada com sucesso");
                    channel.sendMessage(embedBuilder.build()).queue();

                } else {

                    EmbedBuilder embedBuilder = getEmbed(new Color(255, 55, 110));
                    embedBuilder.setDescription(user.getAsMention() + " Não existe nenhuma hash criada para este grupo");
                    channel.sendMessage(embedBuilder.build()).queue();

                }
            } catch (Exception e){
                e.printStackTrace();
                channel.sendMessage(getEmbedErrorCommand(user).build()).queue();
            }
        } else channel.sendMessage(getEmbedErrorCommand(user).build()).queue();
    }


    @Override
    public @NotNull String getName() {
        return "serverkey";
    }

    @Override
    public String[] getAlias() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Gere uma key privada para sincronizar o grupo do discord com seu servidor";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  Permission.ADMINISTRATOR;
    }
}
