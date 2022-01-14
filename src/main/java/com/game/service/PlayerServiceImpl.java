package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRep playerRep;



    @Override
    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order) {
        if (name==null)name="";
        if (title==null)title="";
        if (after==null) after=Long.MIN_VALUE;
        if (before==null)before=Long.MAX_VALUE;
        if (minExperience==null)minExperience=Integer.MIN_VALUE;
        if (maxExperience==null)maxExperience=Integer.MAX_VALUE;
        if (minLevel==null)minLevel=Integer.MIN_VALUE;
        if (maxLevel==null)maxLevel=Integer.MAX_VALUE;

        List<Player> players = playerRep.findAll(Sort.by(Sort.Direction.ASC,order.getFieldName()));
        for (Player p:players) {
            if (!name.equals("")&&!p.getName().contains(name))p.setId(-1L);
            if (!title.equals("")&&!p.getTitle().contains(title))p.setId(-1L);
            if (!p.getBirthday().after(new Date(after)))p.setId(-1L);
            if (!p.getBirthday().before(new Date(before)))p.setId(-1L);
            if (banned!=null&&p.getBanned() != banned)p.setId(-1L);
            if (p.getExperience()<minExperience | p.getExperience()>maxExperience)p.setId(-1L);
            if (p.getLevel()<minLevel|p.getLevel()>maxLevel)p.setId(-1L);
            if (race!=null && p.getRace()!=race)p.setId(-1L);
            if (profession!=null && p.getProfession()!=profession)p.setId(-1L);
        }
        players.removeIf(player -> player.getId()==-1L);
//        List<Player>players = playerRep.getAllPlayers(name,  title, race,  profession, after,  before,  banned, minExperience,  maxExperience,  minLevel, maxLevel);
        return players;
    }

    @Override
    public Player getPlayerById(long id) {
        Optional<Player> player = playerRep.findById(id);
        return player.orElse(null);
    }

    @Override
    public void deletePlayerById(long id) {
        playerRep.deleteById(id);

    }


}
