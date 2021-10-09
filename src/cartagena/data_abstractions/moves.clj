(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.data-abstractions.game :as g :refer [board players active-player  add-random-card-to-active-player move-piece turn-played move-piece]]
   ;; should not need to import any of these. Only work with the game interface
   [cartagena.data-abstractions.deck :refer [remove-card]]
   [cartagena.data-abstractions.player :refer [color cards update-player-in-players set-cards]]
   [cartagena.data-abstractions.board :as b :refer [index-next-empty-slot index-closest-nonempty-slot]]))

;; moves: actions triggered by the players. Each player has 3 actions while it is her turn
;; Each action represents a modification of the state (the "game")
;; modifications available are: 
;;   Decrease active player's number of turns
;;   Decrease/Increase active player's cards
;;   Change of active player. New active player receives 3 turns
;;   
;; Things to check:
;;   Active user
;;   His remaining number of turns
;;   Her available cards to play
;;   Spot where a played car ends
;;   Spot where a falled-back card ends
;;   How many pieces are there in a fall-back slot



(defn pass
  "Player with turn looses an action. If last turn, next's players turn begins. Nothing else is modified in the game"
  [game]
  (turn-played game))

;; TODO: should check there is a place to fall-back to
(defn fall-back
  "For the active player: move piece back to the first non empty slot; add a random card to the player; turn played"
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
  "For the active player: use card to move piece (from board current-position to next available space -empty slot same typ or board-); loose the card; turn played"
  [game card from]
  (let [board (board game)
        players (players game)
        activePlayer (active-player game)
        color (color activePlayer)
        cards (cards activePlayer)
        newCards (remove-card cards card)
        updated-player (set-cards activePlayer newCards)
        updated-players (update-player-in-players players color updated-player)
        to (index-next-empty-slot board card from)]
    (turn-played
     (g/play-card game card from))))
