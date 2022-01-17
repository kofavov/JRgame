package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.PagePlayer;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlayerService {

    List<Player> getAllPlayers(PagePlayer page);
    Player getPlayerById (long id);
    void deletePlayerById(long id);
    Player createPlayer(Player player);

    Player editPlayer(Player player,long id);

//    List<Player> getAllPlayers2(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order);
}
