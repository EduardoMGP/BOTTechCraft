package br.com.techcraftbrasil.app.bot.commandsprivate;

import br.com.techcraftbrasil.app.Main;
import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.interfaces.CommandsPrivate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandDisable implements CommandsPrivate {

    @Override
    public void onCommand(String[] args, String commandName, PrivateChannel channel, User author, long messageIdLong) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("TechCraftBrasil");
        embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
        embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");

        if(!Main.getStorage().isDisableMessage(author.getIdLong())) {
            Main.getStorage().setDisableMessage(author.getIdLong(), true);
            String emoji = Bot.getJDA().getEmotesByName("PepeRain", true).get(0).getAsMention();
            embedBuilder.setTitle("Mensagens desativadas " + emoji + emoji + emoji);
            embedBuilder.setColor(new Color(255, 55, 110));
            embedBuilder.setDescription("" +
                    "Certo, você optou por desativar as mensagens privadas do bot," +
                    " a partir de hoje você não receberá mais mensagens com notícias e novidades do nosso servidor " +
                    " nem cupons exclusivos em nossa loja, se desejar reativar as mensagens utilize o comando novamente " +
                    "" + emoji + emoji);
        } else {
            Main.getStorage().setDisableMessage(author.getIdLong(), false);
            String emoji = Bot.getJDA().getEmotesByName("cool_doge", true).get(0).getAsMention();
            embedBuilder.setTitle("Mensagens reativadas " + emoji + emoji + emoji);
            embedBuilder.setColor(new Color(10, 255, 10));
            embedBuilder.setDescription("" +
                    "Certo, você optou por reativar as mensagens privadas do bot," +
                    " a partir de hoje você receberá todas as mensagens com notícias e novidades do nosso servidor " +
                    " além cupons exclusivos gerados para você em nossa loja, se desejar desativar as mensagens utilize o comando novamente " +
                    "" + emoji + emoji);
        }
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public @NotNull String getName() {
        return "privado";
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "desativarmensagens",
                "mpdisable"
        };
    }

    @Override
    public String getDescription() {
        return "Desative ou reative mensagens privadas do bot utilizando este comando";
    }
}
