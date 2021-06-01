package br.com.techcraftbrasil.app.interfaces;

import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

public interface CommandsPrivate {

    void onCommand(String[] args, String commandName, PrivateChannel channel, User author, long messageIdLong);
    @NotNull String getName();
    String[] getAlias();
    String getDescription();
}
