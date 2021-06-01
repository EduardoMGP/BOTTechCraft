package br.com.techcraftbrasil.app.interfaces;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public interface ReactionsPrivate extends Events {

    void onPrivateReaction(PrivateChannel channel, MessageReaction messageReaction, Message message, MessageReaction.ReactionEmote emote, User user);
}
