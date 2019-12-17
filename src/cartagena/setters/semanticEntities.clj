(ns cartagena.setters.semanticEntities
    (:require   [cartagena.getters.sdariEntities :refer [player]]
                [cartagena.getters.otherEntities :refer [cards-amounts]]
                [cartagena.setters.sdariEntities :refer [update-player-in-players]]
        ))

(defn add-turns [players color]
    (let [player (player players color)
          newPlayer (assoc-in player [color :actions] 3)]
          (update-player-in-players players color newPlayer)))

(defn decrease-turns [players color]
    (let [player (player players color)
          newPlayer (update-in player [color :actions] dec)]
          (update-player-in-players players color newPlayer)))

(defn from-freqs-to-seq [freqs-map]
    (reduce #(concat  %1 (repeat(val %2) (key %2))) nil freqs-map))

(defn remove-card [cards card]
   (let [cards-amounts (cards-amounts cards)
        new-cards-amounts (update-in cards-amounts [card] dec)]
        (from-freqs-to-seq new-cards-amounts)))

(defn add-card [cards card]
    ; (println "add-card params" cards card)
    (let [cards-amounts (cards-amounts cards)
        new-cards-amounts (if   (card cards-amounts)
                                (update-in cards-amounts [card] inc)
                                (merge cards-amounts {card 1}))]
        (from-freqs-to-seq new-cards-amounts)))

