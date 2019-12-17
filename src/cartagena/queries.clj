(ns cartagena.queries
	(:require [cartagena.getters.sdariEntities :refer [boat player square-pieces]]
			  [cartagena.getters.mainEntities :refer [board]]))

; TODO: non predicate functions should be moved to a new getters

(defn winner? [game-state]
	(let [board (board game-state)
		  boat (boat board)
		  pieces (vals (get-in boat [:pieces]))]
		  (> (count (filter #(= % 6) pieces)) 0)
	))

(defn space-available? [board index]
	(< (reduce + (vals ((board index) :pieces))) 2))

(def space-occupied? (complement space-available?))

(defn player-has-card? [players color card]
	(let [player (player players color)]
		(= card (some #{card} (:cards (color player))) )))

(defn square-has-color? [board index color]
	(> (square-pieces board index color) 0))

(defn players-turn? [players color]
	(= 3 (:actions (color (player players color)))))

(defn square-of-type? [square type]
	(= (square :type) type))


