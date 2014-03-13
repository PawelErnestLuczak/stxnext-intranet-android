package com.stxnext.management.server.planningpoker.server.tests;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.stxnext.management.server.planningpoker.server.database.dto.Card;
import com.stxnext.management.server.planningpoker.server.database.dto.Player;
import com.stxnext.management.server.planningpoker.server.database.dto.Ticket;
import com.stxnext.management.server.planningpoker.server.database.dto.Vote;
import com.stxnext.management.server.planningpoker.server.database.managers.DAO;

public class ServerTests {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEntityCRUD() throws Exception{
        //C
        
        DAO dao = DAO.getInstance();
        
        Player player1 = new Player();
        player1.setEmail("player1@gmail.com");
        player1.setName("player1");
        player1.setExternalId(1);
        
        Player player2 = new Player();
        player2.setEmail("player2@gmail.com");
        player2.setName("player2");
        player2.setExternalId(2);
        
        dao.getPlayerDao().create(player1);
        dao.getPlayerDao().create(player2);
        
        Card card = new Card();
        card.setName("trefl ;)");
        dao.getCardDao().create(card);
        
        Ticket ticket = new Ticket();
        ticket.setDisplayValue("some ticket");
        dao.getTicketDao().create(ticket);
        
        Vote vote1 = new Vote();
        vote1.setCard(card);
        vote1.setPlayer(player1);
        vote1.setTicket(ticket);
        
        Vote vote2 = new Vote();
        vote2.setCard(card);
        vote2.setPlayer(player2);
        vote2.setTicket(ticket);
        
        dao.getVoteDao().create(vote1);
        dao.getVoteDao().create(vote2);
        
        
        // R
        
        List<Ticket> tickets = dao.getTicketDao().queryForAll();
        for(Ticket t : tickets){
            ForeignCollection<Vote> votes = t.getVotes();
            String g = "";
        }
        
        
        // U
        
        
        // D
        
        //ticket.setVotes(votes)
        
    }

}
