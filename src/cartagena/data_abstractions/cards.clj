(ns cartagena.data-abstractions.cards)

(defn cards-amounts [cards]
  (frequencies cards))

(defn playable-cards [cards]
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
