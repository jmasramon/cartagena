(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :refer [rand-nth shuffle]]
   [cartagena.core :refer [pirate-colors card-types]]
   [cartagena.data-abstractions.player :refer [random-players players-colors player update-player-in-players]]
   [cartagena.data-abstractions.deck :refer [random-deck]]
   [cartagena.data-abstractions.board :refer [initial-board boat]]))

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
   (let [players (random-players 3 pirate-colors 6 card-types)]
     {:players players
      :turn-order (players-turns (players-colors players))
      :turn (random-initial-turn pirate-colors)
      :deck (random-deck 50 card-types)
      :board (initial-board card-types players)}))
  ([num-players]
   (let [players (random-players num-players pirate-colors 6 card-types)]
     {:players players
      :turn-order (players-turns (players-colors players))
      :turn (random-initial-turn pirate-colors)
      :deck (random-deck 50 card-types)
      :board (initial-board card-types players)})))

;; getters
(defn players [game]
  (game :players))

(defn turn-order [game]
  (game :turn-order))

(defn turn [game]
  (game :turn))

(defn next-turn [game]
  ((turn game) (turn-order game)))

(defn board [game]
  (game :board))

(defn deck [game]
  (game :deck))

(defn active-player [game]
  (let [turn (turn game)
        players (players game)]
    (first (filter #(= turn (first (keys %))) players))))

(defn active-player-color [game]
  (get-in game [:turn]))

(defn winner? [game-state]
  (let [board (board game-state)
        boat (boat board)
        pieces (vals (get-in boat [:pieces]))]
    (> (count (filter #(= % 6) pieces)) 0)))

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
        modified-player (assoc-in player [turn-color :actions] 3)
        modified-players (update-player-in-players players turn-color modified-player)]
    (assoc-in game-state [:players] modified-players)))



