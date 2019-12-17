(ns cartagena.getters.otherEntities
    (:require [cartagena.getters.mainEntities :refer :all]
            [cartagena.queries :refer [square-of-type?]]))

(defn num-pieces [square color]
    (get-in square [:pieces color]))

(defn indexes-squares-of-type [board type]
    (keep-indexed #(when (square-of-type? %2 type) %1) board))

(defn player-color [player]
	(first (keys player)))

(defn player-cards [player]
	(:cards (first (vals player))))

(defn cards-amounts [cards]
	(frequencies cards))

(defn playable-cards [cards] 
	(filter #(> (val %) 0) (cards-amounts cards)))
