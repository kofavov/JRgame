package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRep;
import com.game.repository.PlayerRepWithQuery;
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
//    @Autowired
//    private PlayerRepWithQuery playerRepWithQuery;

//    @Override
//    public List<Player> getAllPlayers2(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order) {
//
//        return playerRepWithQuery.findAllWithQuery(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
//    }

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

    @Override
    public Player createPlayer(Player player) {
//        System.out.println(player.getBirthday().getYear());
//        System.out.println(new Date(100,0,1).getTime());
//        System.out.println(new Date(1100,0,1).getYear());
        if (player.getBirthday().getTime()<new Date(100,0,1).getTime()
                ||player.getBirthday().getTime()>=new Date(1101,0,1).getTime()
        )return null;
//        if (player.getId()==null)player.setId(100L);
        int lvl = (int) (Math.sqrt(2500+200*player.getExperience())-50)/100;
        int untilNextLevel = 50*(lvl+1)*(lvl+2)-player.getExperience();
        if(player.getBanned()==null)player.setBanned(false);
//        Player player = new Player();
//        player.setName(name);
//        player.setTitle(title);
//        player.setRace(race);
//        player.setProfession(profession);
//        player.setExperience(experience);
        player.setLevel(lvl);
//        player.setBirthday(new Date(birthday));
//        player.setBanned(banned);
        player.setUntilNextLevel(untilNextLevel);
//        playerRep.save(new Player(name,title,race,profession,experience,lvl,untilNextLevel,new Date(birthday),banned));
        playerRep.save(player);
        return player;
    }

    @Override
    public Player editPlayer(Player player,long id) {
        System.out.println(player+"   id" + id);


        Optional<Player> playerOptional = playerRep.findById(id);
        if (playerOptional.isPresent()){
            Player p = playerOptional.get();
            System.out.println(p);
            System.out.println(player);
            if (player.getName()!=null){
                    if (player.getName().length()>12
                    &&player.getName().equals(""))return null;
                p.setName(player.getName());}

            if (player.getBanned()!=null)p.setBanned(player.getBanned());



            if (player.getBirthday()!=null){
                     if (player.getBirthday().getTime()<new Date(100,0,1).getTime()
                        ||player.getBirthday().getTime()>=new Date(1101,0,1).getTime()
                        )return null;
                p.setBirthday(player.getBirthday());}



            if (player.getExperience()!=null){
                if(player.getExperience()>10000000&&player.getExperience()<0)return null;
                p.setExperience(player.getExperience());
                int lvl = (int) (Math.sqrt(2500+200*player.getExperience())-50)/100;
                int untilNextLevel = 50*(lvl+1)*(lvl+2)-player.getExperience();

                player.setLevel(lvl);
                player.setUntilNextLevel(untilNextLevel);
                if (player.getUntilNextLevel()!=null)p.setUntilNextLevel(player.getUntilNextLevel());
                if (player.getLevel()!=null)p.setLevel(player.getLevel());
            }

            if (player.getProfession()!=null)p.setProfession(player.getProfession());

            if (player.getRace()!=null)p.setRace(player.getRace());

            if (player.getTitle()!=null){
                if(player.getTitle().length()>30)return null;
                p.setTitle(player.getTitle());}

            playerRep.save(p);
            System.out.println(p);
            return p;
        }
        else return null;


    }
}
