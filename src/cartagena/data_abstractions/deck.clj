(ns cartagena.data-abstractions.deck
  (:require
   [cartagena.core :refer [deck-size card-types]]
   [clojure.data.generators :refer [rand-nth]]))

;; Functions that need to know how deck is implemented
(defn random-card [cards-reservoir]
  (rand-nth (seq cards-reservoir)))

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

(defn random-deck
  ([]
   ;random-deck deck-size card-types
   (for [x (take deck-size (range))]
     (rand-nth (seq card-types))))
  ([num cards-reservoir]
   (for [x (take num (range))]
     (rand-nth (seq cards-reservoir)))))

(defn draw-one-from [deck]
  (let [[card & newDeck] deck]
    [card newDeck]))

;; TODO: change param order so a partial application can generate ex: draw-3-from
(defn draw [deck n]
  (loop [deck deck n n acc []]
    (if (= n 0)
      [(reverse acc) deck]
      (let [[card newDeck] (draw-one-from deck)]
        (recur newDeck (dec n) (cons card acc))))))
	; (for [i (take n (range) newDeck deck)
	; 	:let [[card newDeck] (draw-one-from newDeck)]]
	; 	card))

