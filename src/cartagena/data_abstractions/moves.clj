(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.data-abstractions.game :refer [board active-player active-player-color add-random-card-to-active-player move-piece turn-played move-piece remove-played-card]]
   [cartagena.data-abstractions.board :refer [square index-closest-nonempty-slot next-empty-slot-index]]
   [cartagena.data-abstractions.square-bis :refer [num-pieces-in]]))

;; moves: actions triggered by the players. Each player has 3 actions while it is her turn
;; Each action represents a modification of the state (the "game")
;; modifications available are: 
;;   Decrease active player's number of turns
;;   Decrease/Increase active player's cards
;;   Change of active player. New active player receives 3 turns
;;   
;; Things to check:
;;   Spot where a played car ends
;;   Spot where a falled-back card ends
;;   How many pieces are there in a fall-back slot

(defn pass
  "Player with turn looses an action. 
   If last turn, next's players turn begins."
  [game]
  (turn-played game))

(defn fall-back
  "For the active player: 
   1-move piece back to the first non empty slot
   2-add a random card to the player; 
   3-turn played"
  [game from]
  (let [board (board game)
        player-color (active-player-color game)
        to (index-closest-nonempty-slot board
                                        from)
        origin-square (square board from)
        available-pieces (num-pieces-in origin-square player-color)
        available-destination? (not (nil? to))
        available-piece? (and
                          (not (nil? available-pieces))
                          (> available-pieces 0))]
    (if (and
         available-piece?
         available-destination?)
        (-> (move-piece game from to)
            add-random-card-to-active-player
            turn-played)
      game)))

(defn play-card
  "For the active player: 
   1-use card to move piece from it's current-position to next available space -empty slot same typ or board-) 
   2-remove used card
   2-turn played"
  [game card from]
  (let [board (board game)
        player-color (active-player-color game)
        origin-square (square board from)
        available-pieces (num-pieces-in origin-square player-color)
        to (next-empty-slot-index board
                                  card
                                  from)]
    (if (> available-pieces 0)
     (-> (move-piece game from to)
        (remove-played-card card)
        turn-played)
      game)))
