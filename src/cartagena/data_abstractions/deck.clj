(ns cartagena.data-abstractions.deck
  (:require
   [cartagena.core :refer [deck-size card-types]]
   [clojure.data.generators :as gen]))

;; Deck data abstraction.
;;
;; A deck is represented as a simple sequence of card keywords.
;; Order is not important for most operations; we mostly care about
;; how many of each card type exist, and about drawing/removing cards.
;;
;; This namespace groups all functions that need to know how a deck is
;; represented internally (sequence of card keywords).

;; builders
(defn random-card
  "Return a random valid card from the given reservoir of card types."
  [cards-reservoir]
  (gen/rand-nth (seq cards-reservoir)))

(defn random-deck
  "Create a random deck as a sequence of card keywords."
  ([]
   (random-deck deck-size card-types))
  ([num]
   (random-deck num card-types))
  ([num cards-reservoir]
   (repeatedly num
               #(gen/rand-nth (seq cards-reservoir)))))

;; getters
(defn cards-amounts
  "Return a map from card type to the number of occurrences in deck."
  [deck]
  (frequencies deck))

;; (defn playable-cards
;;   "Returns a list with how many cards of each type we have in deck"
;;   [cards]
;;   (reverse (into '() (cards-amounts cards))))

(declare from-freqs-to-seq)

;; modifiers
(defn remove-card-from
  "Remove a single instance of card from deck, returning a new deck."
  [deck card]
  (let [cards-amounts (cards-amounts deck)
        new-cards-amounts (update-in cards-amounts [card] dec)]
    (from-freqs-to-seq new-cards-amounts)))

(defn add-card
  "Add a single instance of card to deck, returning a new deck."
  [deck card]
  (let [cards-amounts (cards-amounts deck)
        new-cards-amounts (if (card cards-amounts)
                            (update-in cards-amounts [card] inc)
                            (merge cards-amounts {card 1}))]
    (from-freqs-to-seq new-cards-amounts)))

(defn draw-one-from
  "Return [card deck'] with the first card and the rest of the deck."
  [deck]
  (let [[card & newDeck] deck]
    [card newDeck]))

(defn draw
  "Draw n cards from deck, returning [cards deck']."
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
  "Convert a frequency map {:card n, ...} to a sequence of cards."
  [freqs-map]
  (reduce #(concat  %1 (repeat (val %2) (key %2))) nil freqs-map))

