package br.com.techcraftbrasil.restapi.controller;

import br.com.techcraftbrasil.app.DBStorage;
import br.com.techcraftbrasil.app.Main;
import br.com.techcraftbrasil.restapi.model.Result;
import main.framework.sql.models.Where;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class DiscordSync {

    @PostMapping("/sync")
    public Result syncDiscord(@RequestHeader("x-api-key") String key,
                              @RequestParam String username,
                              @RequestParam String token, HttpServletRequest request
    ){
        try {
            String guild_id = Main.getStorage().checkCredentials(key, request.getRemoteAddr());
            if(guild_id != null) {
               boolean result = Main.getStorage().synchronizeDiscord(token, guild_id);
                if(result) return new Result(202,"success", "Sincronizado");
                else return new Result(403,"error", "Este token não existe ou não pertence a você");
            } else return new Result(401, "error", "Acesso não autorizado");

        } catch (Exception e){
            return new Result(500,"error", e.getCause().toString());
        }
    }
}
