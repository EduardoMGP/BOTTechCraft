package br.com.techcraftbrasil.app.bot;

import br.com.techcraftbrasil.app.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class SendMessage {

    interface Exec {
        String getId();
        Member getMember();
        User getUser();
        Long getIDMember();
        EmbedBuilder getEmbed();
        void exec();
    }

    private static BlockingQueue<Exec> taskList;

    public static boolean sendMessage(TextChannel channel, Member publicador, List<Member> members, String message, String title){
        if(taskList != null)
            return false;

        List<Long> blacklist = Main.getStorage().getMembersDisableMessage();
        String data = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss").format(new Date());
        for(Member member : members) {
            Long id = member.getIdLong();
            taskList.add(new Exec() {

                @Override
                public String getId(){
                    return UUID.randomUUID().toString();
                }
                @Override
                public Member getMember() {
                    return channel.getGuild().getMemberById(id);
                }

                @Override
                public User getUser() {
                    return Bot.getJDA().getUserById(id);
                }

                @Override
                public Long getIDMember() {
                    return id;
                }

                @Override
                public EmbedBuilder getEmbed(){
                    String PepeHappy = Bot.getJDA().getEmotesByName("PepeHappy", true).get(0).getAsMention();
                    String snoopdance = Bot.getJDA().getEmotesByName("snoopdance", true).get(0).getAsMention();

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("TechCraftBrasil");
                    embedBuilder.setFooter("Um server feito com amor ❤️", "https://i.imgur.com/4i4PZhJ.png");
                    embedBuilder.setThumbnail("https://i.imgur.com/4i4PZhJ.png");
                    embedBuilder.setTitle(title);
                    embedBuilder.setDescription("Olá, sou o bot oficial do servidor TechCraftBrasil e trago uma notícia para você se manter informado sobre nossos servidores. " +
                            "\n\nCaso não deseja mais receber esse tipo de mensagem em sua caixa privada, " +
                            "por favor utilize o comando  " + Bot.getPrefix() + "mpdisable " +
                            "\n\nMas fique tranquilo, entrarei em contato no privado apenas em " +
                            "notícias muito importantes como informar sobre reset, atualizações importantes e afins... " + snoopdance + snoopdance + snoopdance +
                            "\n\nAlém de notícias também posso realizar sorteios e disponibilizar cupons de desconto " +
                            "exclusivos para você aqui mesmo em nossa conversa particular." + PepeHappy + PepeHappy + PepeHappy);
                    embedBuilder.setColor(new Color(255, 100, 255));
                    embedBuilder.addField("Notícia postada por: ", publicador.getUser().getName() + " (" + publicador.getUser().getAsMention() + ")", true);
                    embedBuilder.addField("Publicado em: ", data, true);
                    return  embedBuilder;
                }

                @Override
                public void exec() {
                    Member member1 = getMember();
                    User usuario = getUser();
                    if (!usuario.isBot())
                        if (!blacklist.contains(getIDMember())) {
                            usuario.openPrivateChannel().submit()
                                    .thenCompose(privateChannel -> {
                                                return privateChannel.sendMessage(getEmbed().build()).submit()
                                                        .thenCompose(message1 -> {
                                                            return message1.reply(">>> \n" + message).submit();
                                                        });
                                            }
                                    )
                                    .whenComplete((m, e) -> {
                                        if (e == null)
                                            Bot.print(ConsoleColors.GREEN + " Mensagem privada enviada para " + usuario.getName() + " (" + usuario.getId() + ")");
                                        else
                                            Bot.print(ConsoleColors.RED + " Mensagem privada não foi enviada para " + usuario.getName() + " (" + usuario.getId() + ") Motivo: " + e.getMessage() + " (" + m + ")");
                                    });
                        } else
                            Bot.print(ConsoleColors.RED + " Usuário " + member1.getUser().getName() + "(" + getIDMember() + ")" + " optou por não receber mensagens privadas");
                }
            });
        }


        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Bot.print(ConsoleColors.CYAN + " [Enviando] Enviando mensagem para mais 10 usuários");
                Bot.print(ConsoleColors.CYAN + " [Enviando] Usuários restantes " + taskList.size());

                channel.sendMessage(" [Enviando] Enviando mensagem para mais 10 usuários, usuários restantes " + taskList.size()).queue();

                for (int j = 0; j < 10; j++){
                    Exec exec = taskList.poll();
                    if(exec != null)
                        exec.exec();
                    else {
                        taskList.clear();
                        timer.cancel();
                        channel.sendMessage(publicador.getAsMention() + " Todas as mensagens foram enviadas").queue();
                        return;
                    }
                }
            }
        };
        timer.schedule(task, 0, 1000 * (60 * 3));
        return true;
    }
}
