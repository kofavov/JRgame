package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class MainController {
    @Autowired
    private PlayerServiceImpl playerService;

    @GetMapping("/players")
    private List<Player> getAllPlayers(@RequestParam(name ="name", defaultValue = "", required = false)String name,
                                       @RequestParam(name ="title", defaultValue = "", required = false)String title,
                                       @RequestParam(name ="race", defaultValue = "", required = false) Race race,
                                       @RequestParam(name ="profession", defaultValue = "", required = false) Profession profession,
                                       @RequestParam(name ="after", defaultValue = "", required = false)Long after,
                                       @RequestParam(name ="before", defaultValue = "", required = false)Long before,
                                       @RequestParam(name ="banned", defaultValue = "", required = false)Boolean banned,
                                       @RequestParam(name ="minExperience", defaultValue = "", required = false)Integer minExperience,
                                       @RequestParam(name ="maxExperience", defaultValue = "", required = false)Integer maxExperience,
                                       @RequestParam(name ="minLevel", defaultValue = "", required = false)Integer minLevel,
                                       @RequestParam(name ="maxLevel", defaultValue = "", required = false)Integer maxLevel,
                                       @RequestParam( name ="order", defaultValue = "ID", required = false) PlayerOrder order,
                                       @RequestParam(name ="pageNumber", defaultValue = "0", required = false)Integer pageNumber,
                                       @RequestParam(name ="pageSize", defaultValue = "3", required = false)Integer pageSize){
        List<Player> players = playerService.getAllPlayers(
                name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel,order);

        return players.subList(pageNumber*pageSize, Math.min(pageNumber * pageSize + pageSize, players.size()));
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(@RequestParam(name ="name", defaultValue = "", required = false)String name,
                                   @RequestParam(name ="title", defaultValue = "", required = false)String title,
                                   @RequestParam(name ="race", defaultValue = "", required = false) Race race,
                                   @RequestParam(name ="profession", defaultValue = "", required = false) Profession profession,
                                   @RequestParam(name ="after", defaultValue = "", required = false)Long after,
                                   @RequestParam(name ="before", defaultValue = "", required = false)Long before,
                                   @RequestParam(name ="banned", defaultValue = "", required = false)Boolean banned,
                                   @RequestParam(name ="minExperience", defaultValue = "", required = false)Integer minExperience,
                                   @RequestParam(name ="maxExperience", defaultValue = "", required = false)Integer maxExperience,
                                   @RequestParam(name ="minLevel", defaultValue = "", required = false)Integer minLevel,
                                   @RequestParam(name ="maxLevel", defaultValue = "", required = false)Integer maxLevel,
                                   @RequestParam( name ="order", defaultValue = "ID", required = false) PlayerOrder order){

        return playerService.getAllPlayers
                (name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order).size();
    }

}
