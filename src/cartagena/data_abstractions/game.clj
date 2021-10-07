(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :refer [rand-nth shuffle]]
   [cartagena.core :refer [pirate-colors card-types def-num-players num-cards deck-size starting-actions]]
   [cartagena.data-abstractions.player :refer [random-players players-colors player update-player-in-players]]
   [cartagena.data-abstractions.deck :refer [random-deck]]
   [cartagena.data-abstractions.board :refer [make-board boat]]))

;; Functions that need to know how game is implemented
;; game-state: 
;; {players turn board deck}
(defn random-initial-turn [turns-reservoir]
  (rand-nth (seq turns-reservoir)))

(defn players-turns [players-colors]
  (let [random-ordered-players-colors (shuffle players-colors)]
    (zipmap random-ordered-players-colors (rest (cycle random-ordered-players-colors)))))

(defn make-game
  ([]
   (let [players (random-players def-num-players)
         players-colors (players-colors players)]
     {:players players
      :turn-order (players-turns players-colors)
      :turn (random-initial-turn players-colors)
      :deck (random-deck)
      :board (make-board players)}))
  ([num-players]
   (let [players (random-players num-players)
         players-colors (players-colors players)]
     {:players players
      :turn-order (players-turns players-colors)
      :turn (random-initial-turn players-colors)
      :deck (random-deck)
      :board (make-board players)})))

;; getters
(defn players [game]
  (game :players))

(defn turn-order [game]
  (game :turn-order))

(defn turn [game]
  (game :turn))

(defn next-turn [game]
  ((turn game) (turn-order game)))

(defn deck [game]
  (game :deck))

(defn board [game]
  (game :board))

(defn active-player [game]
  (let [turn (turn game)
        players (players game)]
    (first (filter #(= turn (first (keys %))) players))))

(defn active-player-color [game]
  (turn game))

(defn player-color [game] (active-player game))

(defn next-player [game]
  (next-turn game))

(defn winner? [game-state]
  (let [board (board game-state)
        boat (boat board)
        pieces (vals (get-in boat [:pieces]))]
    (> (count (filter #(= % num-cards) pieces)) 0)))

;; setters
(defn set-players [game players]
  (assoc-in game [:players] players))

(defn set-turn-order [game turn-order]
  (assoc-in game [:turn-order] turn-order))

(defn set-turn [game turn]
  (assoc-in game [:turn] turn))

(defn set-board [game board]
  (assoc-in game [:board] board))

(defn set-deck [game deck]
  (assoc-in game [:deck] deck))

(defn start-turn [game-state turn-color]
  (let [players (players game-state)
        player (player players turn-color)
        modified-player (assoc-in player [turn-color :actions] starting-actions)
        modified-players (update-player-in-players players turn-color modified-player)]
    (assoc-in game-state [:players] modified-players)))



