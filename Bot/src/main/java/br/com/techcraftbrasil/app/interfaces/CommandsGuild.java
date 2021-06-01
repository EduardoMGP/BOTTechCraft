package br.com.techcraftbrasil.app.interfaces;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface CommandsGuild {

    void onCommand(String args[], String command,
                   TextChannel channel,
                   Guild guild,
                   User user,
                   Member member,
                   long message_id
    );

    @NotNull String getName();
    String[] getAlias();
    String getDescription();
    Permission getPermissionChannel();
    Permission getPermissionGlobal();
}
