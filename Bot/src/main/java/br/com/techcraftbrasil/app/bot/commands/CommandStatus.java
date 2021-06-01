package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.bot.MineStat;
import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandStatus implements CommandsGuild {

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        MineStat stat = new MineStat("54.39.130.119", 25565);
        String online = "";

        if(stat.isServerUp()) online = "\uD83D\uDFE2 (Online)";
        else online = "\uD83D\uDD34 (Offline)";

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("TechCraftBrasil");
        embedBuilder.setTitle("Status do servidor");
        embedBuilder.setColor(new Color(255, 55, 110));
        embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
        embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
        embedBuilder.setImage("https://i.imgur.com/uN9Z019.png");
        embedBuilder.setDescription("" +
                "Estas informações são a respeito de ambos os servidores, " +
                "com elas você poderá saber se algum server se encontra online ou offline, " +
                "quantos jogadores possuem em cada, além de encontrar o ip de conexão " +
                "para jogar em um de nossos servidores" +
                "Também terá o modpack respectivo de cada um" +
                "");

        embedBuilder.addBlankField(false);

        embedBuilder.addField("IP Servidor 1.7.10", "techcraftbrasil.com", true);
        embedBuilder.addField("Status: ", online, true);
        embedBuilder.addField("Players: ", "" + stat.getCurrentPlayers() + "/" + stat.getMaximumPlayers(), true);
        embedBuilder.addField("Modpack: ", "https://bit.ly/3x3gsnY" , true);
        embedBuilder.addField("Site: ", "https://techcraftbrasil.com.br" , true);

        stat = new MineStat("54.39.130.119", 22444);
        if(stat.isServerUp()) online = "\uD83D\uDFE2 (Online)";
        else online = "\uD83D\uDD34 (Offline)";

        embedBuilder.addBlankField(false);

        embedBuilder.addField("IP Servidor 1.12.2", "1.12.2.techcraftbrasil.com", true);
        embedBuilder.addField("Status: ", online, true);
        embedBuilder.addField("Players: ", "" + stat.getCurrentPlayers() + "/" + stat.getMaximumPlayers(), true);
        embedBuilder.addField("Modpack: ", "https://bit.ly/3gl9cOi" , true);
        embedBuilder.addField("Site: ", "https://1.12.2.techcraftbrasil.com.br" , true);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public @NotNull String getName() {
        return "status";
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "ip",
                "modpack",
                "servers",
                "server",
        };
    }

    @Override
    public String getDescription() {
        return "Veja informações do servidor, link do modpack, link do nosso site utilizando este comando";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  null;
    }
}
