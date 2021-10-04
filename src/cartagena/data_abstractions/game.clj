(ns cartagena.data-abstractions.game
  (:require
   [cartagena.data-abstractions.player :refer [update-player player]]))

;; Functions that need to know how game-state is implemented
;; game-state: 
;; {players turn board deck}

(defn make-game [])

;; getters
(defn players [game-state]
  (game-state :players))

(defn turn-order [game-state]
  (game-state :turn-order))

(defn turn [game-state]
  (game-state :turn))

(defn nex-turn [game-state]
  ((turn game-state) (turn-order game-state)))

(defn board [game-state]
  (game-state :board))

(defn deck [game-state]
  (game-state :deck))

(defn active-player [game-state]
  (let [turn (turn game-state)
        players (players game-state)]
    (first (filter #(= turn (first (keys %))) players))))

(defn next-active-player-color [game-state]
  (get-in game-state [:turn]))

;; setters
(defn set-players [game-state players]
  (assoc-in game-state [:players] players))

(defn set-turn-order [game-state turn-order]
  (assoc-in game-state [:turn-order] turn-order))

(defn set-turn [game-state turn]
  (assoc-in game-state [:turn] turn))

(defn set-board [game-state board]
  (assoc-in game-state [:board] board))

(defn set-deck [game-state deck]
  (assoc-in game-state [:deck] deck))

(defn update-player-in-players [players color newPlayer]
  (map (partial update-player color newPlayer) players))

(defn add-turns [players color]
  (let [player (player players color)
        newPlayer (assoc-in player [color :actions] 3)]
    (update-player-in-players players color newPlayer)))

(defn decrease-turns [players color]
  (let [player (player players color)
        newPlayer (update-in player [color :actions] dec)]
    (update-player-in-players players color newPlayer)))

