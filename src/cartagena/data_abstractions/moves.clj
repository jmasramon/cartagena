(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.data-abstractions.game :as g]))

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
  (g/turn-played game))

(defn fall-back
  "For the active player: 
   1-move piece back to the first non empty slot
   2-add a random card to the player; 
   3-turn played"
  [game from]
  (let [to (g/fallback-square-index game from)]
    (if (and
         (g/available-piece? game from)
         (g/available-fall-back? game from))
      (-> (g/move-piece game from to)
          g/add-random-card-to-active-player
          g/turn-played)
      game)))

(defn play-card
  "For the active player: 
   1-use card to move piece from it's current-position to next available space -empty slot same typ or board-) 
   2-remove used card
   2-turn played"
  [game card from]
  (let [to (g/next-empty-slot-index game card from)]
    (if (g/available-piece? game from)
      (-> (g/move-piece game from to)
          (g/remove-played-card card)
          g/turn-played)
      game)))
