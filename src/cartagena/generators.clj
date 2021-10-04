(ns cartagena.generators
  (:require   [clojure.set :refer [union]]
              [clojure.data.generators :refer [rand-nth shuffle *rnd*]]
              [cartagena.core :refer :all]))

; {:red {
;     :cards [:hat :flag :pistol],
;     :actions 3}}

(defn random-card [cards-reservoir]
  (rand-nth (seq cards-reservoir)))

(defn random-deck [num cards-reservoir]
  (for [x (take num (range))]
    (rand-nth (seq cards-reservoir))))

(defn random-players [num players-reservoir cardNum cards-reservoir]
  (vec (for [x (take num players-reservoir)]
         {x {:cards (random-deck cardNum cards-reservoir)
             :actions 3}})))

(defn players-colors [players]
  (flatten (map keys players)))

(defn random-initial-turn [turns-reservoir]
  (rand-nth (seq turns-reservoir)))

(defn players-turns [players-colors]
  (let [random-ordered-players-colors (shuffle players-colors)]
    (zipmap random-ordered-players-colors (rest (cycle random-ordered-players-colors)))))

(defn starting-pieces [used-colors]
  {:pieces (into (sorted-map) (zipmap used-colors (repeat 6)))
   :type :start})

(defn empty-pieces [used-colors]
  {:pieces (into (sorted-map) (zipmap used-colors (repeat 0)))})

(defn new-board-section [types used-colors]
  (let [shuffled-types (shuffle (vec types))]
    (vec (for [type shuffled-types]
           (merge (empty-pieces used-colors)
                  {:type type})))))

(defn initial-board [types players]
  (let [used-colors (flatten (map keys players))]
    (vec (flatten    [(starting-pieces used-colors)
                      (new-board-section types used-colors)
                      (new-board-section types used-colors)
                      (new-board-section types used-colors)
                      (new-board-section types used-colors)
                      (new-board-section types used-colors)
                      (new-board-section types used-colors)
                      (merge (empty-pieces used-colors)
                             {:type :boat})]))))

; TODO: Should initializate the board with all elements
; To generate the initial state
(defn initial-state
  ([]
   (let [players (random-players 3 pirate-colors 6 card-types)]
     {:players players
      :turn-order (players-turns (players-colors players))
      :turn (random-initial-turn pirate-colors)
      :deck (random-deck 50 card-types)
      :board (initial-board card-types players)}))
  ([num-players]
   (let [players (random-players num-players pirate-colors 6 card-types)]
     {:players players
      :turn-order (players-turns (players-colors players))
      :turn (random-initial-turn pirate-colors)
      :deck (random-deck 50 card-types)
      :board (initial-board card-types players)})))

