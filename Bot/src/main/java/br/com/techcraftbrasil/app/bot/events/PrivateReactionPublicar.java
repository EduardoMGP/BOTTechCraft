package br.com.techcraftbrasil.app.bot.events;

import br.com.techcraftbrasil.app.interfaces.ReactionsPrivate;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class PrivateReactionPublicar implements ReactionsPrivate {
    @Override
    public void onPrivateReaction(PrivateChannel channel, MessageReaction messageReaction, Message message, MessageReaction.ReactionEmote emote, User user) {
        System.out.println(message.getContentDisplay());
        System.out.println(emote.getName());
        System.out.println(message.getNonce());
    }
}
