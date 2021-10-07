(ns cartagena.data-abstractions.cards
  (:require  [clojure.data.generators :refer [rand-nth]]))

(defn random-card [cards-reservoir]
  (rand-nth (seq cards-reservoir)))

(defn random-deck [num cards-reservoir]
  (for [x (take num (range))]
    (rand-nth (seq cards-reservoir))))

(defn cards-amounts
  "counts how many cards of each type we have in a vector of cards"
  [cards]
  (frequencies cards))

;; TODO: this function makes no sense because cards-amounts never returns zeroes, remove it
(defn playable-cards
  "returns cards with frequencies bigger than 0"
  [cards]
  (filter #(> (val %) 0) (cards-amounts cards)))

;; helper function
(defn from-freqs-to-seq [freqs-map]
  (reduce #(concat  %1 (repeat (val %2) (key %2))) nil freqs-map))

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
