package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class MainController {
    @Autowired
    private PlayerServiceImpl playerService;

    @GetMapping("/players")
    private List<Player> getAllPlayers(@RequestParam(name = "name", defaultValue = "", required = false) String name,
                                       @RequestParam(name = "title", defaultValue = "", required = false) String title,
                                       @RequestParam(name = "race", defaultValue = "", required = false) Race race,
                                       @RequestParam(name = "profession", defaultValue = "", required = false) Profession profession,
                                       @RequestParam(name = "after", defaultValue = "", required = false) Long after,
                                       @RequestParam(name = "before", defaultValue = "", required = false) Long before,
                                       @RequestParam(name = "banned", defaultValue = "", required = false) Boolean banned,
                                       @RequestParam(name = "minExperience", defaultValue = "", required = false) Integer minExperience,
                                       @RequestParam(name = "maxExperience", defaultValue = "", required = false) Integer maxExperience,
                                       @RequestParam(name = "minLevel", defaultValue = "", required = false) Integer minLevel,
                                       @RequestParam(name = "maxLevel", defaultValue = "", required = false) Integer maxLevel,
                                       @RequestParam(name = "order", defaultValue = "ID", required = false) PlayerOrder order,
                                       @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                       @RequestParam(name = "pageSize", defaultValue = "3", required = false) Integer pageSize) {
        List<Player> players = playerService.getAllPlayers(
                name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel, order);
//        List<Player> players = playerService.getAllPlayers2(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order);
        return players.subList(pageNumber * pageSize, Math.min(pageNumber * pageSize + pageSize, players.size()));
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(@RequestParam(name = "name", defaultValue = "", required = false) String name,
                                   @RequestParam(name = "title", defaultValue = "", required = false) String title,
                                   @RequestParam(name = "race", defaultValue = "", required = false) Race race,
                                   @RequestParam(name = "profession", defaultValue = "", required = false) Profession profession,
                                   @RequestParam(name = "after", defaultValue = "", required = false) Long after,
                                   @RequestParam(name = "before", defaultValue = "", required = false) Long before,
                                   @RequestParam(name = "banned", defaultValue = "", required = false) Boolean banned,
                                   @RequestParam(name = "minExperience", defaultValue = "", required = false) Integer minExperience,
                                   @RequestParam(name = "maxExperience", defaultValue = "", required = false) Integer maxExperience,
                                   @RequestParam(name = "minLevel", defaultValue = "", required = false) Integer minLevel,
                                   @RequestParam(name = "maxLevel", defaultValue = "", required = false) Integer maxLevel,
                                   @RequestParam(name = "order", defaultValue = "ID", required = false) PlayerOrder order) {

        return playerService.getAllPlayers
                (name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order).size();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") long id) {
        if (id == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (playerService.getPlayerById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayerById(@PathVariable("id") long id) {
        if (id == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (playerService.getPlayerById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        playerService.deletePlayerById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity createPlayer(@RequestBody(required = false) Player player) {
//        System.out.println(player.toString());
//        if (player.getId()!=null)player.setId(null);
        if (player == null
                | player.getName() == null
                | player.getTitle() == null
                | player.getProfession() == null
                | player.getRace() == null
                | player.getExperience() == null
                | player.getBirthday() == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (player.getName().length() > 12
                | player.getTitle().length() > 30
                | player.getName().equals("")
                | player.getExperience() > 10000000
                | player.getExperience() < 0
                | player.getBirthday().getTime() < 0
        ) return new ResponseEntity(HttpStatus.BAD_REQUEST);


        Player p = playerService.createPlayer(player);
        if (p == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(p, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable("id") long id, @RequestBody(required = false) Player player) {
        System.out.println(player + "   id" + id);
        if (id <= 0) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (playerService.getPlayerById(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (player == null) new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
        if (player.getId() == null
                && player.getName() == null
                && player.getTitle() == null
                && player.getProfession() == null
                && player.getRace() == null
                && player.getExperience() == null
                && player.getBirthday() == null
                && player.getBanned() == null
                && player.getLevel() == null
                && player.getUntilNextLevel() == null
        ) return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);

        if ((player.getName() != null && player.getName().length() > 12)
                | (player.getTitle() != null && player.getTitle().length() > 30)
                | (player.getName() != null && player.getName().equals(""))
                | (player.getExperience() != null && (player.getExperience() > 10000000 | player.getExperience() < 0))
                | (player.getBirthday() != null && player.getBirthday().getTime() < 0)
        ) return new ResponseEntity(playerService.getPlayerById(id), HttpStatus.BAD_REQUEST);


        Player p = playerService.editPlayer(player, id);
        if (p == null) return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(p, HttpStatus.OK);

    }
}
