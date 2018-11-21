package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Auction;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.AuctionRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.services.GameService;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public boolean checkIfGameHasAuction(Game game) {

        Mono<Auction> auctionMono = auctionRepository.findAuctionByGame(game);

        return false;

    }
}
