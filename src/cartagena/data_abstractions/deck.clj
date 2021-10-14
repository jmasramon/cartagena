(ns cartagena.data-abstractions.deck
  (:require
   [cartagena.core :refer [deck-size card-types]]
   [clojure.data.generators :refer [rand-nth]]))

;; A deck is just a list ofc cards. Order is not needed

;; Functions that need to know how deck is implemented
;; builders
(defn random-card
  "Returns a random valid card"
  [cards-reservoir]
  (rand-nth (seq cards-reservoir)))

(defn random-deck
  "Creates a random deck"
  ([]
   (random-deck deck-size card-types))
  ([num]
   (random-deck num card-types))
  ([num cards-reservoir]
   (repeatedly num 
               #(rand-nth (seq cards-reservoir)))))

;; getters
(defn cards-amounts
  "Returns a map with how many cards of each type we have in deck"
  [deck]
  (frequencies deck))

(defn playable-cards
  "returns list of key-value card-frequencies with frequencies bigger than 0"
  [cards]
  (reverse (into '() (cards-amounts cards))))

(declare from-freqs-to-seq)

;; modifiers
(defn remove-card-from
  "Removes card from deck"
  [deck card]
  (let [cards-amounts (cards-amounts deck)
        new-cards-amounts (update-in cards-amounts [card] dec)]
    (from-freqs-to-seq new-cards-amounts)))

(defn add-card
  "Add a card to a deck"
  [deck card]
  (let [cards-amounts (cards-amounts deck)
        new-cards-amounts (if (card cards-amounts)
                            (update-in cards-amounts [card] inc)
                            (merge cards-amounts {card 1}))]
    (from-freqs-to-seq new-cards-amounts)))

(defn draw-one-from
  "Returns the first card from a deck and the deck without the removed card: [card deck']"
  [deck]
  (let [[card & newDeck] deck]
    [card newDeck]))

(defn draw
  "Returns the n first cards from a deck and the deck without the removed cards: [cards deck']"
  [deck n]
  (loop [deck deck 
         n n 
         acc []]
    (if (= n 0)
      [(reverse acc) deck]
      (let [[card new-deck] (draw-one-from deck)]
        (recur new-deck (dec n) (cons card acc))))))

;; helper function
(defn from-freqs-to-seq
  "Converts map of freqs to seq of cards"
  [freqs-map]
  (reduce #(concat  %1 (repeat (val %2) (key %2))) nil freqs-map))

