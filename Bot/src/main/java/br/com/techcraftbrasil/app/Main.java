package br.com.techcraftbrasil.app;

import br.com.techcraftbrasil.app.audio.PlayerManager;
import br.com.techcraftbrasil.app.bot.Bot;
import br.com.techcraftbrasil.app.bot.commands.CommandHelp;
import br.com.techcraftbrasil.app.bot.commands.CommandServerKey;
import br.com.techcraftbrasil.app.bot.commands.CommandSincronizar;
import br.com.techcraftbrasil.app.bot.commands.CommandStatus;
import br.com.techcraftbrasil.app.bot.commandsprivate.CommandDisable;
import main.framework.sql.ConnectionException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Main {

    private static DBStorage DATABASE;

    public static DBStorage getStorage(){
        return DATABASE;
    }

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, ConnectionException {
        DATABASE = new DBStorage();
        Bot.start("{BOT TOKEN}");
        Bot.registerCommand(new CommandHelp());
        Bot.registerCommand(new CommandStatus());
        Bot.registerCommand(new CommandServerKey());
        Bot.registerCommand(new CommandSincronizar());
        //Bot.registerCommand(new CommandPublicar());
        //Bot.registerCommand(new CommandJoin());
        //Bot.registerCommand(new CommandPlay());

        Bot.registerPrivateCommand(new CommandDisable());
        //Bot.registerEvent(new PrivateReactionPublicar());

        Guild guild = Bot.getJDA().getGuildById(220691390890246144L);
        AudioManager audioManager = guild.getAudioManager();

        audioManager.openAudioConnection(guild.getVoiceChannelById(840347998868865084L));
        PlayerManager.getInstance()
                .loadAndPlay( guild, "https://stream1.thepuremix.net/nexusdance.mp3");
    }

}
