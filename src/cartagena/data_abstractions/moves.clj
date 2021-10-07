(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.core :refer [card-types]]
   [cartagena.data-abstractions.cards :refer [random-card remove-card add-card]]
   [cartagena.data-abstractions.game :refer [board players turn turn-order active-player set-players set-board set-turn]]
   ;; should not need to import any of these. Only work with the game interface
   [cartagena.data-abstractions.player :refer [actions player-color player-cards update-player-in-players set-cards decrease-turns add-turns]]
   [cartagena.data-abstractions.board :refer [move-piece find-empty-slot find-nonempty-slot]]))


(defn pass
  "loose a turn"
  [game]
  (let [current-player (player-color (active-player game))
        next-player ((turn game) (turn-order game))
      	;; TODO: next three lines require its own abstraction new-players-state-after-turn
        updatedPlayers (decrease-turns (players game) current-player)
        remainingActions (actions updatedPlayers current-player)
        newPlayers (if (= 0 remainingActions)
                     (add-turns updatedPlayers next-player)
                     updatedPlayers)
        updatedGameState (set-players game newPlayers)
        newGameState (if (= 0 remainingActions)
                       (set-turn updatedGameState next-player)
                       updatedGameState)]
    newGameState))

(defn fall-back
  "For the active player: moves piece back to the first non empty slot, adds a card to the player"
  [game square-index]
  (let [board (board game)
        players (players game)
        activePlayer (active-player game)
        current-player (player-color activePlayer)
        cards (player-cards activePlayer)
        newCards (add-card cards (random-card card-types));; TODO: should add as many cards as pieces in card
        newPlayer (set-cards activePlayer newCards)
        newPlayers (update-player-in-players players current-player newPlayer)
        fallBackSlot (find-nonempty-slot board square-index)]
    (pass
     (set-players
      (set-board
       game
       (move-piece board square-index fallBackSlot current-player)) ;; TODO: should check that there is a player's piece in the card
      newPlayers))))

(defn play-card
  "getActiveUser
	moveCard (from board current-position to next available space -empty slot same type-)
	pass"
  [game card current-position]
  (let [board (board game)
        players (players game)
        activePlayer (active-player game)
        current-player (player-color activePlayer)
        cards (player-cards activePlayer)
        newCards (remove-card cards card)
        newPlayer (set-cards activePlayer newCards)
        newPlayers (update-player-in-players players current-player newPlayer)
        destination (find-empty-slot board card current-position)]
    (pass
     (set-players
      (set-board game
                 (move-piece board current-position destination current-player)) newPlayers))))