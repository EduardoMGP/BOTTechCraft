package br.com.techcraftbrasil.app.bot.commandsprivate;

import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import br.com.techcraftbrasil.app.interfaces.CommandsPrivate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandHelp implements CommandsGuild {

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("TechCraftBrasil");
        embed.setTitle("Comandos " + Bot.getJDA().getEmotesByName("8908_Meow_Party", true).get(0).getAsMention());
        embed.setColor(new Color(255, 200, 110));
        embed.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
        embed.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
        embed.setDescription("" +
                "Ei, sou um bot oficial do servidor, possuo alguns comandos especiais que são " +
                "conectados ao servidor, você pode ver a lista desses comandos abaixo"
        );

        CommandsPrivate[] commands = Bot.getCommandsPrivate();
        for(CommandsPrivate cmd : commands) {
            embed.addField(Bot.getPrefix()+cmd.getName(), " " + cmd.getDescription(), true);
            String alias = "";
            if(cmd.getAlias() != null)
                for(String a : cmd.getAlias())
                    if(alias.equals(""))
                        alias += Bot.getPrefix()+a;
                    else
                        alias += ", "+Bot.getPrefix()+a;
            embed.addField("Alias", " " + alias, true);
            embed.addField("", " ", true);
        }


        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "ajuda"
        };
    }

    @Override
    public String getDescription() {
        return "Comando de ajuda que mostrará todos os comandos do bot";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  null;
    }
}
