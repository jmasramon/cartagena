(ns cartagena.getters.semanticEntities
    (:require [cartagena.getters.mainEntities :refer [players turn]]
        [cartagena.getters.sdariEntities :refer [actions]]))

; (defn active-player [game-state]
;     (first (filter
;         #(let [actions ( (first (vals %)) :actions)]
;             (> actions 0))
;         (players game-state))))

;; game
(defn active-player [game-state]
    (let [turn (turn game-state)
        players (players game-state)]
        (first (filter #(= turn (first (keys %))) players))))

(defn next-active-player-color [game-state]
    (get-in game-state [:turn]))

;; board
(defn next-open [board index symbol])

(defn next-fallback [board index])

