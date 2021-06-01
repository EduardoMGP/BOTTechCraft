package br.com.techcraftbrasil.app.bot.commands;

import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.interfaces.CommandsGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public class CommandTicket implements CommandsGuild {

    @Override
    public void onCommand(String[] args, String command, TextChannel channel, Guild guild, User user, Member member, long message_id) {

        Collection<Permission> collection = Arrays.asList(
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_EMBED_LINKS
        );

        Collection<Permission> deny = Arrays.asList(
                Permission.VIEW_CHANNEL
        );

        guild.createTextChannel("Ticket: " + user.getName())
                .addRolePermissionOverride(502979566823014410L, collection, null)
                .addMemberPermissionOverride(member.getIdLong(), collection, null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue(
                        ch -> {
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setAuthor("TechCraftBrasil");
                            builder.setTitle("Pedido de Suporte");
                            builder.setColor(new Color(30, 177, 30));
                            builder.setDescription(member.getAsMention() + " seu ticket de suporte foi criado, utilize o canal " + ch.getAsMention() + " que acabou de ser criado onde somente você e nossos staffers tersão acesso para que possamos atendê-lo da melhor forma possível");
                            builder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                            builder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                            channel.sendMessage(builder.build()).queue();

                            builder.setDescription(member.getAsMention() + " Aguarde o suporte, assim que possível iremos responder seus ticket");
                            builder.addField("Como finalizar o ticket?",
                                    "A qualquer momento, para finalizar o ticket, " +
                                            "basta reagir ao emoji ✅ caso sua duvida tenha sido concluida e o staffer te ajudado," +
                                            " se deseja encerrar, mas não conseguimos te ajudar você poderá reagir ao emoji ❌",
                                    true);
                            ch.sendMessage(builder.build()).queue(
                                    message -> {
                                        message.addReaction("✅").queue();
                                        message.addReaction("❌").queue();
                                    }
                            );
                            ch.sendMessage(Bot.getJDA().getRoleById(502979566823014410L).getAsMention() + " Novo pedido de suporte").queue();
                        }
                );

    }

    @Override
    public @NotNull String getName() {
        return "suporte";
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "ticket"
        };
    }

    @Override
    public String getDescription() {
        return "Crie um ticket de suporte";
    }

    @Override
    public Permission getPermissionChannel() {
        return null;
    }
    public Permission getPermissionGlobal() {
        return  null;
    }
}
