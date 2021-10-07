(ns cartagena.data-abstractions.deck
  (:require  [clojure.data.generators :refer [rand-nth]]))

;; Functions that need to know how deck is implemented

(defn random-deck [num cards-reservoir]
  (for [x (take num (range))]
    (rand-nth (seq cards-reservoir))))

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

