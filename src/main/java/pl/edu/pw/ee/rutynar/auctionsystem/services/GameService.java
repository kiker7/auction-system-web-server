package pl.edu.pw.ee.rutynar.auctionsystem.services;

import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;

public interface GameService {

    boolean checkIfGameHasAuction(Game game);
}
