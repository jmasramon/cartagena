(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.data-abstractions.game :refer [board add-random-card-to-active-player move-piece turn-played move-piece remove-played-card]]
   [cartagena.data-abstractions.board :refer [index-closest-nonempty-slot next-empty-slot-index]]))

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

;; TODO: should check there is a place to fall-back to
(defn fall-back
  "For the active player: 
   1-move piece back to the first non empty slot
   2-add a random card to the player; 
   3-turn played"
  [game from]
  (let [board (board game)
        to (index-closest-nonempty-slot board from)]
    (turn-played
     (add-random-card-to-active-player
      (move-piece
       game
       from
       to))) ;; TODO: should check that there is a player's piece in the card
    ))

(defn play-card
  "For the active player: 
   1-use card to move piece from it's current-position to next available space -empty slot same typ or board-) 
   2-remove used card
   2-turn played"
  [game card from]
  (let [board (board game)
        to (next-empty-slot-index board card from)]
    (turn-played
     (remove-played-card
      (move-piece
       game
       from
       to)
      card))))
